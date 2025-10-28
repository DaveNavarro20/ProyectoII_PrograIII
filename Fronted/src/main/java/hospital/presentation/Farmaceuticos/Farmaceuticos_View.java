package hospital.presentation.Farmaceuticos;

import com.itextpdf.text.DocumentException;
import hospital.logic.Farmaceutico;
import hospital.presentation.Interfaces.InterfazAdministrador;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class Farmaceuticos_View  implements PropertyChangeListener {
    private JPanel panel;
    private JLabel searchNombreLbl;
    private JTextField searchNombre;
    private JButton report;
    private JButton search;
    private JTable list;
    private JLabel idLbl;
    private JTextField IdJtext;
    private JLabel NombreLbl;
    private JTextField NombreJtext;
    private JButton save;
    private JButton delete;
    private JButton clear;

    public JPanel getPanel() {
        return panel;
    }

    public Farmaceuticos_View(){

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    Farmaceutico farmaceutico = take();
                    try {
                        controller.save(farmaceutico);
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
                    Farmaceutico filter = new Farmaceutico();
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
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = list.getSelectedRow();
                controller.edit(row);
            }
        });
    }

    private boolean validate(){
        boolean valid = true;
        if(IdJtext.getText().isEmpty()){
            idLbl.setBorder(InterfazAdministrador.BORDER_ERROR);
            idLbl.setToolTipText("Id requerido");
            valid = false;
        } else {
            idLbl.setBorder(null);
            idLbl.setToolTipText(null);
        }
        if(NombreJtext.getText().isEmpty()){
            NombreLbl.setBorder(InterfazAdministrador.BORDER_ERROR);
            NombreLbl.setToolTipText("Nombre requerido");
            valid = false;
        } else {
            NombreLbl.setBorder(null);
            NombreLbl.setToolTipText(null);
        }
        return valid;
    }

    public Farmaceutico take(){
        Farmaceutico farmaceutico = new Farmaceutico();
        farmaceutico.setId(IdJtext.getText());
        farmaceutico.setNombre(NombreJtext.getText());
        farmaceutico.setClave(IdJtext.getText());
        return farmaceutico;
    }

    //MVC
    FarmaceuticosModel model;
    FarmaceuticosController controller;

    public void setModel(FarmaceuticosModel model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(FarmaceuticosController controller) {
        this.controller = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()){
            case FarmaceuticosModel.LIST:
                int[] cols = {FarmaceuticosTableModel.ID, FarmaceuticosTableModel.NOMBRE};
                list.setModel(new FarmaceuticosTableModel(cols, model.getList()));
                list.setRowHeight(30);
                TableColumnModel columnModel = list.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(150);
                break;
            case FarmaceuticosModel.CURRENT:
                IdJtext.setText(model.getCurrent().getId());
                NombreJtext.setText(model.getCurrent().getNombre());

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
                break;
            case FarmaceuticosModel.FILTER:
                searchNombre.setText(model.getFilter().getNombre());
                break;
        }
        this.panel.revalidate();
    }
}
