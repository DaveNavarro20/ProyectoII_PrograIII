package hospital.presentation.Dashboard;

import hospital.presentation.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

public class DashboardTableModel extends AbstractTableModel<DashboardRowData> implements TableModel {

    public static final int MEDICAMENTO = 0;

    private List<String> mesesColumnas;

    public DashboardTableModel(int[] cols, List<DashboardRowData> rows, List<String> mesesColumnas) {
        super(cols, rows);
        this.mesesColumnas = (mesesColumnas != null) ? mesesColumnas : new ArrayList<>();;
        initColNames();
    }

    @Override
    protected Object getPropetyAt(DashboardRowData rowData, int col) {
        if (cols[col] == MEDICAMENTO) {
            return rowData.getNombreMedicamento();
        } else {
            int mesIndex = cols[col] - 1;
            if (mesIndex >= 0 && mesIndex < rowData.getCantidadesPorMes().size()) {
                return rowData.getCantidadesPorMes().get(mesIndex);
            }
            return 0;
        }
    }

    @Override
    protected void initColNames() {
        if (mesesColumnas == null) {
            mesesColumnas = new ArrayList<>();
        }

        int maxIndex = mesesColumnas.size();
        colNames = new String[maxIndex + 1];

        colNames[MEDICAMENTO] = "Medicamento";

        for (int i = 0; i < mesesColumnas.size(); i++) {
            colNames[i + 1] = mesesColumnas.get(i);
        }
    }

    public static int[] crearColumnasArray(int numeroMeses) {
        int[] cols = new int[1 + numeroMeses];
        cols[0] = MEDICAMENTO;

        for (int i = 1; i <= numeroMeses; i++) {
            cols[i] = i;
        }

        return cols;
    }

    @Override
    public int getColumnCount() {
        return cols != null ? cols.length : (colNames != null ? colNames.length : 0);
    }


    public List<String> getMesesColumnas() {
        return mesesColumnas;
    }
}