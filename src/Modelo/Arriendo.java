package Modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Arriendo implements Serializable {
    private long codigo;
    private LocalDate fechaInicio;
    private LocalDate fechaDevolucion;
    private EstadoArriendo estado = EstadoArriendo.INICIADO;
    private Cliente cliente;
    private final ArrayList<DetalleArriendo> detalleArriendos;
    private final ArrayList<Pago> pagos;

    public Arriendo(long codigo, LocalDate fechaInicio, Cliente cliente) {
        this.codigo = codigo;
        this.fechaInicio = fechaInicio;
        this.cliente = cliente;
        detalleArriendos = new ArrayList<DetalleArriendo>();
        pagos = new ArrayList<Pago>();
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
        if (estado.equals(EstadoArriendo.DEVUELTO)) {
            Period period = Period.between(fechaInicio, fechaDevolucion);
            if (period.getDays() == 0) {
                return 1;
            } else {
                return period.getDays();
            }
        }
        return 0;
    }

    public long getMontoTotal() {
        long totalPagar = 0;
        if (estado == EstadoArriendo.INICIADO) {
            return totalPagar;
        }
        for (DetalleArriendo detArriendo : detalleArriendos) {
            totalPagar += detArriendo.getPrecioAplicado();
        }
        if (estado == EstadoArriendo.DEVUELTO) {
            return totalPagar * getNumeroDiasArriendo();
        }
        return totalPagar;
    }

    public String[][] getDetallesToString() {
        String[][] detallesArr = new String[detalleArriendos.size()][3];
        int i = 0;
        if (estado.equals(EstadoArriendo.DEVUELTO) || estado.equals(EstadoArriendo.ENTREGADO)) {
            for (DetalleArriendo detArriendo : detalleArriendos) {
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
        for (DetalleArriendo detArriendo : detalleArriendos) {
            equipoArriendo.add(detArriendo.getEquipo());
        }
        return equipoArriendo.toArray(new Equipo[0]);
    }

    public void addPagoContado(Contado pago) {
        pagos.add(pago);
    }

    public void addPagoDebito(Debito pago) {
        pagos.add(pago);
    }

    public void addPagoCredito(Credito pago) {
        pagos.add(pago);
    }

    public long getMontoPagado() {
        long total = 0;
        if (!pagos.isEmpty()) {
            for (Pago pago : pagos) {
                total += pago.getMonto();
            }
        }
        return total;
    }

    public long getSaldoAdeudado() {
        long porPagar = 0;
        porPagar = getMontoTotal() - getMontoPagado();
        return porPagar;
    }

    public String[][] getPagosToString() {
        if (!pagos.isEmpty()) {
            String[][] pagosAsociados = new String[pagos.size()][3];
            int i = 0;
            for (Pago pago : pagos) {
                pagosAsociados[i][0] = String.valueOf(pago.getMonto());
                pagosAsociados[i][1] = String.valueOf(pago.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                pagosAsociados[i][2] = String.valueOf(pago.getClass().getSimpleName());
                i++;
            }
            return pagosAsociados;
        }
        return new String[0][0];
    }
}
