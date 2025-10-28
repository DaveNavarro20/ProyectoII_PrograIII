package hospital.presentation.Historico;

import com.itextpdf.text.DocumentException;
import hospital.logic.Receta;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class Historico_View extends Component implements PropertyChangeListener  {
    private JPanel panel;
    private JLabel searchNombreLbl;
    private JTextField searchNombre;
    private JButton search;
    private JTable list;

    public JPanel getPanel() {
        return panel;
    }

    public Historico_View(){
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Receta filter = new Receta();
                    filter.setIdReceta(searchNombre.getText());
                    controller.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = list.getSelectedRow();
                controller.edit(row);
            }
        });

    }


    HistoricosModel model;
    HistoricosController controller;

    public void setModel(HistoricosModel model){
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(HistoricosController controller){
        this.controller = controller;
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case HistoricosModel.LIST:
                int[] cols = {HistoricoTableModel.NOMBRE_PACIENTE,HistoricoTableModel.ID_PACIENTE, HistoricoTableModel.NOMBRE_DOCTOR, HistoricoTableModel.ESTADO, HistoricoTableModel.ID_RECETA};
                list.setModel(new HistoricoTableModel(cols, model.getList()));
                list.setRowHeight(30);
                TableColumnModel columnModel = list.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(200);
                columnModel.getColumn(1).setPreferredWidth(200);
                columnModel.getColumn(2).setPreferredWidth(200);
                columnModel.getColumn(3).setPreferredWidth(150);
                columnModel.getColumn(4).setPreferredWidth(150);
                break;
            case HistoricosModel.CURRENT:
                break;
            case HistoricosModel.FILTER:
                searchNombre.setText(model.getFilter().getIdReceta());
                break;
        }
        this.panel.revalidate();
    }
}
