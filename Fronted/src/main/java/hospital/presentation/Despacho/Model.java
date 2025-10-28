package hospital.presentation.Despacho;

import hospital.logic.Prescripcion;
import hospital.logic.Receta;
import hospital.presentation.AbstractModel;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    Receta current;
    List<Receta> listaReceta;
    List<Prescripcion> listaPrescripcion;

    public Model(){
        current = new Receta();
        listaReceta = new ArrayList<>();
        listaPrescripcion = new ArrayList<>();
    }

    public Receta getCurrent() {return current;}

    public void setCurrent(Receta current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public void setList(List<Receta> list){
        this.listaReceta = list;
        firePropertyChange(LIST);
    }

    public List<Receta> getList(){
        return listaReceta;
    }

    public void setListaPrescripcion(List<Prescripcion> listaPrescripcion) {
        this.listaPrescripcion = listaPrescripcion;
    }

    public void setListmedicamentos(){
        firePropertyChange(LISTMEDICAMENTOS);
    }

    public List<Prescripcion> getListmedicamentos(){
        return listaPrescripcion;
    }

    public static final String LIST = "list";
    public static final String CURRENT = "current";
    public static final String LISTMEDICAMENTOS = "list_medicamento";

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }
}