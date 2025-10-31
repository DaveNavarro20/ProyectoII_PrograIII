package hospital.presentation.Prescripcion;

import hospital.presentation.Interfaces.InterfazAdministrador;
import hospital.logic.*;
import hospital.presentation.Medicamentos.MedicamentosController;
import hospital.presentation.Medicamentos.MedicamentosModel;
import hospital.presentation.Medicamentos.Medicamentos_View;
import hospital.presentation.Pacientes.PacientesController;
import hospital.presentation.Pacientes.PacientesModel;
import hospital.presentation.Pacientes.Pacientes_View;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PrescripcionController {
    private View_Prescripcion view;
    private PrescripcionModel model;

    private List<Prescripcion> prescripcionesTemporales;
    private String recetaTemporalId;
    private PacientesModel pacientesModel;
    private Pacientes_View pacientesView;
    private PacientesController pacientesController;

    private MedicamentosModel medicamentosModel;
    private Medicamentos_View medicamentosView;
    private MedicamentosController medicamentosController;

    public String cantidadAux;
    public String duracionAux;
    public String indicacionesAux;

    public PrescripcionController(View_Prescripcion view, PrescripcionModel model) {
        this.prescripcionesTemporales = new ArrayList<>();
        this.recetaTemporalId = null;

        model.init(this.prescripcionesTemporales);

        this.view = view;
        this.model = model;

        this.pacientesModel = new PacientesModel();
        this.pacientesView = new Pacientes_View();
        this.pacientesController = new PacientesController(pacientesView, pacientesModel);

        this.medicamentosModel = new MedicamentosModel();
        this.medicamentosView = new Medicamentos_View();
        this.medicamentosController = new MedicamentosController(medicamentosView, medicamentosModel);

        view.setController(this);
        view.setModel(model);
    }

    public PacientesModel getPacientesModel() {
        return pacientesModel;
    }

    public PacientesController getPacientesController() {
        return pacientesController;
    }

    public MedicamentosModel getMedicamentosModel() {
        return medicamentosModel;
    }

    public MedicamentosController getMedicamentosController() {
        return medicamentosController;
    }

    public void seleccionarPaciente(Paciente p) {
        model.setCurrentPaciente(p);
    }

    public void search(Prescripcion filter) throws Exception {
        model.setFilter(filter);
        model.setMode(InterfazAdministrador.MODE_CREATE);
        model.setCurrent(new Prescripcion());

        model.setList(new ArrayList<>(prescripcionesTemporales));
    }

    public void addPrescripcionTemporal(Prescripcion prescripcion) throws Exception {
        boolean existe = prescripcionesTemporales.stream()
                .anyMatch(p -> p.getNombre().equals(prescripcion.getNombre()) &&
                        p.getPresentacion().equals(prescripcion.getPresentacion()));

        if (!existe) {
            prescripcionesTemporales.add(prescripcion);
            model.setList(new ArrayList<>(prescripcionesTemporales));
        } else {
            throw new Exception("Ya existe una prescripción con el mismo medicamento y presentación");
        }
    }

    public void createPrescripcionTemporal(String nombre, String presentacion, String cantidad, String indicaciones, String duracion) {
        Prescripcion nueva = new Prescripcion(nombre, presentacion, cantidad, indicaciones, duracion);
        try {
            addPrescripcionTemporal(nueva);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void save(Prescripcion p) throws Exception {
        throw new UnsupportedOperationException("Las prescripciones se guardan como parte de la receta");
    }

    public void edit(int row) {
        if (row >= 0 && row < prescripcionesTemporales.size()) {
            Prescripcion prescripcion = prescripcionesTemporales.get(row);
            model.setMode(InterfazAdministrador.MODE_EDIT);
            model.setCurrent(prescripcion);
        }
    }

    public void delete() throws Exception {
        Prescripcion current = model.getCurrent();
        if (current != null && current.getNombre() != null) {
            prescripcionesTemporales.removeIf(p ->
                    p.getNombre().equals(current.getNombre()) &&
                            p.getPresentacion().equals(current.getPresentacion())
            );
            model.setList(new ArrayList<>(prescripcionesTemporales));
        }
    }

    public void clear() {
        model.setMode(InterfazAdministrador.MODE_CREATE);
        model.setCurrent(new Prescripcion());
    }

    public void clearTemporalList() {
        prescripcionesTemporales.clear();
        model.setList(new ArrayList<>(prescripcionesTemporales));
        model.setCurrentPaciente(null);
        recetaTemporalId = null;
    }

    public void setCambios(String cantidad, String duracion, String indicaciones){
        cantidadAux = cantidad;
        duracionAux = duracion;
        indicacionesAux = indicaciones;
    }

    public void aplicarCambios() throws Exception {
        Prescripcion vieja = model.getCurrent();
        if (vieja.getNombre() == null || vieja.getNombre().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleccione un Medicamento", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Prescripcion nueva = new Prescripcion(
                vieja.getNombre(),
                vieja.getPresentacion(),
                cantidadAux,
                indicacionesAux,
                duracionAux
        );
        for (int i = 0; i < prescripcionesTemporales.size(); i++) {
            Prescripcion p = prescripcionesTemporales.get(i);
            if (p.getNombre().equals(vieja.getNombre()) &&
                    p.getPresentacion().equals(vieja.getPresentacion())) {
                prescripcionesTemporales.set(i, nueva);
                break;
            }
        }

        model.setList(new ArrayList<>(prescripcionesTemporales));
        model.setCurrent(nueva);
    }

    public void createReceta(Receta receta) throws Exception {
        receta.setPrescripcions(new ArrayList<>(prescripcionesTemporales));

        if (receta.getPaciente() != null && receta.getPaciente().getId() != null) {
            try {
                Paciente pacienteCompleto = Service.instance().readPaciente(receta.getPaciente());
                receta.setPaciente(pacienteCompleto);
            } catch (Exception e) {
            }
        }

        if (receta.getDoctor() != null && receta.getDoctor().getId() != null) {
            try {
                Usuario usuarioDoctor = Service.instance().buscarUsuario(receta.getDoctor().getId());
                if (usuarioDoctor instanceof Medico) {
                    receta.setDoctor((Medico) usuarioDoctor);
                }
            } catch (Exception e) {
            }
        }

        Service.instance().createReceta(receta);

        clearTemporalList();
    }

    public List<Prescripcion> getPrescripcionesTemporales() {
        return new ArrayList<>(prescripcionesTemporales);
    }
}