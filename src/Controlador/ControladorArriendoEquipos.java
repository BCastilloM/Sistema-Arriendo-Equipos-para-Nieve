package Controlador;

public class ControladorArriendoEquipos {
    private static ControladorArriendoEquipos instance = null;
    private ControladorArriendoEquipos(){

    }
    public static ControladorArriendoEquipos getInstance(){

        if(instance == null){
            instance = new ControladorArriendoEquipos();
        }
        return instance;
    }
    public void creaCliente(String rut, String nom, String dir, String tel){

    }
    public void creaEquipo(long cod, String desc, long precio){

    }
    public String[][] listaClientes(){

        return   ;
    }
    public String[][] listaEquipos(){

        return   ;
    }

}
