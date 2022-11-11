package Modelo;

import java.util.ArrayList;
import java.util.Date;

public class Arriendo {
    private long codigo;
    private Date fechaInicio;
    private Date fechaDevolucion;
    private EstadoArriendo estado = EstadoArriendo.INICIADO;
    private Cliente cliente;
    private final ArrayList<DetalleArriendo> detalleArriendos;

    public Arriendo(long codigo, Date fechaInicio, Cliente cliente) {
        this.codigo = codigo;
        this.fechaInicio = fechaInicio;
        this.cliente = cliente;
        detalleArriendos = new ArrayList<DetalleArriendo>();
    }

    public long getCodigo() {
        return codigo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public EstadoArriendo getEstado() {
        return estado;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public void setEstado(EstadoArriendo estado) {
        this.estado = estado;
    }


    public void addDetalleArriendo(Equipo equipo) {
        detalleArriendos.add(new DetalleArriendo(equipo.getPrecioArriendoDia(), equipo, this));

    }
    /*
    public int getNumeroDiasArriendo() {
        if(estado.equals(EstadoArriendo.DEVUELTO)) {
            return ;
        }
        return 0;
    }

    public long getMontoTotal() {

    }

    public Cliente getCliente() {
        return cliente;
    }

    public Equipo[] getEquipo() {

    }

     */
}
