package co.uniquindio.edu.poo.Model;

public interface IClienteObserver {

    /**
     * Recibe un aviso del sistema.
     *
     * @param mensaje texto del aviso
     */

    void actualizar(String mensaje);
}
