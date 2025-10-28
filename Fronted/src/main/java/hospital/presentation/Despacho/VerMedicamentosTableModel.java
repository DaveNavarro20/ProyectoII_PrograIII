package hospital.presentation.Despacho;

import hospital.logic.Prescripcion;
import hospital.presentation.AbstractTableModel;

import java.util.List;

public class VerMedicamentosTableModel extends AbstractTableModel<Prescripcion> implements javax.swing.table.TableModel{

    public VerMedicamentosTableModel(int[] cols, List<Prescripcion> rows) {
        super(cols, rows);
    }

    public static final int NOMBRE = 0;
    public static final int PRESENTACION = 1;
    public static final int CANTIDAD = 2;
    public static final int INDICACIONES = 3;
    public static final int DURACION = 4;

    @Override
    protected void initColNames() {
        colNames = new String[5];
        colNames[NOMBRE] = "Id Receta";
        colNames[PRESENTACION] = "Presentacion";
        colNames[CANTIDAD] = "Cantidad";
        colNames[INDICACIONES] = "Indicacion";
        colNames[DURACION] = "Duracion";
    }

    @Override
    protected Object getPropetyAt(Prescripcion r, int col) {
            switch (cols[col]) {
                case NOMBRE:
                    return r.getNombre();
                case PRESENTACION:
                    return r.getPresentacion();
                case CANTIDAD:
                    return  r.getCantidad();
                case INDICACIONES:
                    return r.getIndicaciones();
                case DURACION:
                    return r.getDuracion();
                default:
                    return "";
            }
    }
}
