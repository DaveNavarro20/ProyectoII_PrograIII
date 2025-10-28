package hospital.presentation.Despacho;

import hospital.logic.Prescripcion;
import hospital.logic.Receta;
import hospital.logic.Service;

import javax.swing.JOptionPane;
import java.util.List;

public class Controller {

    View_despacho view;
    Model model;

    public Controller(View_despacho view, Model model) {
        this.view = view;
        this.model = model;
        view.setModel(model);
        view.setController(this);
        actualizarLista();
    }

    private void actualizarLista() {
        try {
            model.setList(Service.instance().findRecetasNoEntregadas());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar las recetas: " + e.getMessage());
        }
    }

    public void modificarEstado(Receta receta) {
        try {
            String estadoAnterior = receta.getEstado();
            Service.instance().modificarEstadoReceta(receta);

            if (!estadoAnterior.equals(receta.getEstado())) {
                actualizarLista();
                Service.instance().stop();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al modificar estado: " + ex.getMessage());
        }
    }

    public void entregarReceta(Receta receta) {
        try {
            String estadoAnterior = receta.getEstado();

            if (!"Lista".equals(estadoAnterior)) {
                JOptionPane.showMessageDialog(null,
                        "Solo se pueden entregar recetas en estado 'Lista'.\nEstado actual: " + estadoAnterior);
                return;
            }

            Service.instance().deleteReceta(receta);
            actualizarLista();
            Service.instance().stop();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al entregar receta: " + ex.getMessage());
        }
    }

    public void buscarReceta(String idReceta) {
        try {
            if (idReceta == null || idReceta.trim().isEmpty()) {
                actualizarLista();
            } else {
                Receta criterioBusqueda = new Receta();
                criterioBusqueda.setIdReceta(idReceta.trim());

                List<Receta> resultados = Service.instance().searchRecetaNoEntregadas(criterioBusqueda);
                model.setList(resultados);

                if (resultados.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "No se encontraron recetas con ID que contenga: " + idReceta);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error en la búsqueda: " + ex.getMessage());
        }
    }

    public void limpiarBusqueda() {
        actualizarLista();
    }

    public void verListaMedicamentos(List<Prescripcion> prescripciones) {
        try {
            model.setListaPrescripcion(prescripciones);
            model.setListmedicamentos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar medicamentos: " + ex.getMessage());
        }
    }
}