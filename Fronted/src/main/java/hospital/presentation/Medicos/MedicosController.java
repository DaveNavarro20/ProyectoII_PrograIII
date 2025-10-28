package hospital.presentation.Medicos;

import hospital.presentation.Interfaces.InterfazAdministrador;
import hospital.logic.Medico;
import hospital.logic.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class MedicosController {
    Medicos_View view;
    MedicosModel model;

    public MedicosController(Medicos_View view, MedicosModel model) {
        model.init(Service.instance().searchMedico(new Medico()));
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void search(Medico filter) throws Exception {
        model.setFilter(filter);
        model.setMode(InterfazAdministrador.MODE_CREATE);
        model.setCurrent(new Medico());
        model.setList(Service.instance().searchMedico(filter));
    }

    public void save(Medico current) throws Exception {
        switch (model.getMode()) {
            case InterfazAdministrador.MODE_CREATE:
                Service.instance().createMedico(current);
                break;
            case InterfazAdministrador.MODE_EDIT:
                Service.instance().updateMedico(current);
                break;
        }
        model.setCurrent(new Medico());
        search(model.getFilter());
    }

    public void edit(int row) {
        Medico medico = model.getList().get(row);
        try {
            model.setMode(InterfazAdministrador.MODE_EDIT);
            model.setCurrent(Service.instance().readMedico(medico));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delete() throws Exception {
        Service.instance().deleteMedico(model.getCurrent());
        search(model.getFilter());
    }

    public void clear() {
        model.setMode(InterfazAdministrador.MODE_CREATE);
        model.setCurrent(new Medico());
    }

    public void generatePdfReport() throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("MedicosReporte.pdf"));
        document.open();
        document.add(new Paragraph("Reporte de Cajeros"));

        for (Medico medico : model.getList()) {
            document.add(new Paragraph("ID: " + medico.getId()));
            document.add(new Paragraph("Nombre: " + medico.getNombre()));
            document.add(new Paragraph("Especialidad: " + medico.getEspecialidad()));
            document.add(new Paragraph("--------------------------------------------------"));
        }
        document.close();
    }
}
