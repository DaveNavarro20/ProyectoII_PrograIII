package hospital.presentation.Dashboard;

import java.util.ArrayList;
import java.util.List;

public class DashboardFilter {
    private String mesDesde;
    private String mesHasta;
    private List<String> medicamentosSeleccionados;

    public DashboardFilter() {
        this.medicamentosSeleccionados = new ArrayList<>();
    }

    public String getMesDesde() {
        return mesDesde;
    }

    public void setMesDesde(String mesDesde) {
        this.mesDesde = mesDesde;
    }

    public String getMesHasta() {
        return mesHasta;
    }

    public void setMesHasta(String mesHasta) {
        this.mesHasta = mesHasta;
    }

    public List<String> getMedicamentosSeleccionados() {
        return medicamentosSeleccionados;
    }

    public void setMedicamentosSeleccionados(List<String> medicamentosSeleccionados) {
        this.medicamentosSeleccionados = medicamentosSeleccionados;
    }

    public void addMedicamento(String medicamento) {
        if (!medicamentosSeleccionados.contains(medicamento)) {
            medicamentosSeleccionados.add(medicamento);
        }
    }

    public void removeMedicamento(String medicamento) {
        medicamentosSeleccionados.remove(medicamento);
    }

    public void clearMedicamentos() {
        medicamentosSeleccionados.clear();
    }

    @Override
    public String toString() {
        return "DashboardFilter{" +
                "mesDesde='" + mesDesde + '\'' +
                ", mesHasta='" + mesHasta + '\'' +
                ", medicamentosSeleccionados=" + medicamentosSeleccionados +
                '}';
    }
}