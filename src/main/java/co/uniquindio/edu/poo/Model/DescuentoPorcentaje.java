package co.uniquindio.edu.poo.Model;

/**
 * Decorador que aplica un descuento porcentual al precio que recibe.
 */
public class DescuentoPorcentaje extends PromocionDecorador {

    private final double porcentaje;

    /**
     * Crea una promoción porcentual.
     *
     * @param precio precio sobre el que se aplicará el descuento.
     * @param porcentaje porcentaje de descuento, entre 0 y 100.
     * @throws IllegalArgumentException si el porcentaje está fuera del rango permitido.
     */
    public DescuentoPorcentaje(Precio precio, double porcentaje) {
        super(precio);
        if (!Double.isFinite(porcentaje) || porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100.");
        }
        this.porcentaje = porcentaje;
    }

    /**
     * Calcula el precio después de aplicar el porcentaje de descuento.
     *
     * @return precio descontado, sin permitir valores negativos.
     */
    @Override
    public double calcular() {
        double resultado = precio.calcular() * (1 - porcentaje / 100.0);
        return Math.max(0, resultado);
    }

    /**
     * Construye la descripción de la promoción porcentual.
     *
     * @return descripción del precio y del porcentaje aplicado.
     */
    @Override
    public String descripcion() {
        return precio.descripcion() + " + descuento del " + porcentaje + "%";
    }

    /**
     * Obtiene el porcentaje configurado.
     *
     * @return porcentaje de descuento.
     */
    public double getPorcentaje() {
        return porcentaje;
    }
}
