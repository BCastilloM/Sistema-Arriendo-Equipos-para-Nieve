package Vista;

import Controlador.ControladorArriendoEquipos;

import javax.swing.*;
import java.awt.event.*;
import java.util.zip.DataFormatException;

public class Principal extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton arriendoEquipoButton;
    private JButton nuevoClienteButton;
    private JButton nuevoImplementoButton;
    private JButton nuevoConjuntoButton;
    private JButton devuelveEquipoButton;
    private JButton listadoClientesButton;
    private JButton pagaArriendoButton;
    private JButton listadoArriendosButton;
    private JButton listadoArriendButton;
    private JButton detalleDeUnAButton;
    private JButton listadoPagosArriendoButton;
    private JButton listadoEquiposButton;
    private JButton abrirButton;

    public Principal() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        nuevoImplementoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreaEquipo.display();
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGuardar();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        abrirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLeer();
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
        pagaArriendoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                PagarArriendo dialog = new PagarArriendo();
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        listadoPagosArriendoButton.addActionListener(e -> {
            String codigoString = JOptionPane.showInputDialog(null, "Ingrese el código del arriendo", "Input", JOptionPane.QUESTION_MESSAGE);
            if (codigoString == null) {
                return;
            }
            int codigo;
            try {
                codigo = Integer.parseInt(codigoString);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Se ingresó un valor no válido", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }


            ListadoPagosArriendo.display(codigo);
        });

        listadoArriendButton.addActionListener(e -> ListadoArriendo.display());
    }
    private void onLeer(){
        try {
            ControladorArriendoEquipos.getInstance().readDatosSistema();
            JOptionPane.showMessageDialog(this, "Se leyeron los datos", "EXITO", JOptionPane.INFORMATION_MESSAGE);
        } catch (DataFormatException e) {
            JOptionPane.showMessageDialog(this, "No se puede leer el archivo", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void onGuardar() {
        try {
            ControladorArriendoEquipos.getInstance().saveDatosSistema();
            JOptionPane.showMessageDialog(this,"se guardaron los datos", "EXITO", JOptionPane.INFORMATION_MESSAGE);
        } catch (DataFormatException e) {
            JOptionPane.showMessageDialog(this, "No se puede guardar el archivo", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Principal dialog = new Principal();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
