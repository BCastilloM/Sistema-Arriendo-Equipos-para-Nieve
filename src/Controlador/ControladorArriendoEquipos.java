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


import java.time.LocalDate;
import java.util.ArrayList;

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

    public void creaCliente(String rut, String nom, String dir, String tel) {
        clientes.add(new Cliente(rut, nom, dir, tel));

    }

    public void creaEquipo(long cod, String desc, long precio) {
        equipos.add(new Equipo(cod, desc, precio));
    }

    public String[][] listaClientes() {
        String[][] ClientesArr = new String[clientes.size()][5];
        int i = 0;
        for (Cliente cliente : clientes) {
            ClientesArr[i][0] = cliente.getRut();
            ClientesArr[i][1] = cliente.getNombre();
            ClientesArr[i][2] = cliente.getDireccion();
            ClientesArr[i][3] = cliente.getTelefono();
            if (clientes.get(i).isActivo()) {
                ClientesArr[i][4] = "Activo";
            } else {
                ClientesArr[i][4] = "Inactivo";
            }
            i++;
        }
        return ClientesArr;
    }

    public String[][] listaEquipos() {
        String[][] EquiposArr = new String[equipos.size()][4];
        int i = 0;
        for (Equipo equipo : equipos) {
            EquiposArr[i][0] = String.valueOf(equipo.getCodigo());
            EquiposArr[i][1] = equipo.getDescripcion();
            EquiposArr[i][2] = String.valueOf(equipo.getPrecioArriendoDia());
            EquiposArr[i][3] = String.valueOf(equipo.getEstado());
            i++;
        }
        return EquiposArr;
    }
    /*
    public long creaArriendo(String rut) throws ClienteException {
        long i = 0;
        Cliente cliente = buscaClientes(rut);
        if (cliente == null) {
            throw new ClienteException("No existe un cliente con el rut dado");
        }
        if (!cliente.isActivo()) {
            throw new ClienteException("El cliente está inactivo");
        }
        i = (long) arriendos.lastIndexOf(arriendos);
        arriendos.add(new Arriendo(i, LocalDate.now(), cliente));
        return i;
    }

    public String agregaEquipoToArriendo(long codArriendo, long codEquipo) throws ArriendoException, EquipoException {
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            throw new ArriendoException("No existe un arriendo con el código dado");
        }
        if (arriendo.getEstado() != EstadoArriendo.INICIADO) {
            throw new ArriendoException("El arriendo no tiene el estado INICIADO");
        }
        Equipo equipo = buscaEquipo(codEquipo);
        if (equipo == null) {
            throw new EquipoException("No existe un equipo con el código dado");
        }
        if (equipo.getEstado() != EstadoEquipo.OPERATIVO) {
            throw new EquipoException("El equipo no está operativo");
        }
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
            return equipo.getDescripcion();
        }
    }

    public long cierraArriendo(long codArriendo) throws ArriendoException {
        Arriendo arriendo = buscaArriendo(codArriendo);
        if (arriendo == null) {
            throw new ArriendoException("No existe un arriendo con el código dado");
        }
        if (arriendo.getEquipo() == null) {
            throw new ArriendoException("El arriendo no tiene equipos asociados");
        }
        arriendo.setEstado(EstadoArriendo.ENTREGADO);
        return arriendo.getMontoTotal();
    }


    public void devuelveEquipos(long codArriendo, EstadoEquipo[] estadoEquipos) throws ArriendoException {
        Arriendo arriendo = buscaArriendo(codArriendo);
        boolean bandera;
        int contador = 0;
        if (arriendo == null) {
            throw new ArriendoException("No existe un arriendo con el código dado");
        }
        if (arriendo.getEstado() != EstadoArriendo.ENTREGADO) {
            throw new ArriendoException("El arriendo no tiene devolución pendiente");
        }
      for(Equipo equipo : arriendo.getEquipo()){
          bandera = true;
          for(int i = 0; i < equipos.size() && bandera; i++){
              if(equipo.getCodigo() == equipos.get(i).getCodigo()){
                  equipos.get(i).setEstado(estadoEquipos[contador]);
              }
          }
          contador++;
      }
        arriendos.get((int) codArriendo).setEstado(EstadoArriendo.DEVUELTO);
        arriendos.get((int) codArriendo).setFechaDevolucion(LocalDate.now());
    }

    public void cambiaEstadoCliente(String rutCliente) throws ClienteException {
        Cliente cliente = buscaCliente(rutCliente);
        if (cliente == null) {
            throw new ClienteException("No existe un cliente con el rut dado");
        }
        if (cliente.isActivo()) {
            cliente.setActivo(false);
        } else {
            cliente.setInactivo(true);
        }
    }

    public String[] consultaCliente(String rut) {
        Cliente cliente = buscaCliente(rut);
        String[] ClientesArr = new String[6];
        if (cliente == null) {
            return ClientesArr;
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
            return EquiposArr;
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
}

     */
    }