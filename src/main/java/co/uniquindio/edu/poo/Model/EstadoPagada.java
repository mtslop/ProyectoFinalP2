package co.uniquindio.edu.poo.Model;

/**
 * Estado de una compra cuyo pago fue aprobado.
 */
public class EstadoPagada implements EstadoCompra {

    /**
     * Crea el estado pagado.
     */
    public EstadoPagada() {
    }

    /**
     * Rechaza un segundo pago sobre la misma compra.
     *
     * @return no retorna; la operación siempre genera una excepción.
     * @throws IllegalStateException porque la compra ya fue pagada.
     */
    @Override
    public EstadoCompra pagar() {
        throw new IllegalStateException("La compra ya se encuentra pagada.");
    }

    /**
     * Inicia la cancelación de una compra pagada.
     * El cálculo y la ejecución del reembolso se realizan desde CompraFacade.
     *
     * @return una nueva instancia de EstadoCancelada.
     */
    @Override
    public EstadoCompra cancelar() {
        return new EstadoCancelada();
    }

    /**
     * Impide reembolsar directamente sin pasar primero por la cancelación.
     *
     * @return no retorna; la operación siempre genera una excepción.
     * @throws IllegalStateException porque primero debe cancelarse la compra.
     */
    @Override
    public EstadoCompra reembolsar() {
        throw new IllegalStateException("La compra debe cancelarse antes de reembolsarse.");
    }

    /**
     * Identifica este estado.
     *
     * @return texto PAGADA.
     */
    @Override
    public String nombre() {
        return "PAGADA";
    }
}
