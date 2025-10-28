package hospital.presentation.Pacientes;

import hospital.presentation.AbstractTableModel;
import hospital.logic.Paciente;

import javax.swing.table.TableModel;
import java.util.List;

public class PacientesTableModel extends AbstractTableModel<Paciente> implements TableModel {
    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int FECHA_NACIMIENTO = 2;
    public static final int TELEFONO = 3;

    public PacientesTableModel(int[] cols, List<Paciente> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropetyAt(Paciente e, int col) {
        switch (cols[col]) {
            case ID:
                return e.getId();
            case NOMBRE:
                return e.getNombre();
            case FECHA_NACIMIENTO:
                return e.getFechaNacimiento();
            case TELEFONO:
                return e.getTelefono();
            default:
                return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[4];
        colNames[ID] = "Id";
        colNames[NOMBRE] = "Nombre";
        colNames[FECHA_NACIMIENTO] = "Fecha De Nacimiento";
        colNames[TELEFONO] = "Teléfono";
    }
}
