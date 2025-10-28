package hospital.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Receta implements Serializable {
    private String idReceta;
    private String estado;
    private List<Prescripcion> prescripciones;
    private String fecha;
    Paciente paciente;
    Medico doctor;

    public Receta(String idReceta, String estado, Paciente paciente, Medico doctor, String fecha) {
        this.idReceta = idReceta;
        this.estado = estado;
        this.paciente = paciente;
        this.doctor = doctor;
        this.prescripciones = new ArrayList<>();
        this.fecha = fecha;
    }

    public Receta(){
        this("","", new Paciente(), new Medico(), "");
    }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Medico getDoctor() { return doctor; }
    public void setDoctor(Medico doctor) { this.doctor = doctor; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getIdReceta() { return idReceta; }
    public void setIdReceta(String idReceta) { this.idReceta = idReceta; }

    public List<Prescripcion> getPrescripcions() { return prescripciones; }
    public void setPrescripcions(List<Prescripcion> prescripcions) { this.prescripciones = prescripcions; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getIdPaciente() {
        return (paciente != null) ? paciente.getId() : "";
    }

    public String getIdDoctor() {
        return (doctor != null) ? doctor.getId() : "";
    }


    public void setIdPaciente(String idPaciente) {
        if (this.paciente == null) this.paciente = new Paciente();
        this.paciente.setId(idPaciente);
    }

    public void setIdDoctor(String idDoctor) {
        if (this.doctor == null) this.doctor = new Medico();
        this.doctor.setId(idDoctor);
    }
}
