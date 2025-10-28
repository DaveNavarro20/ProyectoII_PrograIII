package hospital.presentation.Medicos;

import com.itextpdf.text.DocumentException;
import hospital.presentation.Interfaces.InterfazAdministrador;
import hospital.logic.Medico;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class Medicos_View implements PropertyChangeListener {
    private JPanel panel;
    private JLabel searchNombreLbl;
    private JTextField searchNombre;
    private JButton report;
    private JButton search;
    private JTable list;
    private JLabel idLbl;
    private JTextField IdJtext;
    private JLabel EspecialidadLbl;
    private JTextField EspecialidadJtext;
    private JLabel NombreLbl;
    private JTextField NombreJtext;
    private JButton save;
    private JButton delete;
    private JButton clear;

    public JPanel getPanel() {
        return panel;
    }

    public Medicos_View(){
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    Medico medico = take();
                    try {
                        controller.save(medico);
                        JOptionPane.showMessageDialog(panel, "REGISTRO APLICADO", "", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.delete();
                    JOptionPane.showMessageDialog(panel, "REGISTRO ELIMINADO", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Medico filter = new Medico();
                    filter.setNombre(searchNombre.getText());
                    controller.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = list.getSelectedRow();
                controller.edit(row);
            }
        });

        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    controller.generatePdfReport();
                    JOptionPane.showMessageDialog(panel, "REPORTE GENERADO", "PDF", JOptionPane.INFORMATION_MESSAGE);
                } catch (DocumentException | IOException ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error al generar el reporte", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public Medico take (){
        Medico medico = new Medico();
        medico.setId(IdJtext.getText());
        medico.setNombre(NombreJtext.getText());
        medico.setEspecialidad(EspecialidadJtext.getText());
        medico.setClave(IdJtext.getText());
        return medico;
    }

    public boolean validate(){
      boolean valid = true;
        if (IdJtext.getText().isEmpty()){
            valid=false;
            idLbl.setBorder(InterfazAdministrador.BORDER_ERROR);
            idLbl.setToolTipText("ID requerido");
        } else {
            idLbl.setBorder(null);
            idLbl.setToolTipText(null);
        }
        if (NombreJtext.getText().isEmpty()){
            valid=false;
            NombreLbl.setBorder(InterfazAdministrador.BORDER_ERROR);
            NombreLbl.setToolTipText("Nombre requerido");
        } else {
            NombreLbl.setBorder(null);
            NombreLbl.setToolTipText(null);
        }
        if (EspecialidadJtext.getText().isEmpty()){
            valid=false;
            EspecialidadLbl.setBorder(InterfazAdministrador.BORDER_ERROR);
            EspecialidadLbl.setToolTipText("Especialidad requerida");
        } else {
            EspecialidadLbl.setBorder(null);
            EspecialidadLbl.setToolTipText(null);
        }
        return valid;
    }

    MedicosModel model;
    MedicosController controller;

    public void setModel(MedicosModel model){
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(MedicosController controller){
        this.controller = controller;
    }
     @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case MedicosModel.LIST:
                int[] cols = {MedicosTableModel.ID, MedicosTableModel.NOMBRE, MedicosTableModel.ESPECIALIDAD};
                list.setModel(new MedicosTableModel(cols, model.getList()));
                list.setRowHeight(30);
                TableColumnModel columnModel = list.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(150);
                columnModel.getColumn(2).setPreferredWidth(150);
                break;
            case MedicosModel.CURRENT:
                IdJtext.setText(model.getCurrent().getId());
                NombreJtext.setText(model.getCurrent().getNombre());
                EspecialidadJtext.setText(model.getCurrent().getEspecialidad());

                if(model.getMode() == InterfazAdministrador.MODE_EDIT){
                    IdJtext.setEnabled(false);
                    delete.setEnabled(true);
                } else {
                    IdJtext.setEnabled(true);
                    delete.setEnabled(false);
                }
                idLbl.setBorder(null);
                idLbl.setToolTipText(null);
                NombreLbl.setBorder(null);
                NombreLbl.setToolTipText(null);
                EspecialidadLbl.setBorder(null);
                EspecialidadLbl.setToolTipText(null);
                break;
            case MedicosModel.FILTER:
                searchNombre.setText(model.getFilter().getNombre());
                break;
        }
        this.panel.revalidate();
    }

}
