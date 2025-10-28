package hospital.presentation.Loggin;

import hospital.presentation.Interfaces.InterfazLogin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View_CambiarClave implements PropertyChangeListener {
    private JButton botonCancelar;
    private JButton botonAceptarCambio;
    private JPasswordField claveActualField;
    private JPasswordField claveNuevaField;
    private JPasswordField claveNuevaFieldConfirmacion;
    private JPanel panelCambiarCleve;
    private JLabel labelClaveActual;
    private JLabel labelClaveNueva;
    private JLabel labelClaveActual_Confirmacion;

    LoginModel model;
    LoginController controller;
    String idUsuario;

    public View_CambiarClave() {

        botonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                claveActualField.setText("");
                claveNuevaField.setText("");
                claveNuevaFieldConfirmacion.setText("");
            }
        });

        botonAceptarCambio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.cambiarClave();
            }
        });
    }

    public void setIdUsuario(String id) {
        this.idUsuario = id;
    }

    public String getIdUsuario() {return this.idUsuario;}

    public void setController(LoginController controller) {
        this.controller = controller;
    }

    public void setModel(LoginModel model){
        if (this.model != null) this.model.removePropertyChangeListener(this);
        this.model = model;
        this.model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "claveUsuario":
                JOptionPane.showMessageDialog(null,"Clave cambiada con éxito");
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panelCambiarCleve);
                topFrame.dispose();
                InterfazLogin.ventanaLogin();
                break;
            case "claveError":
                JOptionPane.showMessageDialog(null, evt.getNewValue(), "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    public JPanel getPanelCambiarClave(){
        return panelCambiarCleve;
    }

    public String getClaveActual() {
        return new String(claveActualField.getPassword());
    }

    public String getClaveNueva() {
        return new String(claveNuevaField.getPassword());
    }

    public String getClaveNuevaConfirmacion() {
        return new String(claveNuevaFieldConfirmacion.getPassword());
    }
}
