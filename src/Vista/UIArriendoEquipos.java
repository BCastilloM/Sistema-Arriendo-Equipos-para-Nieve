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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class UIArriendoEquipos {
    // Atributos
    private static UIArriendoEquipos instance = null;
    private final Scanner scan;

    //Constructor
    private UIArriendoEquipos() {
        scan = new Scanner(System.in);
        scan.useDelimiter("[\t|\r\n]+");
    }

    // Métodos

    // Crea objeto de tipo UIArriendoEquipos
    public static UIArriendoEquipos getInstance() {
        if (instance == null) {
            instance = new UIArriendoEquipos();
        }
        return instance;
    }

    public void menu() {
        int opcion;

        do {
            System.out.println("\n\n\n******* SISTEMA DE ARRIENDO DE EQUIPOS DE NIEVE *******");
            System.out.println("\n*** MENÚ DE OPCIONES ***");
            System.out.println("1. Crea un nuevo cliente");
            System.out.println("2. Crea un nuevo equipo");
            System.out.println("3. Arrienda equipos");
            System.out.println("4. Devuelve equipos");
            System.out.println("5. Cambia estado de un cliente");
            System.out.println("6. Paga arriendo");
            System.out.println("7. Genera reportes");
            System.out.println("8. Cargar datos desde archivo");
            System.out.println("9. Guardar datos a archivo");
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
                case 6 -> pagaArriendo();
                case 7 -> generaReportes();
                case 8 -> {
                    try {
                        ControladorArriendoEquipos.getInstance().readDatosSistema();
                    } catch (DataFormatException e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                }
                case 9 -> {
                    try {
                        ControladorArriendoEquipos.getInstance().saveDatosSistema();
                    } catch (DataFormatException e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                }
                case 10 -> {}
                default -> System.out.println("Error! El valor no está dentro del rango válido");
            }
        } while (opcion != 10);
    }

    //FALTA VALIDAR (Se deja para el final para hacer las pruebas de forma mas rapida)********************************************************************************************

    private void creaCliente() {
        String rut, nombre, direccion, telefono;
        boolean validador = false;

        System.out.println("\nCreando un nuevo cliente...");

        do {
            System.out.print("\nRut: ");
            rut = scan.next();
            try {
                int intRut = (rut.charAt(0) + rut.charAt(1) + rut.charAt(3) + rut.charAt(4) + rut.charAt(5) + rut.charAt(7) + rut.charAt(8) + rut.charAt(9));
            } catch (NumberFormatException e) {
                System.out.println("No se ingresó una opcion válida");
                return;
            }
            if (rut.length() == 12) {
                String ultDig = String.valueOf(rut.charAt(11));
                for (int i = 0; i <= 9; i++) {
                    if (ultDig.equals(i)) {
                        validador = true;
                    }
                }
                if (validador == false) {
                    if ((ultDig).toLowerCase().equals("k")) {
                        validador = true;
                    }
                }
                if (rut.charAt(2) != ('.') && rut.charAt(6) != ('.') && rut.charAt(10) != ('-') && validador != true) {
                    System.out.println("\nValor ingresado no valido como rut");
                }
            } else {
                System.out.println("\nValor ingresado no valido como rut, tiene que ser de la forma 11.111.111-1");
                rut = "aaaaaaaaaaa";
            }

        } while (rut.charAt(2) != ('.') && rut.charAt(6) != ('.') && rut.charAt(10) != ('-') && validador != true);

        System.out.print("\nNombre: ");
        nombre = scan.next();

        System.out.print("\nDomicilio: ");
        direccion = scan.next();

        do {
            System.out.print("\nTeléfono: ");
            telefono = scan.next();
            if (telefono.charAt(0) != ('+')) {
                System.out.println("\nEse no es un telefono, tiene que ser de la forma +111...");
            }
        } while (telefono.charAt(0) != ('+'));

        try {
            ControladorArriendoEquipos.getInstance().creaCliente(rut, nombre, direccion, telefono);
        } catch (ClienteException e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    private void creaEquipo() {
        long codigo, precioArriendoDia;
        String descripcion;
        int tipoEquipo;

        System.out.println("\nCreando un nuevo equipo...");

        System.out.print("\nCódigo: ");
        try {
            String codStr = scan.next();
            codigo = Integer.parseInt(codStr);
        } catch (NumberFormatException e) {
            System.out.println("No se ingresó una opcion válida");
            return;
        }

        System.out.print("\nDescripción: ");
        descripcion = scan.next();

        do {
            System.out.print("\nTipo equipo(1: Implemento, 2: Conjunto): ");
            try {
                String tipoEquipoStr = scan.next();
                tipoEquipo = Integer.parseInt(tipoEquipoStr);
            } catch (NumberFormatException e) {
                System.out.println("No se ingresó una opcion válida");
                return;
            }
            if (tipoEquipo < 1 | tipoEquipo > 2) {
                System.out.println("Valor fuera de rango");
            }
        } while (tipoEquipo < 1 | tipoEquipo > 2);

        if (tipoEquipo == 1) {
            System.out.print("\nPrecio de arriendo por día: ");
            try {
                String PADStr = scan.next();
                precioArriendoDia = Integer.parseInt(PADStr);
            } catch (NumberFormatException e) {
                System.out.println("No se ingresó una opcion válida");
                return;
            }

            try {
                ControladorArriendoEquipos.getInstance().creaImplemento(codigo, descripcion, precioArriendoDia);
            } catch (EquipoException e) {
                System.out.println(e.getMessage());
                return;
            }

            System.out.print("\n\nSe ha creado exitosamente un nuevo implemento");
        } else {
            if (ControladorArriendoEquipos.getInstance().listaEquipos().length == 0) {
                System.out.println("\nNo existen equipos para agregar a un conjunto");
            } else {
                System.out.print("\nNumero de equipos componentes: ");
                int repeticion, nroEquipos = scan.nextInt();
                long[] codigosEquipos = new long[nroEquipos];
                long codigoDeImplemento;

                if (nroEquipos > ControladorArriendoEquipos.getInstance().listaEquipos().length) {
                    System.out.println("\nEl numero de equipos ingresado supera la cantidad de equipos disponible");
                } else {
                    if (nroEquipos > 1) {
                        for (int i = 0; i < nroEquipos; i++) {
                            do {
                                repeticion = 0;
                                System.out.print("\nCodigo equipo " + (i + 1) + " de " + nroEquipos + ": ");
                                try {
                                    String codStr = scan.next();
                                    codigoDeImplemento = Integer.parseInt(codStr);
                                } catch (NumberFormatException e) {
                                    System.out.println("No se ingresó una opcion válida");
                                    return;
                                }
                                if (ControladorArriendoEquipos.getInstance().consultaEquipo(codigoDeImplemento).length > 0) {
                                    codigosEquipos[i] = codigoDeImplemento;
                                } else {
                                    System.out.println("El equipo no existe");
                                }
                                for (int cont = 0; cont < codigosEquipos.length; cont++) {
                                    if (codigoDeImplemento == codigosEquipos[cont]) {
                                        repeticion++;
                                    }
                                }
                                if (repeticion > 1) {
                                    System.out.println("\nNo se pueden repetir codigos de equipos");
                                }
                            } while (ControladorArriendoEquipos.getInstance().consultaEquipo(codigoDeImplemento).length == 0 | repeticion > 1);
                        }

                        try {
                            ControladorArriendoEquipos.getInstance().creaConjunto(codigo, descripcion, codigosEquipos);
                        } catch (EquipoException e) {
                            System.out.println(e.getMessage());
                            return;
                        }

                        System.out.print("\nSe ha creado exitosamente un nuevo conjunto");
                    } else {
                        System.out.println("\nNumero de equipos componentes no valido o insuficiente");
                    }
                }
            }
        }

    }


    private void arriendaEquipos() {
        if (ControladorArriendoEquipos.getInstance().listaEquipos().length == 0) {
            System.out.println("\nNo existen equipos");
        } else {
            String pregunta, rutCliente;
            long codigoEquipo, codigoArriendo, precioTotal;

            System.out.println("\nArrendando equipos...");
            System.out.print("\nRut cliente: ");
            rutCliente = scan.next();
            String[] cliente = ControladorArriendoEquipos.getInstance().consultaCliente(rutCliente);

            try {
                codigoArriendo = ControladorArriendoEquipos.getInstance().creaArriendo(rutCliente);
            } catch (ClienteException e) {
                System.out.println(e.getMessage());
                return;
            }

            System.out.print("\nNombre cliente: " + cliente[1]);

            do {
                String[] datosEquipo;
                do {
                    System.out.print("\nCódigo equipo: ");
                    try {
                        String codStr = scan.next();
                        codigoEquipo = Integer.parseInt(codStr);
                    } catch (NumberFormatException e) {
                        System.out.println("No se ingresó una opcion válida");
                        return;
                    }
                    datosEquipo = ControladorArriendoEquipos.getInstance().consultaEquipo(codigoEquipo);
                    if (datosEquipo.length == 0) {
                        System.out.println("\nEl equipo con el codigo dado no existe");
                    }
                } while (datosEquipo.length == 0);

                if (datosEquipo[4].equals("Arrendado")) {
                    System.out.print("\nEl equipo se encuentra arrendado");
                } else {
                    try {
                        ControladorArriendoEquipos.getInstance().agregaEquipoToArriendo(codigoArriendo, codigoEquipo);
                    } catch (ArriendoException | EquipoException e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                    System.out.print("\nSe ha agregado " + datosEquipo[1] + " al arriendo");
                }

                if ((ControladorArriendoEquipos.getInstance().listaEquipos().length - 1) == 0) {
                    System.out.print("\nNo hay mas equipos, por lo que no es posible agregar otro equipo al arriendo");
                    pregunta = "n";
                } else {
                    do {
                        System.out.print("\n¿Desea agregar otro equipo al arriendo? (s/n) ");
                        pregunta = scan.next();
                        if (!(pregunta.equals("s") | pregunta.equals("n"))) {
                            System.out.println("\nRespuesta no valida");
                        }
                    } while (!(pregunta.equals("s") | pregunta.equals("n")));
                }
            } while (pregunta.equals("s"));

            try {
                precioTotal = ControladorArriendoEquipos.getInstance().cierraArriendo(codigoArriendo);
            } catch (ArriendoException e) {
                System.out.println(e.getMessage());
                return;
            }

            System.out.print("\nMonto total por día de arriendo: --> $" + precioTotal);
        }

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
            return;
        }

        if (arriendosDelCliente.length == 0) {
            System.out.println("\nEl cliente no tiene arriendos");
        } else {
            System.out.print("\nNombre cliente: " + cliente[1]);
            System.out.println("\n\nLos arriendos por devolver son =>>");
            System.out.printf("%-15s%-15s%-15s%-15s%-15s%15s%n", "Codigo", "Fecha Inicio", "Fecha Devol.", "Estado", "Rut Cliente", "Monto total");
            for (String[] arriendo : arriendosDelCliente) {
                System.out.printf("%-15s%-15s%-15s%-15s%-15s%15s%n", arriendo[0], arriendo[1], arriendo[2], arriendo[3], rutCliente, arriendo[6]);
            }

            System.out.print("\n\nCodigo arriendo a devolver: ");
            try {
                String codStr = scan.next();
                codArriendoADevolver = Integer.parseInt(codStr);
            } catch (NumberFormatException e) {
                System.out.println("No se ingresó una opcion válida");
                return;
            }
            String[][] detalleArriendo = ControladorArriendoEquipos.getInstance().listaDetallesArriendo(codArriendoADevolver);
            EstadoEquipo[] estadoEquipos = new EstadoEquipo[detalleArriendo.length];

            if (detalleArriendo.length == 0) {
                System.out.println("\nEl cliente no tiene un arriendo con el codigo dado");
            } else {
                System.out.print("\n\nIngrese código y estado en que se devuelve cada equipo que se indica >>>");

                for (int i = 0; i < detalleArriendo.length; i++) {
                    int opcion;
                    System.out.print("\n(" + detalleArriendo[i][1] + ") -> Estado (1: Operativo, 2: En Reparacion, 3: Dado de baja): ");
                    try {
                        String opcStr = scan.next();
                        opcion = Integer.parseInt(opcStr);
                    } catch (NumberFormatException e) {
                        System.out.println("No se ingresó una opcion válida");
                        return;
                    }

                    switch (opcion) {
                        case 1 -> estadoEquipos[i] = EstadoEquipo.OPERATIVO;
                        case 2 -> estadoEquipos[i] = EstadoEquipo.EN_REPARACION;
                        case 3 -> estadoEquipos[i] = EstadoEquipo.DADO_DE_BAJA;
                        default -> { System.out.println("\nValor no valido");
                            return; }
                    }
                }

                try {
                    ControladorArriendoEquipos.getInstance().devuelveEquipos(codArriendoADevolver, estadoEquipos);
                    System.out.print("\n\n" + detalleArriendo.length + " equipo(s) fue(ron) devuelto(s) exitosamente");
                } catch (ArriendoException e) {
                    System.out.println(e.getMessage());
                    return;
                }
            }
        }
    }

    private void pagaArriendo() {
        long codArriendo, monto;
        int medioDePago;
        System.out.println("\nPagando arriendo...");
        System.out.print("\nCodigo arriendo a pagar: ");
        try {
            String codStr = scan.next();
            codArriendo = Integer.parseInt(codStr);
        } catch (NumberFormatException e) {
            System.out.println("No se ingresó una opcion válida");
            return;
        }
        String[] arriendo = ControladorArriendoEquipos.getInstance().consultaArriendoAPagar(codArriendo);

        if (arriendo.length == 0) {
            System.out.println("\nNo existe un arriendo con el codigo dado");
        } else {

            System.out.print("\n\n---- ANTECEDENTES DEL ARRIENDO ----");
            System.out.print("\nCodigo: " + arriendo[0]);
            System.out.print("\nEstado: " + arriendo[1]);
            System.out.print("\nRut cliente: " + arriendo[2]);
            System.out.print("\nNombre cliente: " + arriendo[3]);
            System.out.print("\nMonto total: " + arriendo[4]);
            System.out.print("\nMonto pagado: " + arriendo[5]);
            System.out.print("\nSaldo adeudado: " + arriendo[6]);

            System.out.println("\n\n---- ANTECEDENTES DEL PAGO ----");
            System.out.print("Medio de pago (1: Contado, 2: Debito, 3: Credito): ");

            try {
                String opcionStr = scan.next();
                medioDePago = Integer.parseInt(opcionStr);
            } catch (NumberFormatException e) {
                System.out.println("No se ingresó una opcion válida");
                return;
            }

            switch (medioDePago) {
                case 1 -> {
                    System.out.print("\nMonto: ");
                    try {
                        String montoStr = scan.next();
                        monto = Integer.parseInt(montoStr);
                    } catch (NumberFormatException e) {
                        System.out.println("No se ingresó una opcion válida");
                        return;
                    }
                    try {
                        ControladorArriendoEquipos.getInstance().pagaArriendoContado(codArriendo, monto);
                    } catch (ArriendoException e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                    System.out.println("\nPago realizado exitosamente");
                }
                case 2 -> {
                    System.out.print("\nMonto: ");
                    try {
                        String montoStr = scan.next();
                        monto = Integer.parseInt(montoStr);
                    } catch (NumberFormatException e) {
                        System.out.println("No se ingresó una opcion válida");
                        return;
                    }
                    String codTran, numTar;

                    System.out.print("\nCodigo transaccion: ");
                    try {
                        codTran = scan.next();
                        int intCodTran = Integer.parseInt(codTran);
                    } catch (NumberFormatException e) {
                        System.out.println("No se ingresó un numero");
                        return;
                    }

                    System.out.print("\nNumero tarjeta debito: ");
                    try {
                        numTar = scan.next();
                        int intnumTar = Integer.parseInt(numTar);
                    } catch (NumberFormatException e) {
                        System.out.println("No se ingresó un numero");
                        return;
                    }

                    try {
                        ControladorArriendoEquipos.getInstance().pagaArriendoDebito(codArriendo, monto, codTran, numTar);
                    } catch (ArriendoException e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                    System.out.println("\nPago realizado exitosamente");
                }
                case 3 -> {
                    System.out.print("\nMonto: ");
                    try {
                        String montoStr = scan.next();
                        monto = Integer.parseInt(montoStr);
                    } catch (NumberFormatException e) {
                        System.out.println("No se ingresó una opcion válida");
                        return;
                    }
                    String codTran, numTar;
                    int nroCuotas;

                    System.out.print("\nCodigo transaccion: ");
                    try {
                        codTran = scan.next();
                        int intCodTran = Integer.parseInt(codTran);
                    } catch (NumberFormatException e) {
                        System.out.println("No se ingresó un numero");
                        return;
                    }

                    System.out.print("\nNumero tarjeta credito: ");
                    try {
                        numTar = scan.next();
                        int intnumTar = Integer.parseInt(numTar);
                    } catch (NumberFormatException e) {
                        System.out.println("No se ingresó un numero");
                        return;
                    }

                    System.out.print("\nNumero cuotas: ");
                    try {
                        String nCuotasStr = scan.next();
                        nroCuotas = Integer.parseInt(nCuotasStr);
                    } catch (NumberFormatException e) {
                        System.out.println("No se ingresó una opcion válida");
                        return;
                    }
                    if (nroCuotas <= 0) {
                        System.out.println("No se ingreso una opcion valida");
                        return;
                    }

                    try {
                        ControladorArriendoEquipos.getInstance().pagaArriendoCredito(codArriendo, monto, codTran, numTar, nroCuotas);
                    } catch (ArriendoException e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                    System.out.println("\nPago realizado exitosamente");
                }
                default -> System.out.print("\nNumero fuera de rango");
            }
        }

    }


    private void cambiaEstadoCliente() {
        String rutCliente;

        System.out.println("\nCambiando el estado a un cliente...");
        System.out.print("Rut cliente: ");
        rutCliente = scan.next();

        try {
            ControladorArriendoEquipos.getInstance().cambiaEstadoCliente(rutCliente);
        } catch (ClienteException e) {
            System.out.println(e.getMessage());
            return;
        }

        String[] datosCliente = ControladorArriendoEquipos.getInstance().consultaCliente(rutCliente);
        System.out.print("\nSe ha cambiado exitosamente el estado del cliente \"" + datosCliente[1] + "\" a \"" + datosCliente[4] + "\"");
    }

    private void generaReportes() {
        int opcion;
        System.out.println("\n*** MENU DE REPORTES ***");
        System.out.println("1. Lista todos los clientes");
        System.out.println("2. Lista todos los equipos");
        System.out.println("3. Lista todos los arriendos");
        System.out.println("4. Lista detalles de un arriendo");
        System.out.println("5. Lista arriendos con pagos");
        System.out.println("6. Lista los pagos de un arriendo");
        System.out.println("7. Salir");
        System.out.print("\tIngrese opción: ");
        try {
            String opcionStr = scan.next();
            opcion = Integer.parseInt(opcionStr);
        } catch (NumberFormatException e) {
            System.out.println("No se ingresó una opcion válida");
            return;
        }
        switch (opcion) {
            case 1 -> listaClientes();
            case 2 -> listaEquipos();
            case 3 -> listaArriendos();
            case 4 -> listaDetallesArriendo();
            case 5 -> listaArriendosPagados();
            case 6 -> listaPagosDeUnArriendo();
            case 7 -> {}
            default -> System.out.println("Error! El valor no está dentro del rango válido");
        }
    }

    private void listaClientes() {
        String[][] listadoClientes = ControladorArriendoEquipos.getInstance().listaClientes();

        if (listadoClientes.length > 0) {
            System.out.println("LISTADO DE CLIENTES");
            System.out.println("-------------------\n");
            System.out.printf("%-15s%-20s%-20s%-16s%-8s%-14s%n", "Rut", "Nombre", "Direccion", "Telefono", "Estado", "Nro.Arr.Pdtes.");
            for (String[] columna : listadoClientes) {
                System.out.printf("%-15s%-20s%-20s%-16s%-8s%14s%n", columna[0], columna[1], columna[2], columna[3], columna[4], columna[5]);
            }
        } else {
            System.out.println("No se han registrado clientes");
        }
    }

    private void listaEquipos() {
        String[][] datosEquipos = ControladorArriendoEquipos.getInstance().listaEquipos();

        if (datosEquipos.length > 0) {
            System.out.println("LISTADO DE EQUIPOS");
            System.out.println("------------------");
            System.out.printf("%-15s%-50s%-20s%-15s%-15s%n", "Código", "Descripción", "Precio", "Estado", "Situacion");
            for (String[] columna : datosEquipos) {
                System.out.printf("%-15s%-50s%,-20d%-15s%-15s%n", columna[0], columna[1], Long.parseLong(columna[2]), columna[3], columna[4]);
            }
        } else {
            System.out.println("No se han registrado equipos");
        }
    }

    private void listaArriendos() {
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate ldFechaInicio = null, ldFechaFin = null;

        System.out.print("\nFecha inicio periodo (dd/mm/aaaa): ");
        String fechaInicioPer = scan.next();

        try {
            ldFechaInicio = LocalDate.parse(fechaInicioPer, tf);
        } catch (Exception e) {
            System.out.println("Formato de fecha invalido");
            return;
        }

        System.out.print("\nFecha fin periodo (dd/mm/aaaa): ");
        String fechaFinPer = scan.next();

        try {
            ldFechaFin = LocalDate.parse(fechaFinPer, tf);
        } catch (Exception e) {
            System.out.println("Formato de fecha invalido");
            return;
        }

        String[][] datosArriendos = ControladorArriendoEquipos.getInstance().listaArriendos(ldFechaInicio, ldFechaFin);

        if (datosArriendos.length > 0) {
            System.out.println("\n\n\n\nLISTADO DE ARRIENDOS");
            System.out.println("--------------------\n");
            System.out.printf("%-8s%-15s%-15s%-12s%-15s%-12s%n", "Codigo", "Fecha inicio", "Fecha devol.", "Estado", "Rut cliente", "Monto total");

            for (String[] datosArriendo : datosArriendos) {
                System.out.printf("%-8s%-15s%-15s%-12s%-15s%10s%n", datosArriendo[0], datosArriendo[1], datosArriendo[2], datosArriendo[3], datosArriendo[4], datosArriendo[6]);
            }
        } else {
            System.out.println("No se han registrado Arriendos");
        }

    }

    private void listaDetallesArriendo() {
        int codigoArriendo;

        System.out.print("\nCodigo arriendo: ");
        try {
            String codStr = scan.next();
            codigoArriendo = Integer.parseInt(codStr);
        } catch (NumberFormatException e) {
            System.out.println("No se ingresó una opcion válida");
            return;
        }
        String[] Arriendo = ControladorArriendoEquipos.getInstance().consultaArriendo(codigoArriendo);

        if (Arriendo.length > 0) {
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
            for (int i = 0; i < detalleArriendo.length; i++) {
                System.out.printf("%-15s%-23s%25s%n", detalleArriendo[i][0], detalleArriendo[i][1], detalleArriendo[i][2]);
            }
        } else {
            System.out.println("El arriendo con el codigo dado no existe");
        }
    }

    private void listaArriendosPagados() {
        String[][] listaArrPag = ControladorArriendoEquipos.getInstance().listaArriendosPagados();
        if (listaArrPag.length > 0) {
            System.out.println("\nLISTADO DE ARRIENDOS PAGADOS");
            System.out.println("----------------------------\n");
            System.out.printf("%-12s%-10s%-15s%-20s%-12s%-14s%-16s%n", "Codigo", "Estado", "Rut cliente", "Nombre cliente", "Monto deuda", "Monto pagado", "Saldo adeudado");
            for (int i = 0; i < listaArrPag.length; i++) {
                System.out.printf("%-12s%-10s%-15s%-20s%11s%13s%16s%n", listaArrPag[i][0], listaArrPag[i][1], listaArrPag[i][2], listaArrPag[i][3], listaArrPag[i][4], listaArrPag[i][5], listaArrPag[i][6]);
            }
        } else {
            System.out.println("\nNo hay arriendos pagados");
        }

    }

    private void listaPagosDeUnArriendo() {
        long codArr;

        System.out.print("\nCodigo arriendo: ");
        try {
            String codStr = scan.next();
            codArr = Integer.parseInt(codStr);
        } catch (NumberFormatException e) {
            System.out.println("No se ingresó una opcion válida");
            return;
        }
        String[][] listaPagDeArr = ControladorArriendoEquipos.getInstance().listaPagosDeArriendo(codArr);
        if (listaPagDeArr.length == 0) {
            System.out.println("\n\nEl arriendo no tiene pagos asociados");
        } else {
            System.out.println("\n\n>>>>>>>>>>>\tPAGOS REALIZADOS\t<<<<<<<<<<<\n");
            System.out.printf("%-7s%-12s%-12s%n", "Monto", "Fecha", "Tipo pago");

            for (int i = 0; i < listaPagDeArr.length; i++) {
                System.out.printf("%-7s%-12s%-12s%n", listaPagDeArr[i][0], listaPagDeArr[i][1], listaPagDeArr[i][2]);
            }
        }
    }
}
