package hospital.presentation.Interfaces;

import hospital.logic.Usuario;
import hospital.presentation.Acerca_De;
import hospital.presentation.Dashboard.Dashboard_View;
import hospital.presentation.Despacho.Controller;
import hospital.presentation.Despacho.Model;
import hospital.presentation.Despacho.View_despacho;
import hospital.presentation.Historico.Historico_View;
import hospital.presentation.Historico.HistoricosController;
import hospital.presentation.Historico.HistoricosModel;
import hospital.logic.Service;
import hospital.presentation.Mensajeria.VentanaMensajeria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class InterfazDespacho {

    public static JFrame window;
    public static Controller Controller;
    public static HistoricosController historicosController;
    private static VentanaMensajeria ventanaMensajeria;

    public static void ventanaDespacho(String idFarmaceuta, Usuario usuarioActual) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {}

        window = new JFrame();
        JTabbedPane tabbedPane = new JTabbedPane();
        window.setContentPane(tabbedPane);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (ventanaMensajeria != null) {
                    ventanaMensajeria.cerrar();
                }
                Service.instance().stop();
            }
        });

        // --- Despacho
        Model model = new Model();
        View_despacho view = new View_despacho();
        Controller = new Controller(view,model);
        tabbedPane.addTab("Despacho",new ImageIcon(Objects.requireNonNull(InterfazDespacho.class.getResource("/Imagenes/Despacho.png"))), view.getPanel());

        // --- Historico
        HistoricosModel historicosModel = new HistoricosModel();
        Historico_View historicoView = new Historico_View();
        historicosController = new HistoricosController(historicoView,historicosModel);
        tabbedPane.addTab("Historico", new ImageIcon(Objects.requireNonNull(InterfazDespacho.class.getResource("/Imagenes/Historico.png"))) , historicoView.getPanel());

        // --- Dashboard
        hospital.presentation.Dashboard.DashboardModel dashboardModel = new hospital.presentation.Dashboard.DashboardModel();
        Dashboard_View dashboardView = new Dashboard_View();
        hospital.presentation.Dashboard.DashboardController dashboardController = new hospital.presentation.Dashboard.DashboardController(dashboardView, dashboardModel);
        tabbedPane.addTab("Dashboard", new ImageIcon(Objects.requireNonNull(InterfazDespacho.class.getResource("/Imagenes/Dashboard.png"))) , dashboardView.getPanel());

        // --- Acerca de
        Acerca_De acercaDe = new Acerca_De();
        tabbedPane.addTab("Acerca de...", new ImageIcon(Objects.requireNonNull(InterfazDespacho.class.getResource("/Imagenes/Receta.png"))), acercaDe.getPanel());

        // --- Ventana principal
        window.setSize(1200, 600);
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setTitle("Recetas - Farmaceuta - " + idFarmaceuta + " (Farm)");
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Abrir ventana de mensajería automáticamente
        SwingUtilities.invokeLater(() -> {
            ventanaMensajeria = new VentanaMensajeria(usuarioActual, window);
        });
    }

}