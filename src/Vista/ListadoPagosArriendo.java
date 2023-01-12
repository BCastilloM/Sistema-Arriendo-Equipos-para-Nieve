package Vista;

import Controlador.ControladorArriendoEquipos;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;

public class ListadoPagosArriendo extends JDialog {
    private JPanel contentPane;
    private JButton buttonBack;
    private JLabel labelTitle;
    private JPanel panel;
    private JTable table;
    private JScrollPane scrollPane;
    private JLabel labelCodigo;
    private TableModel tableModel;

    public ListadoPagosArriendo(String[][] datosListado, String[] nombreColumnas, int cod) {
        setContentPane(contentPane);
        setModal(true);
        labelCodigo.setText(String.valueOf(cod));

        tableModel = new DefaultTableModel(datosListado, nombreColumnas){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        for (int i=0; i<table.getColumnCount();i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        buttonBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBack();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onBack();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBack();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public static void display(int codigo) {
        String[] titulos = {"Monto", "Fecha", "Tipo pago"};
        String[][] datos = ControladorArriendoEquipos.getInstance().listaPagosDeArriendo(codigo);
        if (datos.length == 0) {
            JOptionPane.showMessageDialog(null, "No existen pagos asociados", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ListadoPagosArriendo dialog = new ListadoPagosArriendo(datos, titulos, codigo);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void onBack() {
        dispose();
    }

}
