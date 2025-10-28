package hospital.logic;

import java.io.Serializable;

public class Farmaceutico extends Usuario implements Serializable {

    public Farmaceutico() {
        this("", "", "");
    }

    public Farmaceutico(String id, String nombre, String clave) {
        super(id, nombre, clave);
    }

    @Override
    public String toString() {
        return "Farmaceutico{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}

