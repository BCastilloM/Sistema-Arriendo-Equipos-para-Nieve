package Vista;

import Controlador.ControladorArriendoEquipos;
import Excepciones.EquipoException;

import javax.swing.*;
import java.awt.event.*;

public class CreaEquipo extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;

    public CreaEquipo() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {

        String codigo = textField1.getText();
        String descripcion = textField2.getText();
        String PrecioArriendoDiario = textField3.getText();

        if(!codigo.isEmpty() && !descripcion.isEmpty() && !PrecioArriendoDiario.isEmpty()){
            try {
                ControladorArriendoEquipos.getInstance().creaImplemento(Long.parseLong(codigo),descripcion,Long.parseLong(PrecioArriendoDiario));
                JOptionPane.showMessageDialog(this, "Implemento creado satisfactoriamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            } catch (EquipoException e) {
                JOptionPane.showMessageDialog(this, "Ya existe un equipo con el codigo dado", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "Ha ocurrido un error, no se puede ingresar el implemento", "Error", JOptionPane.ERROR_MESSAGE);
        }

        textField1.setText("");
        textField2.setText("");
        textField3.setText("");



    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void display() {
        CreaEquipo dialog = new CreaEquipo();
        dialog.pack();
        dialog.setSize(500,500);
        dialog.setVisible(true);
    }
}
