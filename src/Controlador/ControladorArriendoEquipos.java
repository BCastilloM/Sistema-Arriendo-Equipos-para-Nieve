package Controlador;

import Modelo.Cliente;
import Modelo.Equipo;

import java.util.ArrayList;

public class ControladorArriendoEquipos {
    private static ControladorArriendoEquipos instance = null;
    private final ArrayList<Cliente> clientes;
    private final ArrayList<Equipo> equipos;
    private ControladorArriendoEquipos(){
        clientes = new ArrayList();
        equipos = new ArrayList();
    }
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

        return   ;
    }
    public String[][] listaEquipos(){

        return   ;
    }

}
