package Modelo;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

public class Arriendo {
    private long codigo;
    private LocalDate fechaInicio;
    private LocalDate fechaDevolucion;
    private EstadoArriendo estado = EstadoArriendo.INICIADO;
    private Cliente cliente;
    private final ArrayList<DetalleArriendo> detalleArriendos;

    public Arriendo(long codigo, LocalDate fechaInicio, Cliente cliente) {
        this.codigo = codigo;
        this.fechaInicio = fechaInicio;
        this.cliente = cliente;
        detalleArriendos = new ArrayList<DetalleArriendo>();
    }

    public long getCodigo() {
        return codigo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public EstadoArriendo getEstado() {
        return estado;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public void setEstado(EstadoArriendo estado) {
        this.estado = estado;
    }

    public void addDetalleArriendo(Equipo equipo) {
        detalleArriendos.add(new DetalleArriendo(equipo.getPrecioArriendoDia(), equipo, this));
    }

    public int getNumeroDiasArriendo() {
        if(estado.equals(EstadoArriendo.DEVUELTO)) {
            Period period = Period.between(fechaInicio, fechaDevolucion);
            if(period.getDays() == 0) {
                return period.getDays()+1;
            }else {
                return period.getDays();
            }
        }
        return 0;
    }

    public long getMontoTotal() {
        long totalPagar=0;
        if (estado == EstadoArriendo.INICIADO) {
            return totalPagar;
        }
        for (DetalleArriendo detArriendo: detalleArriendos) {
                totalPagar+= detArriendo.getPrecioAplicado();
        }
        if (estado == EstadoArriendo.DEVUELTO) {
            return totalPagar * getNumeroDiasArriendo();
        }
        return totalPagar;
    }

    public String[][] getDetallesToString() {
        String[][] detallesArr = new String[detalleArriendos.size()][3];
        int i=0;
        if(estado.equals(EstadoArriendo.DEVUELTO) || estado.equals(EstadoArriendo.ENTREGADO)) {
            for (DetalleArriendo detArriendo: detalleArriendos) {
                detallesArr[i][0] = String.valueOf(codigo);
                detallesArr[i][1] = detArriendo.getEquipo().getDescripcion();
                detallesArr[i][2] = String.valueOf(detArriendo.getPrecioAplicado());
                i++;
            }
            return detallesArr;
        }
        return detallesArr;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Equipo[] getEquipo() {
        ArrayList<Equipo> equipoArriendo = new ArrayList<Equipo>();
        for (DetalleArriendo detArriendo: detalleArriendos) {
            equipoArriendo.add(detArriendo.getEquipo());
        }
        return equipoArriendo.toArray(new Equipo[0]);
    }

}
