package hospital.logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    ServerSocket ss;
    List<Worker> workers;

    public Server() {
        try {
            ss = new ServerSocket(Protocol.PORT);
            workers = Collections.synchronizedList(new ArrayList<Worker>());
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
        System.out.println("Cliente desconectado. Clientes restantes: " + workers.size());
    }

    public void shutdown() {
        try {
            System.out.println("Cerrando servidor...");
            for (Worker worker : workers) {
                worker.interrupt();
            }
            ss.close();
            Service.instance().stop();
            System.out.println("Servidor cerrado correctamente");
        } catch (IOException ex) {
            System.err.println("Error al cerrar servidor: " + ex.getMessage());
        }
    }
}
