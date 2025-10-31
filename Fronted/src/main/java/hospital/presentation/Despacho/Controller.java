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

            // Mostrar mensaje de éxito en el cliente
            switch (estadoAnterior) {
                case "Confeccionada":
                    JOptionPane.showMessageDialog(null, "Estado actualizado: Confeccionada → Proceso");
                    break;
                case "Proceso":
                    JOptionPane.showMessageDialog(null, "Estado actualizado: Proceso → Lista");
                    break;
            }

            actualizarLista();

        } catch (Exception ex) {
            // Mostrar mensajes específicos según el error
            String mensaje = ex.getMessage();
            if (mensaje.contains("RECETA_YA_LISTA")) {
                JOptionPane.showMessageDialog(null,
                        "La receta está lista para entregar.\nUse el botón 'Entregar' para completar la entrega.");
            } else if (mensaje.contains("RECETA_YA_ENTREGADA")) {
                JOptionPane.showMessageDialog(null,
                        "Esta receta ya fue entregada y no se puede modificar.");
            } else if (mensaje.contains("ESTADO_NO_RECONOCIDO")) {
                JOptionPane.showMessageDialog(null, "Estado no reconocido");
            } else {
                JOptionPane.showMessageDialog(null, "Error al modificar estado: " + mensaje);
            }
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
            JOptionPane.showMessageDialog(null, "La receta ha sido entregada exitosamente");
            actualizarLista();

        } catch (Exception ex) {
            String mensaje = ex.getMessage();
            if (mensaje.contains("RECETA_YA_ENTREGADA")) {
                JOptionPane.showMessageDialog(null, "La receta ya fue entregada anteriormente");
            } else if (mensaje.contains("RECETA_NO_LISTA")) {
                JOptionPane.showMessageDialog(null,
                        "La receta no está lista para entregar.\nEstado actual: " + receta.getEstado());
            } else {
                JOptionPane.showMessageDialog(null, "Error al entregar receta: " + mensaje);
            }
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

    public Receta obtenerRecetaCompleta(Receta receta) throws Exception {
        return Service.instance().readReceta(receta);
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