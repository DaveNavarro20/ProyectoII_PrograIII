package hospital.presentation.Mensajeria;

import hospital.logic.Mensaje;
import hospital.logic.Service;
import hospital.logic.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MensajeriaPanel extends JPanel {
    private Usuario usuarioActual;
    private Service service;

    private DefaultListModel<String> modeloUsuarios;
    private JList<String> listaUsuarios;
    private JButton btnEnviar;
    private JButton btnRefrescar;
    private JButton btnRecibirMensajes;
    private JTextArea areaMensajes;
    private Timer timer; // Para auto-refresh

    public MensajeriaPanel(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.service = Service.instance();

        inicializarComponentes();
        iniciarAutoRefresh();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior con información del usuario
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Usuario conectado: " + usuarioActual.getNombre() + " (" + usuarioActual.getId() + ")"));
        add(panelSuperior, BorderLayout.NORTH);

        // Panel central dividido
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(350);

        // Lado izquierdo: lista de usuarios activos
        JPanel panelUsuarios = new JPanel(new BorderLayout(5, 5));
        panelUsuarios.setBorder(BorderFactory.createTitledBorder("Usuarios Activos (logueados)"));

        modeloUsuarios = new DefaultListModel<>();
        listaUsuarios = new JList<>(modeloUsuarios);
        listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaUsuarios.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollUsuarios = new JScrollPane(listaUsuarios);
        scrollUsuarios.setPreferredSize(new Dimension(300, 300));
        panelUsuarios.add(scrollUsuarios, BorderLayout.CENTER);

        JPanel panelBotonesUsuarios = new JPanel(new GridLayout(3, 1, 5, 5));

        btnRefrescar = new JButton("🔄 Refrescar Lista");
        btnRefrescar.addActionListener(e -> cargarUsuariosActivos());

        btnEnviar = new JButton("✉️ Enviar Mensaje");
        btnEnviar.addActionListener(e -> enviarMensaje());

        btnRecibirMensajes = new JButton("📥 Recibir Mensajes");
        btnRecibirMensajes.addActionListener(e -> recibirMensajes());

        panelBotonesUsuarios.add(btnRefrescar);
        panelBotonesUsuarios.add(btnEnviar);
        panelBotonesUsuarios.add(btnRecibirMensajes);
        panelUsuarios.add(panelBotonesUsuarios, BorderLayout.SOUTH);

        splitPane.setLeftComponent(panelUsuarios);

        // Lado derecho: mensajes
        JPanel panelMensajes = new JPanel(new BorderLayout(5, 5));
        panelMensajes.setBorder(BorderFactory.createTitledBorder("Mensajes"));

        areaMensajes = new JTextArea();
        areaMensajes.setEditable(false);
        areaMensajes.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaMensajes.setLineWrap(true);
        areaMensajes.setWrapStyleWord(true);
        areaMensajes.setText("=== BANDEJA DE MENSAJES ===\n\n" +
                "• Seleccione un usuario y presione 'Enviar Mensaje' para enviar\n" +
                "• Presione 'Recibir Mensajes' para ver nuevos mensajes\n" +
                "• Los mensajes se actualizan automáticamente cada 5 segundos\n\n");

        JScrollPane scrollMensajes = new JScrollPane(areaMensajes);
        panelMensajes.add(scrollMensajes, BorderLayout.CENTER);

        JButton btnLimpiar = new JButton("🗑️ Limpiar");
        btnLimpiar.addActionListener(e -> {
            areaMensajes.setText("=== BANDEJA DE MENSAJES ===\n\n");
        });
        panelMensajes.add(btnLimpiar, BorderLayout.SOUTH);

        splitPane.setRightComponent(panelMensajes);

        add(splitPane, BorderLayout.CENTER);

        // Cargar usuarios activos al inicializar
        cargarUsuariosActivos();
    }

    private void iniciarAutoRefresh() {
        // Timer para refrescar mensajes cada 5 segundos
        timer = new Timer(5000, e -> {
            recibirMensajes();
            cargarUsuariosActivos();
        });
        timer.start();
    }

    private void cargarUsuariosActivos() {
        modeloUsuarios.clear();
        try {
            List<String> usuariosActivos = service.getUsuariosActivos();

            if (usuariosActivos.isEmpty()) {
                modeloUsuarios.addElement("(No hay otros usuarios conectados)");
                btnEnviar.setEnabled(false);
            } else {
                for (String userId : usuariosActivos) {
                    Usuario user = service.buscarUsuario(userId);
                    if (user != null) {
                        modeloUsuarios.addElement(userId + " - " + user.getNombre());
                    } else {
                        modeloUsuarios.addElement(userId);
                    }
                }
                btnEnviar.setEnabled(true);
            }
        } catch (Exception ex) {
            // Silencioso para no molestar con errores repetitivos
        }
    }

    private void enviarMensaje() {
        String seleccion = listaUsuarios.getSelectedValue();
        if (seleccion == null || seleccion.contains("(No hay otros usuarios")) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un usuario de la lista",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String destinatarioId = seleccion.split(" - ")[0];

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Destinatario: " + seleccion), BorderLayout.NORTH);

        JTextArea textArea = new JTextArea(5, 30);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        int resultado = JOptionPane.showConfirmDialog(this,
                panel,
                "Enviar Mensaje",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            String contenido = textArea.getText().trim();

            if (!contenido.isEmpty()) {
                try {
                    Mensaje mensaje = new Mensaje(
                            usuarioActual.getId(),
                            usuarioActual.getNombre(),
                            destinatarioId,
                            contenido
                    );

                    service.enviarMensaje(mensaje);

                    areaMensajes.append("=== MENSAJE ENVIADO ===\n");
                    areaMensajes.append("Para: " + seleccion + "\n");
                    areaMensajes.append("Mensaje: " + contenido + "\n");
                    areaMensajes.append("Fecha: " + java.time.LocalDateTime.now() + "\n");
                    areaMensajes.append("========================\n\n");

                    areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());

                    JOptionPane.showMessageDialog(this,
                            "Mensaje enviado exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error al enviar mensaje: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "El mensaje no puede estar vacío",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void recibirMensajes() {
        try {
            List<Mensaje> mensajes = service.getMensajesPendientes();

            if (!mensajes.isEmpty()) {
                for (Mensaje mensaje : mensajes) {
                    areaMensajes.append("★★★ MENSAJE RECIBIDO ★★★\n");
                    areaMensajes.append("De: " + mensaje.getRemitenteNombre() +
                            " (" + mensaje.getRemitenteId() + ")\n");
                    areaMensajes.append("Mensaje: " + mensaje.getContenido() + "\n");
                    areaMensajes.append("Fecha: " + mensaje.getFecha() + "\n");
                    areaMensajes.append("============================\n\n");
                }

                areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());

                // Notificación emergente solo para el primero (para no saturar)
                if (mensajes.size() == 1) {
                    Mensaje primer = mensajes.get(0);
                    JOptionPane.showMessageDialog(this,
                            "De: " + primer.getRemitenteNombre() + "\n" + primer.getContenido(),
                            "Nuevo Mensaje",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Tienes " + mensajes.size() + " mensajes nuevos",
                            "Nuevos Mensajes",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception ex) {
            System.err.println("Error recibiendo mensajes: " + ex.getMessage());
        }
    }

    public JPanel getPanel() {
        return this;
    }
}