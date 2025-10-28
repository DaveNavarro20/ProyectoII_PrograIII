package hospital.presentation.Dashboard;

import hospital.logic.Medicamento;
import hospital.logic.Receta;
import hospital.logic.Service;

import java.util.*;

public class DashboardController {

    private Dashboard_View view;
    private DashboardModel model;

    public DashboardController(Dashboard_View view, DashboardModel model) {
        this.view = view;
        this.model = model;

        try {
            List<Medicamento> medicamentos = Service.instance().findAll();
            Map<String, Integer> recetasData = cargarRecetasData();
            model.init(medicamentos, recetasData);

        } catch (Exception e) {
            model.init(new ArrayList<>(), new HashMap<>());
        }

        view.setController(this);
        view.setModel(model);
    }

    public void updateDateRange(String mesDesde, String mesHasta) throws Exception {
        if (mesDesde == null || mesDesde.isEmpty() || mesHasta == null || mesHasta.isEmpty()) {
            model.setList(new ArrayList<>());
            return;
        }

        model.getFilter().setMesDesde(mesDesde);
        model.getFilter().setMesHasta(mesHasta);
        model.setFilter(model.getFilter());

        if (!model.getFilter().getMedicamentosSeleccionados().isEmpty()) {
            search();
        }
    }

    private Map<String, Integer> cargarRecetasData() throws Exception {
        Map<String, Integer> recetasPorEstado = new HashMap<>();
        List<Receta> recetas = Service.instance().findAllRecetas();

        for (Receta receta : recetas) {
            String estado = receta.getEstado();
            if (estado != null && !estado.isEmpty()) {
                recetasPorEstado.merge(estado.toUpperCase(), 1, Integer::sum);
            }
        }

        if (recetasPorEstado.isEmpty()) {
            recetasPorEstado.put("CONFECCIONADA", 3);
            recetasPorEstado.put("PROCESO", 4);
            recetasPorEstado.put("LISTA", 4);
            recetasPorEstado.put("ENTREGADA", 3);
        }

        return recetasPorEstado;
    }

    public void search() throws Exception {
        DashboardFilter filter = model.getFilter();
        List<DashboardRowData> datosTabla = generarDatosTabla(filter);
        model.setList(datosTabla);
    }

    private List<DashboardRowData> generarDatosTabla(DashboardFilter filter) throws Exception {
        List<DashboardRowData> datosTabla = new ArrayList<>();

        if (filter.getMedicamentosSeleccionados().isEmpty()) {
            return datosTabla;
        }

        List<Receta> todasRecetas = Service.instance().findAllRecetas();
        Map<String, Map<String, Integer>> medicamentosPorMes = procesarRecetas(todasRecetas);

        List<String> mesesRango = model.getMesesEnRango();

        if (mesesRango == null || mesesRango.isEmpty()) {
            mesesRango = new ArrayList<>();
            for (Map<String, Integer> datosMedicamento : medicamentosPorMes.values()) {
                for (String mes : datosMedicamento.keySet()) {
                    if (!mesesRango.contains(mes)) {
                        mesesRango.add(mes);
                    }
                }
            }
            mesesRango.sort(Comparator.naturalOrder());

            model.setMesesDisponibles(mesesRango);
        }

        for (String medicamento : filter.getMedicamentosSeleccionados()) {
            Map<String, Integer> datosMedicamento = medicamentosPorMes.get(medicamento);
            if (datosMedicamento == null) {
                datosMedicamento = new HashMap<>();
            }

            List<Integer> cantidadesPorMes = new ArrayList<>();
            for (String mes : mesesRango) {
                cantidadesPorMes.add(datosMedicamento.getOrDefault(mes, 0));
            }

            DashboardRowData rowData = new DashboardRowData(medicamento, cantidadesPorMes);
            datosTabla.add(rowData);
        }

        return datosTabla;
    }


    private Map<String, Map<String, Integer>> procesarRecetas(List<Receta> recetas) {
        Map<String, Map<String, Integer>> medicamentosPorMes = new HashMap<>();

        for (Receta receta : recetas) {
            String fecha = receta.getFecha();
            if (fecha != null && !fecha.isEmpty()) {
                String mes = extraerMes(fecha);
                if (mes != null) {
                    if (receta.getPrescripcions() != null) {
                        for (var prescripcion : receta.getPrescripcions()) {
                            String nombreMedicamento = prescripcion.getNombre();
                            int cantidad = 0;
                            try {
                                cantidad = Integer.parseInt(prescripcion.getCantidad());
                            } catch (NumberFormatException e) {
                                cantidad = 1;
                            }

                            medicamentosPorMes
                                    .computeIfAbsent(nombreMedicamento, k -> new HashMap<>())
                                    .merge(mes, cantidad, Integer::sum);
                        }
                    }
                }
            }
        }

        return medicamentosPorMes;
    }

    private String extraerMes(String fecha) {
        try {
            if (fecha.contains("septiembre") || fecha.contains("9")) {
                return "2025-9";
            } else if (fecha.contains("octubre") || fecha.contains("10")) {
                return "2025-10";
            } else if (fecha.contains("agosto") || fecha.contains("8")) {
                return "2025-8";
            }

            String[] partes = fecha.split("[-/\\s]");
            for (String parte : partes) {
                if (parte.equals("8") || parte.equals("08")) return "2025-8";
                if (parte.equals("9") || parte.equals("09")) return "2025-9";
                if (parte.equals("10")) return "2025-10";
            }

            return "2025-9";
        } catch (Exception e) {
            return "2025-9";
        }
    }

    public void addMedicamento(String medicamento) throws Exception {
        if (medicamento != null && !medicamento.isEmpty()) {
            model.getFilter().addMedicamento(medicamento);
            model.setFilter(model.getFilter());
            search();
        }
    }

    public void removeMedicamento(String medicamento) throws Exception {
        model.getFilter().removeMedicamento(medicamento);
        model.setFilter(model.getFilter());
        search();
    }

    public void removeSelectedMedicamento(int row) throws Exception {
        if (row >= 0 && row < model.getList().size()) {
            DashboardRowData rowData = model.getList().get(row);
            removeMedicamento(rowData.getNombreMedicamento());
        }
    }

    public void clearMedicamentos() throws Exception {
        model.getFilter().clearMedicamentos();
        model.setFilter(model.getFilter());
        search();
    }

    public void selectAllMedicamentos() throws Exception {
        List<String> todosMedicamentos = new ArrayList<>();
        for (Medicamento med : model.getMedicamentosDisponibles()) {
            todosMedicamentos.add(med.getNombre());
        }
        model.getFilter().setMedicamentosSeleccionados(todosMedicamentos);
        model.setFilter(model.getFilter());
        search();
    }


    public void clear() throws Exception {
        model.setFilter(new DashboardFilter());
        search();
    }


}