package hospital.presentation.Interfaces;

import hospital.logic.Usuario;
import hospital.presentation.Acerca_De;
import hospital.presentation.Dashboard.Dashboard_View;
import hospital.logic.Service;
import hospital.presentation.Medicamentos.MedicamentosController;
import hospital.presentation.Medicamentos.MedicamentosModel;
import hospital.presentation.Mensajeria.MensajeriaPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class InterfazAdministrador {
    public static JFrame window;
    public static hospital.presentation.Medicos.MedicosController medicosController;
    public static hospital.presentation.Farmaceuticos.FarmaceuticosController farmaceuticosController;
    public static hospital.presentation.Pacientes.PacientesController pacientesController;
    public static hospital.presentation.Historico.HistoricosController HistoricosController;

    public final static int MODE_CREATE = 1;
    public final static int MODE_EDIT = 2;
    public static Border BORDER_ERROR = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED);

    public static void ventanaMedicos(String idAdmin, Usuario usuarioActual) {
        try{
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }catch (Exception e){}

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

        // --- Médicos
        hospital.presentation.Medicos.MedicosModel medicosModel = new hospital.presentation.Medicos.MedicosModel();
        hospital.presentation.Medicos.Medicos_View medicosView = new hospital.presentation.Medicos.Medicos_View();
        medicosController = new hospital.presentation.Medicos.MedicosController(medicosView, medicosModel);
        tabbedPane.addTab("Medicos", new ImageIcon(Objects.requireNonNull(InterfazAdministrador.class.getResource("/Imagenes/Medicos.png"))), medicosView.getPanel());

        // --- Farmacéuticos
        hospital.presentation.Farmaceuticos.FarmaceuticosModel farmaceuticosModel = new hospital.presentation.Farmaceuticos.FarmaceuticosModel();
        hospital.presentation.Farmaceuticos.Farmaceuticos_View farmaceuticosView = new hospital.presentation.Farmaceuticos.Farmaceuticos_View();
        farmaceuticosController = new hospital.presentation.Farmaceuticos.FarmaceuticosController(farmaceuticosView, farmaceuticosModel);
        tabbedPane.addTab("Farmaceuticos",new ImageIcon(Objects.requireNonNull(InterfazAdministrador.class.getResource("/Imagenes/Farmaceuticos.png"))), farmaceuticosView.getPanel());

        // --- Pacientes
        hospital.presentation.Pacientes.PacientesModel pacientesModel = new hospital.presentation.Pacientes.PacientesModel();
        hospital.presentation.Pacientes.Pacientes_View pacientesView = new hospital.presentation.Pacientes.Pacientes_View();
        pacientesController = new hospital.presentation.Pacientes.PacientesController(pacientesView, pacientesModel);
        tabbedPane.addTab("Pacientes", new ImageIcon(Objects.requireNonNull(InterfazAdministrador.class.getResource("/Imagenes/Pacientes.png"))), pacientesView.getPanel());

        // --- Medicamentos
        MedicamentosModel medicamentosModel = new MedicamentosModel();
        hospital.presentation.Medicamentos.Medicamentos_View medicamentosView = new hospital.presentation.Medicamentos.Medicamentos_View();
        MedicamentosController medicamentosController = new MedicamentosController(medicamentosView, medicamentosModel);
        tabbedPane.addTab("Medicamentos", new ImageIcon(Objects.requireNonNull(InterfazAdministrador.class.getResource("/Imagenes/Medicamentos.png"))), medicamentosView.getPanel());

        // --- Dashboard
        hospital.presentation.Dashboard.DashboardModel dashboardModel = new hospital.presentation.Dashboard.DashboardModel();
        Dashboard_View dashboardView = new Dashboard_View();
        hospital.presentation.Dashboard.DashboardController dashboardController = new hospital.presentation.Dashboard.DashboardController(dashboardView, dashboardModel);
        tabbedPane.addTab("Dashboard", new ImageIcon(Objects.requireNonNull(InterfazAdministrador.class.getResource("/Imagenes/Dashboard.png"))) , dashboardView.getPanel());

        // --- Historico
        hospital.presentation.Historico.HistoricosModel historicosModel = new  hospital.presentation.Historico.HistoricosModel();
        hospital.presentation.Historico.Historico_View historicoView = new hospital.presentation.Historico.Historico_View();
        HistoricosController = new hospital.presentation.Historico.HistoricosController(historicoView,historicosModel);
        tabbedPane.addTab("Historico", new ImageIcon(Objects.requireNonNull(InterfazAdministrador.class.getResource("/Imagenes/Historico.png"))) , historicoView.getPanel());

        // --- NUEVO: Mensajería
        MensajeriaPanel mensajeriaPanel = new MensajeriaPanel(usuarioActual);
        tabbedPane.addTab("Mensajes", new ImageIcon(Objects.requireNonNull(InterfazAdministrador.class.getResource("/Imagenes/Receta.png"))), mensajeriaPanel.getPanel());

        // --- Acerca de
        Acerca_De acercaDe = new Acerca_De();
        tabbedPane.addTab("Acerca de...", new ImageIcon(Objects.requireNonNull(InterfazAdministrador.class.getResource("/Imagenes/Receta.png"))) , acercaDe.getPanel());

        // --- Ventana principal
        window.setSize(1300, 500);
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setTitle("Recetas - Administrador - " + idAdmin + " (ADM)" );
        Image icon = Toolkit.getDefaultToolkit().getImage(
                InterfazAdministrador.class.getResource("/Imagenes/Receta.png")
        );
        window.setIconImage(icon);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}