package Modelo;

import java.io.Serializable;

public enum EstadoArriendo implements Serializable {
    INICIADO,
    ENTREGADO,
    DEVUELTO,
    PAGADO
}
