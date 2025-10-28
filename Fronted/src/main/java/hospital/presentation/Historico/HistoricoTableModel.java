package hospital.presentation.Historico;

import hospital.presentation.AbstractTableModel;
import hospital.logic.Medico;
import hospital.logic.Receta;

import javax.swing.table.TableModel;
import java.util.List;

public class HistoricoTableModel extends AbstractTableModel <Receta> implements TableModel {

    public static final int NOMBRE_PACIENTE = 0;
    public static final int ID_PACIENTE = 1;
    public static final int NOMBRE_DOCTOR = 2;
    public static final int ESTADO = 3;
    public static final int ID_RECETA = 4;

    public HistoricoTableModel(int[] cols, List<Receta> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropetyAt(Receta e, int col) {
        switch (cols[col]){
            case NOMBRE_PACIENTE:
                return (e.getPaciente() != null) ? e.getPaciente().getNombre() : "Sin paciente";
                case ID_PACIENTE:
                    return (e.getPaciente() != null) ? e.getPaciente().getId() : "Sin paciente";
            case NOMBRE_DOCTOR:
                return (e.getDoctor() != null) ? e.getDoctor().getNombre() : "Sin doctor";
            case ESTADO:
                return e.getEstado();
            case ID_RECETA:
                return e.getIdReceta();
            default:
                return "";
        }
    }

    @Override
    protected void initColNames(){
        colNames = new String[5];
        colNames[NOMBRE_PACIENTE] = "Nombre del Paciente";
        colNames[ID_PACIENTE] = "Id de Paciente";
        colNames[NOMBRE_DOCTOR] = "Nombre del Doctor";
        colNames[ESTADO] = "Estado";
        colNames[ID_RECETA] = "ID de la Receta";
    }
}