package hospital.presentation.Historico;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import hospital.presentation.Interfaces.InterfazAdministrador;
import hospital.logic.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class HistoricosController {
    Historico_View view;
    HistoricosModel model;

    public HistoricosController(Historico_View view, HistoricosModel model) {
        List<Receta> todasLasRecetas = Service.instance().findAllRecetas();

        for (Receta receta : todasLasRecetas) {
            if (receta.getPaciente() != null && receta.getPaciente().getId() != null &&
                    (receta.getPaciente().getNombre() == null || receta.getPaciente().getNombre().isEmpty())) {
                try {
                    Paciente pacienteCompleto = Service.instance().readPaciente(receta.getPaciente());
                    receta.setPaciente(pacienteCompleto);
                } catch (Exception e) {
                }
            }

            if (receta.getDoctor() != null && receta.getDoctor().getId() != null &&
                    (receta.getDoctor().getNombre() == null || receta.getDoctor().getNombre().isEmpty())) {
                try {
                    Usuario usuarioDoctor = Service.instance().buscarUsuario(receta.getDoctor().getId());
                    if (usuarioDoctor instanceof Medico) {
                        receta.setDoctor((Medico) usuarioDoctor);
                    }
                } catch (Exception e) {
                }
            }
        }

        model.init(todasLasRecetas);
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void search(Receta filter) throws Exception {
        model.setFilter(filter);
        model.setMode(InterfazAdministrador.MODE_CREATE);
        model.setCurrent(new Receta());

        List<Receta> recetas = Service.instance().searchRecetas(filter);

        for (Receta receta : recetas) {
            if (receta.getPaciente() != null && receta.getPaciente().getId() != null &&
                    (receta.getPaciente().getNombre() == null || receta.getPaciente().getNombre().isEmpty())) {
                try {
                    Paciente pacienteCompleto = Service.instance().readPaciente(receta.getPaciente());
                    receta.setPaciente(pacienteCompleto);
                } catch (Exception e) {
                    System.out.println("No se pudo cargar paciente " + receta.getPaciente().getId());
                }
            }

            if (receta.getDoctor() != null && receta.getDoctor().getId() != null &&
                    (receta.getDoctor().getNombre() == null || receta.getDoctor().getNombre().isEmpty())) {
                try {
                    Usuario usuarioDoctor = Service.instance().buscarUsuario(receta.getDoctor().getId());
                    if (usuarioDoctor instanceof Medico) {
                        receta.setDoctor((Medico) usuarioDoctor);
                    }
                } catch (Exception e) {
                    System.out.println("No se pudo cargar doctor " + receta.getDoctor().getId());
                }
            }
        }

        model.setList(recetas);
    }

    public void save(Receta current) throws Exception {
        switch (model.getMode()) {
            case InterfazAdministrador.MODE_CREATE:
                Service.instance().createReceta(current);
                break;
            case InterfazAdministrador.MODE_EDIT:
                Service.instance().updateReceta(current);
                break;
        }
        model.setCurrent(new Receta());
        search(model.getFilter());
    }


    public void edit(int row) {
        Receta receta = model.getList().get(row);
        try {
            model.setMode(InterfazAdministrador.MODE_EDIT);
            model.setCurrent(Service.instance().readReceta(receta));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delete() throws Exception {
        Service.instance().deleteReceta(model.getCurrent());
        search(model.getFilter());
    }

    public void clear() {
        model.setMode(InterfazAdministrador.MODE_CREATE);
        model.setCurrent(new Receta());
    }

}
