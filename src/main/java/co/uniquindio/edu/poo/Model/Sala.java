package co.uniquindio.edu.poo.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Representa una sala de proyección con su capacidad y sus asientos.
 */
public class Sala {

    private int numero;
    private int capacidad;
    private final List<Asiento> asientos;

    /**
     * Crea una sala vacía con la capacidad indicada.
     *
     * @param numero número de la sala.
     * @param capacidad cantidad máxima de asientos.
     * @throws IllegalArgumentException si el número o la capacidad no son positivos.
     */
    public Sala(int numero, int capacidad) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de la sala debe ser mayor que cero.");
        }
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }
        this.numero = numero;
        this.capacidad = capacidad;
        this.asientos = new ArrayList<>();
    }

    /**
     * Crea una sala a partir de sus asientos. La capacidad se calcula
     * automáticamente según la cantidad de asientos recibidos.
     *
     * @param numero número de la sala.
     * @param asientos asientos que componen la sala.
     * @throws NullPointerException si la lista de asientos es null.
     * @throws IllegalArgumentException si el número no es positivo.
     */
    public Sala(int numero, List<Asiento> asientos) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de la sala debe ser mayor que cero.");
        }
        Objects.requireNonNull(asientos, "La lista de asientos no puede ser null.");
        this.numero = numero;
        this.asientos = new ArrayList<>(asientos);
        this.capacidad = this.asientos.size();
    }

    /**
     * Busca un asiento por fila y número.
     *
     * @param fila fila del asiento.
     * @param numero número del asiento.
     * @return el asiento encontrado o null si no existe en la sala.
     */
    public Asiento buscarAsiento(String fila, int numero) {
        if (fila == null) {
            return null;
        }
        for (Asiento asiento : asientos) {
            if (asiento.getFila().equalsIgnoreCase(fila.trim())
                    && asiento.getNumero() == numero) {
                return asiento;
            }
        }
        return null;
    }

    /**
     * Agrega un asiento a la sala si todavía hay capacidad disponible.
     *
     * @param asiento asiento que se desea agregar.
     * @return true si se agregó correctamente.
     */
    public boolean agregarAsiento(Asiento asiento) {
        if (asiento == null || asientos.size() >= capacidad) {
            return false;
        }
        return asientos.add(asiento);
    }

    /**
     * Obtiene el número de la sala.
     *
     * @return número de la sala.
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Actualiza el número de la sala.
     *
     * @param numero nuevo número.
     */
    public void setNumero(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de la sala debe ser mayor que cero.");
        }
        this.numero = numero;
    }

    /**
     * Obtiene la capacidad de la sala.
     *
     * @return capacidad máxima.
     */
    public int getCapacidad() {
        return capacidad;
    }

    /**
     * Obtiene los asientos de la sala en una vista no modificable.
     *
     * @return lista de asientos.
     */
    public List<Asiento> getAsientos() {
        return Collections.unmodifiableList(asientos);
    }
}
