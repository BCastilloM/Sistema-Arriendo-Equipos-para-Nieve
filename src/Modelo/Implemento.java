package Modelo;

import java.io.Serializable;

public class Implemento extends Equipo implements Serializable {
    private long precioArriendoDia;

    public Implemento(long codigo, String descripcion, long precioArriendoDia) {
        super(codigo, descripcion);
        this.precioArriendoDia = precioArriendoDia;
    }

    @Override
    public long getPrecioArriendoDia() {
        return precioArriendoDia;
    }
}