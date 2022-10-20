package Controlador;

import Modelo.Cliente;
import Modelo.Equipo;

import java.util.ArrayList;

public class ControladorArriendoEquipos {
    // Atributos
    private static ControladorArriendoEquipos instance = null;
    private final ArrayList<Cliente> clientes;
    private final ArrayList<Equipo> equipos;

    // Constructor
    private ControladorArriendoEquipos(){
        clientes = new ArrayList<>();
        equipos = new ArrayList<>();
    }

    // Métodos

    // Crea objeto de tipo ControladorArriendoEquipos
    public static ControladorArriendoEquipos getInstance(){
        if(instance == null){
            instance = new ControladorArriendoEquipos();
        }
        return instance;
    }

    public void creaCliente(String rut, String nom, String dir, String tel){
        clientes.add(new Cliente(rut, nom, dir, tel));

    }

    public void creaEquipo(long cod, String desc, long precio){
        equipos.add(new Equipo(cod, desc, precio));
    }

    public String[][] listaClientes(){
        String[][] ClientesArr = new String[clientes.size()][5];
        int i = 0;
        for (Cliente cliente : clientes){
            ClientesArr[i][0] = cliente.getRut();
            ClientesArr[i][1] = cliente.getNombre();
            ClientesArr[i][2] = cliente.getDireccion();
            ClientesArr[i][3] = cliente.getTelefono();
            if(clientes.get(i).isActivo()) {
                ClientesArr[i][4] = "Activo";
            }else{
                ClientesArr[i][4] = "Inactivo";
            }
            i++;
        }
        return ClientesArr;
    }

    public String[][] listaEquipos(){
        String[][] EquiposArr = new String[equipos.size()][4];
        int i = 0;
        for (Equipo equipo : equipos){
            EquiposArr[i][0] = String.valueOf(equipo.getCodigo());
            EquiposArr[i][1] = equipo.getDescripcion();
            EquiposArr[i][2] = String.valueOf(equipo.getPrecioArriendoDia());
            EquiposArr[i][3] = String.valueOf(equipo.getEstado());
            i++;
        }
        return EquiposArr;
    }
}
