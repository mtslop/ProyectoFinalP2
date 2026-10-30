package co.uniquindio.edu.poo.Model;


public abstract class Persona {

    private String id;
    private String nombre;
    private String correo;
    private String contrasena;

    public Persona(String id, String nombre, String correo, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    /**
     * Valida si la contrasena ingresada coincide con la registrada.
     *
     * @param contrasenaIngresada Contrasena a verificar.
     * @return true si coincide, false en caso contrario.
     */
    public boolean validarContrasena(String contrasenaIngresada) {
        if (this.contrasena != null && this.contrasena.equals(contrasenaIngresada)) {
            return true;
        }
        return false;
    }

    /**
     * Metodo abstracto para determinar si la persona tiene rol de administrador.
     *
     * @return true si es administrador, false si es cliente.
     */
    public abstract boolean esAdministrador();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}