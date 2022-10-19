package Controlador;

import java.sql.SQLOutput;
import java.util.Scanner;
public class ControladorArriendoEquipos {
    private static ControladorArriendoEquipos instance = null;
    private final Scanner scan;
    private ControladorArriendoEquipos(){
        scan = new Scanner(System.in);
        scan.useDelimiter("[\t|\r\n]+");
    }
    public static ControladorArriendoEquipos getInstance(){

        if(instance == null){
            instance = new ControladorArriendoEquipos();
        }
        return instance;
    }
    public void creaCliente(String rut, String nom, String dir, String tel){
        System.out.println("Ingrese Rut del cliente");
        rut = scan.next();
        System.out.println("Ingrese Nombre del cliente");
        nom = scan.next();
        System.out.println("Ingrese Direccion del cliente");
        dir = scan.next();
        System.out.println("Ingrese Telefono del cliente");
        tel = scan.next();
    }
    public void creaEquipo(String cod, String desc, long precio){
        System.out.println("Ingrese Codigo del equipo");
        cod = scan.next();
        System.out.println("Ingrese Descripcion del equipo");
        desc = scan.next();
        System.out.println("Ingrese Precio de arriendo por dia del equipo");
        precio = scan.nextLong();
    }
    /*

    public String[][] listaClientes(){
        //Figura 5 (?

        return   ;
    }
    */

    /*
    public String[][] listaEquipos(){
        //Figura 6 (?

        return   ;
    }
     */

}
