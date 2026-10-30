package co.uniquindio.edu.poo.Model;

import java.util.Objects;

/**
 * Clase abstracta que envuelve otro objeto Precio para añadirle una promoción.
 * Las promociones concretas heredan de esta clase y modifican el resultado
 * del precio que reciben.
 */
public abstract class PromocionDecorador implements Precio {

    protected final Precio precio;

    /**
     * Construye un decorador alrededor de otro precio.
     *
     * @param precio precio que será decorado.
     * @throws NullPointerException si el precio recibido es null.
     */
    public PromocionDecorador(Precio precio) {
        this.precio = Objects.requireNonNull(precio, "El precio que se va a decorar no puede ser null.");
    }

    /**
     * Obtiene el precio envuelto por este decorador.
     *
     * @return componente Precio decorado.
     */
    public Precio getPrecio() {
        return precio;
    }

    /**
     * Delega el cálculo al precio envuelto.
     * Las subclases normalmente sobrescriben este método para aplicar el descuento.
     *
     * @return resultado del precio envuelto.
     */
    @Override
    public double calcular() {
        return precio.calcular();
    }

    /**
     * Delega la descripción al precio envuelto.
     *
     * @return descripción del precio envuelto.
     */
    @Override
    public String descripcion() {
        return precio.descripcion();
    }
}
