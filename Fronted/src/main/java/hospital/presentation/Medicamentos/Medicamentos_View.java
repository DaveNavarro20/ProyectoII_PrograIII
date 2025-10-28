package hospital.presentation.Medicamentos;

import hospital.presentation.Interfaces.InterfazAdministrador;
import hospital.logic.Medicamento;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Medicamentos_View implements PropertyChangeListener {
    private JPanel panel;
    private JLabel searchNombreLbl;
    private JTextField searchNombre;
    private JButton report;
    private JButton search;
    private JTable list;
    private JLabel idLbl;
    private JTextField IdJtext;
    private JLabel PresentacionLbl;
    private JLabel NombreLbl;
    private JTextField NombreJtext;
    private JButton save;
    private JButton delete;
    private JButton clear;
    private JTextField PresentacionJtext;

    public JPanel getPanel() {
        return panel;
    }

    public Medicamentos_View(){


        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(validate()){
                    Medicamento n = take();
                    try {
                        medicamentosController.save(n);
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
                medicamentosController.clear();
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    medicamentosController.delete();
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
                    medicamentosController.search(new Medicamento("", searchNombre.getText(), ""));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    medicamentosController.generatePdfReport();
                    JOptionPane.showMessageDialog(panel, "REPORTE GENERADO", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        list.addMouseListener((new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = list.getSelectedRow();
                medicamentosController.edit(row);
            }
        }));
    }

    private boolean validate(){
        boolean valid = true;
        if(IdJtext.getText().isEmpty()){
            valid = false;
            idLbl.setBorder(InterfazAdministrador.BORDER_ERROR);
            idLbl.setToolTipText("Id requerido");
        } else {
            idLbl.setBorder(null);
            idLbl.setToolTipText(null);
        }
        if(NombreJtext.getText().isEmpty()){
            valid = false;
            NombreLbl.setBorder(InterfazAdministrador.BORDER_ERROR);
            NombreLbl.setToolTipText("Nombre requerido");
        } else {
            NombreLbl.setBorder(null);
            NombreLbl.setToolTipText(null);
        }
        if(PresentacionJtext.getText().isEmpty()){
            valid = false;
            PresentacionLbl.setBorder(InterfazAdministrador.BORDER_ERROR);
            PresentacionLbl.setToolTipText("Presentacion requerida");
        } else {
            PresentacionLbl.setBorder(null);
            PresentacionLbl.setToolTipText(null);
        }
        return valid;
    }

    public Medicamento take(){
        Medicamento medicamento = new Medicamento();
        medicamento.setId(IdJtext.getText());
        medicamento.setNombre(NombreJtext.getText());
        medicamento.setPresentacion(PresentacionJtext.getText());

        return medicamento;
    }

    // MVC
    MedicamentosModel medicamentosModel;
    MedicamentosController medicamentosController;

    public void setModel(MedicamentosModel medicamentosModel){
        this.medicamentosModel = medicamentosModel;
        medicamentosModel.addPropertyChangeListener(this);
    }

    public void setController(MedicamentosController medicamentosController){
        this.medicamentosController = medicamentosController;
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()){
            case MedicamentosModel.LIST:
                int[] cols={MedicamentosTableModel.ID, MedicamentosTableModel.NOMBRE, MedicamentosTableModel.PRESENTACION};
                list.setModel(new MedicamentosTableModel(cols, medicamentosModel.getList()));
                list.setRowHeight(30);
                TableColumnModel columnModel = list.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(150);
                columnModel.getColumn(2).setPreferredWidth(150);
                break;
            case MedicamentosModel.CURRENT:
                IdJtext.setText(medicamentosModel.getCurrent().getId());
                NombreJtext.setText(medicamentosModel.getCurrent().getNombre());
                PresentacionJtext.setText(medicamentosModel.getCurrent().getPresentacion());

                if (medicamentosModel.getMode() == InterfazAdministrador.MODE_EDIT){
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
                PresentacionLbl.setBorder(null);
                PresentacionLbl.setToolTipText(null);
                break;
            case MedicamentosModel.FILTER:
                searchNombre.setText(medicamentosModel.getFilter().getNombre());
                break;
        }
        this.panel.revalidate();
    }
}
