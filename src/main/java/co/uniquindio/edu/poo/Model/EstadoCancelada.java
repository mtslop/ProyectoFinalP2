package co.uniquindio.edu.poo.Model;

/**
 * Estado de una compra cancelada que todavía puede tener pendiente su reembolso.
 */
public class EstadoCancelada implements EstadoCompra {

    /**
     * Crea el estado cancelado.
     */
    public EstadoCancelada() {
    }

    /**
     * Impide pagar una compra que ya fue cancelada.
     *
     * @return no retorna; la operación siempre genera una excepción.
     * @throws IllegalStateException porque la compra está cancelada.
     */
    @Override
    public EstadoCompra pagar() {
        throw new IllegalStateException("No se puede pagar una compra cancelada.");
    }

    /**
     * Impide cancelar dos veces la misma compra.
     *
     * @return no retorna; la operación siempre genera una excepción.
     * @throws IllegalStateException porque la compra ya está cancelada.
     */
    @Override
    public EstadoCompra cancelar() {
        throw new IllegalStateException("La compra ya se encuentra cancelada.");
    }

    /**
     * Confirma que el reembolso de la compra cancelada fue realizado.
     *
     * @return una nueva instancia de EstadoReembolsada.
     */
    @Override
    public EstadoCompra reembolsar() {
        return new EstadoReembolsada();
    }

    /**
     * Identifica este estado.
     *
     * @return texto CANCELADA.
     */
    @Override
    public String nombre() {
        return "CANCELADA";
    }
}
