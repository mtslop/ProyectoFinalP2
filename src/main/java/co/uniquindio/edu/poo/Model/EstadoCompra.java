package co.uniquindio.edu.poo.Model;

/**
 * Define las operaciones permitidas según el estado actual de una compra.
 * Cada operación retorna el objeto que representa el nuevo estado.
 */
public interface EstadoCompra {

    /**
     * Intenta cambiar la compra al estado pagado.
     *
     * @return nuevo estado después del pago.
     * @throws IllegalStateException si el pago no está permitido desde el estado actual.
     */
    EstadoCompra pagar();

    /**
     * Intenta cancelar la compra.
     *
     * @return nuevo estado después de la cancelación.
     * @throws IllegalStateException si la cancelación no está permitida desde el estado actual.
     */
    EstadoCompra cancelar();

    /**
     * Intenta marcar la compra como reembolsada.
     *
     * @return nuevo estado después del reembolso.
     * @throws IllegalStateException si el reembolso no está permitido desde el estado actual.
     */
    EstadoCompra reembolsar();

    /**
     * Obtiene el nombre del estado actual.
     *
     * @return nombre del estado.
     */
    String nombre();
}
