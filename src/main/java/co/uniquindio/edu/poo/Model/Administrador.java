package co.uniquindio.edu.poo.Model;

public class Administrador extends Persona {

    private String cargo;

    public Administrador(String id, String nombre, String correo, String contrasena, String cargo) {
        super(id, nombre, correo, contrasena);
        this.cargo = cargo;
    }

    /**
     * Indica si este usuario tiene permisos de administrador.
     *
     * @return true, ya que pertenece a la clase Administrador.
     */
    @Override
    public boolean esAdministrador() {
        return true;
    }


    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}