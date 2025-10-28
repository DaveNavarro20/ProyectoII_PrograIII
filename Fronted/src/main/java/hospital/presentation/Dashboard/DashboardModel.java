package hospital.presentation.Dashboard;

import hospital.presentation.AbstractModel;
import hospital.logic.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardModel extends AbstractModel {

    DashboardFilter filter;
    List<DashboardRowData> list;
    Map<String, Integer> recetasData;
    List<String> mesesDisponibles;
    List<Medicamento> medicamentosDisponibles;

    public static final String LIST = "list";
    public static final String FILTER = "filter";
    public static final String RECETAS_DATA = "recetas_data";
    public static final String MEDICAMENTOS_DISPONIBLES = "medicamentos_disponibles";

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(FILTER);
        firePropertyChange(RECETAS_DATA);
        firePropertyChange(MEDICAMENTOS_DISPONIBLES);
    }

    public DashboardModel() {
    }

    public void init(List<Medicamento> medicamentosDisponibles, Map<String, Integer> recetasData) {
        this.medicamentosDisponibles = medicamentosDisponibles;
        this.recetasData = recetasData;
        this.list = new ArrayList<>();
        this.filter = new DashboardFilter();
        this.mesesDisponibles = new ArrayList<>();

    }

    public List<DashboardRowData> getList() {
        return list;
    }

    public void setList(List<DashboardRowData> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public DashboardFilter getFilter() {
        return filter;
    }

    public void setFilter(DashboardFilter filter) {
        this.filter = filter;
        firePropertyChange(FILTER);
    }

    public Map<String, Integer> getRecetasData() {
        return recetasData;
    }

    public void setRecetasData(Map<String, Integer> recetasData) {
        this.recetasData = recetasData;
        firePropertyChange(RECETAS_DATA);
    }

    public List<String> getMesesDisponibles() {
        return mesesDisponibles;
    }

    public void setMesesDisponibles(List<String> mesesDisponibles) {
        this.mesesDisponibles = mesesDisponibles;
    }

    public List<Medicamento> getMedicamentosDisponibles() {
        return medicamentosDisponibles;
    }

    public void setMedicamentosDisponibles(List<Medicamento> medicamentosDisponibles) {
        this.medicamentosDisponibles = medicamentosDisponibles;
        firePropertyChange(MEDICAMENTOS_DISPONIBLES);
    }

    public List<String> getMesesEnRango() {
        List<String> mesesEnRango = new ArrayList<>();

        if (filter.getMesDesde() == null || filter.getMesHasta() == null) {
            return new ArrayList<>(mesesDisponibles);
        }

        String[] desdePartes = filter.getMesDesde().split("-");
        String[] hastaPartes = filter.getMesHasta().split("-");

        if (desdePartes.length != 2 || hastaPartes.length != 2) {
            return new ArrayList<>(mesesDisponibles);
        }

        try {
            int añoDesde = Integer.parseInt(desdePartes[0]);
            int mesDesdeNum = Integer.parseInt(desdePartes[1]);
            int añoHasta = Integer.parseInt(hastaPartes[0]);
            int mesHastaNum = Integer.parseInt(hastaPartes[1]);

            int añoActual = añoDesde;
            int mesActual = mesDesdeNum;

            while (añoActual < añoHasta || (añoActual == añoHasta && mesActual <= mesHastaNum)) {
                mesesEnRango.add(añoActual + "-" + mesActual);
                mesActual++;
                if (mesActual > 12) {
                    mesActual = 1;
                    añoActual++;
                }
            }
        } catch (NumberFormatException e) {
            return new ArrayList<>(mesesDisponibles);
        }
        return mesesEnRango;
    }
}