package hospital.presentation.Prescripcion;

import hospital.logic.Paciente;
import hospital.presentation.Pacientes.PacientesTableModel;
import hospital.presentation.Pacientes.PacientesController;
import hospital.presentation.Pacientes.PacientesModel;

import java.awt.event.MouseAdapter;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View_buscarPaciente extends JDialog implements PropertyChangeListener {
    private JPanel panel;
    private JTable table;
    private JTextField filtrarTexto;
    private JComboBox filtrarOpciones;
    private JButton OK;
    private JButton cancelar;
    private JLabel filtrar;
    private JButton buscar;

    public View_buscarPaciente() {
        setContentPane(panel);
        setModal(true);
        setTitle("Buscar Paciente");
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                View_buscarPaciente.this.setVisible(false);
            }
        });

        OK.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Paciente p = model.getList().get(row);
                controllerPr.seleccionarPaciente(p);
                View_buscarPaciente.this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(panel, "Seleccione un paciente.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        table.addMouseListener((new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.getSelectedRow();
                controller.edit(row);
            }
        }));

        buscar.addActionListener(e -> {
            int selectedIndex = filtrarOpciones.getSelectedIndex();
            try {
                Paciente filter = new Paciente();
                switch (selectedIndex) {
                    case 0: filter.setId(filtrarTexto.getText()); break;
                    case 1: filter.setNombre(filtrarTexto.getText()); break;
                    case 2: filter.setFechaNacimiento(filtrarTexto.getText()); break;
                    case 3: filter.setTelefono(filtrarTexto.getText()); break;
                }
                controller.search(filter);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    PacientesModel model;
    PacientesController controller;
    PrescripcionController controllerPr;

    public void setControllerPr(PrescripcionController controllerPr) {
        this.controllerPr = controllerPr;
    }

    public void setModel(PacientesModel model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }
    public void setController(PacientesController controller){
        this.controller = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PacientesModel.LIST.equals(evt.getPropertyName())) {
            int[] cols = {PacientesTableModel.ID, PacientesTableModel.NOMBRE, PacientesTableModel.FECHA_NACIMIENTO, PacientesTableModel.TELEFONO};
            table.setModel(new PacientesTableModel(cols, model.getList()));
            table.setRowHeight(30);
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(100);
            columnModel.getColumn(1).setPreferredWidth(150);
            columnModel.getColumn(2).setPreferredWidth(120);
            columnModel.getColumn(3).setPreferredWidth(120);
        }
        this.panel.revalidate();
    }
}
