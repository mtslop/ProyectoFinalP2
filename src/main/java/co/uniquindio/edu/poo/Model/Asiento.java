package co.uniquindio.edu.poo.Model;

import java.util.Objects;

/**
 * Representa un asiento físico de una sala.
 * Se identifica por su fila y su número.
 */
public class Asiento {

    private String fila;
    private int numero;
    private TipoAsiento tipo;

    /**
     * Crea un asiento con su ubicación y tipo.
     *
     * @param fila fila a la que pertenece el asiento.
     * @param numero número del asiento dentro de la fila.
     * @param tipo tipo de asiento.
     * @throws NullPointerException si la fila o el tipo son null.
     * @throws IllegalArgumentException si el número no es positivo.
     */
    public Asiento(String fila, int numero, TipoAsiento tipo) {
        this.fila = Objects.requireNonNull(fila, "La fila no puede ser null.");
        if (fila.isBlank()) {
            throw new IllegalArgumentException("La fila no puede estar vacía.");
        }
        if (numero <= 0) {
            throw new IllegalArgumentException("El número del asiento debe ser mayor que cero.");
        }
        this.numero = numero;
        this.tipo = Objects.requireNonNull(tipo, "El tipo de asiento no puede ser null.");
    }

    /**
     * Obtiene el código legible del asiento, por ejemplo "A5".
     *
     * @return fila y número concatenados.
     */
    public String codigo() {
        return fila.trim().toUpperCase() + numero;
    }

    /**
     * Obtiene la fila del asiento.
     *
     * @return fila.
     */
    public String getFila() {
        return fila;
    }

    /**
     * Actualiza la fila del asiento.
     *
     * @param fila nueva fila.
     */
    public void setFila(String fila) {
        this.fila = Objects.requireNonNull(fila, "La fila no puede ser null.");
        if (fila.isBlank()) {
            throw new IllegalArgumentException("La fila no puede estar vacía.");
        }
    }

    /**
     * Obtiene el número del asiento.
     *
     * @return número dentro de la fila.
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Actualiza el número del asiento.
     *
     * @param numero nuevo número.
     */
    public void setNumero(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número del asiento debe ser mayor que cero.");
        }
        this.numero = numero;
    }

    /**
     * Obtiene el tipo de asiento.
     *
     * @return tipo de asiento.
     */
    public TipoAsiento getTipo() {
        return tipo;
    }

    /**
     * Actualiza el tipo de asiento.
     *
     * @param tipo nuevo tipo.
     */
    public void setTipo(TipoAsiento tipo) {
        this.tipo = Objects.requireNonNull(tipo, "El tipo de asiento no puede ser null.");
    }
}
