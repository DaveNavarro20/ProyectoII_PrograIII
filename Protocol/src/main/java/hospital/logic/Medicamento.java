package hospital.logic;

import java.io.Serializable;

public class Medicamento implements Serializable {
    private String id;
    private String nombre;
    private String presentacion;

    public Medicamento(String id, String nombre, String presentacion) {
        this.id = id;
        this.nombre = nombre;
        this.presentacion = presentacion;
    }

    public Medicamento(){this("", "", "");}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

}