package hospital.presentation.Prescripcion;

import hospital.logic.Medicamento;
import hospital.logic.Prescripcion;
import hospital.presentation.Medicamentos.MedicamentosController;
import hospital.presentation.Medicamentos.MedicamentosModel;
import hospital.presentation.Medicamentos.MedicamentosTableModel;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View_buscarMedicamento extends JDialog implements PropertyChangeListener {
    private JPanel panel;
    private JTable tabla;
    private JLabel filtrar;
    private JComboBox filtrarOpciones;
    private JTextField filtrarTexto;
    private JButton buscar;
    private JButton OK;
    private JButton cancelar;

    private MedicamentosModel medicamentosModel;
    private MedicamentosController medicamentosController;
    private PrescripcionController controllerPr;

    public View_buscarMedicamento() {
        setContentPane(panel);
        setModal(true);
        setTitle("Buscar Medicamento");
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = filtrarOpciones.getSelectedIndex();
                try {
                    Medicamento filter = new Medicamento();
                    switch (selectedIndex) {
                        case 0: filter.setId(filtrarTexto.getText()); break;
                        case 1: filter.setNombre(filtrarTexto.getText()); break;
                        case 2: filter.setPresentacion(filtrarTexto.getText()); break;
                    }
                    medicamentosController.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                View_buscarMedicamento.this.setVisible(false);
            }
        });

        tabla.addMouseListener((new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tabla.getSelectedRow();
                medicamentosController.edit(row);
            }
        }));

        OK.addActionListener(e -> {
            int row = tabla.getSelectedRow();
            if (row != -1) {
                Medicamento m = medicamentosModel.getList().get(row);

                try {
                    controllerPr.createPrescripcionTemporal(
                            m.getNombre(),
                            m.getPresentacion(),
                            "0",
                            "N/A",
                            "0"
                    );

                    View_buscarMedicamento.this.setVisible(false);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Seleccione un medicamento.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setModel(MedicamentosModel medicamentosModel) {
        this.medicamentosModel = medicamentosModel;
        medicamentosModel.addPropertyChangeListener(this);
    }

    public void setController(MedicamentosController medicamentosController) {
        this.medicamentosController = medicamentosController;
    }

    public void setControllerPr(PrescripcionController controllerPr) {
        this.controllerPr = controllerPr;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case MedicamentosModel.LIST:
                int[] cols = {MedicamentosTableModel.ID, MedicamentosTableModel.NOMBRE, MedicamentosTableModel.PRESENTACION};
                tabla.setModel(new MedicamentosTableModel(cols, medicamentosModel.getList()));
                tabla.setRowHeight(30);
                TableColumnModel columnModel = tabla.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(100);
                columnModel.getColumn(1).setPreferredWidth(150);
                columnModel.getColumn(2).setPreferredWidth(120);
                break;
        }
        this.panel.revalidate();
    }
}