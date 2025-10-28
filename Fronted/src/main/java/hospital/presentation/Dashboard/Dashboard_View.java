package hospital.presentation.Dashboard;

import hospital.logic.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Dashboard_View implements PropertyChangeListener {
    private JPanel panel;
    private JPanel datosPanel;
    private JComboBox desdeAños;
    private JComboBox desdeMes;
    private JComboBox hastaAño;
    private JComboBox hastaMes;
    private JComboBox medicamentosBox;
    private JButton agregarMedicamento;
    private JButton seleccionarTodos;
    private JButton borrarUno;
    private JButton borrarTodos;
    private JPanel medicamentosPanel;
    private JPanel recetasPanel;
    private JTable table1;
    private ChartPanel chartPanelLineas;
    private ChartPanel chartPanelPastel;


    public JPanel getPanel() {
        return panel;
    }

    public Dashboard_View() {
        initializeCharts();
        setupEventListeners();
        setupComboBoxes();
    }

    private void initializeCharts() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Medicamentos", "Mes", "Cantidad", dataset,
                PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, true);
        plot.setRenderer(renderer);

        chartPanelLineas = new ChartPanel(lineChart);
        medicamentosPanel.setLayout(new BorderLayout());
        medicamentosPanel.add(chartPanelLineas, BorderLayout.CENTER);

        DefaultPieDataset pieDataset = new DefaultPieDataset();
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Recetas", pieDataset, true, true, false);

        PiePlot piePlot = (PiePlot) pieChart.getPlot();
        piePlot.setSectionPaint("CONFECCIONADA", Color.YELLOW);
        piePlot.setSectionPaint("PROCESO", Color.BLUE);
        piePlot.setSectionPaint("LISTA", Color.GREEN);
        piePlot.setSectionPaint("ENTREGADA", Color.RED);

        chartPanelPastel = new ChartPanel(pieChart);
        recetasPanel.setLayout(new BorderLayout());
        recetasPanel.add(chartPanelPastel, BorderLayout.CENTER);
    }

    private void setupComboBoxes() {
        String[] años = {"", "2024", "2025", "2026"}; // Agregar opción vacía
        desdeAños.setModel(new DefaultComboBoxModel<>(años));
        hastaAño.setModel(new DefaultComboBoxModel<>(años));

        String[] meses = {"", "1-Enero", "2-Febrero", "3-Marzo", "4-Abril", "5-Mayo", "6-Junio",
                "7-Julio", "8-Agosto", "9-Septiembre", "10-Octubre", "11-Noviembre", "12-Diciembre"};
        desdeMes.setModel(new DefaultComboBoxModel<>(meses));
        hastaMes.setModel(new DefaultComboBoxModel<>(meses));

        medicamentosBox.setModel(new DefaultComboBoxModel<>());

        desdeAños.addActionListener(e -> actualizarFiltrosFecha());
        desdeMes.addActionListener(e -> actualizarFiltrosFecha());
        hastaAño.addActionListener(e -> actualizarFiltrosFecha());
        hastaMes.addActionListener(e -> actualizarFiltrosFecha());

        actualizarFiltrosFecha();
    }

    private void actualizarFiltrosFecha() {
        if (controller == null) return;

        String añoDesde = (String) desdeAños.getSelectedItem();
        String mesDesdeCompleto = (String) desdeMes.getSelectedItem();

        String añoHasta = (String) hastaAño.getSelectedItem();
        String mesHastaCompleto = (String) hastaMes.getSelectedItem();

        if (añoDesde == null || añoDesde.isEmpty() ||
                mesDesdeCompleto == null || mesDesdeCompleto.isEmpty() ||
                añoHasta == null || añoHasta.isEmpty() ||
                mesHastaCompleto == null || mesHastaCompleto.isEmpty()) {

            try {
                controller.clearMedicamentos();
            } catch (Exception ex) {
            }
            return;
        }

        String mesDesde = extraerNumeroMes(mesDesdeCompleto);
        String mesHasta = extraerNumeroMes(mesHastaCompleto);

        String fechaDesde = añoDesde + "-" + mesDesde;
        String fechaHasta = añoHasta + "-" + mesHasta;

        if (!validarRangoFechas(fechaDesde, fechaHasta)) {
            JOptionPane.showMessageDialog(panel,
                    "La fecha 'Desde' debe ser anterior o igual a la fecha 'Hasta'",
                    "Rango de fechas inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            controller.updateDateRange(fechaDesde, fechaHasta);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String extraerNumeroMes(String mesCompleto) {
        if (mesCompleto == null || mesCompleto.isEmpty() || !mesCompleto.contains("-")) {
            return null;
        }

        String[] partes = mesCompleto.split("-");
        return partes[0];
    }


    private boolean validarRangoFechas(String fechaDesde, String fechaHasta) {
        try {
            String[] desdePartes = fechaDesde.split("-");
            String[] hastaPartes = fechaHasta.split("-");

            int añoDesde = Integer.parseInt(desdePartes[0]);
            int mesDesde = Integer.parseInt(desdePartes[1]);
            int añoHasta = Integer.parseInt(hastaPartes[0]);
            int mesHasta = Integer.parseInt(hastaPartes[1]);

            int valorDesde = añoDesde * 12 + mesDesde;
            int valorHasta = añoHasta * 12 + mesHasta;

            return valorDesde <= valorHasta;
        } catch (Exception e) {
            return false;
        }
    }

    private void setupEventListeners() {
        agregarMedicamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String medicamento = (String) medicamentosBox.getSelectedItem();
                if (medicamento != null && !medicamento.isEmpty()) {
                    try {
                        controller.addMedicamento(medicamento);
                        JOptionPane.showMessageDialog(panel, "Medicamento agregado", "", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        seleccionarTodos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.selectAllMedicamentos();
                    JOptionPane.showMessageDialog(panel, "Todos los medicamentos seleccionados", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        borrarUno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table1.getSelectedRow();
                if (row >= 0) {
                    try {
                        controller.removeSelectedMedicamento(row);
                        JOptionPane.showMessageDialog(panel, "Medicamento eliminado", "", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Seleccione una fila para eliminar", "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        borrarTodos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.clearMedicamentos();
                    JOptionPane.showMessageDialog(panel, "Todos los medicamentos eliminados", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
    }

    private void actualizarGraficoLineas() {
        if (model == null) return;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<String> mesesRango = model.getMesesEnRango();

        for (DashboardRowData rowData : model.getList()) {
            String medicamento = rowData.getNombreMedicamento();
            List<Integer> cantidades = rowData.getCantidadesPorMes();

            for (int i = 0; i < mesesRango.size() && i < cantidades.size(); i++) {
                dataset.addValue(cantidades.get(i), medicamento, mesesRango.get(i));
            }
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Medicamentos", "Mes", "Cantidad", dataset,
                PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);

        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, true);
        plot.setRenderer(renderer);

        chartPanelLineas.setChart(lineChart);
    }

    private void actualizarGraficoPastel() {
        if (model == null) return;

        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Integer> recetasPorEstado = model.getRecetasData();

        int total = recetasPorEstado.values().stream().mapToInt(Integer::intValue).sum();

        for (Map.Entry<String, Integer> entry : recetasPorEstado.entrySet()) {
            double porcentaje = total > 0 ? (entry.getValue() * 100.0) / total : 0;
            String label = entry.getKey() + " (" + String.format("%.0f%%", porcentaje) + ")";
            dataset.setValue(label, entry.getValue());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Recetas", dataset, true, true, false);

        PiePlot plot = (PiePlot) pieChart.getPlot();
        for (String key : recetasPorEstado.keySet()) {
            String labelKey = key + " (" + String.format("%.0f%%",
                    total > 0 ? (recetasPorEstado.get(key) * 100.0) / total : 0) + ")";

            switch (key.toUpperCase()) {
                case "CONFECCIONADA":
                    plot.setSectionPaint(labelKey, Color.YELLOW);
                    break;
                case "PROCESO":
                    plot.setSectionPaint(labelKey, Color.BLUE);
                    break;
                case "LISTA":
                    plot.setSectionPaint(labelKey, Color.GREEN);
                    break;
                case "ENTREGADA":
                    plot.setSectionPaint(labelKey, Color.RED);
                    break;
            }
        }

        chartPanelPastel.setChart(pieChart);
    }

    private void actualizarComboBoxMedicamentos() {
        if (model == null) return;

        DefaultComboBoxModel<String> medicamentosModel = new DefaultComboBoxModel<>();
        medicamentosModel.addElement("");

        for (Medicamento med : model.getMedicamentosDisponibles()) {
            medicamentosModel.addElement(med.getNombre());
        }
        medicamentosBox.setModel(medicamentosModel);
    }

    private void actualizarFiltros() {
        if (model == null || model.getFilter() == null) return;

        String mesDesde = model.getFilter().getMesDesde();
        String mesHasta = model.getFilter().getMesHasta();

        if (mesDesde != null && mesDesde.contains("-")) {
            String[] partes = mesDesde.split("-");
            if (partes.length == 2) {
                desdeAños.setSelectedItem(partes[0]);
                desdeMes.setSelectedItem(partes[1]);
            }
        }

        if (mesHasta != null && mesHasta.contains("-")) {
            String[] partes = mesHasta.split("-");
            if (partes.length == 2) {
                hastaAño.setSelectedItem(partes[0]);
                hastaMes.setSelectedItem(partes[1]);
            }
        }
    }

    DashboardModel model;
    DashboardController controller;

    public void setModel(DashboardModel model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(DashboardController controller) {
        this.controller = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case DashboardModel.LIST:
                List<String> mesesRango = model.getMesesEnRango();

                if (mesesRango != null && !mesesRango.isEmpty()) {
                    int[] cols = DashboardTableModel.crearColumnasArray(mesesRango.size());
                    DashboardTableModel tableModel = new DashboardTableModel(cols, model.getList(), mesesRango);

                    table1.setModel(tableModel);
                    table1.setRowHeight(30);
                    table1.getTableHeader().revalidate();
                    table1.getTableHeader().repaint();

                    if (table1.getColumnModel().getColumnCount() > 0) {
                        TableColumnModel columnModel = table1.getColumnModel();
                        columnModel.getColumn(0).setPreferredWidth(120); // Medicamento
                        for (int i = 1; i < columnModel.getColumnCount(); i++) {
                            columnModel.getColumn(i).setPreferredWidth(80); // Meses
                        }
                    }
                } else {
                    int[] cols = {DashboardTableModel.MEDICAMENTO};
                    List<String> columnasVacias = new ArrayList<>();
                    DashboardTableModel tableModel = new DashboardTableModel(cols, model.getList(), columnasVacias);
                    table1.setModel(tableModel);
                }

                actualizarGraficoLineas();
                break;

            case DashboardModel.RECETAS_DATA:
                actualizarGraficoPastel();
                break;

            case DashboardModel.MEDICAMENTOS_DISPONIBLES:
                actualizarComboBoxMedicamentos();
                break;

            case DashboardModel.FILTER:
                actualizarFiltros();
                break;
        }
        this.panel.revalidate();
    }
}