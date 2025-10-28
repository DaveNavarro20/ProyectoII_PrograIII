package hospital.presentation.Pacientes;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import hospital.logic.Paciente;
import hospital.logic.Service;
import hospital.presentation.Interfaces.InterfazAdministrador;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PacientesController {
    private Pacientes_View view;
    private PacientesModel model;

    public PacientesController(Pacientes_View view, PacientesModel model) {
        this.view = view;
        this.model = model;
        List<Paciente> allPacientes = Service.instance().searchPaciente(new Paciente());
        model.init(allPacientes);

        view.setController(this);
        view.setModel(model);
    }

    public void search(Paciente filter) throws Exception {
        model.setFilter(filter);
        model.setMode(InterfazAdministrador.MODE_CREATE);
        model.setCurrent(new Paciente());

        List<Paciente> result = Service.instance().searchPaciente(new Paciente()).stream()
                .filter(p -> filter.getId() == null || filter.getId().isEmpty() || p.getId().equalsIgnoreCase(filter.getId()))
                .filter(p -> filter.getNombre() == null || filter.getNombre().isEmpty() || p.getNombre().toLowerCase().contains(filter.getNombre().toLowerCase()))
                .filter(p -> filter.getFechaNacimiento() == null || filter.getFechaNacimiento().isEmpty() || p.getFechaNacimiento().equals(filter.getFechaNacimiento()))
                .filter(p -> filter.getTelefono() == null || filter.getTelefono().isEmpty() || p.getTelefono().equals(filter.getTelefono()))
                .collect(Collectors.toList());

        model.setList(result);
    }

    public void save(Paciente e) throws Exception {
        switch (model.getMode()) {
            case InterfazAdministrador.MODE_CREATE:
                Service.instance().createPaciente(e);
                break;
            case InterfazAdministrador.MODE_EDIT:
                Service.instance().updatePaciente(e);
                break;
        }
        model.setFilter(new Paciente());
        search(model.getFilter());
    }

    public void edit(int row) {
        Paciente paciente = model.getList().get(row);
        try {
            model.setMode(InterfazAdministrador.MODE_EDIT);
            model.setCurrent(Service.instance().readPaciente(paciente));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delete() throws Exception {
        Service.instance().deletePaciente(model.getCurrent());
        search(model.getFilter());
    }

    public void clear() {
        model.setMode(InterfazAdministrador.MODE_CREATE);
        model.setCurrent(new Paciente());
    }

    public void generatePdfReport() throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("PacientesReporte.pdf"));
        document.open();

        document.add(new Paragraph("Reporte de Pacientes"));

        for (Paciente paciente : model.getList()) {
            document.add(new Paragraph("ID: " + paciente.getId()));
            document.add(new Paragraph("Nombre: " + paciente.getNombre()));
            document.add(new Paragraph("Fecha de Nacimiento: " + paciente.getFechaNacimiento()));
            document.add(new Paragraph("Teléfono: " + paciente.getTelefono()));
            document.add(new Paragraph("-----"));
        }

        document.close();
    }
}
