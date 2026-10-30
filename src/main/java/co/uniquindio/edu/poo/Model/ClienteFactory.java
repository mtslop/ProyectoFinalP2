package co.uniquindio.edu.poo.Model;

public class ClienteFactory extends PersonaFactory {

    /**
     * Crea un nuevo objeto Cliente con sus datos basicos y telefono.
     *
     * @param id         Identificacion del cliente.
     * @param nombre     Nombre completo.
     * @param correo     Correo electronico.
     * @param contrasena Contrasena de acceso.
     * @param telefono   Numero de telefono del cliente.
     * @return Nueva instancia de Cliente.
     */
    @Override
    public Persona crearPersona(String id, String nombre, String correo, String contrasena, String telefono) {
        return new Cliente(id, nombre, correo, contrasena, telefono);
    }
}