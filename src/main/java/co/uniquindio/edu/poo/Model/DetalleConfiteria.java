package co.uniquindio.edu.poo.Model;

import java.util.Objects;

/**
 * Representa una línea de la compra correspondiente a un producto de confitería.
 * Conserva el precio unitario para que el subtotal no cambie si posteriormente
 * se modifica el precio del producto en el catálogo.
 */
public class DetalleConfiteria {

    private final int cantidad;
    private final double precioUnitario;
    private final ItemConfiteria item;

    /**
     * Crea un detalle con cantidad y precio unitario explícitos.
     *
     * @param cantidad unidades del producto.
     * @param precioUnitario precio de cada unidad al momento de la compra.
     * @param item producto de confitería asociado.
     * @throws NullPointerException si el producto es null.
     * @throws IllegalArgumentException si la cantidad no es positiva o el precio no es válido.
     */
    public DetalleConfiteria(int cantidad, double precioUnitario, ItemConfiteria item) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        if (!Double.isFinite(precioUnitario) || precioUnitario < 0) {
            throw new IllegalArgumentException("El precio unitario debe ser válido y no negativo.");
        }
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.item = Objects.requireNonNull(item, "El producto de confitería no puede ser null.");
    }

    /**
     * Crea un detalle tomando el precio actual directamente del producto.
     *
     * @param cantidad unidades del producto.
     * @param item producto de confitería.
     */
    public DetalleConfiteria(int cantidad, ItemConfiteria item) {
        this(cantidad, Objects.requireNonNull(item, "El producto no puede ser null.").precio ( ), item);
    }

    /**
     * Calcula el subtotal de esta línea de confitería.
     *
     * @return cantidad multiplicada por el precio unitario.
     */
    public double subtotal() {
        return cantidad * precioUnitario;
    }

    /**
     * Obtiene la cantidad de unidades del producto.
     *
     * @return cantidad comprada.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Obtiene el precio unitario guardado en el detalle.
     *
     * @return precio unitario.
     */
    public double getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * Obtiene el producto asociado al detalle.
     *
     * @return producto de confitería.
     */
    public ItemConfiteria getItem() {
        return item;
    }
}
