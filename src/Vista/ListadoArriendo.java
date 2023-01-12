package Vista;

import Controlador.ControladorArriendoEquipos;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ListadoArriendo extends JDialog {
    private JPanel contentPane;
    private JButton buttonList;
    private JButton buttonBack;
    private JScrollPane scrollPane;
    private JPanel panel;
    private JLabel labelTitle;
    private JTable table;
    private JTextField textFieldFin;
    private JTextField textFieldInicio;
    private JLabel labelFInicio;
    private JLabel labelFFin;
    private TableModel tableModel;
    private String[] nombreColumnas;

    public ListadoArriendo() {
        nombreColumnas = new String[]{"Codigo", "Fecha inicio", "Fecha devol.", "Estado", "Rut cliente", "Nombre cliente" ,"Monto total"};

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        textFieldFin.setText(LocalDate.now().plusMonths(1).format(formatter));
        textFieldInicio.setText(LocalDate.now().minusMonths(10).format(formatter));

        String[][] datosListado = {};

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonList);

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

        buttonList.addActionListener(e -> listar());

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

    private void listar() {
        LocalDate inicio, fin;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            inicio = LocalDate.parse(textFieldInicio.getText(), formatter);
            fin = LocalDate.parse(textFieldFin.getText(), formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "error", "error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[][] datosListado = ControladorArriendoEquipos.getInstance().listaArriendos(inicio, fin);
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
    }

    private void onBack() {
        dispose();
    }

    public static void display() {
        ListadoArriendo dialog = new ListadoArriendo();
        dialog.pack();
        dialog.setVisible(true);
    }

}
