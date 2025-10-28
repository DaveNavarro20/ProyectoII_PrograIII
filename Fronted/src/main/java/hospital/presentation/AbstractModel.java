package hospital.presentation;

import hospital.logic.Usuario;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractModel {
    protected PropertyChangeSupport propertyChangeSupport;

    public AbstractModel(){
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName) {
        propertyChangeSupport.firePropertyChange(propertyName, null, null);
    }

    protected void firePropertyChange(String propertyName,Usuario Old,Usuario New) {
        propertyChangeSupport.firePropertyChange(propertyName, Old, New);
    }

    protected void firePropertyChange(String propertyName,String Old,String New) {
        propertyChangeSupport.firePropertyChange(propertyName, Old, New);
    }

}