package hospital.presentation.Farmaceuticos;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import hospital.logic.Farmaceutico;
import hospital.logic.Service;
import hospital.presentation.Interfaces.InterfazAdministrador;
import java.io.FileOutputStream;
import java.io.IOException;

public class FarmaceuticosController {

    private Farmaceuticos_View view;
    private FarmaceuticosModel model;

    public FarmaceuticosController(Farmaceuticos_View view, FarmaceuticosModel model) {
        model.init(Service.instance().searchFarmaceutico(new Farmaceutico()));
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void search(Farmaceutico filter) throws Exception {
        model.setFilter(filter);
        model.setMode(InterfazAdministrador.MODE_CREATE);
        model.setCurrent(new Farmaceutico());
        model.setList(Service.instance().searchFarmaceutico(model.getFilter()));
    }


    public void save(Farmaceutico e) throws Exception {
        switch (model.getMode()) {
            case InterfazAdministrador.MODE_CREATE:
                Service.instance().createFarmaceutico(e);
                break;
            case InterfazAdministrador.MODE_EDIT:
                Service.instance().updateFarmaceutico(e);
                break;
        }
        model.setFilter(new Farmaceutico());
        search(model.getFilter());
    }

    public void edit(int row) {
        Farmaceutico farmaceutico = model.getList().get(row);
        try {
            model.setMode(InterfazAdministrador.MODE_EDIT);
            model.setCurrent(Service.instance().readFarmaceutico(farmaceutico));
        } catch (Exception ex) {}
    }

    public void delete() throws Exception {
        Service.instance().deleteFarmaceutico(model.getCurrent());
        search(model.getFilter());
    }

    public void clear() {
        model.setMode(InterfazAdministrador.MODE_CREATE);
        model.setCurrent(new Farmaceutico());
    }

    public void generatePdfReport() throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("FaramaceuticosReporte.pdf"));
        document.open();

        document.add(new Paragraph("Reporte de Faramaceuticos"));

        for (Farmaceutico farmaceutico : model.getList()) {
            document.add(new Paragraph("ID: " + farmaceutico.getId()));
            document.add(new Paragraph("Nombre: " + farmaceutico.getNombre()));
            document.add(new Paragraph("-----"));
        }

        document.close();
    }
}
