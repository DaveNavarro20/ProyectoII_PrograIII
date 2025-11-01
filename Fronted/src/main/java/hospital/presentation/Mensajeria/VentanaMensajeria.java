package hospital.presentation.Mensajeria;

import hospital.logic.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaMensajeria extends JFrame {
    private MensajeriaPanel mensajeriaPanel;
    private JFrame ventanaPrincipal;
    private Timer sincronizadorPosicion;

    public VentanaMensajeria(Usuario usuario, JFrame ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;

        setTitle("Chat");
        setSize(350, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Evita que la ventana de los mensajes se cierre
        setResizable(false);
        actualizarPosicion();
        mensajeriaPanel = new MensajeriaPanel(usuario);
        setContentPane(mensajeriaPanel.getPanel());
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(
                    getClass().getResource("/Imagenes/Mensajes.png")
            );
            setIconImage(icon);
        } catch (Exception e) {}

        ventanaPrincipal.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                actualizarPosicion();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                actualizarPosicion();
            }
        });

        sincronizadorPosicion = new Timer(100, e -> {
            if (isVisible() && ventanaPrincipal.isVisible()) {
                actualizarPosicion();
            }
        });
        sincronizadorPosicion.start();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {}
        });
        setAlwaysOnTop(false);
        setVisible(true);
    }
    private void actualizarPosicion() {
        if (ventanaPrincipal != null && ventanaPrincipal.isVisible()) {
            Point ubicacionPrincipal = ventanaPrincipal.getLocation();
            Dimension tamanoPrincipal = ventanaPrincipal.getSize();
            int y = ubicacionPrincipal.x + tamanoPrincipal.width;
            int x = ubicacionPrincipal.y;
            Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
            if (y + getWidth() > pantalla.width) {
                y = ubicacionPrincipal.x - getWidth();
            }
            setLocation(y, x);
        }
    }
    public void cerrar() {
        if (sincronizadorPosicion != null) {
            sincronizadorPosicion.stop();
        }
        dispose();
    }
}