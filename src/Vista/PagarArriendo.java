package Vista;

import Controlador.ControladorArriendoEquipos;
import Excepciones.ArriendoException;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PagarArriendo extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
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
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JLabel numTranLabel;
    private JLabel numTarLabel;
    private JLabel numCuoLabel;

    public PagarArriendo() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        fechaLabel.setText(LocalDate.now().toString());
        //REVISAR*****************************************************************

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
                textField3.setEnabled(true);
                numTarLabel.setEnabled(true);
                textField4.setEnabled(true);
            }
        });
        creditoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numTranLabel.setEnabled(true);
                textField3.setEnabled(true);
                numTarLabel.setEnabled(true);
                textField4.setEnabled(true);
                numCuoLabel.setEnabled(true);
                textField5.setEnabled(true);
            }
        });
    }

    private void buscaArriendo() {
        long codArriendo;
        try {
            String codStr = textField1.getText();
            codArriendo = Integer.parseInt(codStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un n??mero", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] arriendo = ControladorArriendoEquipos.getInstance().consultaArriendoAPagar(codArriendo);

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
        if(labelMonto.getText()==null|textField2.getText()==null) {
            JOptionPane.showMessageDialog(this, "No ha pagado ning??n arriendo", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }else {
            long codArriendo = Long.parseLong(textField1.getText());
            if(contadoRadioButton.isSelected()){
                try {
                    String montoStr = textField2.getText();
                    monto = Integer.parseInt(montoStr);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un n??mero en monto", "Error", JOptionPane.ERROR_MESSAGE);
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
                    String montoStr = textField2.getText();
                    monto = Integer.parseInt(montoStr);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un n??mero en monto", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String codTran, numTar;

                try {
                    codTran = textField3.getText();
                    int intCodTran = Integer.parseInt(codTran);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un n??mero en N??mero de la transacci??n", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    numTar = textField4.getText();
                    int intnumTar = Integer.parseInt(numTar);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un n??mero en N??mero de tarjeta", "Error", JOptionPane.ERROR_MESSAGE);
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
                    String montoStr = textField2.getText();
                    monto = Integer.parseInt(montoStr);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un n??mero en monto", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String codTran, numTar;
                int nroCuotas;

                try {
                    codTran = textField3.getText();
                    int intCodTran = Integer.parseInt(codTran);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un n??mero en N??mero de la transacci??n", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    numTar = textField4.getText();
                    int intnumTar = Integer.parseInt(numTar);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un n??mero en N??mero de tarjeta", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    String nCuotasStr = textField5.getText();
                    nroCuotas = Integer.parseInt(nCuotasStr);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ingrese un n??mero en N??mero de cuotas", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (nroCuotas <= 0) {
                    JOptionPane.showMessageDialog(this, "Ingrese un n??mero de cuotas v??lido", "Error", JOptionPane.ERROR_MESSAGE);
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
        // add your code here if necessary
        dispose();
    }
}
