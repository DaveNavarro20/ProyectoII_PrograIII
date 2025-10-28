package hospital.data;

import hospital.logic.Usuario;
import hospital.logic.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {
    private Database db;

    public UsuarioDao() {
        db = Database.instance();
    }

    public Usuario login(String id, String clave) throws Exception {
        String sql = "SELECT u.*, m.especialidad " +
                "FROM Usuario u " +
                "LEFT JOIN Medico m ON u.id = m.id " +
                "WHERE u.id = ? AND u.clave = ?";

        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        stm.setString(2, clave);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            String tipo = rs.getString("tipoUsuario");

            switch (tipo) {
                case "MEDICO":
                    Medico medico = new Medico();
                    medico.setId(rs.getString("id"));
                    medico.setNombre(rs.getString("nombre"));
                    medico.setClave(rs.getString("clave"));
                    medico.setEspecialidad(rs.getString("especialidad"));
                    return medico;

                case "ADMINISTRADOR":
                    Administrador admin = new Administrador();
                    admin.setId(rs.getString("id"));
                    admin.setNombre(rs.getString("nombre"));
                    admin.setClave(rs.getString("clave"));
                    return admin;

                case "FARMACEUTICO":
                    Farmaceutico farm = new Farmaceutico();
                    farm.setId(rs.getString("id"));
                    farm.setNombre(rs.getString("nombre"));
                    farm.setClave(rs.getString("clave"));
                    return farm;

                default:
                    throw new Exception("Tipo de usuario desconocido");
            }
        } else {
            throw new Exception("Usuario o contraseña incorrectos");
        }
    }

    public void createMedico(Medico m) throws Exception {
        String sql1 = "INSERT INTO Usuario (id, nombre, clave, tipoUsuario) VALUES (?, ?, ?, 'MEDICO')";
        PreparedStatement stm1 = db.prepareStatement(sql1);
        stm1.setString(1, m.getId());
        stm1.setString(2, m.getNombre());
        stm1.setString(3, m.getClave());

        int count1 = db.executeUpdate(stm1);
        if (count1 == 0) {
            throw new Exception("Error al crear usuario");
        }

        String sql2 = "INSERT INTO Medico (id, especialidad) VALUES (?, ?)";
        PreparedStatement stm2 = db.prepareStatement(sql2);
        stm2.setString(1, m.getId());
        stm2.setString(2, m.getEspecialidad());

        int count2 = db.executeUpdate(stm2);
        if (count2 == 0) {
            throw new Exception("Error al crear médico");
        }
    }

    public void createAdministrador(Administrador a) throws Exception {
        String sql1 = "INSERT INTO Usuario (id, nombre, clave, tipoUsuario) VALUES (?, ?, ?, 'ADMINISTRADOR')";
        PreparedStatement stm1 = db.prepareStatement(sql1);
        stm1.setString(1, a.getId());
        stm1.setString(2, a.getNombre());
        stm1.setString(3, a.getClave());

        int count1 = db.executeUpdate(stm1);
        if (count1 == 0) {
            throw new Exception("Error al crear usuario");
        }

        String sql2 = "INSERT INTO Administrador (id) VALUES (?)";
        PreparedStatement stm2 = db.prepareStatement(sql2);
        stm2.setString(1, a.getId());

        int count2 = db.executeUpdate(stm2);
        if (count2 == 0) {
            throw new Exception("Error al crear administrador");
        }
    }

    public void createFarmaceutico(Farmaceutico f) throws Exception {
        String sql1 = "INSERT INTO Usuario (id, nombre, clave, tipoUsuario) VALUES (?, ?, ?, 'FARMACEUTICO')";
        PreparedStatement stm1 = db.prepareStatement(sql1);
        stm1.setString(1, f.getId());
        stm1.setString(2, f.getNombre());
        stm1.setString(3, f.getClave());

        int count1 = db.executeUpdate(stm1);
        if (count1 == 0) {
            throw new Exception("Error al crear usuario");
        }

        String sql2 = "INSERT INTO Farmaceutico (id) VALUES (?)";
        PreparedStatement stm2 = db.prepareStatement(sql2);
        stm2.setString(1, f.getId());

        int count2 = db.executeUpdate(stm2);
        if (count2 == 0) {
            throw new Exception("Error al crear farmacéutico");
        }
    }

    public Medico readMedico(Medico medicoRef) throws Exception {
        String sql = "SELECT u.*, m.especialidad " +
                "FROM Usuario u " +
                "INNER JOIN Medico m ON u.id = m.id " +
                "WHERE u.id = ?";

        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, medicoRef.getId());
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            Medico medico = new Medico();
            medico.setId(rs.getString("id"));
            medico.setNombre(rs.getString("nombre"));
            medico.setClave(rs.getString("clave"));
            medico.setEspecialidad(rs.getString("especialidad"));
            return medico;
        } else {
            throw new Exception("Médico no existe");
        }
    }

    public Administrador readAdministrador(String id) throws Exception {
        String sql = "SELECT u.* " +
                "FROM Usuario u " +
                "INNER JOIN Administrador a ON u.id = a.id " +
                "WHERE u.id = ?";

        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            Administrador admin = new Administrador();
            admin.setId(rs.getString("id"));
            admin.setNombre(rs.getString("nombre"));
            admin.setClave(rs.getString("clave"));
            return admin;
        } else {
            throw new Exception("Administrador no existe");
        }
    }

    public Farmaceutico readFarmaceutico(String id) throws Exception {
        String sql = "SELECT u.* " +
                "FROM Usuario u " +
                "INNER JOIN Farmaceutico f ON u.id = f.id " +
                "WHERE u.id = ?";

        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            Farmaceutico farm = new Farmaceutico();
            farm.setId(rs.getString("id"));
            farm.setNombre(rs.getString("nombre"));
            farm.setClave(rs.getString("clave"));
            return farm;
        } else {
            throw new Exception("Farmacéutico no existe");
        }
    }

    public void updateMedico(Medico m) throws Exception {
        String sql1 = "UPDATE Usuario SET nombre=?, clave=? WHERE id=?";
        PreparedStatement stm1 = db.prepareStatement(sql1);
        stm1.setString(1, m.getNombre());
        stm1.setString(2, m.getClave());
        stm1.setString(3, m.getId());
        db.executeUpdate(stm1);

        String sql2 = "UPDATE Medico SET especialidad=? WHERE id=?";
        PreparedStatement stm2 = db.prepareStatement(sql2);
        stm2.setString(1, m.getEspecialidad());
        stm2.setString(2, m.getId());

        int count = db.executeUpdate(stm2);
        if (count == 0) {
            throw new Exception("Médico no existe");
        }
    }

    public void updateAdministrador(Administrador a) throws Exception {
        String sql = "UPDATE Usuario SET nombre=?, clave=? WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, a.getNombre());
        stm.setString(2, a.getClave());
        stm.setString(3, a.getId());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Administrador no existe");
        }
    }

    public void updateFarmaceutico(Farmaceutico f) throws Exception {
        String sql = "UPDATE Usuario SET nombre=?, clave=? WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, f.getNombre());
        stm.setString(2, f.getClave());
        stm.setString(3, f.getId());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Farmacéutico no existe");
        }
    }

    public void delete(String id) throws Exception {
        String sql = "DELETE FROM Usuario WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Usuario no existe");
        }
    }

    public List<Medico> findAllMedicos() {
        List<Medico> medicos = new ArrayList<>();
        try {
            String sql = "SELECT u.*, m.especialidad " +
                    "FROM Usuario u " +
                    "INNER JOIN Medico m ON u.id = m.id";

            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                Medico medico = new Medico();
                medico.setId(rs.getString("id"));
                medico.setNombre(rs.getString("nombre"));
                medico.setClave(rs.getString("clave"));
                medico.setEspecialidad(rs.getString("especialidad"));
                medicos.add(medico);
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return medicos;
    }

    public List<Medico> findMedicosByNombre(String nombre) {
        List<Medico> medicos = new ArrayList<>();
        try {
            String sql = "SELECT u.*, m.especialidad " +
                    "FROM Usuario u " +
                    "INNER JOIN Medico m ON u.id = m.id " +
                    "WHERE u.nombre LIKE ?";

            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%" + nombre + "%");
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                Medico medico = new Medico();
                medico.setId(rs.getString("id"));
                medico.setNombre(rs.getString("nombre"));
                medico.setClave(rs.getString("clave"));
                medico.setEspecialidad(rs.getString("especialidad"));
                medicos.add(medico);
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return medicos;
    }

    public List<Farmaceutico> findFarmaceuticosByNombre(String nombre) {
        List<Farmaceutico> farmaceuticos = new ArrayList<>();
        try {
            String sql = "SELECT u.* " +
                    "FROM Usuario u " +
                    "INNER JOIN Farmaceutico f ON u.id = f.id " +
                    "WHERE u.nombre LIKE ?";

            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%" + nombre + "%");
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                Farmaceutico farm = new Farmaceutico();
                farm.setId(rs.getString("id"));
                farm.setNombre(rs.getString("nombre"));
                farm.setClave(rs.getString("clave"));
                farmaceuticos.add(farm);
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return farmaceuticos;
    }

    public List<Administrador> findAllAdministradores() {
        List<Administrador> admins = new ArrayList<>();
        try {
            String sql = "SELECT u.* " +
                    "FROM Usuario u " +
                    "INNER JOIN Administrador a ON u.id = a.id";

            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                Administrador admin = new Administrador();
                admin.setId(rs.getString("id"));
                admin.setNombre(rs.getString("nombre"));
                admin.setClave(rs.getString("clave"));
                admins.add(admin);
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return admins;
    }

    public List<Farmaceutico> findAllFarmaceuticos() {
        List<Farmaceutico> farmaceuticos = new ArrayList<>();
        try {
            String sql = "SELECT u.* " +
                    "FROM Usuario u " +
                    "INNER JOIN Farmaceutico f ON u.id = f.id";

            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                Farmaceutico farm = new Farmaceutico();
                farm.setId(rs.getString("id"));
                farm.setNombre(rs.getString("nombre"));
                farm.setClave(rs.getString("clave"));
                farmaceuticos.add(farm);
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return farmaceuticos;
    }

    public List<Medico> findMedicosByEspecialidad(String especialidad) {
        List<Medico> medicos = new ArrayList<>();
        try {
            String sql = "SELECT u.*, m.especialidad " +
                    "FROM Usuario u " +
                    "INNER JOIN Medico m ON u.id = m.id " +
                    "WHERE m.especialidad LIKE ?";

            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%" + especialidad + "%");
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                Medico medico = new Medico();
                medico.setId(rs.getString("id"));
                medico.setNombre(rs.getString("nombre"));
                medico.setClave(rs.getString("clave"));
                medico.setEspecialidad(rs.getString("especialidad"));
                medicos.add(medico);
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return medicos;
    }

    public boolean exists(String id) {
        try {
            String sql = "SELECT COUNT(*) FROM Usuario WHERE id = ?";
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

    public int countByTipo(String tipoUsuario) {
        try {
            String sql = "SELECT COUNT(*) FROM Usuario WHERE tipoUsuario = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, tipoUsuario);
            ResultSet rs = db.executeQuery(stm);

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return 0;
    }
}
