package hospital.data;

import hospital.logic.Paciente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {

    private Database db;

    public PacienteDao() {
        db = Database.instance();
    }

    /**
     * Crea un nuevo paciente en la base de datos
     * @param p Objeto Paciente a insertar
     * @throws Exception Si el paciente ya existe o hay error SQL
     */

    public void create(Paciente p) throws Exception {
        String sql = "INSERT INTO Paciente (id, nombre, fechaNacimiento, telefono) " +
                "VALUES (?, ?, ?, ?)";

        PreparedStatement stm = db.prepareStatement(sql);

        stm.setString(1, p.getId());
        stm.setString(2, p.getNombre());
        stm.setString(3, p.getFechaNacimiento());
        stm.setString(4, p.getTelefono());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Paciente ya existe o error al insertar");
        }
    }

    /**
     * Busca un paciente por su ID
     * @param id Identificador del paciente
     * @return Objeto Paciente encontrado
     * @throws Exception Si el paciente no existe
     */

    public Paciente read(String id) throws Exception {
        String sql = "SELECT * FROM Paciente WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);

        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            return from(rs);
        } else {
            throw new Exception("Paciente no existe");
        }
    }

    /**
     * Actualiza los datos de un paciente existente
     * @param p Paciente con datos actualizados
     * @throws Exception Si el paciente no existe
     */
    public void update(Paciente p) throws Exception {
        String sql = "UPDATE Paciente SET nombre=?, fechaNacimiento=?, telefono=? " +
                "WHERE id=?";

        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getNombre());
        stm.setString(2, p.getFechaNacimiento());
        stm.setString(3, p.getTelefono());
        stm.setString(4, p.getId());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Paciente no existe");
        }
    }

    /**
     * Elimina un paciente de la base de datos
     * @param p Paciente a eliminar
     * @throws Exception Si el paciente no existe
     */
    public void delete(Paciente p) throws Exception {
        String sql = "DELETE FROM Paciente WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getId());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Paciente no existe");
        }
    }

    /**
     * Busca pacientes por nombre (búsqueda parcial)
     * @param nombre Texto a buscar en el nombre
     * @return Lista de pacientes que coinciden
     */
    public List<Paciente> findByNombre(String nombre) {
        List<Paciente> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Paciente WHERE nombre LIKE ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%" + nombre + "%");

            ResultSet rs = db.executeQuery(stm);
            while (rs.next()) {
                resultado.add(from(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error en búsqueda: " + ex.getMessage());
        }
        return resultado;
    }

    /**
     * Convierte un registro de ResultSet a objeto Paciente
     * @param rs ResultSet con los datos
     * @return Objeto Paciente construido
     */
    private Paciente from(ResultSet rs) {
        try {
            Paciente p = new Paciente();
            p.setId(rs.getString("id"));
            p.setNombre(rs.getString("nombre"));
            p.setFechaNacimiento(rs.getString("fechaNacimiento"));
            p.setTelefono(rs.getString("telefono"));
            return p;
        } catch (SQLException ex) {
            System.err.println("Error al mapear Paciente: " + ex.getMessage());
            return null;
        }
    }
}