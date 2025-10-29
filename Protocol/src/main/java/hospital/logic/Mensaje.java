package hospital.logic;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Mensaje implements Serializable {
    private static final long serialVersionUID = 1L;

    private String remitenteId;
    private String remitenteNombre;
    private String destinatarioId;
    private String contenido;
    private LocalDateTime fecha;

    public Mensaje(String remitenteId, String remitenteNombre, String destinatarioId, String contenido) {
        this.remitenteId = remitenteId;
        this.remitenteNombre = remitenteNombre;
        this.destinatarioId = destinatarioId;
        this.contenido = contenido;
        this.fecha = LocalDateTime.now();
    }

    public String getRemitenteId() { return remitenteId; }
    public void setRemitenteId(String remitenteId) { this.remitenteId = remitenteId; }

    public String getRemitenteNombre() { return remitenteNombre; }
    public void setRemitenteNombre(String remitenteNombre) { this.remitenteNombre = remitenteNombre; }

    public String getDestinatarioId() { return destinatarioId; }
    public void setDestinatarioId(String destinatarioId) { this.destinatarioId = destinatarioId; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    @Override
    public String toString() {
        return "De: " + remitenteNombre + " (" + remitenteId + ")\n" +
                "Mensaje: " + contenido + "\n" +
                "Fecha: " + fecha;
    }
}