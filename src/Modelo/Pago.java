package Modelo;

import java.util.Date;

public abstract class Pago {
    protected long monto;
    protected Date fecha;

    public Pago(long monto, Date fecha) {
        this.monto = monto;
        this.fecha = fecha;
    }

    public long getMonto() {
        return monto;
    }

    public Date getFecha() {
        return fecha;
    }
}
