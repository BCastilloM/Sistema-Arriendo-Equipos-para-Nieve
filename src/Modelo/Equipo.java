/*
 *  Autores:
 *  Ángel Tomás Montecino Flores
 */

package Modelo;

import java.util.ArrayList;

public abstract class Equipo {
    // Atributos
    private long codigo;
    private String descripcion;
    private long precioArriendoDia;
    private EstadoEquipo estado = EstadoEquipo.OPERATIVO;
    private final ArrayList<DetalleArriendo> detalleArriendos;

    // Constructor
    public Equipo(long codigo, String descripcion, long precioArriendoDia) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precioArriendoDia = precioArriendoDia;
        detalleArriendos = new ArrayList<DetalleArriendo>();
    }

    // Métodos
    public long getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public abstract long getPrecioArriendoDia();

    public EstadoEquipo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEquipo estado) {
        this.estado = estado;
    }

    public void addDetalleArriendo(DetalleArriendo detalleArriendo) {
        detalleArriendos.add(detalleArriendo);
    }

    public boolean isArrendado() {
        if (!detalleArriendos.isEmpty()) {
            return detalleArriendos.get(detalleArriendos.size() - 1).getArriendo().getEstado() == EstadoArriendo.ENTREGADO;
        }
        return false;
    }
}

