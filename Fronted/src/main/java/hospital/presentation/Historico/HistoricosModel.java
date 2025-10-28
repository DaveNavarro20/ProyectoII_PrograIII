package hospital.presentation.Historico;

import hospital.presentation.AbstractModel;
import hospital.presentation.Interfaces.InterfazAdministrador;
import hospital.logic.Receta;

import java.beans.PropertyChangeListener;
import java.util.List;

public class HistoricosModel extends AbstractModel {

    Receta filter;
    List<Receta> list;
    Receta current;
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

    public HistoricosModel() {}

    public void init(List<Receta> list) {
        this.list = list;
        this.current = new Receta();
        this.filter = new Receta();
        this.mode = InterfazAdministrador.MODE_CREATE;
    }

    public List<Receta> getList() {
        return list;
    }

    public void setList(List<Receta> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public Receta getCurrent() {
        return current;
    }

    public void setCurrent(Receta current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public Receta getFilter() {
        return filter;
    }

    public void setFilter(Receta filter) {
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
