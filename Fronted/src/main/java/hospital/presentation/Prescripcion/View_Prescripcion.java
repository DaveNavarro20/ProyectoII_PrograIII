package hospital.presentation.Prescripcion;

import com.github.lgooddatepicker.components.DatePicker;
import hospital.presentation.Interfaces.InterfazAdministrador;
import hospital.logic.*;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Random;

public class View_Prescripcion implements PropertyChangeListener {
    private JPanel panel;
    private JButton buscarPaciente;
    private JButton agregarMedicamento;
    private JTable table;
    private JLabel FechaRetiro;
    private DatePicker elegirFecha;
    private JLabel verPaciente;
    private JButton guardar;
    private JButton detalles;
    private JButton descartarMedicamento;
    private JButton limpiar;
    private String doctorIngresado;

    public String getDoctorIngresado() {
        return doctorIngresado;
    }

    public void setDoctorIngresado(String doctorIngresado) {
        this.doctorIngresado = doctorIngresado;
    }

    private View_buscarPaciente buscarPacienteView;
    private View_buscarMedicamento buscarMedicamentoView;
    private View_modificarMedicamento modificarMedicamentoView;

    public View_Prescripcion() {
        buscarPacienteView = new View_buscarPaciente();
        buscarMedicamentoView = new View_buscarMedicamento();
        modificarMedicamentoView = new View_modificarMedicamento();

        buscarPaciente.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPacienteView.setVisible(true);
            }
        });

        agregarMedicamento.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarMedicamentoView.setVisible(true);
            }
        });

        descartarMedicamento.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.delete();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        table.addMouseListener((new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.getSelectedRow();
                controller.edit(row);
            }
        }));

        detalles.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarMedicamentoView.setVisible(true);
            }
        });

        guardar.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (model.getCurrentPaciente() == null) {
                        JOptionPane.showMessageDialog(panel,
                                "Debe seleccionar un paciente antes de guardar la receta.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    List<Prescripcion> lista = model.getList();
                    if (lista == null || lista.isEmpty()) {
                        JOptionPane.showMessageDialog(panel,
                                "Debe agregar al menos un medicamento a la receta.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (elegirFecha.getText() == null || elegirFecha.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(panel,
                                "Debe seleccionar una fecha de retiro.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Receta receta = new Receta();
                    for (Prescripcion objeto : lista) {
                        receta.getPrescripcions().add(objeto);
                    }

                    String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                    Random random = new Random();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 5; i++) {
                        int index = random.nextInt(caracteres.length());
                        sb.append(caracteres.charAt(index));
                    }

                    receta.setIdReceta(sb.toString());
                    receta.setEstado("Confeccionada");
                    receta.setPaciente(model.getCurrentPaciente());

                    try {
                        Usuario usuarioDoctor = Service.instance().buscarUsuario(getDoctorIngresado());
                        if (usuarioDoctor instanceof Medico) {
                            receta.setDoctor((Medico) usuarioDoctor);
                        } else {
                            Medico doctorBasico = new Medico();
                            doctorBasico.setId(getDoctorIngresado());
                            doctorBasico.setNombre("Doctor no encontrado");
                            receta.setDoctor(doctorBasico);
                        }
                    } catch (Exception ex) {
                        Medico doctorBasico = new Medico();
                        doctorBasico.setId(getDoctorIngresado());
                        doctorBasico.setNombre("Doctor no encontrado");
                        receta.setDoctor(doctorBasico);
                    }

                    receta.setFecha(elegirFecha.getText());
                    controller.createReceta(receta);

                    JOptionPane.showMessageDialog(panel,
                            "Receta guardada exitosamente con código: " + receta.getIdReceta(),
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    elegirFecha.clear();
                    verPaciente.setText("Paciente");
                    controller.clearTemporalList();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                            "Error al guardar la receta: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elegirFecha.clear();
                verPaciente.setText("Paciente");
                controller.clearTemporalList(); // Usar el método del controller
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    PrescripcionController controller;
    PrescripcionModel model;

    public void setController(PrescripcionController controller) {
        this.controller = controller;

        buscarPacienteView.setModel(controller.getPacientesModel());
        buscarPacienteView.setController(controller.getPacientesController());
        buscarPacienteView.setControllerPr(controller);

        buscarMedicamentoView.setModel(controller.getMedicamentosModel());
        buscarMedicamentoView.setController(controller.getMedicamentosController());
        buscarMedicamentoView.setControllerPr(controller);

        modificarMedicamentoView.setController(controller);
    }

    public void setModel(PrescripcionModel model) {
        this.model = model;
        model.addPropertyChangeListener(this);
        modificarMedicamentoView.setModel(model);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()){
            case PrescripcionModel.LIST:
                int[] cols={PrescripcionTableModel.NOMBRE, PrescripcionTableModel.PRESENTACION, PrescripcionTableModel.CANTIDAD, PrescripcionTableModel.INDICACIONES, PrescripcionTableModel.DURACION};
                table.setModel(new PrescripcionTableModel(cols, model.getList()));
                table.setRowHeight(30);
                TableColumnModel columnModel = table.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(150);
                columnModel.getColumn(2).setPreferredWidth(150);
                columnModel.getColumn(3).setPreferredWidth(150);
                columnModel.getColumn(4).setPreferredWidth(150);
                break;
            case PrescripcionModel.PACIENTE:
                if (model.getCurrentPaciente() != null) {
                    verPaciente.setText(model.getCurrentPaciente().getNombre());
                } else {
                    verPaciente.setText("Paciente");
                }
                break;
        }
        this.panel.revalidate();
    }
}