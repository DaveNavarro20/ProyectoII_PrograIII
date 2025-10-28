package hospital.presentation.Pacientes;

import hospital.presentation.AbstractModel;

import hospital.logic.Paciente;
import hospital.presentation.Interfaces.InterfazAdministrador;

import java.beans.PropertyChangeListener;
import java.util.List;

public class PacientesModel extends AbstractModel {

    Paciente filter;

    List<Paciente> list;

    Paciente current;

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

    public PacientesModel() {}

    public void init(List<Paciente> list) {
        this.list = list;
        this.current = new Paciente();
        this.filter = new Paciente();
        this.mode = InterfazAdministrador.MODE_CREATE;
    }

    public List<Paciente> getList() {
        return list;
    }

    public void setList(List<Paciente> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public Paciente getCurrent() {
        return current;
    }

    public void setCurrent(Paciente current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public Paciente getFilter() {
        return filter;
    }

    public void setFilter(Paciente filter) {
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

