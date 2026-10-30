package co.uniquindio.edu.poo.Model;

public abstract class PersonaFactory {

    /**
     * Metodo fabrica abstracto encargado de instanciar una Persona.
     *
     * @param id         Identificacion de la persona.
     * @param nombre     Nombre completo.
     * @param correo     Correo electronico.
     * @param contrasena Contrasena de la cuenta.
     * @param datoExtra  Representa el telefono (si es Cliente) o el cargo (si es Administrador).
     * @return Una instancia concreta de Persona.
     */
    public abstract Persona crearPersona(String id, String nombre, String correo, String contrasena, String datoExtra);
}