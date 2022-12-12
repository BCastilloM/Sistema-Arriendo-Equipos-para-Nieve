/*
 *  Autores:
 *  Benjamín Antonio Castillo Molina
 *  Ángel Tomás Montecino Flores
 */

package Modelo;

import java.util.ArrayList;

public class Cliente {
    // Atributos
    private String rut;
    private String nombre;
    private String direccion;
    private String telefono;
    private boolean activo = true;

    private final ArrayList<Arriendo> arriendos;

    // Constructor
    public Cliente(String rut, String nombre, String direccion, String telefono) {
        this.rut = rut;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        arriendos = new ArrayList<Arriendo>();
    }

    // Métodos
    public String getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public boolean isActivo() {
        return activo;
    }


    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setInactivo(boolean inactivo) {
        this.activo = inactivo;

    }

    public void addArriendo(Arriendo arriendo) {
        arriendos.add(arriendo);
    }


    public Arriendo[] getArriendosPorDevolver() {
        for (Arriendo arriendo : arriendos) {
            if (arriendo.getEstado() == EstadoArriendo.ENTREGADO) {
                arriendos.add(arriendo);
            }
        }
        return arriendos.toArray(new Arriendo[0]);
    }


}