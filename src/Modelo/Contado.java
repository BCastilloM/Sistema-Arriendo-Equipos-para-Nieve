package Modelo;

import java.util.Date;

public class Contado extends Pago {
    public Contado(long monto, Date fecha) {
        super(monto, fecha);
    }
}