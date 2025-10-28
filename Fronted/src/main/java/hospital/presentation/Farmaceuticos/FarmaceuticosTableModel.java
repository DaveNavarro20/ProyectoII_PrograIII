package hospital.presentation.Farmaceuticos;

import hospital.presentation.AbstractTableModel;
import hospital.logic.Farmaceutico;
import javax.swing.table.TableModel;
import java.util.List;

public class FarmaceuticosTableModel extends AbstractTableModel <Farmaceutico> implements TableModel {
    public static final int ID=0;
    public static final int NOMBRE=1;

    public FarmaceuticosTableModel(int[] cols, List<Farmaceutico> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropetyAt(Farmaceutico e, int col) {
        switch (cols[col]){
            case ID: return e.getId();
            case NOMBRE: return e.getNombre();
            default: return "";
        }
    }

    @Override
    protected void initColNames(){
        colNames = new String[3];
        colNames[ID]= "Id";
        colNames[NOMBRE]= "Nombre";
    }
}
