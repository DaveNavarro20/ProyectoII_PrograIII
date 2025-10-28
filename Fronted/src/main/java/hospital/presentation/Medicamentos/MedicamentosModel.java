package hospital.presentation.Medicamentos;

import hospital.presentation.AbstractModel;
import hospital.presentation.Interfaces.InterfazAdministrador;
import hospital.logic.Medicamento;

import java.beans.PropertyChangeListener;
import java.util.List;

public class MedicamentosModel extends AbstractModel {

    Medicamento filter;
    List<Medicamento> list;
    Medicamento current;
    int mode;

    public static final String LIST = "list";
    public static final String CURRENT = "current";
    public static final String FILTER = "filter";
    private Medicamento currentMedicamento;

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(CURRENT);
        firePropertyChange(FILTER);
    }

    public Medicamento getCurrentMedicamento() {
        return currentMedicamento;
    }

    public void setCurrentMedicamento(Medicamento currentMedicamento) {
        this.currentMedicamento = currentMedicamento;
        firePropertyChange(CURRENT);
    }

    public MedicamentosModel() {
    }

    public void init(List<Medicamento> list) {
        this.list = list;
        this.current = new Medicamento();
        this.filter = new Medicamento();
        this.mode = InterfazAdministrador.MODE_CREATE;
    }

    public List<Medicamento> getList() {
        return list;
    }

    public void setList(List<Medicamento> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public Medicamento getCurrent() {
        return current;
    }

    public void setCurrent(Medicamento current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public Medicamento getFilter() {
        return filter;
    }

    public void setFilter(Medicamento filter) {
        this.filter = filter;
        firePropertyChange(FILTER);
    }
    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
