package co.uniquindio.edu.poo.Model;

import co.uniquindio.edu.poo.Model.CinemaUQ;
import co.uniquindio.edu.poo.Model.Cliente;
import co.uniquindio.edu.poo.Model.ClienteFactory;
import co.uniquindio.edu.poo.Model.Persona;

public class ServicioAutenticacion {

    private CinemaUQ cinema;
    private ClienteFactory clienteFactory;


    public ServicioAutenticacion(CinemaUQ cinema) {
        this.cinema = cinema;
        this.clienteFactory = new ClienteFactory();
    }

    /**
     * Registra un nuevo cliente en el sistema validando que el correo no exista previamente.
     *
     * @param id         Documento de identidad.
     * @param nombre     Nombre completo.
     * @param correo     Correo electronico.
     * @param contrasena Contrasena de la cuenta.
     * @param telefono   Telefono del cliente.
     * @return El objeto Cliente creado.
     * @throws Exception Si ya existe una persona registrada con ese correo.
     */
    public Cliente registrarCliente(String id, String nombre, String correo, String contrasena, String telefono) throws Exception {
        Persona existente = cinema.buscarPersonaPorCorreo(correo);
        if (existente != null) {
            throw new Exception("Ya existe un usuario registrado con el correo: " + correo);
        }

        Cliente nuevoCliente = (Cliente) clienteFactory.crearPersona(id, nombre, correo, contrasena, telefono);
        cinema.agregarPersona(nuevoCliente);
        return nuevoCliente;
    }

    /**
     * Valida las credenciales de un usuario para permitir su ingreso al sistema.
     *
     * @param correo     Correo electronico ingresado.
     * @param contrasena Contrasena ingresada.
     * @return La Persona (Cliente o Administrador) que inicio sesion.
     * @throws Exception Si el correo no existe o la contrasena es incorrecta.
     */
    public Persona iniciarSesion(String correo, String contrasena) throws Exception {
        Persona persona = cinema.buscarPersonaPorCorreo(correo);

        if (persona == null) {
            throw new Exception("No se encontro ningun usuario con el correo ingresado.");
        }

        if (!persona.validarContrasena(contrasena)) {
            throw new Exception("La contrasena ingresada es incorrecta.");
        }

        return persona;
    }



    public CinemaUQ getCinema() {
        return cinema;
    }

    public void setCinema(CinemaUQ cinema) {
        this.cinema = cinema;
    }

    public ClienteFactory getClienteFactory() {
        return clienteFactory;
    }

    public void setClienteFactory(ClienteFactory clienteFactory) {
        this.clienteFactory = clienteFactory;
    }
}