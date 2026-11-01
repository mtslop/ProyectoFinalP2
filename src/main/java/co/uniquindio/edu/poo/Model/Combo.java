package co.uniquindio.edu.poo.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Representa un combo de confitería compuesto por varios artículos.
 * El descuento se expresa como porcentaje entre 0 y 100.
 */
public class Combo implements ItemConfiteria {

    private String codigo;
    private String nombre;
    private double descuento;
    private int puntosRedencion;
    private final List<ItemConfiteria> items;

    /**
     * Crea un combo vacío.
     *
     * @param codigo identificador único del combo.
     * @param nombre nombre comercial.
     * @param descuento porcentaje de descuento entre 0 y 100.
     * @param puntosRedencion puntos que otorga o que cuesta redimir.
     * @throws NullPointerException si el código o el nombre son null.
     * @throws IllegalArgumentException si el descuento o los puntos no son válidos.
     */
    public Combo(String codigo, String nombre, double descuento, int puntosRedencion) {
        this.codigo = Objects.requireNonNull(codigo, "El código no puede ser null.");
        if (codigo.isBlank()) {
            throw new IllegalArgumentException("El código del combo no puede estar vacío.");
        }
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser null.");
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del combo no puede estar vacío.");
        }
        if (!Double.isFinite(descuento) || descuento < 0 || descuento > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100.");
        }
        if (puntosRedencion < 0) {
            throw new IllegalArgumentException("Los puntos de redención no pueden ser negativos.");
        }
        this.descuento = descuento;
        this.puntosRedencion = puntosRedencion;
        this.items = new ArrayList<>();
    }

    /**
     * Obtiene el nombre comercial del combo.
     *
     * @return nombre del combo.
     */
    @Override
    public String nombre() {
        return nombre;
    }

    /**
     * Calcula el precio del combo sumando sus artículos y aplicando el descuento.
     *
     * @return precio final del combo.
     */
    @Override
    public double precio() {
        double total = 0;
        for (ItemConfiteria item : items) {
            total += item.precio();
        }
        return total * (1 - descuento / 100.0);
    }

    /**
     * Obtiene los puntos de redención del combo.
     *
     * @return puntos de redención.
     */
    @Override
    public int puntosRedencion() {
        return puntosRedencion;
    }

    /**
     * Agrega un artículo al combo.
     *
     * @param item artículo que se desea agregar.
     * @return true si se agregó correctamente.
     */
    public boolean agregar(ItemConfiteria item) {
        if (item == null || item == this) {
            return false;
        }
        return items.add(item);
    }

    /**
     * Elimina un artículo del combo.
     *
     * @param item artículo que se desea eliminar.
     * @return true si el artículo estaba en el combo.
     */
    public boolean eliminar(ItemConfiteria item) {
        if (item == null) {
            return false;
        }
        return items.remove(item);
    }

    /**
     * Obtiene el código del combo.
     *
     * @return código único.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Actualiza el código del combo.
     *
     * @param codigo nuevo código.
     */
    public void setCodigo(String codigo) {
        this.codigo = Objects.requireNonNull(codigo, "El código no puede ser null.");
        if (codigo.isBlank()) {
            throw new IllegalArgumentException("El código del combo no puede estar vacío.");
        }
    }

    /**
     * Actualiza el nombre del combo.
     *
     * @param nombre nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser null.");
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del combo no puede estar vacío.");
        }
    }

    /**
     * Obtiene el porcentaje de descuento del combo.
     *
     * @return descuento entre 0 y 100.
     */
    public double getDescuento() {
        return descuento;
    }

    /**
     * Actualiza el porcentaje de descuento del combo.
     *
     * @param descuento nuevo descuento entre 0 y 100.
     */
    public void setDescuento(double descuento) {
        if (!Double.isFinite(descuento) || descuento < 0 || descuento > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100.");
        }
        this.descuento = descuento;
    }

    /**
     * Actualiza los puntos de redención del combo.
     *
     * @param puntosRedencion nuevos puntos.
     */
    public void setPuntosRedencion(int puntosRedencion) {
        if (puntosRedencion < 0) {
            throw new IllegalArgumentException("Los puntos de redención no pueden ser negativos.");
        }
        this.puntosRedencion = puntosRedencion;
    }

    /**
     * Obtiene los artículos del combo en una vista no modificable.
     *
     * @return artículos que componen el combo.
     */
    public List<ItemConfiteria> getItems() {
        return Collections.unmodifiableList(items);
    }
}
