package hospital.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Worker {
    Server srv;
    Socket s;
    Service service;
    ObjectOutputStream os;
    ObjectInputStream is;
    Usuario usuarioLogueado;

    public Worker(Server srv, Socket s, Service service) {
        try {
            this.srv = srv;
            this.s = s;
            os = new ObjectOutputStream(s.getOutputStream());
            is = new ObjectInputStream(s.getInputStream());
            this.service = service;
        } catch (IOException ex) {
            System.err.println("Error al crear Worker: " + ex.getMessage());
        }
    }

    boolean continuar;

    public void start() {
        try {
            System.out.println("Worker atendiendo peticiones...");
            Thread t = new Thread(new Runnable() {
                public void run() {
                    listen();
                }
            });
            continuar = true;
            t.start();
        } catch (Exception ex) {
            System.err.println("Error al iniciar Worker: " + ex.getMessage());
        }
    }

    public void stop() {
        continuar = false;
        System.out.println("Conexión cerrada...");
    }

    public void interrupt() {
        stop();
        try {
            if (s != null && !s.isClosed()) {
                s.close();
            }
        } catch (IOException ex) {
            System.err.println("Error al cerrar socket: " + ex.getMessage());
        }
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void listen() {
        int method;
        while (continuar) {
            try {
                method = is.readInt();
                System.out.println("Operación: " + method);

                switch (method) {
                    // ================= MÉDICOS =================
                    case Protocol.MEDICO_CREATE:
                        try {
                            service.createMedico((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MEDICO_READ:
                        try {
                            Medico medico = service.readMedico((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(medico);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MEDICO_UPDATE:
                        try {
                            service.updateMedico((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MEDICO_DELETE:
                        try {
                            service.deleteMedico((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MEDICO_SEARCH:
                        try {
                            List<Medico> medicos = service.searchMedico((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(medicos);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    // ================= FARMACÉUTICOS =================
                    case Protocol.FARMACEUTICO_CREATE:
                        try {
                            service.createFarmaceutico((Farmaceutico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.FARMACEUTICO_READ:
                        try {
                            Farmaceutico farm = service.readFarmaceutico((Farmaceutico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(farm);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.FARMACEUTICO_UPDATE:
                        try {
                            service.updateFarmaceutico((Farmaceutico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.FARMACEUTICO_DELETE:
                        try {
                            service.deleteFarmaceutico((Farmaceutico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.FARMACEUTICO_SEARCH:
                        try {
                            List<Farmaceutico> farms = service.searchFarmaceutico((Farmaceutico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(farms);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    // ================= ADMINISTRADORES =================
                    case Protocol.ADMINISTRADOR_READ:
                        try {
                            Administrador admin = service.readAdministrador((Administrador) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(admin);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.ADMINISTRADOR_UPDATE:
                        try {
                            service.updateAdministrador((Administrador) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    // ================= USUARIOS (Login) =================
                    case Protocol.USUARIO_LOGIN:
                        try {
                            String id = (String) is.readObject();
                            String clave = (String) is.readObject();
                            Usuario usuario = service.login(id, clave);
                            if (usuario != null) {
                                usuarioLogueado = usuario;
                                srv.registrarUsuarioActivo(usuario.getId(), this);
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(usuario);
                            } else {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.USUARIO_BUSCAR:
                        try {
                            String id = (String) is.readObject();
                            Usuario usuario = service.buscarUsuario(id);
                            if (usuario != null) {
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(usuario);
                            } else {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.USUARIO_ACTUALIZAR:
                        try {
                            Usuario usuario = (Usuario) is.readObject();
                            service.actualizarUsuario(usuario);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    // ================= NUEVAS OPERACIONES =================
                    case Protocol.USUARIO_GET_ACTIVOS:
                        try {
                            List<String> activos = srv.getUsuariosActivos();
                            if (usuarioLogueado != null) {
                                activos.remove(usuarioLogueado.getId());
                            }
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(activos);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.USUARIO_ENVIAR_MENSAJE:
                        try {
                            Mensaje mensaje = (Mensaje) is.readObject();
                            // Guardar el mensaje en la cola del destinatario
                            srv.agregarMensaje(mensaje.getDestinatarioId(), mensaje);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            System.out.println("Mensaje guardado: " + mensaje.getRemitenteId() +
                                    " -> " + mensaje.getDestinatarioId());
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.USUARIO_GET_MENSAJES:
                        try {
                            if (usuarioLogueado != null) {
                                List<Mensaje> mensajes = srv.getMensajesPendientes(usuarioLogueado.getId());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(mensajes);
                            } else {
                                os.writeInt(Protocol.ERROR_ERROR);
                            }
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    // ================= PACIENTES =================
                    case Protocol.PACIENTE_CREATE:
                        try {
                            service.createPaciente((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.PACIENTE_READ:
                        try {
                            Paciente pac = service.readPaciente((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(pac);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.PACIENTE_UPDATE:
                        try {
                            service.updatePaciente((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.PACIENTE_DELETE:
                        try {
                            service.deletePaciente((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.PACIENTE_SEARCH:
                        try {
                            List<Paciente> pacs = service.searchPaciente((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(pacs);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    // ================= MEDICAMENTOS =================
                    case Protocol.MEDICAMENTO_CREATE:
                        try {
                            service.createMedicamento((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MEDICAMENTO_READ:
                        try {
                            Medicamento med = service.readMedicamento((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(med);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MEDICAMENTO_UPDATE:
                        try {
                            service.updateMedicamento((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MEDICAMENTO_DELETE:
                        try {
                            service.deleteMedicamento((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MEDICAMENTO_SEARCH:
                        try {
                            List<Medicamento> meds = service.searchMedicamento((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(meds);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MEDICAMENTO_FINDALL:
                        try {
                            List<Medicamento> allMeds = service.findAll();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(allMeds);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    // ================= RECETAS =================
                    case Protocol.RECETA_CREATE:
                        try {
                            service.createReceta((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.RECETA_READ:
                        try {
                            Receta rec = service.readReceta((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(rec);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.RECETA_UPDATE:
                        try {
                            service.updateReceta((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.RECETA_DELETE:
                        try {
                            Receta receta = (Receta) is.readObject();
                            service.deleteReceta(receta);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.RECETA_MODIFICAR_ESTADO:
                        try {
                            Receta receta = (Receta) is.readObject();
                            service.modificarEstadoReceta(receta);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.RECETA_SEARCH:
                        try {
                            List<Receta> recs = service.searchRecetas((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(recs);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.RECETA_SEARCH_NO_ENTREGADAS:
                        try {
                            List<Receta> recs = service.searchRecetaNoEntregadas((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(recs);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.RECETA_FIND_POR_ESTADO:
                        try {
                            String estado = (String) is.readObject();
                            List<Receta> recs = service.findRecetasPorEstado(estado);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(recs);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.RECETA_FIND_NO_ENTREGADAS:
                        try {
                            List<Receta> recs = service.findRecetasNoEntregadas();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(recs);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.RECETA_FINDALL:
                        try {
                            List<Receta> allRecs = service.findAllRecetas();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(allRecs);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    // ================= DESCONEXIÓN =================
                    case Protocol.DISCONNECT:
                        stop();
                        srv.remove(this);
                        break;

                    default:
                        System.err.println("Operación desconocida: " + method);
                        os.writeInt(Protocol.ERROR_ERROR);
                        break;
                }
                os.flush();
            } catch (IOException e) {
                System.err.println("Error de I/O en Worker: " + e.getMessage());
                stop();
            }
        }
    }
}