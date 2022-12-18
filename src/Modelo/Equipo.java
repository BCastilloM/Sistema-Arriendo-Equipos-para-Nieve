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
    private EstadoEquipo estado = EstadoEquipo.OPERATIVO;
    private final ArrayList<DetalleArriendo> detalleArriendos;
    private final ArrayList<Equipo> equipos;

    // Constructor
    public Equipo(long codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        detalleArriendos = new ArrayList<DetalleArriendo>();
        equipos = new ArrayList<Equipo>();
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

    public void addEquipo(Equipo equipo) {
        if (!equipos.contains(equipo)) {
            equipos.add(equipo);
        }
    }

    public int getNroEquipos() {
        return equipos.size();
    }
}

