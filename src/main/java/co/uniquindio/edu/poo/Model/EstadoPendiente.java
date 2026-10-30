package co.uniquindio.edu.poo.Model;

/**
 * Estado inicial de una compra que todavía no ha sido pagada.
 */
public class EstadoPendiente implements EstadoCompra {

    /**
     * Crea el estado pendiente.
     */
    public EstadoPendiente() {
    }

    /**
     * Confirma el pago y cambia el estado a pagada.
     *
     * @return una nueva instancia de EstadoPagada.
     */
    @Override
    public EstadoCompra pagar() {
        return new EstadoPagada();
    }

    /**
     * Cancela una compra que aún no ha sido pagada.
     *
     * @return una nueva instancia de EstadoCancelada.
     */
    @Override
    public EstadoCompra cancelar() {
        return new EstadoCancelada();
    }

    /**
     * Rechaza el reembolso porque no se ha realizado ningún pago.
     *
     * @return no retorna; la operación siempre genera una excepción.
     * @throws IllegalStateException porque una compra pendiente no tiene dinero pagado.
     */
    @Override
    public EstadoCompra reembolsar() {
        throw new IllegalStateException("No se puede reembolsar una compra pendiente.");
    }

    /**
     * Identifica este estado.
     *
     * @return texto PENDIENTE.
     */
    @Override
    public String nombre() {
        return "PENDIENTE";
    }
}
