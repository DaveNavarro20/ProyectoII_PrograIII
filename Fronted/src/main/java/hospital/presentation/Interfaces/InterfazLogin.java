package hospital.presentation.Interfaces;

import hospital.presentation.Loggin.LoginController;
import hospital.presentation.Loggin.LoginModel;
import hospital.presentation.Loggin.View_Login;
import javax.swing.*;
import java.awt.*;

public class InterfazLogin {

    public static void ventanaLogin() {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {}

        View_Login view = new View_Login();
        LoginModel model = new LoginModel();
        LoginController controller = new LoginController(model, view);

        view.setModel(model);
        view.setController(controller);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setTitle("Recetas");
        Image icon = Toolkit.getDefaultToolkit().getImage(
                InterfazLogin.class.getResource("/Imagenes/Receta.png")
        );
        frame.setIconImage(icon);
        frame.setContentPane(view.getPanelLogin());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
