package hospital.logic;

import java.io.Serializable;

public class Prescripcion implements Serializable {
    private String nombre;
    private String presentacion;
    private String cantidad;
    private String indicaciones;
    private String duracion;

    public Prescripcion(String nombre, String presentacion, String cantidad, String indicaciones, String duracion) {
        this.nombre = nombre;
        this.presentacion = presentacion;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.duracion = duracion;
    }

    public Prescripcion(){this("", "", "", "", "");}

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

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

}
