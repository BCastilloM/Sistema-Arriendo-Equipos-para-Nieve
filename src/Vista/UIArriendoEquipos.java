package Vista;

import Controlador.ControladorArriendoEquipos;

import java.util.Scanner;
public class UIArriendoEquipos {
    private static UIArriendoEquipos instance=null;
    private final Scanner scan;

    private UIArriendoEquipos() {
        scan = new Scanner(System.in);
        scan.useDelimiter("[\t|\r\n]+");
    }

    public static UIArriendoEquipos getInstance() {
        if(instance == null) {
            instance = new UIArriendoEquipos();
        }
        return instance;
    }

    public void menu() {
        int opcion=0;

        do{
            System.out.println("\n\n\n******* SISTEMA DE ARRIENDO DE EQUIPOS DE NIEVE ******");
            System.out.println("*** MENÚ DE OPCIONES ***");
            System.out.println("1.- Crea un nuevo cliente");
            System.out.println("2.- Crea un nuevo equipo");
            System.out.println("3.- Lista todos los clientes");
            System.out.println("4.- Lista todos los equipos");
            System.out.println("5.- Salir");
            System.out.print("\tIngrese opción: ");
                opcion = scan.nextInt();

            switch (opcion) {
                case 1 -> creaCliente();
                case 2 -> creaEquipo();
                case 3 -> listaClientes();
                case 4 -> listaEquipos();
                case 5 -> {}
                default -> System.out.println("Error! El valor no está dentro del rango válido");
            }
        }while(opcion != 5);
    }

    public void creaCliente() {
        String rut, nombre, direccion, telefono;

        System.out.println("\nIngrese su Rut");
        rut = scan.next();

        System.out.println("Ingrese su nombre");
        nombre = scan.next();

        System.out.println("Ingrese su domicilio");
        direccion = scan.next();

        System.out.println("Ingrese su número de teléfono");
        telefono = scan.next();

        ControladorArriendoEquipos.getInstance().creaCliente(rut, nombre, direccion, telefono);
    }

    public void creaEquipo() {
        long codigo, precioArriendoDia;
        String descripcion;

        System.out.println("\nIngrese el código");
        codigo = scan.nextLong();

        System.out.println("Ingrese la descripción");
        descripcion = scan.next();

        System.out.println("Ingrese el precio de arriendo por día");
        precioArriendoDia = scan.nextLong();

        ControladorArriendoEquipos.getInstance().creaEquipo(codigo, descripcion, precioArriendoDia);
    }

    public void listaClientes() {
        String[][] listadoClientes = ControladorArriendoEquipos.getInstance().listaClientes();
        if(listadoClientes.length > 0){
            System.out.println("LISTADO DE CLIENTES");
            System.out.println("-------------------\n");
            System.out.printf("%-15s%-20s%-20s%-16s%-8s%n", "Rut", "Nombre", "Direccion",  "Telefono", "Estado");
            for(String[] columna : listadoClientes){
                System.out.printf("%-15s%-20s%-20s%-16s%-8s%n", columna[0], columna[1], columna[2], columna[3], columna[4]);
            }

        }else{
            System.out.println("No se han registrado clientes");
        }


    }

    public void listaEquipos() {
        String[][] datosEquipos = ControladorArriendoEquipos.getInstance().listaEquipos();
        if(datosEquipos.length > 0) {
            System.out.println("LISTADO DE EQUIPOS");
            System.out.println("------------------");
            System.out.printf("%-15s%-55s%-20s%-15s%n", "Código", "Descripción", "Precio", "Estado");
            for(String[] columna : datosEquipos) {
                System.out.printf("%-15s%-50s%-20s%-15s%n", columna[0], columna[1], columna[2], columna[3]);
            }
        }else {
            System.out.println("No se han registrado equipos");
        }
    }
}
