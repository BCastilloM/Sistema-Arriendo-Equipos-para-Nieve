package Modelo;

import java.util.Date;

public class Debito extends Pago {
    private String codTransaccion;
    private String numTarjeta;

    public Debito(long monto, Date fecha, String codTransaccion, String numTarjeta) {
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
