package co.uniquindio.edu.poo.Model;

/**
 * Decorador que resta una cantidad fija al precio que recibe.
 */
public class DescuentoValorFijo extends PromocionDecorador {

    private final double valor;

    /**
     * Crea una promoción de descuento por valor fijo.
     *
     * @param precio precio sobre el que se aplicará el descuento.
     * @param valor cantidad monetaria que se descontará.
     * @throws IllegalArgumentException si el descuento es negativo o no es finito.
     */
    public DescuentoValorFijo(Precio precio, double valor) {
        super(precio);
        if (!Double.isFinite(valor) || valor < 0) {
            throw new IllegalArgumentException("El descuento fijo debe ser válido y no negativo.");
        }
        this.valor = valor;
    }

    /**
     * Calcula el precio restando el descuento fijo.
     *
     * @return precio final; nunca será inferior a cero.
     */
    @Override
    public double calcular() {
        return Math.max(0, precio.calcular() - valor);
    }

    /**
     * Describe el descuento fijo aplicado.
     *
     * @return descripción de la promoción.
     */
    @Override
    public String descripcion() {
        return precio.descripcion() + " + descuento fijo de " + valor;
    }

    /**
     * Obtiene el valor fijo de la promoción.
     *
     * @return cantidad descontada.
     */
    public double getValor() {
        return valor;
    }
}
