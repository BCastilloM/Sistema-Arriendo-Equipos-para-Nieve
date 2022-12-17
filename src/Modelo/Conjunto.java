package Modelo;

import java.util.ArrayList;

public class Conjunto extends Equipo{
    private ArrayList<Equipo> equipos;
    public Conjunto(long codigo, String descripcion, long precioArriendoDia) {
        super(codigo, descripcion, precioArriendoDia);
        equipos = new ArrayList<Equipo>();
    }

    @Override
    public long getPrecioArriendoDia() {
        return 0;
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
