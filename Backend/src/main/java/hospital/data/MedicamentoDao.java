package hospital.data;

import hospital.logic.Medicamento;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDao {
    private Database db;

    public MedicamentoDao() {
        db = Database.instance();
    }

    public void create(Medicamento m) throws Exception {
        String sql = "INSERT INTO Medicamento (id, nombre, presentacion) VALUES (?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getId());
        stm.setString(2, m.getNombre());
        stm.setString(3, m.getPresentacion());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Medicamento ya existe");
        }
    }

    public Medicamento read(String id) throws Exception {
        String sql = "SELECT * FROM Medicamento WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            return from(rs);
        } else {
            throw new Exception("Medicamento no existe");
        }
    }

    public void update(Medicamento m) throws Exception {
        String sql = "UPDATE Medicamento SET nombre=?, presentacion=? WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getNombre());
        stm.setString(2, m.getPresentacion());
        stm.setString(3, m.getId());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Medicamento no existe");
        }
    }

    public void delete(Medicamento m) throws Exception {
        String sql = "DELETE FROM Medicamento WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getId());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Medicamento no existe");
        }
    }

    public List<Medicamento> findAll() {
        List<Medicamento> medicamentos = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Medicamento";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                medicamentos.add(from(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return medicamentos;
    }

    public List<Medicamento> findByNombre(String nombre) {
        List<Medicamento> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Medicamento WHERE nombre LIKE ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%" + nombre + "%");
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                resultado.add(from(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return resultado;
    }

    public List<Medicamento> findByPresentacion(String presentacion) {
        List<Medicamento> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Medicamento WHERE presentacion LIKE ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%" + presentacion + "%");
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                resultado.add(from(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return resultado;
    }

    private Medicamento from(ResultSet rs) {
        try {
            Medicamento m = new Medicamento();
            m.setId(rs.getString("id"));
            m.setNombre(rs.getString("nombre"));
            m.setPresentacion(rs.getString("presentacion"));
            return m;
        } catch (SQLException ex) {
            System.err.println("Error al mapear Medicamento: " + ex.getMessage());
            return null;
        }
    }

    public boolean exists(String id) {
        try {
            String sql = "SELECT COUNT(*) FROM Medicamento WHERE id = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, id);
            ResultSet rs = db.executeQuery(stm);

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return false;
    }
}
