package hospital.logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    ServerSocket ss;
    List<Worker> workers;
    Map<String, Worker> usuariosActivos;
    Map<String, List<Mensaje>> mensajesPendientes;

    public Server() {
        try {
            ss = new ServerSocket(Protocol.PORT);
            workers = Collections.synchronizedList(new ArrayList<Worker>());
            usuariosActivos = Collections.synchronizedMap(new HashMap<>());
            mensajesPendientes = Collections.synchronizedMap(new HashMap<>());
            System.out.println("Servidor iniciado en puerto " + Protocol.PORT + "...");
        } catch (IOException ex) {
            System.err.println("Error al iniciar el servidor: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        Service service = Service.instance();
        boolean continuar = true;
        Socket s;
        Worker worker;

        while (continuar) {
            try {
                s = ss.accept();
                System.out.println("Conexión establecida desde: " + s.getInetAddress().getHostAddress());
                worker = new Worker(this, s, service);
                workers.add(worker);
                System.out.println("Clientes conectados: " + workers.size());
                worker.start();
            } catch (IOException ex) {
                System.err.println("Error al aceptar conexión: " + ex.getMessage());
            } catch (Exception ex) {
                System.err.println("Error inesperado: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public void remove(Worker w) {
        workers.remove(w);
        if (w.getUsuarioLogueado() != null) {
            usuariosActivos.remove(w.getUsuarioLogueado().getId());
            System.out.println("Usuario " + w.getUsuarioLogueado().getId() + " desconectado");
        }
        System.out.println("Cliente desconectado. Clientes restantes: " + workers.size());
    }

    public void registrarUsuarioActivo(String usuarioId, Worker worker) {
        usuariosActivos.put(usuarioId, worker);
        if (!mensajesPendientes.containsKey(usuarioId)) {
            mensajesPendientes.put(usuarioId, Collections.synchronizedList(new ArrayList<>()));
        }
        System.out.println("Usuario " + usuarioId + " registrado como activo");
        System.out.println("Usuarios activos: " + usuariosActivos.keySet());
    }

    public List<String> getUsuariosActivos() {
        return new ArrayList<>(usuariosActivos.keySet());
    }

    // Agregar mensaje a la cola del destinatario
    public void agregarMensaje(String destinatarioId, Mensaje mensaje) {
        if (!mensajesPendientes.containsKey(destinatarioId)) {
            mensajesPendientes.put(destinatarioId, Collections.synchronizedList(new ArrayList<>()));
        }
        mensajesPendientes.get(destinatarioId).add(mensaje);
        System.out.println("Mensaje agregado para " + destinatarioId + ". Total pendientes: " +
                mensajesPendientes.get(destinatarioId).size());
    }

    // Obtener mensajes pendientes de un usuario
    public List<Mensaje> getMensajesPendientes(String usuarioId) {
        List<Mensaje> mensajes = mensajesPendientes.get(usuarioId);
        if (mensajes != null && !mensajes.isEmpty()) {
            List<Mensaje> copia = new ArrayList<>(mensajes);
            mensajes.clear();
            System.out.println("Devolviendo " + copia.size() + " mensajes a " + usuarioId);
            return copia;
        }
        return new ArrayList<>();
    }

}