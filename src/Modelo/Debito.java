package Modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Debito extends Pago implements Serializable {
    private String codTransaccion;
    private String numTarjeta;

    public Debito(long monto, LocalDate fecha, String codTransaccion, String numTarjeta) {
        super(monto, fecha);
        this.codTransaccion = codTransaccion;
        this.numTarjeta = numTarjeta;
    }

    public String getCodTransaccion() {
        return codTransaccion;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }
}
