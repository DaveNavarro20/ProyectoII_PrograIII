package hospital.presentation.Pacientes;

import com.github.lgooddatepicker.components.DatePicker;
import com.itextpdf.text.DocumentException;
import hospital.logic.Paciente;
import hospital.presentation.Interfaces.InterfazAdministrador;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class Pacientes_View implements PropertyChangeListener {
    private JPanel panel;
    private JLabel searchNombreLbl;
    private JTextField searchNombre;
    private JButton report;
    private JButton search;
    private JTable list;
    private JLabel idLbl;
    private JTextField IdJtext;
    private JLabel NacimientoLbl;
    private JLabel NombreLbl;
    private JTextField NombreJtext;
    private JButton save;
    private JButton delete;
    private JButton clear;
    private JLabel TelefonoLbl;
    private DatePicker NacimientoPicker;
    private JTextField TelefenoJText;

    public JPanel getPanel() {
        return panel;
    }

    public Pacientes_View() {
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    Paciente n = take();
                    try {
                        controller.save(n);
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
                    Paciente filter = new Paciente();
                    filter.setNombre(searchNombre.getText());
                    controller.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.generatePdfReport();
                    JOptionPane.showMessageDialog(panel, "REPORTE GENERADO", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (DocumentException | IOException ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        list.addMouseListener((new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = list.getSelectedRow();
                controller.edit(row);
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
        if(NacimientoPicker.getText().isEmpty()){
            valid = false;
            NacimientoLbl.setBorder(InterfazAdministrador.BORDER_ERROR);
            NacimientoLbl.setToolTipText("Fecha de nacimiento requerida");
        } else {
            NacimientoLbl.setBorder(null);
            NacimientoLbl.setToolTipText(null);
        }
        if(TelefenoJText.getText().isEmpty()){
            valid = false;
            TelefonoLbl.setBorder(InterfazAdministrador.BORDER_ERROR);
            TelefonoLbl.setToolTipText("Teléfono requerido");
        } else {
            TelefonoLbl.setBorder(null);
            TelefonoLbl.setToolTipText(null);
        }
        return valid;
    }

    public Paciente take(){
        Paciente paciente = new Paciente();
        paciente.setId(IdJtext.getText());
        paciente.setNombre(NombreJtext.getText());
        paciente.setFechaNacimiento(NacimientoPicker.getText());
        paciente.setTelefono(TelefenoJText.getText());
        return paciente;
    }

    // MVC
    PacientesModel model;
    PacientesController controller;

    public void setModel(PacientesModel model){
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(PacientesController controller){
        this.controller = controller;
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()){
            case PacientesModel.LIST:
                int[] cols={PacientesTableModel.ID, PacientesTableModel.NOMBRE, PacientesTableModel.FECHA_NACIMIENTO, PacientesTableModel.TELEFONO};
                list.setModel(new PacientesTableModel(cols, model.getList()));
                list.setRowHeight(30);
                TableColumnModel columnModel = list.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(150);
                columnModel.getColumn(2).setPreferredWidth(150);
                columnModel.getColumn(3).setPreferredWidth(150);
                break;
            case PacientesModel.CURRENT:
                IdJtext.setText(model.getCurrent().getId());
                NombreJtext.setText(model.getCurrent().getNombre());
                NacimientoPicker.setText(model.getCurrent().getFechaNacimiento());
                TelefenoJText.setText(model.getCurrent().getTelefono());

                if (model.getMode() == InterfazAdministrador.MODE_EDIT){
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
                NacimientoLbl.setBorder(null);
                NacimientoLbl.setToolTipText(null);
                TelefonoLbl.setBorder(null);
                TelefonoLbl.setToolTipText(null);
                break;
            case PacientesModel.FILTER:
                searchNombre.setText(model.getFilter().getNombre());
                break;
        }
        this.panel.revalidate();
    }
}
