package hospital.presentation.Dashboard;

import java.util.List;

public class DashboardRowData {
    private String nombreMedicamento;
    private List<Integer> cantidadesPorMes;

    public DashboardRowData(String nombreMedicamento, List<Integer> cantidadesPorMes) {
        this.nombreMedicamento = nombreMedicamento;
        this.cantidadesPorMes = cantidadesPorMes;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    public List<Integer> getCantidadesPorMes() {
        return cantidadesPorMes;
    }

    public void setCantidadesPorMes(List<Integer> cantidadesPorMes) {
        this.cantidadesPorMes = cantidadesPorMes;
    }

    public Integer getCantidadPorMes(int mesIndex) {
        if (mesIndex >= 0 && mesIndex < cantidadesPorMes.size()) {
            return cantidadesPorMes.get(mesIndex);
        }
        return 0;
    }

    @Override
    public String toString() {
        return "DashboardRowData{" +
                "nombreMedicamento='" + nombreMedicamento + '\'' +
                ", cantidadesPorMes=" + cantidadesPorMes +
                '}';
    }
}