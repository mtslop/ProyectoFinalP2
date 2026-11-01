package co.uniquindio.edu.poo.Model;

import java.util.Objects;

/**
 * Representa un producto individual de confitería, como palomitas, bebidas o dulces.
 */
public class Producto implements ItemConfiteria {

    private String codigo;
    private String nombre;
    private double precio;
    private int puntosRedencion;

    /**
     * Crea un producto de confitería.
     *
     * @param codigo identificador único del producto.
     * @param nombre nombre comercial.
     * @param precio precio de venta.
     * @param puntosRedencion puntos que otorga o que cuesta redimir.
     * @throws NullPointerException si el código o el nombre son null.
     * @throws IllegalArgumentException si el precio o los puntos no son válidos.
     */
    public Producto(String codigo, String nombre, double precio, int puntosRedencion) {
        this.codigo = Objects.requireNonNull(codigo, "El código no puede ser null.");
        if (codigo.isBlank()) {
            throw new IllegalArgumentException("El código del producto no puede estar vacío.");
        }
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser null.");
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (!Double.isFinite(precio) || precio < 0) {
            throw new IllegalArgumentException("El precio del producto debe ser válido y no negativo.");
        }
        if (puntosRedencion < 0) {
            throw new IllegalArgumentException("Los puntos de redención no pueden ser negativos.");
        }
        this.precio = precio;
        this.puntosRedencion = puntosRedencion;
    }

    /**
     * Obtiene el nombre comercial del producto.
     *
     * @return nombre del producto.
     */
    @Override
    public String nombre() {
        return nombre;
    }

    /**
     * Obtiene el precio del producto.
     *
     * @return precio de venta.
     */
    @Override
    public double precio() {
        return precio;
    }

    /**
     * Obtiene los puntos de redención del producto.
     *
     * @return puntos de redención.
     */
    @Override
    public int puntosRedencion() {
        return puntosRedencion;
    }

    /**
     * Obtiene el código del producto.
     *
     * @return código único.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Actualiza el código del producto.
     *
     * @param codigo nuevo código.
     */
    public void setCodigo(String codigo) {
        this.codigo = Objects.requireNonNull(codigo, "El código no puede ser null.");
        if (codigo.isBlank()) {
            throw new IllegalArgumentException("El código del producto no puede estar vacío.");
        }
    }

    /**
     * Actualiza el nombre del producto.
     *
     * @param nombre nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser null.");
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
    }

    /**
     * Actualiza el precio del producto.
     *
     * @param precio nuevo precio.
     */
    public void setPrecio(double precio) {
        if (!Double.isFinite(precio) || precio < 0) {
            throw new IllegalArgumentException("El precio del producto debe ser válido y no negativo.");
        }
        this.precio = precio;
    }

    /**
     * Actualiza los puntos de redención del producto.
     *
     * @param puntosRedencion nuevos puntos.
     */
    public void setPuntosRedencion(int puntosRedencion) {
        if (puntosRedencion < 0) {
            throw new IllegalArgumentException("Los puntos de redención no pueden ser negativos.");
        }
        this.puntosRedencion = puntosRedencion;
    }
}
