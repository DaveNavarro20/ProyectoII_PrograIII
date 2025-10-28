package hospital.presentation.Medicos;

import hospital.presentation.AbstractModel;
import hospital.presentation.Interfaces.InterfazAdministrador;
import hospital.logic.Medico;

import java.beans.PropertyChangeListener;
import java.util.List;

public class MedicosModel extends AbstractModel {

    Medico filter;
    List<Medico> list;
    Medico current;
    int mode;

    public static final String LIST = "list";
    public static final String CURRENT = "current";
    public static final String FILTER = "filter";

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(CURRENT);
        firePropertyChange(FILTER);
    }

    public MedicosModel() {}

    public void init(List<Medico> list) {
        this.list = list;
        this.current = new Medico();
        this.filter = new Medico();
        this.mode = InterfazAdministrador.MODE_CREATE;
    }

    public List<Medico> getList() {
        return list;
    }

    public void setList(List<Medico> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public Medico getCurrent() {
        return current;
    }

    public void setCurrent(Medico current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public Medico getFilter() {
        return filter;
    }

    public void setFilter(Medico filter) {
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
