package co.uniquindio.edu.poo.Model;

/**
 * Estado final de una compra cuyo dinero ya fue devuelto.
 */
public class EstadoReembolsada implements EstadoCompra {

    /**
     * Crea el estado reembolsado.
     */
    public EstadoReembolsada() {
    }

    /**
     * Impide pagar nuevamente una compra reembolsada.
     *
     * @return no retorna; la operación siempre genera una excepción.
     * @throws IllegalStateException porque el proceso de compra ya terminó.
     */
    @Override
    public EstadoCompra pagar() {
        throw new IllegalStateException("No se puede pagar una compra reembolsada.");
    }

    /**
     * Impide cancelar nuevamente una compra reembolsada.
     *
     * @return no retorna; la operación siempre genera una excepción.
     * @throws IllegalStateException porque la compra ya fue reembolsada.
     */
    @Override
    public EstadoCompra cancelar() {
        throw new IllegalStateException("La compra ya fue reembolsada.");
    }

    /**
     * Impide ejecutar un segundo reembolso sobre la misma compra.
     *
     * @return no retorna; la operación siempre genera una excepción.
     * @throws IllegalStateException para evitar un reembolso duplicado.
     */
    @Override
    public EstadoCompra reembolsar() {
        throw new IllegalStateException("La compra ya fue reembolsada.");
    }

    /**
     * Identifica este estado.
     *
     * @return texto REEMBOLSADA.
     */
    @Override
    public String nombre() {
        return "REEMBOLSADA";
    }
}
