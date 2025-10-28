package hospital.presentation.Interfaces;

import hospital.presentation.Acerca_De;
import hospital.presentation.Dashboard.DashboardController;
import hospital.presentation.Dashboard.DashboardModel;
import hospital.presentation.Dashboard.Dashboard_View;
import hospital.presentation.Historico.Historico_View;
import hospital.presentation.Historico.HistoricosController;
import hospital.presentation.Historico.HistoricosModel;
import hospital.logic.Medico;
import hospital.logic.Service;
import hospital.presentation.Prescripcion.PrescripcionController;
import hospital.presentation.Prescripcion.PrescripcionModel;
import hospital.presentation.Prescripcion.View_Prescripcion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class InterfazPrescripcion {
    public static JFrame window;
    public static PrescripcionController prescripcionController;
    public static HistoricosController historicosController;
    public static DashboardController dashboardController;

    public final static int MODE_CREATE = 1;
    public final static int MODE_EDIT = 2;
    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102);

    public static void ventanaPrescripcion(Medico medico,String idMedico) {
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
                Service.instance().stop();
            }
        });

        // --- Prescripción
        PrescripcionModel prescripcionModel = new PrescripcionModel();
        View_Prescripcion prescripcionView = new View_Prescripcion();
        prescripcionView.setDoctorIngresado(medico.getId()); // ✅ pasamos el objeto completo
        prescripcionController = new PrescripcionController(prescripcionView, prescripcionModel);
        tabbedPane.addTab("Prescripción", new ImageIcon(Objects.requireNonNull(InterfazPrescripcion.class.getResource("/Imagenes/Prescribir.png"))), prescripcionView.getPanel());

        // --- Dashboard
        DashboardModel dashboardModel = new DashboardModel();
        Dashboard_View dashboardView = new Dashboard_View();
        dashboardController = new DashboardController(dashboardView, dashboardModel);
        tabbedPane.addTab("Dashboard", new ImageIcon(Objects.requireNonNull(InterfazPrescripcion.class.getResource("/Imagenes/Dashboard.png"))), dashboardView.getPanel());

        // --- Historico
        HistoricosModel historicosModel = new HistoricosModel();
        Historico_View historicoView = new Historico_View();
        historicosController = new HistoricosController(historicoView, historicosModel);
        tabbedPane.addTab("Historico", new ImageIcon(Objects.requireNonNull(InterfazDespacho.class.getResource("/Imagenes/Historico.png"))), historicoView.getPanel());

        // --- Acerca de
        Acerca_De acercaDe = new Acerca_De();
        tabbedPane.addTab("Acerca de...", new ImageIcon(Objects.requireNonNull(InterfazPrescripcion.class.getResource("/Imagenes/Receta.png"))), acercaDe.getPanel());

        // --- Ventana principal
        window.setSize(1300, 500);
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setTitle("Recetas - Medico - " + idMedico + " (Med)");
        Image icon = Toolkit.getDefaultToolkit().getImage(
                InterfazPrescripcion.class.getResource("/Imagenes/Receta.png")
        );
        window.setIconImage(icon);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
