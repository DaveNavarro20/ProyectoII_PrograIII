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

        setTitle("Usuarios Activos");
        setSize(350, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // No se puede cerrar
        setResizable(false);

        // Posicionar a la derecha de la ventana principal
        actualizarPosicion();

        // Agregar el panel de mensajería
        mensajeriaPanel = new MensajeriaPanel(usuario);
        setContentPane(mensajeriaPanel.getPanel());

        // Configurar icono
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(
                    getClass().getResource("/Imagenes/Receta.png")
            );
            setIconImage(icon);
        } catch (Exception e) {
            // Ignorar si no existe el icono
        }

        // Sincronizar posición cuando la ventana principal se mueve
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

        // Timer para mantener la posición sincronizada (por si acaso)
        sincronizadorPosicion = new Timer(100, e -> {
            if (isVisible() && ventanaPrincipal.isVisible()) {
                actualizarPosicion();
            }
        });
        sincronizadorPosicion.start();

        // Prevenir cierre con X
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // No hacer nada, no se puede cerrar
            }
        });

        // Siempre visible
        setAlwaysOnTop(false);
        setVisible(true);
    }

    private void actualizarPosicion() {
        if (ventanaPrincipal != null && ventanaPrincipal.isVisible()) {
            Point ubicacionPrincipal = ventanaPrincipal.getLocation();
            Dimension tamanoPrincipal = ventanaPrincipal.getSize();

            // Posicionar exactamente a la derecha (sin espacio)
            int y = ubicacionPrincipal.x + tamanoPrincipal.width;
            int x = ubicacionPrincipal.y;

            // Verificar que no se salga de la pantalla
            Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
            if (y + getWidth() > pantalla.width) {
                // Si no cabe a la izquierda, ponerla a la derecha
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