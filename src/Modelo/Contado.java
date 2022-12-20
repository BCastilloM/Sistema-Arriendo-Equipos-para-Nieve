package Modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Contado extends Pago implements Serializable {
    public Contado(long monto, LocalDate fecha) {
        super(monto, fecha);
    }
}