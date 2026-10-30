package co.uniquindio.edu.poo.Model;

import java.util.Objects;

/**
 * Representa una entrada asociada a una función y a un asiento específico.
 */
public class Entrada {

    private final String codigo;
    private final double precio;
    private final Funcion funcion;
    private final Asiento asiento;

    /**
     * Crea una entrada para una función y un asiento.
     *
     * @param codigo identificador único de la entrada.
     * @param precio precio cobrado por la entrada.
     * @param funcion función para la que se emite la entrada.
     * @param asiento asiento asignado.
     * @throws NullPointerException si alguno de los objetos obligatorios es null.
     * @throws IllegalArgumentException si el precio es negativo o no es finito.
     */
    public Entrada(String codigo, double precio, Funcion funcion, Asiento asiento) {
        this.codigo = Objects.requireNonNull(codigo, "El código no puede ser null.");
        if (codigo.isBlank()) {
            throw new IllegalArgumentException("El código de la entrada no puede estar vacío.");
        }
        if (!Double.isFinite(precio) || precio < 0) {
            throw new IllegalArgumentException("El precio de la entrada debe ser válido y no negativo.");
        }
        this.precio = precio;
        this.funcion = Objects.requireNonNull(funcion, "La función no puede ser null.");
        this.asiento = Objects.requireNonNull(asiento, "El asiento no puede ser null.");
    }

    /**
     * Obtiene el código identificador de la entrada.
     *
     * @return código de la entrada.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Obtiene el precio de la entrada.
     *
     * @return precio de la entrada.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Obtiene la función asociada a la entrada.
     *
     * @return función correspondiente.
     */
    public Funcion getFuncion() {
        return funcion;
    }

    /**
     * Obtiene el asiento asignado a la entrada.
     *
     * @return asiento correspondiente.
     */
    public Asiento getAsiento() {
        return asiento;
    }
}
