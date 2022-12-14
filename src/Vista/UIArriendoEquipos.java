/*
 *  Autores:
 *  Benjamín Antonio Castillo Molina
 *  Felipe Alonso Abello Varela
 */

package Vista;

import Controlador.ControladorArriendoEquipos;
import Excepciones.ArriendoException;
import Excepciones.ClienteException;
import Excepciones.EquipoException;
import Modelo.EstadoEquipo;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
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
            try {
                String opcionStr = scan.next();
                opcion = Integer.parseInt(opcionStr);
            } catch (NumberFormatException e) {
                System.out.println("No se ingresó una opcion válida");
                return;
            }

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

        System.out.println("\nCreando un nuevo cliente...");

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

        System.out.println("\nCreando un nuevo equipo...");

        System.out.print("\nCódigo: ");
        codigo = scan.nextLong();

        System.out.print("\nDescripción: ");
        descripcion = scan.next();

        System.out.print("\nPrecio de arriendo por día: ");
        precioArriendoDia = scan.nextLong();

        ControladorArriendoEquipos.getInstance().creaEquipo(codigo, descripcion, precioArriendoDia);
    }

    private void arriendaEquipos() {
        String pregunta, rutCliente;
        long codigoEquipo, codigoArriendo, precioTotal;

        System.out.println("\nArrendando equipos...");
        System.out.print("\nRut cliente: ");
        rutCliente = scan.next();
        String[] cliente = ControladorArriendoEquipos.getInstance().consultaCliente(rutCliente);

        try{
            codigoArriendo = ControladorArriendoEquipos.getInstance().creaArriendo(rutCliente);
        }catch(ClienteException e){
            System.out.println(e.getMessage());
            return;
        }

        System.out.print("\nNombre cliente: " + cliente[1]);

        do {
            System.out.print("\nCódigo equipo: ");
            codigoEquipo = scan.nextLong();
            String[] datosEquipo = ControladorArriendoEquipos.getInstance().consultaEquipo(codigoEquipo);

            if(datosEquipo[3].equals("entregado")){
                System.out.print("\nEl equipo se encuentra arrendado");
            }else{
                try{
                    ControladorArriendoEquipos.getInstance().agregaEquipoToArriendo(codigoArriendo, codigoEquipo);
                }catch(ArriendoException | EquipoException e){
                    System.out.println(e.getMessage());
                    return;
                }
                System.out.print("\nSe ha agregado " + datosEquipo[1] + " al arriendo");
            }

            System.out.print("\n¿Desea agregar otro equipo al arriendo? (s/n) ");
            pregunta = scan.next();
        }while(pregunta.equals("s"));

        try{
            precioTotal=ControladorArriendoEquipos.getInstance().cierraArriendo(codigoArriendo);
        }catch(ArriendoException e){
            System.out.println(e.getMessage());
            return;
        }

        System.out.print("\nMonto total por día de arriendo: --> $" + precioTotal);
    }

    private void devuelveEquipos() {
        String rutCliente;
        String[][] arriendosDelCliente;
        long codArriendoADevolver;

        System.out.println("\nDevolviendo equipos arrendados...");
        System.out.print("Rut cliente: ");
        rutCliente = scan.next();
        String[] cliente = ControladorArriendoEquipos.getInstance().consultaCliente(rutCliente);

        try {
            arriendosDelCliente = ControladorArriendoEquipos.getInstance().listaArriendosPorDevolver(rutCliente);
        } catch (ClienteException e) {
            System.out.println(e.getMessage());
            return ;
        }

        System.out.print("\nNombre cliente: " + cliente[1]);
        System.out.println("\n\nLos arriendos por devolver son =>>");
        System.out.printf("%-15s%-15s%-15s%-15s%-15s%15s%n", "Codigo", "Fecha Inicio", "Fecha Devol.", "Estado", "Rut Cliente", "Monto total");
        for (String[] arriendo : arriendosDelCliente) {
            System.out.printf("%-15s%-15s%-15s%-15s%-15s%15s%n", arriendo[0], arriendo[1], arriendo[2], arriendo[3], rutCliente, arriendo[6]);
        }

        System.out.print("\n\nCodigo arriendo a devolver: ");
        codArriendoADevolver = scan.nextLong();
        String[][] detalleArriendo = ControladorArriendoEquipos.getInstance().listaDetallesArriendo(codArriendoADevolver);
        EstadoEquipo[] estadoEquipos = new EstadoEquipo[detalleArriendo.length];

        System.out.print("\n\nIngrese código y estado en que se devuelve cada equipo que se indica >>>");

        for(int i = 0; i < detalleArriendo.length; i++){
        System.out.print("\n(" + detalleArriendo[i][1] + ") -> Estado (1: Operativo, 2: En Reparacion, 3: Dado de baja): ");
        int opcion = scan.nextInt();

        switch(opcion){
            case 1 -> estadoEquipos[i] = EstadoEquipo.OPERATIVO;
            case 2 -> estadoEquipos[i] = EstadoEquipo.EN_REPARACION;
            case 3 -> estadoEquipos[i] = EstadoEquipo.DADO_DE_BAJA;
            }
        }

        try{
            ControladorArriendoEquipos.getInstance().devuelveEquipos(codArriendoADevolver, estadoEquipos);
            System.out.print("\n\n" + detalleArriendo.length + " equipo(s) fue(ron) devuelto(s) exitosamente");
        }catch (ArriendoException e){
            System.out.println(e.getMessage());
            return;
        }
    }

    private void cambiaEstadoCliente() {
        String rutCliente;

        System.out.println("\nCambiando el estado a un cliente...");
        System.out.print("Rut cliente: ");
        rutCliente = scan.next();

        try{
            ControladorArriendoEquipos.getInstance().cambiaEstadoCliente(rutCliente);
        }catch(ClienteException e){
            System.out.println(e.getMessage());
            return;
        }

        String[] datosCliente = ControladorArriendoEquipos.getInstance().consultaCliente(rutCliente);
        System.out.print("\nSe ha cambiado exitosamente el estado del cliente \"" + datosCliente[1] + "\" a \"" + datosCliente[4] + "\"");
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
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate ldFechaInicio = null, ldFechaFin = null;

        System.out.print("\nFecha inicio periodo (dd/mm/aaaa): ");
        String fechaInicioPer = scan.next();

        try{
            ldFechaInicio = LocalDate.parse(fechaInicioPer, tf);
        } catch (Exception e) {
            System.out.println("Formato de fecha invalido");
        }

        System.out.print("\nFecha fin periodo (dd/mm/aaaa): ");
        String fechaFinPer = scan.next();

        try{
            ldFechaFin = LocalDate.parse(fechaFinPer, tf);
        } catch (Exception e) {
            System.out.println("Formato de fecha invalido");
        }

        String[][] datosArriendos = ControladorArriendoEquipos.getInstance().listaArriendos();

        System.out.println("\n\n\n\nLISTADO DE ARRIENDOS");
        System.out.println("--------------------\n");
        System.out.printf("%-8s%-15s%-15s%-12s%-15s%-12s%n", "Codigo", "Fecha inicio", "Fecha devol.", "Estado", "Rut cliente", "Monto total");

        for (String[] datosArriendo : datosArriendos) {
            if (LocalDate.parse(datosArriendo[1], tf).isAfter(ldFechaInicio) && LocalDate.parse(datosArriendo[1], tf).isBefore(ldFechaFin)) {
                System.out.printf("%-8s%-15s%-15s%-12s%-15s%10s%n", datosArriendo[0], datosArriendo[1], datosArriendo[2], datosArriendo[3], datosArriendo[4], datosArriendo[6]);
            }
        }
    }

    private void listaDetallesArriendo() {
        int codigoArriendo;

        System.out.print("\nCodigo arriendo: ");
        codigoArriendo = scan.nextInt();
        String[] Arriendo = ControladorArriendoEquipos.getInstance().consultaArriendo(codigoArriendo);

        System.out.print("\n----------------------------------------------------------------");
        System.out.print("\nCodigo: " + codigoArriendo);
        System.out.print("\nFecha Inicio: " + Arriendo[1]);
        System.out.print("\nFecha Devolucion: " + Arriendo[2]);
        System.out.print("\nEstado: " + Arriendo[3]);
        System.out.print("\nRut cliente: " + Arriendo[4]);
        System.out.print("\nNombre cliente: " + Arriendo[5]);
        System.out.print("\nMonto total: $" + Arriendo[6]);
        System.out.print("\n----------------------------------------------------------------");

        String[][] detalleArriendo = ControladorArriendoEquipos.getInstance().listaDetallesArriendo(codigoArriendo);

        System.out.print("\n\t\t\t\t\t\tDETALLE DEL ARRIENDO");
        System.out.println("\n----------------------------------------------------------------");
        System.out.printf("%-15s%-25s%-25s%n", "Codigo equipo", "Descripcion equipo", "Precio arriendo por dia");
        for(int i = 0; i<detalleArriendo.length; i++){
            System.out.printf("%-15s%-23s%25s%n", detalleArriendo[i][0], detalleArriendo[i][1], detalleArriendo[i][2]);
        }
    }
}
