package hospital.presentation.Despacho;

import hospital.logic.Prescripcion;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class View_VerMedicamentos implements PropertyChangeListener {
    private JTable Medicamentostable;
    private JPanel panel;

    private List<Prescripcion> objeto;
    private Model model;

    public List<Prescripcion> getObjeto() {
        return objeto;
    }

    public void setObjeto(List<Prescripcion> objeto) {
        this.objeto = objeto;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LISTMEDICAMENTOS:
                int[] colsm = {VerMedicamentosTableModel.NOMBRE,
                        VerMedicamentosTableModel.PRESENTACION,
                        VerMedicamentosTableModel.CANTIDAD,
                        VerMedicamentosTableModel.INDICACIONES,
                        VerMedicamentosTableModel.DURACION};

                List<Prescripcion> prescripciones = model != null && model.getListmedicamentos() != null
                        ? model.getListmedicamentos()
                        : getObjeto();

                if (prescripciones != null) {
                    Medicamentostable.setModel(new VerMedicamentosTableModel(colsm, prescripciones));
                    Medicamentostable.setRowHeight(30);
                } else {
                    JOptionPane.showMessageDialog(null, "No hay medicamentos para mostrar.");
                }
                break;
        }
    }
}