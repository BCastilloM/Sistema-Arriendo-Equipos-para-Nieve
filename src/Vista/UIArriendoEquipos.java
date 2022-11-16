/*
 *  Autores:
 *  Benjamín Antonio Castillo Molina
 *  Felipe Alonso Abello Varela
 */

package Vista;

import Controlador.ControladorArriendoEquipos;
import Modelo.EstadoArriendo;

import java.security.spec.RSAOtherPrimeInfo;
import java.sql.SQLOutput;
import java.util.Scanner;
public class UIArriendoEquipos {
    // Atributos
    private static UIArriendoEquipos instance=null;
    private final Scanner scan;

    //Constructor
    private UIArriendoEquipos() {
        scan = new Scanner(System.in);
        scan.useDelimiter("[\t|\r\n]+");
    }

    // Métodos

    // Crea objeto de tipo UIArriendoEquipos
    public static UIArriendoEquipos getInstance() {
        if(instance == null) {
            instance = new UIArriendoEquipos();
        }
        return instance;
    }

    public void menu() {
        int opcion;

        do{
            System.out.println("\n\n\n******* SISTEMA DE ARRIENDO DE EQUIPOS DE NIEVE *******");
            System.out.println("*** MENÚ DE OPCIONES ***");
            System.out.println("1. Crea un nuevo cliente");
            System.out.println("2. Crea un nuevo equipo");
            System.out.println("3. Arrienda equipos");
            System.out.println("4. Devuelve equipos");
            System.out.println("5. Cambia estado de un cliente");
            System.out.println("6. Lista todos los clientes");
            System.out.println("7. Lista todos los equipos");
            System.out.println("8. Lista todos los arriendos");
            System.out.println("9. Lista detalles de un arriendo");
            System.out.println("10. Salir");
            System.out.print("\tIngrese opción: ");
                opcion = scan.nextInt();

            switch (opcion) {
                case 1 -> creaCliente();
                case 2 -> creaEquipo();
                case 3 -> arriendaEquipos();
                case 4 -> devuelveEquipos();
                case 5 -> cambiaEstadoCliente();
                case 6 -> listaClientes();
                case 7 -> listaEquipos();
                case 8 -> listaArriendos();
                case 9 -> listaDetallesArriendo();
                case 10 -> {}
                default -> System.out.println("Error! El valor no está dentro del rango válido");
            }
        }while(opcion != 10);
    }

    private void creaCliente() {
        String rut, nombre, direccion, telefono;

        System.out.println("Creando un nuevo cliente...");

        System.out.print("\nRut: ");
        rut = scan.next();

        System.out.print("\nNombre: ");
        nombre = scan.next();

        System.out.print("\nDomicilio: ");
        direccion = scan.next();

        System.out.print("\nTeléfono: ");
        telefono = scan.next();

        ControladorArriendoEquipos.getInstance().creaCliente(rut, nombre, direccion, telefono);
    }

    private void creaEquipo() {
        long codigo, precioArriendoDia;
        String descripcion;

        System.out.println("Creando un nuevo equipo...");

        System.out.print("\nCódigo: ");
        codigo = scan.nextLong();

        System.out.print("\nDescripción: ");
        descripcion = scan.next();

        System.out.print("\nPrecio de arriendo por día: ");
        precioArriendoDia = scan.nextLong();

        ControladorArriendoEquipos.getInstance().creaEquipo(codigo, descripcion, precioArriendoDia);
    }

    private void arriendaEquipos() {
        //FALTA VALIDAR RUT
        String pregunta = new String();
        String rutCliente = new String();
        String nombreCliente = new String();
        String codigoEquipo = new String();
        String[][] datosEquipos = ControladorArriendoEquipos.getInstance().listaEquipos();
        int numeroEquipo = 0;
        int precioTotal = 0;
        System.out.println("Arrendando equipos...");
        do {
            System.out.print("\nRut cliente: ");
            rutCliente = scan.next();
            System.out.print("\nNombre cliente: ");
            nombreCliente = scan.next();
            System.out.print("\nCódigo equipo: ");
            codigoEquipo = scan.next();
            int i = 0;
            for(String[] columna : datosEquipos){
                if(codigoEquipo.equals(columna[0])){
                    numeroEquipo = i;
                }else{
                    i++;
                }
            }
            if(datosEquipos[numeroEquipo][3].equals(EstadoArriendo.ENTREGADO)){
                System.out.print("\nEl equipo se encuentra arrendado");
            }else{
                //Estado del arriendo donde está disponible
                //ControladorArriendoEquipos.getInstance().agregaEquipoToArriendo(datosArriendo, datosEquipos[numeroEquipo][0]);
                //Revisar
                System.out.print("\nSe ha agregado " + datosEquipos[numeroEquipo][1] + " al arriendo");
                precioTotal+=Integer.parseInt(datosEquipos[numeroEquipo][2]);
            }
            System.out.print("\n¿Desea agregar otro equipo al arriendo? (s/n)");
            pregunta = scan.next();
        }while(pregunta=="s");
        System.out.print("\nMonto total por día de arriendo: --> $" + precioTotal);
    }

    private void devuelveEquipos() {
        String rutCliente;
        int arriendoADevolver;
        int estadoEquipo;
        System.out.println("Devolviendo equipos arrendados...");
        System.out.print("Rut cliente: ");
        rutCliente = scan.next();
        System.out.print("\nNombre cliente: ");
        System.out.println("");
        System.out.println("Los arriendos por devolver son =>>");
        System.out.printf("%-15s%-15s%-15s%-15s%-15s%15s%n", "Codigo", "Fecha Inicio", "Fecha Devol.",  "Estado", "Rut Cliente", "Monto total");
        System.out.printf("%-15s%-15s%-15s%-15s%-15s%15s%n", "", "", "",  "", rutCliente, "");
        System.out.print("\n\nCodigo arriendo a devolver: ");
        arriendoADevolver = scan.nextInt();
        System.out.println("");
        System.out.print("Ingrese código y estado en que se devuelve cada equipo que se indica >>>");
        //Ocupar un ciclo iterativo basado en la cantidad de equipos que ha arrendado el cliente
        System.out.print("\n(" + ") -> Estado (1: Operativo, 2: En Reparacion, 3: Dado de baja): ");
        estadoEquipo = scan.nextInt();
        System.out.print("\n\n" + "equipo(s) fue(ron) devuelto(s) exitosamente");
    }

    private void cambiaEstadoCliente() {
        String rutCliente;
        System.out.println("Cambiando el estado a un cliente...");
        System.out.print("Rut cliente: ");
        rutCliente = scan.next();
        //ControladorArriendoEquipos.getInstance().cambiaEstadoCliente(rutCliente);
        System.out.print("\nSe ha cambiado exitosamente el estado del cliente ");
    }

    private void listaClientes() {
        String[][] listadoClientes = ControladorArriendoEquipos.getInstance().listaClientes();
        if(listadoClientes.length > 0){
            System.out.println("LISTADO DE CLIENTES");
            System.out.println("-------------------\n");
            System.out.printf("%-15s%-20s%-20s%-16s%-8s%-14s%n", "Rut", "Nombre", "Direccion",  "Telefono", "Estado", "Nro.Arr.Pdtes.");
            for(String[] columna : listadoClientes){
                System.out.printf("%-15s%-20s%-20s%-16s%-8s%14s%n", columna[0], columna[1], columna[2], columna[3], columna[4], columna[5]);
            }

        }else{
            System.out.println("No se han registrado clientes");
        }
    }

    private void listaEquipos() {
        String[][] datosEquipos = ControladorArriendoEquipos.getInstance().listaEquipos();
        if(datosEquipos.length > 0) {
            System.out.println("LISTADO DE EQUIPOS");
            System.out.println("------------------");
            System.out.printf("%-15s%-50s%-20s%-15s%-15s%n", "Código", "Descripción", "Precio", "Estado", "Situacion");
            for(String[] columna : datosEquipos) {
                System.out.printf("%-15s%-50s%,-20d%-15s%-15s%n", columna[0], columna[1], Long.parseLong(columna[2]), columna[3], columna[4]);
            }
        }else {
            System.out.println("No se han registrado equipos");
        }
    }

    private void listaArriendos() {
        System.out.print("\nFecha inicio periodo (dd/mm/aaaa): ");

        System.out.print("\nFecha fin periodo (dd/mm/aaaa): ");

        System.out.println("\n\n\n\nLISTADO DE ARRIENDOS");
        System.out.println("--------------------\n");
        System.out.printf("%-8s%-15s%-15s%-12s%-15s%-12s%n", "Codigo", "Fecha inicio", "Fecha devol.", "Estado", "Rut cliente", "Monto total");
        System.out.printf("%-8s%-15s%-15s%-12s%-15s%12s%n", "", "", "", "", "", "");
    }

    private void listaDetallesArriendo() {
        int codigoArriendo;
        System.out.print("Codigo arriendo: ");
        codigoArriendo = scan.nextInt();
        System.out.print("\n----------------------------------------------------------------");
        System.out.print("\nCodigo: " + codigoArriendo);
        System.out.print("\nFecha Inicio: ");
        System.out.print("\nFecha Devolucion: ");
        System.out.print("\nEstado: ");
        System.out.print("\nRut cliente: ");
        System.out.print("\nNombre cliente: ");
        System.out.print("\nMonto total: $");
        System.out.print("\n----------------------------------------------------------------");
        System.out.print("\n\t\t\t\t\t\tDETALLE DEL ARRIENDO");
        System.out.print("\n----------------------------------------------------------------");
        System.out.printf("%-15s%-25s%-25s%n", "Codigo equipo", "Descripcion equipo", "Precio arriendo por dia");
        System.out.printf("%-15s%-25s%25s%n", "", "", "");
    }
}
