package hospital.presentation.Mensajeria;

import hospital.logic.Mensaje;
import hospital.logic.Service;
import hospital.logic.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MensajeriaPanel extends JPanel {
    private Usuario usuarioActual;
    private Service service;

    private DefaultListModel<String> modeloUsuarios;
    private JList<String> listaUsuarios;
    private JButton btnEnviar;
    private JButton btnRefrescar;
    private JTextArea areaMensajes;
    private Timer timer;

    public MensajeriaPanel(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.service = Service.instance();

        inicializarComponentes();
        iniciarAutoRefresh();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        // Panel superior: Info usuario
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Tú"));
        JLabel lblUsuario = new JLabel(usuarioActual.getNombre());
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 12));
        lblUsuario.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelSuperior.add(lblUsuario, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // Panel central: Usuarios activos
        JPanel panelUsuarios = new JPanel(new BorderLayout(3, 3));
        panelUsuarios.setBorder(BorderFactory.createTitledBorder("Usuarios en Linea"));

        modeloUsuarios = new DefaultListModel<>();
        listaUsuarios = new JList<>(modeloUsuarios);
        listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaUsuarios.setFont(new Font("Arial", Font.PLAIN, 11));

        JScrollPane scrollUsuarios = new JScrollPane(listaUsuarios);
        scrollUsuarios.setPreferredSize(new Dimension(320, 150));
        panelUsuarios.add(scrollUsuarios, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 5, 5));
        panelBotones.setBorder(new EmptyBorder(3, 0, 0, 0));

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setFont(new Font("Arial", Font.PLAIN, 11));
        btnRefrescar.addActionListener(e -> cargarUsuariosActivos());

        btnEnviar = new JButton("Enviar");
        btnEnviar.setFont(new Font("Arial", Font.PLAIN, 11));
        btnEnviar.addActionListener(e -> enviarMensaje());

        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEnviar);
        panelUsuarios.add(panelBotones, BorderLayout.SOUTH);

        add(panelUsuarios, BorderLayout.CENTER);

        // Panel inferior: Mensajes
        JPanel panelMensajes = new JPanel(new BorderLayout(3, 3));
        panelMensajes.setBorder(BorderFactory.createTitledBorder("Mensajes Enviados"));

        areaMensajes = new JTextArea();
        areaMensajes.setEditable(false);
        areaMensajes.setFont(new Font("Monospaced", Font.PLAIN, 10));
        areaMensajes.setLineWrap(true);
        areaMensajes.setWrapStyleWord(true);

        JScrollPane scrollMensajes = new JScrollPane(areaMensajes);
        scrollMensajes.setPreferredSize(new Dimension(320, 200));
        panelMensajes.add(scrollMensajes, BorderLayout.CENTER);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Arial", Font.PLAIN, 11));
        btnLimpiar.addActionListener(e -> areaMensajes.setText(""));
        panelMensajes.add(btnLimpiar, BorderLayout.SOUTH);

        add(panelMensajes, BorderLayout.SOUTH);

        cargarUsuariosActivos();
    }

    private void iniciarAutoRefresh() {
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
                modeloUsuarios.addElement("(No hay usuarios conectados)");
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
            // Silencioso
        }
    }

    private void enviarMensaje() {
        String seleccion = listaUsuarios.getSelectedValue();
        if (seleccion == null || seleccion.contains("(No hay usuarios")) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un usuario",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String destinatarioId = seleccion.split(" - ")[0];

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Para: " + seleccion), BorderLayout.NORTH);

        JTextArea textArea = new JTextArea(4, 25);
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
                    areaMensajes.append("Para: " + destinatarioId + "\n");
                    areaMensajes.append("Mensaje: " + contenido + "\n");
                    areaMensajes.append("========================\n\n");

                    areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void recibirMensajes() {
        try {
            List<Mensaje> mensajes = service.getMensajesPendientes();

            if (!mensajes.isEmpty()) {
                for (Mensaje mensaje : mensajes) {
                    areaMensajes.append("=== MENSAJE RECIBIDO ===\n");
                    areaMensajes.append("De: " + mensaje.getRemitenteNombre() + "\n");
                    areaMensajes.append(mensaje.getContenido() + "\n");
                    areaMensajes.append("==========================\n\n");
                }

                areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());

                // Notificación solo si hay mensajes
                Mensaje primer = mensajes.get(0);
                JOptionPane.showMessageDialog(this,
                        "De: " + primer.getRemitenteNombre() + "\n" + primer.getContenido(),
                        "Nuevo Mensaje",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {}
    }

    public JPanel getPanel() {
        return this;
    }
}