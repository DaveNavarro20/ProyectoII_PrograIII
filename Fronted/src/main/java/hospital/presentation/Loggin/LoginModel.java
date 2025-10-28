package hospital.presentation.Loggin;

import hospital.presentation.AbstractModel;
import hospital.logic.Usuario;

public class LoginModel extends AbstractModel {
    private Usuario currentUser;

    public Usuario getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Usuario currentUser) {
        Usuario tempUser = this.currentUser;
        this.currentUser = currentUser;
        firePropertyChange("currentUser",tempUser,this.currentUser);
    }

    public boolean cambiarClaveUsuario(String claveActual, String claveNueva, String claveConfirmacion) {
        if (currentUser == null) {
            return false;
        }

        if (!currentUser.getClave().equals(claveActual)) {
            firePropertyChange("claveError", null, "La clave actual no es correcta");
            return false;
        }

        if (!claveNueva.equals(claveConfirmacion)) {
            firePropertyChange("claveError", null, "La nueva clave y su confirmación no coinciden");
            return false;
        }

        String oldClave = currentUser.getClave();
        currentUser.setClave(claveNueva);
        firePropertyChange("claveUsuario", oldClave, claveNueva);

        return true;
    }

    public LoginModel() {}



}
