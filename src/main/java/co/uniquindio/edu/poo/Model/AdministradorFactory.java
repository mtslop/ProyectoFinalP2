package co.uniquindio.edu.poo.Model;


public class AdministradorFactory extends PersonaFactory {

    /**
     * Crea un nuevo objeto Administrador con sus datos basicos y cargo.
     *
     * @param id         Identificacion del administrador.
     * @param nombre     Nombre completo.
     * @param correo     Correo electronico.
     * @param contrasena Contrasena de acceso.
     * @param cargo      Cargo administrativo dentro del cine.
     * @return Nueva instancia de Administrador.
     */
    @Override
    public Persona crearPersona(String id, String nombre, String correo, String contrasena, String cargo) {
        return new Administrador(id, nombre, correo, contrasena, cargo);
    }
}