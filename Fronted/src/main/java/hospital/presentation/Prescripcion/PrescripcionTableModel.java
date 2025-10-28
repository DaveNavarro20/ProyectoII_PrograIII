package hospital.presentation.Prescripcion;

import hospital.presentation.AbstractTableModel;
import hospital.logic.Prescripcion;

import java.util.List;

public class PrescripcionTableModel extends AbstractTableModel<Prescripcion> implements javax.swing.table.TableModel {
    public static final int NOMBRE = 0;
    public static final int PRESENTACION = 1;
    public static final int CANTIDAD = 2;
    public static final int INDICACIONES = 3;
    public static final int DURACION = 4;

    public PrescripcionTableModel(int[] cols, List<Prescripcion> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropetyAt(Prescripcion e, int col) {
        switch (cols[col]) {
            case NOMBRE:
                return e.getNombre();
            case PRESENTACION:
                return e.getPresentacion();
            case CANTIDAD:
                return e.getCantidad();
            case INDICACIONES:
                return e.getIndicaciones();
            case DURACION:
                return e.getDuracion();
            default:
                return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[5];
        colNames[NOMBRE] = "Nombre";
        colNames[PRESENTACION] = "Presentación";
        colNames[CANTIDAD] = "Cantidad";
        colNames[INDICACIONES] = "Indicaciones";
        colNames[DURACION] = "Duracion";
    }
}
