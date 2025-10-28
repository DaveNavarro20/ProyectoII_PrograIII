package hospital.logic;

import java.io.Serializable;

public class Administrador extends  Usuario implements Serializable {

    public Administrador() {
        this("", "", "");
    }

    public Administrador(String id, String nombre, String clave) {
        super(id, nombre, clave);
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
