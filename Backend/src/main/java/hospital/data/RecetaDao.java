package hospital.data;

import hospital.logic.Prescripcion;
import hospital.logic.Receta;
import hospital.logic.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecetaDao {
    private Database db;

    public RecetaDao() {
        db = Database.instance();
    }

    public void create(Receta r) throws Exception {
        if (r.getPrescripcions() == null || r.getPrescripcions().isEmpty()) {
            throw new Exception("La receta debe tener al menos una prescripción");
        }

        String sql1 = "INSERT INTO Receta (idReceta, estado, fecha, idPaciente, idMedico) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stm1 = db.prepareStatement(sql1);
        stm1.setString(1, r.getIdReceta());
        stm1.setString(2, r.getEstado());
        stm1.setString(3, r.getFecha());
        stm1.setString(4, r.getIdPaciente());
        stm1.setString(5, r.getIdDoctor());

        int count = db.executeUpdate(stm1);
        if (count == 0) {
            throw new Exception("Error al crear receta");
        }

        String sql2 = "INSERT INTO Prescripcion (idReceta, nombre, presentacion, cantidad, indicaciones, duracion) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        for (Prescripcion p : r.getPrescripcions()) {
            PreparedStatement stm2 = db.prepareStatement(sql2);
            stm2.setString(1, r.getIdReceta());
            stm2.setString(2, p.getNombre());
            stm2.setString(3, p.getPresentacion());
            stm2.setString(4, p.getCantidad());
            stm2.setString(5, p.getIndicaciones());
            stm2.setString(6, p.getDuracion());
            db.executeUpdate(stm2);
        }
    }

    public Receta read(String idReceta) throws Exception {
        String sql = "SELECT r.*, " +
                "p.id as p_id, p.nombre as p_nombre, p.fechaNacimiento as p_fecha, p.telefono as p_tel, " +
                "u.nombre as m_nombre, m.especialidad as m_especialidad " +
                "FROM Receta r " +
                "INNER JOIN Paciente p ON r.idPaciente = p.id " +
                "INNER JOIN Medico m ON r.idMedico = m.id " +
                "INNER JOIN Usuario u ON m.id = u.id " +
                "WHERE r.idReceta = ?";

        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, idReceta);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            Receta receta = new Receta();
            receta.setIdReceta(rs.getString("idReceta"));
            receta.setEstado(rs.getString("estado"));
            receta.setFecha(rs.getString("fecha"));

            Paciente paciente = new Paciente();
            paciente.setId(rs.getString("p_id"));
            paciente.setNombre(rs.getString("p_nombre"));
            paciente.setFechaNacimiento(rs.getString("p_fecha"));
            paciente.setTelefono(rs.getString("p_tel"));
            receta.setPaciente(paciente);

            Medico medico = new Medico();
            medico.setId(rs.getString("idMedico"));
            medico.setNombre(rs.getString("m_nombre"));
            medico.setEspecialidad(rs.getString("m_especialidad"));
            receta.setDoctor(medico);

            receta.setPrescripcions(findPrescripcionesByReceta(idReceta));

            return receta;
        } else {
            throw new Exception("Receta no existe");
        }
    }

    public void update(Receta r) throws Exception {
        String sql1 = "UPDATE Receta SET estado=?, fecha=?, idPaciente=?, idMedico=? WHERE idReceta=?";
        PreparedStatement stm1 = db.prepareStatement(sql1);
        stm1.setString(1, r.getEstado());
        stm1.setString(2, r.getFecha());
        stm1.setString(3, r.getIdPaciente());
        stm1.setString(4, r.getIdDoctor());
        stm1.setString(5, r.getIdReceta());

        int count = db.executeUpdate(stm1);
        if (count == 0) {
            throw new Exception("Receta no existe");
        }

        String sql2 = "DELETE FROM Prescripcion WHERE idReceta=?";
        PreparedStatement stm2 = db.prepareStatement(sql2);
        stm2.setString(1, r.getIdReceta());
        db.executeUpdate(stm2);

        String sql3 = "INSERT INTO Prescripcion (idReceta, nombre, presentacion, cantidad, indicaciones, duracion) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        for (Prescripcion p : r.getPrescripcions()) {
            PreparedStatement stm3 = db.prepareStatement(sql3);
            stm3.setString(1, r.getIdReceta());
            stm3.setString(2, p.getNombre());
            stm3.setString(3, p.getPresentacion());
            stm3.setString(4, p.getCantidad());
            stm3.setString(5, p.getIndicaciones());
            stm3.setString(6, p.getDuracion());
            db.executeUpdate(stm3);
        }
    }

    public List<Receta> findAll() {
        List<Receta> recetas = new ArrayList<>();
        try {
            String sql = "SELECT r.*, " +
                    "p.nombre as p_nombre, " +
                    "u.nombre as m_nombre " +
                    "FROM Receta r " +
                    "INNER JOIN Paciente p ON r.idPaciente = p.id " +
                    "INNER JOIN Medico m ON r.idMedico = m.id " +
                    "INNER JOIN Usuario u ON m.id = u.id";

            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                Receta receta = new Receta();
                receta.setIdReceta(rs.getString("idReceta"));
                receta.setEstado(rs.getString("estado"));
                receta.setFecha(rs.getString("fecha"));

                Paciente paciente = new Paciente();
                paciente.setId(rs.getString("idPaciente"));
                paciente.setNombre(rs.getString("p_nombre"));
                receta.setPaciente(paciente);

                Medico medico = new Medico();
                medico.setId(rs.getString("idMedico"));
                medico.setNombre(rs.getString("m_nombre"));
                receta.setDoctor(medico);

                recetas.add(receta);
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return recetas;
    }

    private List<Prescripcion> findPrescripcionesByReceta(String idReceta) {
        List<Prescripcion> prescripciones = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Prescripcion WHERE idReceta = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, idReceta);
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                Prescripcion p = new Prescripcion();
                p.setNombre(rs.getString("nombre"));
                p.setPresentacion(rs.getString("presentacion"));
                p.setCantidad(rs.getString("cantidad"));
                p.setIndicaciones(rs.getString("indicaciones"));
                p.setDuracion(rs.getString("duracion"));
                prescripciones.add(p);
            }
        } catch (SQLException ex) {
            System.err.println("Error al cargar prescripciones: " + ex.getMessage());
        }
        return prescripciones;
    }
}
