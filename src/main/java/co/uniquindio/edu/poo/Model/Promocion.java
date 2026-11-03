package co.uniquindio.edu.poo.Model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa una promoción de cartelera aplicable a un monto de compra.
 *
 * <p>El valor se interpreta de la siguiente forma:</p>
 * <ul>
 *     <li>PORCENTAJE: porcentaje de descuento entre 0 y 100.</li>
 *     <li>DIA_ESPECIAL: porcentaje de descuento aplicable únicamente el día indicado.</li>
 *     <li>VALORFIJO: monto fijo de descuento.</li>
 * </ul>
 */
public class Promocion {

    private String nombre;
    private TipoPromocion tipo;
    private double valor;
    private DayOfWeek dia;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    /**
     * Crea una promoción.
     *
     * @param nombre nombre de la promoción.
     * @param tipo tipo de promoción.
     * @param valor valor del descuento.
     * @param dia día de la semana para las promociones de día especial.
     * @param fechaInicio fecha desde la que rige la promoción.
     * @param fechaFin fecha hasta la que rige la promoción.
     * @throws NullPointerException si el nombre, el tipo o las fechas son null.
     * @throws IllegalArgumentException si el valor o las fechas no son válidos.
     */
    public Promocion(String nombre, TipoPromocion tipo, double valor, DayOfWeek dia,
                     LocalDate fechaInicio, LocalDate fechaFin) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser null.");
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la promoción no puede estar vacío.");
        }
        this.tipo = Objects.requireNonNull(tipo, "El tipo no puede ser null.");
        if (!Double.isFinite(valor) || valor < 0) {
            throw new IllegalArgumentException("El valor de la promoción debe ser válido y no negativo.");
        }
        if ((tipo == TipoPromocion.PORCENTAJE || tipo == TipoPromocion.DIA_ESPECIAL) && valor > 100) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0 y 100.");
        }
        this.valor = valor;
        this.dia = dia;
        this.fechaInicio = Objects.requireNonNull(fechaInicio, "La fecha de inicio no puede ser null.");
        this.fechaFin = Objects.requireNonNull(fechaFin, "La fecha de fin no puede ser null.");
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        if (tipo == TipoPromocion.DIA_ESPECIAL && dia == null) {
            throw new IllegalArgumentException("Una promoción de día especial requiere un día.");
        }
    }

    /**
     * Indica si la promoción está vigente hoy, considerando el rango de fechas
     * y, cuando corresponde, el día de la semana.
     *
     * @return true si la promoción se puede aplicar hoy.
     */
    public boolean estaVigente() {
        LocalDate hoy = LocalDate.now();
        if (hoy.isBefore(fechaInicio) || hoy.isAfter(fechaFin)) {
            return false;
        }
        return tipo != TipoPromocion.DIA_ESPECIAL || dia == hoy.getDayOfWeek();
    }

    /**
     * Aplica la promoción a un monto. Si la promoción no está vigente,
     * devuelve el monto sin cambios.
     *
     * @param monto monto original.
     * @return monto final después de aplicar la promoción.
     * @throws IllegalArgumentException si el monto es negativo o no es finito.
     */
    public double aplicar(double monto) {
        if (!Double.isFinite(monto) || monto < 0) {
            throw new IllegalArgumentException("El monto debe ser válido y no negativo.");
        }
        if (!estaVigente()) {
            return monto;
        }

        double resultado;
        switch (tipo) {
            case PORCENTAJE:
            case DIA_ESPECIAL:
                resultado = monto * (1 - valor / 100.0);
                break;
            case VALORFIJO:
                resultado = monto - valor;
                break;
            default:
                resultado = monto;
        }
        return Math.max(0, resultado);
    }

    /**
     * Obtiene el nombre de la promoción.
     *
     * @return nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Actualiza el nombre de la promoción.
     *
     * @param nombre nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser null.");
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la promoción no puede estar vacío.");
        }
    }

    /**
     * Obtiene el tipo de promoción.
     *
     * @return tipo.
     */
    public TipoPromocion getTipo() {
        return tipo;
    }

    /**
     * Actualiza el tipo de promoción.
     *
     * @param tipo nuevo tipo.
     */
    public void setTipo(TipoPromocion tipo) {
        this.tipo = Objects.requireNonNull(tipo, "El tipo no puede ser null.");
    }

    /**
     * Obtiene el valor del descuento.
     *
     * @return valor.
     */
    public double getValor() {
        return valor;
    }

    /**
     * Actualiza el valor del descuento.
     *
     * @param valor nuevo valor.
     */
    public void setValor(double valor) {
        if (!Double.isFinite(valor) || valor < 0) {
            throw new IllegalArgumentException("El valor de la promoción debe ser válido y no negativo.");
        }
        this.valor = valor;
    }

    /**
     * Obtiene el día especial de la promoción.
     *
     * @return día de la semana o null si no aplica.
     */
    public DayOfWeek getDia() {
        return dia;
    }

    /**
     * Actualiza el día especial de la promoción.
     *
     * @param dia nuevo día.
     */
    public void setDia(DayOfWeek dia) {
        this.dia = dia;
    }

    /**
     * Obtiene la fecha de inicio de la promoción.
     *
     * @return fecha de inicio.
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Actualiza la fecha de inicio de la promoción.
     *
     * @param fechaInicio nueva fecha de inicio.
     */
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = Objects.requireNonNull(fechaInicio, "La fecha de inicio no puede ser null.");
    }

    /**
     * Obtiene la fecha de fin de la promoción.
     *
     * @return fecha de fin.
     */
    public LocalDate getFechaFin() {
        return fechaFin;
    }

    /**
     * Actualiza la fecha de fin de la promoción.
     *
     * @param fechaFin nueva fecha de fin.
     */
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = Objects.requireNonNull(fechaFin, "La fecha de fin no puede ser null.");
    }
}
