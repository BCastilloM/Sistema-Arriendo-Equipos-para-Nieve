/*
 *  Autores:
 *  Benjamín Antonio Castillo Molina
 *  Felipe Alonso Abello Varela
 */

package Controlador;

import Excepciones.ArriendoException;
import Excepciones.ClienteException;
import Excepciones.EquipoException;
import Modelo.*;


import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

public class ControladorArriendoEquipos {
    // Atributos
    private static ControladorArriendoEquipos instance = null;
    private final ArrayList<Cliente> clientes;
    private final ArrayList<Equipo> equipos;
    private final ArrayList<Arriendo> arriendos;

    // Constructor
    private ControladorArriendoEquipos() {
        clientes = new ArrayList<>();
        equipos = new ArrayList<>();
        arriendos = new ArrayList<>();
    }

    // Métodos

    // Crea objeto de tipo ControladorArriendoEquipos
    public static ControladorArriendoEquipos getInstance() {
        if (instance == null) {
            instance = new ControladorArriendoEquipos();
        }
        return instance;
    }

    public void creaCliente(String rut, String nom, String dir, String tel) throws ClienteException {
        Cliente cliente = buscaCliente(rut);
        if (cliente != null) {
            throw new ClienteException("Ya existe un cliente con el rut dado");
        }else {
            clientes.add(new Cliente(rut, nom, dir, tel));
        }
    }

    public void creaImplemento ( long cod, String desc,long precio) throws EquipoException {
        Equipo equipo = buscaEquipo(cod);
        if (equipo == null) {
            equipos.add(new Implemento(cod, desc, precio));
        } else {
            throw new EquipoException("Ya existe un equipo con el código dado");
        }
    }

    public void creaConjunto(long cod, String desc, long[] codEquipos) throws EquipoException {
        Equipo equipoConjunto = buscaEquipo(cod);
        if (equipoConjunto != null) {
            throw new EquipoException("Ya existe un equipo con el código dado");
        }
        for(int i = 0; i < codEquipos.length; i++){
            if(buscaEquipo(codEquipos[i]) == null){
                throw new EquipoException("Código de un equipo componente es incorrecto");
            }
        }
        Equipo conjunto = new Conjunto(cod, desc);
        for(int i = 0; i < codEquipos.length; i++){
            conjunto.addEquipo(buscaEquipo(codEquipos[i]));
        }
        equipos.add(conjunto);
    }

    public long creaArriendo(String rut) throws ClienteException {
        long i = 0;
        Arriendo arriendo = null;
        Cliente cliente = buscaCliente(rut);
        if (cliente == null) {
            throw new ClienteException("No existe un cliente con el rut dado");
        } else if (!cliente.isActivo()) {
            throw new ClienteException("El cliente está inactivo");
        } else {
            i = arriendos.size() + 1;
            arriendo = new Arriendo(i, LocalDate.now(), cliente);
            arriendos.add(arriendo);
            cliente.addArriendo(arriendo);
            return i;
        }
    }

    public String agregaEquipoToArriendo(long codArriendo, long codEquipo) throws ArriendoException, EquipoException {
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            throw new ArriendoException("No existe un arriendo con el código dado");
        } else if (arriendo.getEstado() != EstadoArriendo.INICIADO) {
            throw new ArriendoException("El arriendo no tiene el estado INICIADO");
        } else {
            Equipo equipo = buscaEquipo(codEquipo);
            if (equipo == null) {
                throw new EquipoException("No existe un equipo con el código dado");
            } else if (equipo.getEstado() != EstadoEquipo.OPERATIVO) {
                throw new EquipoException("El equipo no está operativo");
            } else {
                Equipo[] equiposArriendo = arriendo.getEquipo();
                int cont = 0;
                for (int i = 0; i < equiposArriendo.length; i++) {
                    if (equiposArriendo[i].getCodigo() == codEquipo) {
                        cont++;
                    }
                }
                if (cont != 0) {
                    throw new EquipoException("El equipo se encuentra arrendado");
                } else {
                    arriendo.addDetalleArriendo(equipo);
                    DetalleArriendo detalleArriendo = new DetalleArriendo(equipo.getPrecioArriendoDia(), equipo, arriendo);
                    equipo.addDetalleArriendo(detalleArriendo);
                    return equipo.getDescripcion();
                }
            }
        }
    }

    public long cierraArriendo(long codArriendo) throws ArriendoException {
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            throw new ArriendoException("No existe un arriendo con el código dado");
        } else if (arriendo.getEquipo() == null) {
            throw new ArriendoException("El arriendo no tiene equipos asociados");
        } else {
            arriendo.setEstado(EstadoArriendo.ENTREGADO);
            return arriendo.getMontoTotal();
        }
    }

    public void devuelveEquipos(long codArriendo, EstadoEquipo[] estadoEquipos) throws ArriendoException {
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            throw new ArriendoException("No existe un arriendo con el código dado");
        } else if (arriendo.getEstado() != EstadoArriendo.ENTREGADO) {
            throw new ArriendoException("El arriendo no tiene devolución pendiente");
        } else {
            ArrayList<Equipo> equipos = new ArrayList<>(List.of(arriendo.getEquipo()));
            for (int i = 0; i < equipos.size(); i++) {
                equipos.get(i).setEstado(estadoEquipos[i]);

            }
            arriendo.setEstado(EstadoArriendo.DEVUELTO);
            arriendo.setFechaDevolucion(LocalDate.now());
        }
    }

    public void pagaArriendoContado(long codArriendo, long monto) throws  ArriendoException{
        Arriendo arriendo = buscaArriendo(codArriendo);
        if(arriendo == null){
            throw new ArriendoException("No existe un arriendo con el código dado");
        } else if(arriendo.getEstado() != EstadoArriendo.DEVUELTO){
            throw new ArriendoException("No se ha devuelto el o los equipos del arriendo");
        } else if(arriendo.getSaldoAdeudado() < monto){
            throw new ArriendoException("Monto supera al saldo adeudado");
        }
        arriendo.addPagoContado(new Contado(monto, LocalDate.now()));
    }

    public void pagaArriendoDebito(long codArriendo, long monto, String codTransaccion, String numTarjeta)throws ArriendoException{
        Arriendo arriendo = buscaArriendo(codArriendo);
        if(arriendo == null){
            throw new ArriendoException("No existe un arriendo con el código dado");
        }else if(arriendo.getEstado() != EstadoArriendo.DEVUELTO){
            throw new ArriendoException("No se ha devuelto el o los equipos del arriendo");
        } else if(arriendo.getSaldoAdeudado() < monto){
            throw new ArriendoException("Monto supera al saldo adeudado");
        }
        arriendo.addPagoDebito(new Debito(monto, LocalDate.now(), codTransaccion, numTarjeta));
    }

    public void pagaArriendoCredito(long codArriendo, long monto, String codTransaccion, String numTarjeta, int nroCuotas) throws ArriendoException{
        Arriendo arriendo = buscaArriendo(codArriendo);
        if(arriendo == null){
            throw new ArriendoException("No existe un arriendo con el código dado");
        } else if(arriendo.getEstado() != EstadoArriendo.DEVUELTO){
            throw new ArriendoException("No se ha devuelto el o los equipos del arriendo");
        } else if(arriendo.getSaldoAdeudado() < monto){
            throw new ArriendoException("Monto supera al saldo adeudado");
        }
        arriendo.addPagoCredito(new Credito(monto, LocalDate.now(), codTransaccion, numTarjeta, nroCuotas));
    }

    public void cambiaEstadoCliente(String rutCliente) throws ClienteException {
        Cliente cliente = buscaCliente(rutCliente);
        if (cliente == null) {
            throw new ClienteException("No existe un cliente con el rut dado");
        } else if (cliente.isActivo()) {
            cliente.setInactivo();
        } else {
            cliente.setActivo();
        }
    }

    public String[] consultaCliente(String rut) {
        Cliente cliente = buscaCliente(rut);
        String[] ClientesArr = new String[6];
        if (cliente == null) {
            return new String[0];
        }
        ClientesArr[0] = cliente.getRut();
        ClientesArr[1] = cliente.getNombre();
        ClientesArr[2] = cliente.getDireccion();
        ClientesArr[3] = cliente.getTelefono();
        if (cliente.isActivo()) {
            ClientesArr[4] = "Activo";
        } else {
            ClientesArr[4] = "Inactivo";
        }
        ClientesArr[5] = String.valueOf(cliente.getArriendosPorDevolver().length);
        return ClientesArr;
    }


    public String[] consultaEquipo(long codigo) {
        Equipo equipo = buscaEquipo(codigo);
        String[] EquiposArr = new String[5];
        if (equipo == null) {
            return new String[0];
        }
        EquiposArr[0] = String.valueOf(equipo.getCodigo());
        EquiposArr[1] = equipo.getDescripcion();
        EquiposArr[2] = String.valueOf(equipo.getPrecioArriendoDia());
        EquiposArr[3] = String.valueOf(equipo.getEstado()).toLowerCase().replace("_", " ");
        if (equipo.isArrendado()) {
            EquiposArr[4] = "Arrendado";
        } else {
            EquiposArr[4] = "Disponible";
        }
        return EquiposArr;
    }

    public String[] consultaArriendo(long codigo) {
        Arriendo arriendo = buscaArriendo(codigo);
        if (arriendo != null) {
            String[] datosArr = new String[7];
            datosArr[0] = String.valueOf(arriendo.getCodigo());
            datosArr[1] = String.valueOf(arriendo.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            if (arriendo.getEstado() == EstadoArriendo.DEVUELTO) {
                datosArr[2] = String.valueOf(arriendo.getFechaDevolucion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            } else {
                datosArr[2] = "No devuelto";
            }
            datosArr[3] = String.valueOf(arriendo.getEstado()).toLowerCase();
            datosArr[4] = arriendo.getCliente().getRut();
            datosArr[5] = arriendo.getCliente().getNombre();
            datosArr[6] = String.valueOf(arriendo.getMontoTotal());
            return datosArr;
        } else {
            return new String[0];
        }
    }

    public String[] consultaArriendoAPagar(long codigo){
        Arriendo arriendo = buscaArriendo(codigo);
        if(arriendo == null){
            return new String[0];
        } else if (arriendo.getEstado() != EstadoArriendo.DEVUELTO) {
            return new String[0];
        }
        String[] MontoAPagar = new String[7];
        MontoAPagar[0] = String.valueOf(arriendo.getCodigo());
        MontoAPagar[1] = String.valueOf(arriendo.getEstado()).toLowerCase();
        MontoAPagar[2] = arriendo.getCliente().getRut();
        MontoAPagar[3] = arriendo.getCliente().getNombre();
        MontoAPagar[4] = String.valueOf(arriendo.getMontoTotal());
        MontoAPagar[5] = String.valueOf(arriendo.getMontoPagado());
        MontoAPagar[6] = String.valueOf(arriendo.getSaldoAdeudado());

        return MontoAPagar;
    }

    public String[][] listaClientes() {
        String[][] ClientesArr = new String[clientes.size()][6];
        int i = 0;
        if (!clientes.isEmpty()) {
            for (Cliente cliente : clientes) {
                ClientesArr[i][0] = cliente.getRut();
                ClientesArr[i][1] = cliente.getNombre();
                ClientesArr[i][2] = cliente.getDireccion();
                ClientesArr[i][3] = cliente.getTelefono();
                if (cliente.isActivo()) {
                    ClientesArr[i][4] = "Activo";
                } else {
                    ClientesArr[i][4] = "Inactivo";
                }
                ClientesArr[i][5] = String.valueOf(cliente.getArriendosPorDevolver().length);
                i++;
            }
            return ClientesArr;
        }
        return new String[0][0];
    }

    public String[][] listaArriendos() {
        if (!(arriendos.isEmpty())) {
            String[][] ArriendosArr = new String[arriendos.size()][7];
            int i = 0;
            for (Arriendo arriendo : arriendos) {
                ArriendosArr[i][0] = String.valueOf(arriendo.getCodigo());
                ArriendosArr[i][1] = String.valueOf(arriendo.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                if (arriendo.getEstado() == EstadoArriendo.DEVUELTO) {
                    ArriendosArr[i][2] = String.valueOf(arriendo.getFechaDevolucion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                } else {
                    ArriendosArr[i][2] = "No devuelto";
                }
                ArriendosArr[i][3] = String.valueOf(arriendo.getEstado()).toLowerCase();
                ArriendosArr[i][4] = arriendo.getCliente().getRut();
                ArriendosArr[i][5] = arriendo.getCliente().getNombre();
                ArriendosArr[i][6] = String.valueOf(arriendo.getMontoTotal());
                i++;
            }
            return ArriendosArr;
        }
        return new String[0][0];
    }

    public String[][] listaArriendosPorDevolver(String rut) throws ClienteException {
        Cliente cliente = buscaCliente(rut);
        if (cliente == null) {
            throw new ClienteException("No existe un cliente con el rut dado");
        } else {
            ArrayList<Arriendo> arriendosCliente = new ArrayList<Arriendo>(List.of(cliente.getArriendosPorDevolver()));
            String[][] arrDevolver = new String[arriendosCliente.size()][7];
            int i = 0;
            for (Arriendo arriendo : arriendosCliente) {
                arrDevolver[i][0] = String.valueOf(arriendo.getCodigo());
                arrDevolver[i][1] = String.valueOf(arriendo.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                arrDevolver[i][2] = "No devuelto";
                arrDevolver[i][3] = String.valueOf(arriendo.getEstado()).toLowerCase();
                arrDevolver[i][4] = arriendo.getCliente().getRut();
                arrDevolver[i][5] = arriendo.getCliente().getNombre();
                arrDevolver[i][6] = String.valueOf(arriendo.getMontoTotal());
            }
            return arrDevolver;
        }
    }

    public String[][] listaDetallesArriendo(long codArriendo) {
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo != null) {
            return arriendo.getDetallesToString();
        }
        return new String[0][0];
    }

    public String[][] listaEquipos() {
        String[][] EquiposArr = new String[equipos.size()][5];
        int i = 0;
        if (!equipos.isEmpty()) {
            for (Equipo equipo : equipos) {
                EquiposArr[i][0] = String.valueOf(equipo.getCodigo());
                EquiposArr[i][1] = equipo.getDescripcion();
                EquiposArr[i][2] = String.valueOf(equipo.getPrecioArriendoDia());
                EquiposArr[i][3] = String.valueOf(equipo.getEstado());

                if (equipo.isArrendado()) {
                    EquiposArr[i][4] = "Arrendado";
                } else {
                    EquiposArr[i][4] = "Disponible";
                }
                i++;
            }
            return EquiposArr;
        }
        return new String[0][0];
    }

    public void readDatosSistema() throws DataFormatException {
        ObjectInputStream archivoCliente = null;
        ObjectInputStream archivoEquipo = null;
        ObjectInputStream archivoArriendo = null;

        try {
            archivoCliente = new ObjectInputStream(new FileInputStream("Cliente.obj"));
            archivoEquipo = new ObjectInputStream(new FileInputStream("Equipo.obj"));
            archivoArriendo = new ObjectInputStream(new FileInputStream("Arriendo.obj"));
            while (true) {
                clientes.add((Cliente) archivoCliente.readObject());
                equipos.add((Equipo) archivoEquipo.readObject());
                arriendos.add((Arriendo) archivoArriendo.readObject());
            }
        } catch (EOFException e) {
            try {
                archivoCliente.close();
                archivoEquipo.close();
                archivoArriendo.close();
            } catch (IOException e2) {
                throw new DataFormatException("Problemas al cerrar uno o más archivos");
            }
        } catch (FileNotFoundException e) {
            throw new DataFormatException("Problemas al abrir uno o más archivos");
        } catch (IOException | ClassNotFoundException e) {
            throw new DataFormatException("Problemas al leer uno o más archivos");
        }
    }

    public void saveDatosSistema() throws DataFormatException {
        ObjectOutputStream archivoCliente = null;
        ObjectOutputStream archivoEquipo = null;
        ObjectOutputStream archivoArriendo = null;

        try {
            archivoCliente = new ObjectOutputStream(new FileOutputStream("Cliente.obj"));
            archivoEquipo = new ObjectOutputStream(new FileOutputStream("Equipo.obj"));
            archivoArriendo = new ObjectOutputStream(new FileOutputStream("Arriendo.obj"));
            for (Cliente cliente : clientes) {
                archivoCliente.writeObject(cliente);
            }
            for (Equipo equipo : equipos) {
                archivoEquipo.writeObject(equipo);
            }
            for (Arriendo arriendo : arriendos) {
                archivoArriendo.writeObject(arriendo);
            }
        } catch (FileNotFoundException e) {
            throw new DataFormatException("Problemas al abrir uno o más archivos");
        } catch (IOException e) {
            throw new DataFormatException("Problemas al grabar uno o más archivos");
        } finally {
            try {
                archivoCliente.close();
                archivoEquipo.close();
                archivoArriendo.close();
            } catch (IOException E) {
                throw new DataFormatException("Problemas al cerrar uno o más archivos");
            }
        }
    }

    private Arriendo buscaArriendo(long codigo) {
        for (Arriendo arriendo : arriendos) {
            if (codigo == arriendo.getCodigo()) {
                return arriendo;
            }
        }
        return null;
    }

    private Cliente buscaCliente(String rut) {
        for (Cliente cliente : clientes) {
            if (rut.equals(cliente.getRut())) {
                return cliente;
            }
        }
        return null;
    }

    private Equipo buscaEquipo(long codigo) {
        for (Equipo equipo : equipos) {
            if (codigo == equipo.getCodigo()) {
                return equipo;
            }
        }
        return null;
    }

    public String[][] listaArriendosPagados() {
        if (!(arriendos.isEmpty())){
            int cantArrConPago = 0;
            for(Arriendo arriendo: arriendos){
                if(arriendo.getMontoPagado() > 0){
                    cantArrConPago++;
                }
            }
            if(cantArrConPago>0){
                String[][] ArriendosPag = new String[cantArrConPago][7];
                int cont = 0;
                for(Arriendo arriendo: arriendos){
                    if(arriendo.getMontoPagado() > 0){
                        String[] datos = consultaArriendoAPagar(arriendo.getCodigo());
                        if(datos.length>0){
                            ArriendosPag[cont][0] = datos[0];
                            ArriendosPag[cont][1] = datos[1];
                            ArriendosPag[cont][2] = datos[2];
                            ArriendosPag[cont][3] = datos[3];
                            ArriendosPag[cont][4] = datos[4];
                            ArriendosPag[cont][5] = datos[5];
                            ArriendosPag[cont][6] = datos[6];
                            cont++;
                        }
                    }
                }
                return ArriendosPag;
            }else{
                return new String[0][0];
            }

        }else{
            return new String[0][0];
        }

    }

    public String[][] listaPagosDeArriendo(long codArriendo) {
        Arriendo arriendo = buscaArriendo(codArriendo);
        if(arriendo!=null){
            return arriendo.getPagosToString();
        }else{
            return new String[0][0];
        }
    }
}




