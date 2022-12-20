package Modelo;

import java.util.ArrayList;

public class Conjunto extends Equipo{
    private final ArrayList<Equipo> equipos;
    public Conjunto(long codigo, String descripcion) {
        super(codigo, descripcion);
        equipos = new ArrayList<Equipo>();
    }

    @Override
    public long getPrecioArriendoDia() {
        long precioArriendoDia=0;
        for (Equipo equipo: equipos) {
            precioArriendoDia+= equipo.getPrecioArriendoDia();
        }
        return precioArriendoDia;
    }

    @Override
    public void addEquipo(Equipo equipo) {
        if (!equipos.contains(equipo)) {
            equipos.add(equipo);
        }
    }

    @Override
    public int getNroEquipos() {
        return equipos.size();
    }
}
