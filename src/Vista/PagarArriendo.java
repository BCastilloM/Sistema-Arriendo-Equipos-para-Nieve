package Vista;

import Controlador.ControladorArriendoEquipos;
import Excepciones.ArriendoException;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;

public class PagarArriendo extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField arriendoCodtextField;
    private JButton buscarArriendoButton;
    private JLabel labelEstado;
    private JLabel labelRut;
    private JLabel labelNombre;
    private JLabel labelMonto;
    private JLabel labelMontoPagado;
    private JLabel labelSaldoAdeudado;
    private JRadioButton contadoRadioButton;
    private JRadioButton debitoRadioButton;
    private JRadioButton creditoRadioButton;
    private JLabel fechaLabel;
    private JTextField montotextField;
    private JTextField numTranTextField;
    private JTextField numTarTextField;
    private JTextField numCuoTextField;
    private JLabel numTranLabel;
    private JLabel numTarLabel;
    private JLabel numCuoLabel;

    public PagarArriendo() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        fechaLabel.setText(LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getYear());

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
                arriendoCodtextField.setText("");
                labelMonto.setText("");
                labelMontoPagado.setText("");
                labelSaldoAdeudado.setText("");
                labelNombre.setText("");
                labelRut.setText("");
                labelEstado.setText("");
                fechaLabel.setText("");
                numCuoLabel.setText("");
                numTarLabel.setText("");
                numTranLabel.setText("");
                montotextField.setText("");
                numTranTextField.setText("");
                numTarTextField.setText("");
                numCuoTextField.setText("");
                dispose();
                JOptionPane.showMessageDialog(null, "El pago se ha realizado exitosamente", "", JOptionPane.INFORMATION_MESSAGE);
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
        buscarArriendoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscaArriendo();
            }
        });
        debitoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numTranLabel.setEnabled(true);
                numTranTextField.setEnabled(true);
                numTarLabel.setEnabled(true);
                numTarTextField.setEnabled(true);
                numCuoLabel.setEnabled(false);
                numCuoTextField.setEnabled(false);
            }
        });
        creditoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numTranLabel.setEnabled(true);
                numTranTextField.setEnabled(true);
                numTarLabel.setEnabled(true);
                numTarTextField.setEnabled(true);
                numCuoLabel.setEnabled(true);
                numCuoTextField.setEnabled(true);
            }
        });
        contadoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                numTranLabel.setEnabled(false);
                numTranTextField.setEnabled(false);
                numTarLabel.setEnabled(false);
                numTarTextField.setEnabled(false);
                numCuoLabel.setEnabled(false);
                numCuoTextField.setEnabled(false);
            }
        });
    }

    private void buscaArriendo() {
        long codArriendo;
        try {
            String codStr = arriendoCodtextField.getText();
            codArriendo = Integer.parseInt(codStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] arriendo = ControladorArriendoEquipos.getInstance().consultaArriendoAPagar(codArriendo);
        System.out.printf("%d %d", codArriendo, arriendo.length);

        if (arriendo.length == 0) {
            JOptionPane.showMessageDialog(this, "El arriendo no existe", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            labelEstado.setText(arriendo[1]);
            labelRut.setText(arriendo[2]);
            labelNombre.setText(arriendo[3]);
            labelMonto.setText(arriendo[4]);
            labelMontoPagado.setText(arriendo[5]);
            labelSaldoAdeudado.setText(arriendo[6]);
        }
    }
    private void onOK() {
        int monto;
        if(labelMonto.getText()==null| montotextField.getText()==null) {
            JOptionPane.showMessageDialog(this, "No ha pagado ningún arriendo", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }else {
            long codArriendo;
            try {
                codArriendo = Long.parseLong(arriendoCodtextField.getText());
            }catch (NumberFormatException e){
                JOptionPane.showMessageDialog(this, "Por favor, revisar el código del arriendo", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(contadoRadioButton.isSelected()){
                try {
                    String montoStr = montotextField.getText();
                    monto = Integer.parseInt(montoStr);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un número en monto", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    ControladorArriendoEquipos.getInstance().pagaArriendoContado(codArriendo, monto);
                } catch (ArriendoException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                return;
            }
            if(debitoRadioButton.isSelected()){
                try {
                    String montoStr = montotextField.getText();
                    monto = Integer.parseInt(montoStr);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un número en monto", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String codTran, numTar;

                try {
                    codTran = numTranTextField.getText();
                    int intCodTran = Integer.parseInt(codTran);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un número en Número de la transacción", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    numTar = numTarTextField.getText();
                    int intnumTar = Integer.parseInt(numTar);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un número en Número de tarjeta", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    ControladorArriendoEquipos.getInstance().pagaArriendoDebito(codArriendo, monto, codTran, numTar);
                } catch (ArriendoException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if(creditoRadioButton.isSelected()){
                try {
                    String montoStr = montotextField.getText();
                    monto = Integer.parseInt(montoStr);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un número en monto", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String codTran, numTar;
                int nroCuotas;

                try {
                    codTran = numTranTextField.getText();
                    int intCodTran = Integer.parseInt(codTran);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un número en Número de la transacción", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    numTar = numTarTextField.getText();
                    int intnumTar = Integer.parseInt(numTar);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un número en Número de tarjeta", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    String nCuotasStr = numCuoTextField.getText();
                    nroCuotas = Integer.parseInt(nCuotasStr);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un número en Número de cuotas", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (nroCuotas <= 0) {
                    JOptionPane.showMessageDialog(this, "Ingrese un número de cuotas válido", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    ControladorArriendoEquipos.getInstance().pagaArriendoCredito(codArriendo, monto, codTran, numTar, nroCuotas);
                } catch (ArriendoException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
    }

    private void onCancel() {

        dispose();
    }
}
