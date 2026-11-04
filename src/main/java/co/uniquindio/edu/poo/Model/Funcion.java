package co.uniquindio.edu.poo.Model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Representa la proyección de una película en una sala y en una fecha determinada.
 * Controla los asientos ocupados y el estado de la función.
 */
public class Funcion {

    private String id;
    private LocalDateTime fechaHora;
    private double precioBase;
    private boolean cancelada;
    private Pelicula pelicula;
    private Sala sala;
    private final Set<Asiento> asientosOcupados;

    /**
     * Crea una función disponible para la venta de entradas.
     *
     * @param id identificador único de la función.
     * @param fechaHora fecha y hora de inicio.
     * @param precioBase precio base de la entrada.
     * @param pelicula película que se proyectará.
     * @param sala sala donde se realizará la función.
     * @throws NullPointerException si algún dato obligatorio es null.
     * @throws IllegalArgumentException si el precio base es negativo o no es finito.
     */
    public Funcion(String id, LocalDateTime fechaHora, double precioBase,
                   Pelicula pelicula, Sala sala) {
        this.id = Objects.requireNonNull(id, "El identificador no puede ser null.");
        if (id.isBlank()) {
            throw new IllegalArgumentException("El identificador de la función no puede estar vacío.");
        }
        this.fechaHora = Objects.requireNonNull(fechaHora, "La fecha y hora no pueden ser null.");
        if (!Double.isFinite(precioBase) || precioBase < 0) {
            throw new IllegalArgumentException("El precio base debe ser válido y no negativo.");
        }
        this.precioBase = precioBase;
        this.pelicula = Objects.requireNonNull(pelicula, "La película no puede ser null.");
        this.sala = Objects.requireNonNull(sala, "La sala no puede ser null.");
        this.cancelada = false;
        this.asientosOcupados = new HashSet<>();
    }

    /**
     * Indica si la función puede seguir vendiendo entradas: no está cancelada
     * y todavía no ha comenzado.
     *
     * @return true si la función está disponible.
     */
    public boolean estaDisponible() {
        return !cancelada && fechaHora.isAfter(LocalDateTime.now());
    }

    /**
     * Calcula las horas que faltan para el inicio de la función.
     * Si la función ya comenzó, el resultado puede ser negativo.
     *
     * @return horas restantes.
     */
    public long horasRestantes() {
        return Duration.between(LocalDateTime.now(), fechaHora).toHours();
    }

    /**
     * Marca la función como cancelada.
     */
    public void cancelar() {
        this.cancelada = true;
    }

    /**
     * Calcula el porcentaje de ocupación de la sala.
     *
     * @return porcentaje entre 0 y 100.
     */
    public double ocupacion() {
        if (sala.getCapacidad() == 0) {
            return 0;
        }
        return (asientosOcupados.size() * 100.0) / sala.getCapacidad();
    }

    /**
     * Ocupa un asiento de la sala para esta función.
     *
     * @param asiento asiento que se desea ocupar.
     * @return true si el asiento pertenece a la sala y estaba libre.
     * @throws NullPointerException si el asiento es null.
     */
    public boolean ocuparAsiento(Asiento asiento) {
        Objects.requireNonNull(asiento, "El asiento no puede ser null.");
        if (cancelada) {
            return false;
        }
        Asiento asientoDeLaSala = sala.buscarAsiento(asiento.getFila(), asiento.getNumero());
        if (asientoDeLaSala == null || asientosOcupados.contains(asientoDeLaSala)) {
            return false;
        }
        return asientosOcupados.add(asientoDeLaSala);
    }

    /**
     * Libera un asiento previamente ocupado.
     *
     * @param asiento asiento que se desea liberar.
     * @return true si el asiento estaba ocupado.
     */
    public boolean liberarAsiento(Asiento asiento) {
        if (asiento == null) {
            return false;
        }
        Asiento asientoDeLaSala = sala.buscarAsiento(asiento.getFila(), asiento.getNumero());
        return asientoDeLaSala != null && asientosOcupados.remove(asientoDeLaSala);
    }

    /**
     * Indica si la función fue cancelada.
     *
     * @return true si está cancelada.
     */
    public boolean estaCancelada() {
        return cancelada;
    }

    /**
     * Indica si la película ya terminó de proyectarse.
     *
     * @return true si la función ya finalizó.
     */
    public boolean estaFinalizada() {
        LocalDateTime fin = fechaHora.plusMinutes(pelicula.getDuracionMin());
        return !LocalDateTime.now().isBefore(fin);
    }

    /**
     * Obtiene el identificador de la función.
     *
     * @return identificador.
     */
    public String getId() {
        return id;
    }

    /**
     * Actualiza el identificador de la función.
     *
     * @param id nuevo identificador.
     */
    public void setId(String id) {
        this.id = Objects.requireNonNull(id, "El identificador no puede ser null.");
        if (id.isBlank()) {
            throw new IllegalArgumentException("El identificador de la función no puede estar vacío.");
        }
    }

    /**
     * Obtiene la fecha y hora de la función.
     *
     * @return fecha y hora de inicio.
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Actualiza la fecha y hora de la función.
     *
     * @param fechaHora nueva fecha y hora.
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = Objects.requireNonNull(fechaHora, "La fecha y hora no pueden ser null.");
    }

    /**
     * Obtiene el precio base de la entrada.
     *
     * @return precio base.
     */
    public double getPrecioBase() {
        return precioBase;
    }

    /**
     * Actualiza el precio base de la entrada.
     *
     * @param precioBase nuevo precio base.
     */
    public void setPrecioBase(double precioBase) {
        if (!Double.isFinite(precioBase) || precioBase < 0) {
            throw new IllegalArgumentException("El precio base debe ser válido y no negativo.");
        }
        this.precioBase = precioBase;
    }

    /**
     * Obtiene la película de la función.
     *
     * @return película.
     */
    public Pelicula getPelicula() {
        return pelicula;
    }

    /**
     * Actualiza la película de la función.
     *
     * @param pelicula nueva película.
     */
    public void setPelicula(Pelicula pelicula) {
        this.pelicula = Objects.requireNonNull(pelicula, "La película no puede ser null.");
    }

    /**
     * Obtiene la sala de la función.
     *
     * @return sala.
     */
    public Sala getSala() {
        return sala;
    }

    /**
     * Actualiza la sala de la función y libera los asientos ocupados.
     *
     * @param sala nueva sala.
     */
    public void setSala(Sala sala) {
        this.sala = Objects.requireNonNull(sala, "La sala no puede ser null.");
        this.asientosOcupados.clear();
    }

    /**
     * Obtiene los asientos ocupados en una vista no modificable.
     *
     * @return asientos ocupados.
     */
    public Set<Asiento> getAsientosOcupados() {
        return Collections.unmodifiableSet(asientosOcupados);
    }
}
