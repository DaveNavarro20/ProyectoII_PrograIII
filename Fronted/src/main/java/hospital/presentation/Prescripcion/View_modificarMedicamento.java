package hospital.presentation.Prescripcion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class View_modificarMedicamento extends JDialog {
    private JButton guardar;
    private JSpinner cantidadS;
    private JSpinner duracionS;
    private JButton cancelar;
    private JTextField indicacionesT;
    private JLabel cantidad;
    private JLabel duracion;
    private JLabel indicaciones;
    private JPanel panel;

    public View_modificarMedicamento() {
        setContentPane(panel);
        setModal(true);
        setTitle("Modificar Medicamento");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                limpiarCampos();
            }
        });

        cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
                View_modificarMedicamento.this.setVisible(false);
            }
        });

        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setCambios(cantidadS.getValue().toString(), duracionS.getValue().toString(), indicacionesT.getText());
                try {
                    controller.aplicarCambios();
                    limpiarCampos();
                    View_modificarMedicamento.this.setVisible(false);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void limpiarCampos() {
        cantidadS.setValue(0);
        duracionS.setValue(0);
        indicacionesT.setText("");
    }

    public JPanel getPanel() {
        return panel;
    }

    PrescripcionController controller;
    PrescripcionModel model;

    public void setController(PrescripcionController controller) {
        this.controller = controller;
    }

    public void setModel(PrescripcionModel model) {
        this.model = model;
    }
}