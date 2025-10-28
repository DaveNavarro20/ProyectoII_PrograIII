package hospital.presentation.Despacho;

import hospital.presentation.AbstractTableModel;
import java.util.List;
import hospital.logic.Receta;

public class DespachoTableModel extends AbstractTableModel<Receta> implements javax.swing.table.TableModel {

    public DespachoTableModel(int[] cols, List<Receta> rows) {
        super(cols, rows);
    }

    public static final int IDRECETA = 0;
    public static final int ESTADO = 1;
    public static final int IDPACIENTE = 2;
    public static final int IDDOCTOR = 3;

    @Override
    protected void initColNames() {
        colNames = new String[4];
        colNames[IDRECETA] = "Id Receta";
        colNames[ESTADO] = "Estado";
        colNames[IDPACIENTE] = "Id Paciente";
        colNames[IDDOCTOR] = "Id Doctor";
    }

    @Override
    protected Object getPropetyAt(Receta r, int col) {
        switch (cols[col]) {
            case IDRECETA:
                return r.getIdReceta();
            case ESTADO:
                return r.getEstado();
            case IDPACIENTE:
                return r.getPaciente().getNombre();
            case IDDOCTOR:
                return (r.getDoctor() != null) ? r.getDoctor().getId() : "";
            default:
                return "";
        }
    }
}

