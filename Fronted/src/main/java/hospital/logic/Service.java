package hospital.logic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Service {
    private static Service theInstance;

    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    Socket s;
    ObjectOutputStream os;
    ObjectInputStream is;
    List<Consumer<Mensaje>> observadores = new ArrayList<>();

    private Service() {
        try {
            s = new Socket(Protocol.SERVER, Protocol.PORT);
            os = new ObjectOutputStream(s.getOutputStream());
            is = new ObjectInputStream(s.getInputStream());
        } catch (Exception e) {
            System.exit(-1);
        }
    }

    public void agregarObservadorMensajes(Consumer<Mensaje> observador) {
        observadores.add(observador);
    }

    private void notificarObservadores(Mensaje mensaje) {
        for (Consumer<Mensaje> observador : observadores) {
            observador.accept(mensaje);
        }
    }

    public void stop() {
        try {
            disconnect();
        } catch (Exception e) {
            System.exit(-1);
        }
    }

    private void disconnect() throws Exception {
        os.writeInt(Protocol.DISCONNECT);
        os.flush();
        s.shutdownOutput();
        s.close();
    }

    // ================= Mensajes y Usuarios =================

    public List<String> getUsuariosActivos() {
        try {
            os.writeInt(Protocol.USUARIO_GET_ACTIVOS);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<String>) is.readObject();
            } else {
                return List.of();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void enviarMensaje(Mensaje mensaje) throws Exception {
        os.writeInt(Protocol.USUARIO_ENVIAR_MENSAJE);
        os.writeObject(mensaje);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("ERROR AL ENVIAR MENSAJE");
        }
    }

    public List<Mensaje> getMensajesPendientes() {
        try {
            os.writeInt(Protocol.USUARIO_GET_MENSAJES);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Mensaje>) is.readObject();
            } else {
                return List.of();
            }
        } catch (Exception ex) {
            System.err.println("Error obteniendo mensajes: " + ex.getMessage());
            return List.of();
        }
    }

    // ================= MÉDICOS ================= //

    public void createMedico(Medico medico) throws Exception {
        os.writeInt(Protocol.MEDICO_CREATE);
        os.writeObject(medico);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MÉDICO DUPLICADO");
    }

    public Medico readMedico(Medico medico) throws Exception {
        os.writeInt(Protocol.MEDICO_READ);
        os.writeObject(medico);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Medico) is.readObject();
        else throw new Exception("MÉDICO NO EXISTE");
    }

    public void updateMedico(Medico medico) throws Exception {
        os.writeInt(Protocol.MEDICO_UPDATE);
        os.writeObject(medico);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MÉDICO NO EXISTE");
    }

    public void deleteMedico(Medico medico) throws Exception {
        os.writeInt(Protocol.MEDICO_DELETE);
        os.writeObject(medico);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MÉDICO NO EXISTE");
    }

    public List<Medico> searchMedico(Medico medico) {
        try {
            os.writeInt(Protocol.MEDICO_SEARCH);
            os.writeObject(medico);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Medico>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // ================= FARMACÉUTICOS ================= //

    public void createFarmaceutico(Farmaceutico farmaceutico) throws Exception {
        os.writeInt(Protocol.FARMACEUTICO_CREATE);
        os.writeObject(farmaceutico);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("FARMACÉUTICO DUPLICADO");
    }

    public Farmaceutico readFarmaceutico(Farmaceutico farmaceutico) throws Exception {
        os.writeInt(Protocol.FARMACEUTICO_READ);
        os.writeObject(farmaceutico);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Farmaceutico) is.readObject();
        else throw new Exception("FARMACÉUTICO NO EXISTE");
    }

    public void updateFarmaceutico(Farmaceutico farmaceutico) throws Exception {
        os.writeInt(Protocol.FARMACEUTICO_UPDATE);
        os.writeObject(farmaceutico);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("FARMACÉUTICO NO EXISTE");
    }

    public void deleteFarmaceutico(Farmaceutico farmaceutico) throws Exception {
        os.writeInt(Protocol.FARMACEUTICO_DELETE);
        os.writeObject(farmaceutico);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("FARMACÉUTICO NO EXISTE");
    }

    public List<Farmaceutico> searchFarmaceutico(Farmaceutico farmaceutico) {
        try {
            os.writeInt(Protocol.FARMACEUTICO_SEARCH);
            os.writeObject(farmaceutico);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Farmaceutico>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // ================= ADMINISTRADORES ================= //

    public Administrador readAdministrador(Administrador administrador) throws Exception {
        os.writeInt(Protocol.ADMINISTRADOR_READ);
        os.writeObject(administrador);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Administrador) is.readObject();
        else throw new Exception("ADMINISTRADOR NO EXISTE");
    }

    public void updateAdministrador(Administrador administrador) throws Exception {
        os.writeInt(Protocol.ADMINISTRADOR_UPDATE);
        os.writeObject(administrador);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("ADMINISTRADOR NO EXISTE");
    }

    // ================= LOGIN ================= //

    public Usuario login(String id, String clave) {
        try {
            os.writeInt(Protocol.USUARIO_LOGIN);
            os.writeObject(id);
            os.writeObject(clave);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (Usuario) is.readObject();
            }
            else return null;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Usuario buscarUsuario(String id) {
        try {
            os.writeInt(Protocol.USUARIO_BUSCAR);
            os.writeObject(id);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (Usuario) is.readObject();
            }
            else return null;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void actualizarUsuario(Usuario usuario) {
        try {
            os.writeInt(Protocol.USUARIO_ACTUALIZAR);
            os.writeObject(usuario);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
            else throw new Exception("ERROR AL ACTUALIZAR USUARIO");
        } catch (Exception e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    // ================= PACIENTES ================= //

    public void createPaciente(Paciente paciente) throws Exception {
        os.writeInt(Protocol.PACIENTE_CREATE);
        os.writeObject(paciente);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("PACIENTE DUPLICADO");
    }

    public Paciente readPaciente(Paciente paciente) throws Exception {
        os.writeInt(Protocol.PACIENTE_READ);
        os.writeObject(paciente);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Paciente) is.readObject();
        else throw new Exception("PACIENTE NO EXISTE");
    }

    public void updatePaciente(Paciente paciente) throws Exception {
        os.writeInt(Protocol.PACIENTE_UPDATE);
        os.writeObject(paciente);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("PACIENTE NO EXISTE");
    }

    public void deletePaciente(Paciente paciente) throws Exception {
        os.writeInt(Protocol.PACIENTE_DELETE);
        os.writeObject(paciente);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("PACIENTE NO EXISTE");
    }

    public List<Paciente> searchPaciente(Paciente paciente) {
        try {
            os.writeInt(Protocol.PACIENTE_SEARCH);
            os.writeObject(paciente);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Paciente>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // ================= MEDICAMENTOS ================= //

    public void createMedicamento(Medicamento medicamento) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_CREATE);
        os.writeObject(medicamento);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MEDICAMENTO DUPLICADO");
    }

    public Medicamento readMedicamento(Medicamento medicamento) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_READ);
        os.writeObject(medicamento);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Medicamento) is.readObject();
        else throw new Exception("MEDICAMENTO NO EXISTE");
    }

    public void updateMedicamento(Medicamento medicamento) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_UPDATE);
        os.writeObject(medicamento);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MEDICAMENTO NO EXISTE");
    }

    public void deleteMedicamento(Medicamento medicamento) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_DELETE);
        os.writeObject(medicamento);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MEDICAMENTO NO EXISTE");
    }

    public List<Medicamento> searchMedicamento(Medicamento medicamento) {
        try {
            os.writeInt(Protocol.MEDICAMENTO_SEARCH);
            os.writeObject(medicamento);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Medicamento>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Medicamento> findAll() {
        try {
            os.writeInt(Protocol.MEDICAMENTO_FINDALL);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Medicamento>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // ================= RECETAS ================= //

    public void createReceta(Receta receta) throws Exception {
        os.writeInt(Protocol.RECETA_CREATE);
        os.writeObject(receta);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("RECETA DUPLICADA");
    }

    public Receta readReceta(Receta receta) throws Exception {
        os.writeInt(Protocol.RECETA_READ);
        os.writeObject(receta);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Receta) is.readObject();
        else throw new Exception("RECETA NO EXISTE");
    }

    public void updateReceta(Receta receta) throws Exception {
        os.writeInt(Protocol.RECETA_UPDATE);
        os.writeObject(receta);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("RECETA NO EXISTE");
    }

    public void deleteReceta(Receta receta) throws Exception {
        os.writeInt(Protocol.RECETA_DELETE);
        os.writeObject(receta);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("ERROR AL ENTREGAR RECETA");
    }

    public void modificarEstadoReceta(Receta receta) throws Exception {
        os.writeInt(Protocol.RECETA_MODIFICAR_ESTADO);
        os.writeObject(receta);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("ERROR AL MODIFICAR ESTADO");
    }

    public List<Receta> searchRecetaNoEntregadas(Receta receta) {
        try {
            os.writeInt(Protocol.RECETA_SEARCH_NO_ENTREGADAS);
            os.writeObject(receta);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Receta>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Receta> findRecetasPorEstado(String estado) {
        try {
            os.writeInt(Protocol.RECETA_FIND_POR_ESTADO);
            os.writeObject(estado);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Receta>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Receta> findRecetasNoEntregadas() {
        try {
            os.writeInt(Protocol.RECETA_FIND_NO_ENTREGADAS);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Receta>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Receta> searchRecetas(Receta receta) {
        try {
            os.writeInt(Protocol.RECETA_SEARCH);
            os.writeObject(receta);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Receta>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Receta> findAllRecetas() {
        try {
            os.writeInt(Protocol.RECETA_FINDALL);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Receta>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}