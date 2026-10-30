package co.uniquindio.edu.poo.Model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Decorador que aplica un descuento únicamente cuando la fecha consultada
 * corresponde al día de la semana configurado.
 */
public class DescuentoDiaEspecial extends PromocionDecorador {

    private final DayOfWeek dia;
    private final double porcentaje;

    /**
     * Crea una promoción válida para un día específico de la semana.
     *
     * @param precio precio sobre el que se aplicará la promoción.
     * @param dia día de la semana en el que se activa el descuento.
     * @param porcentaje porcentaje de descuento, entre 0 y 100.
     * @throws NullPointerException si el día o el precio son null.
     * @throws IllegalArgumentException si el porcentaje está fuera del rango permitido.
     */
    public DescuentoDiaEspecial(Precio precio, DayOfWeek dia, double porcentaje) {
        super(precio);
        this.dia = Objects.requireNonNull(dia, "El día especial no puede ser null.");
        if (!Double.isFinite(porcentaje) || porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100.");
        }
        this.porcentaje = porcentaje;
    }

    /**
     * Calcula el precio usando la fecha actual del sistema para comprobar el día especial.
     *
     * @return precio con descuento cuando hoy coincide con el día configurado;
     *         de lo contrario, retorna el precio original.
     */
    @Override
    public double calcular() {
        return calcular(LocalDate.now());
    }

    /**
     * Calcula el precio para una fecha determinada.
     * Este método permite evaluar promociones sobre una fecha distinta a la actual.
     *
     * @param fecha fecha que se desea evaluar.
     * @return precio descontado si el día coincide; en caso contrario, precio original.
     * @throws NullPointerException si la fecha es null.
     */
    public double calcular(LocalDate fecha) {
        Objects.requireNonNull(fecha, "La fecha no puede ser null.");
        if (fecha.getDayOfWeek() != dia) {
            return precio.calcular();
        }
        return Math.max(0, precio.calcular() * (1 - porcentaje / 100.0));
    }

    /**
     * Describe el día especial y el porcentaje de descuento configurado.
     *
     * @return descripción de la promoción.
     */
    @Override
    public String descripcion() {
        return precio.descripcion() + " + descuento del " + porcentaje + "% los " + dia;
    }

    /**
     * Obtiene el día de la semana en el que se activa la promoción.
     *
     * @return día especial.
     */
    public DayOfWeek getDia() {
        return dia;
    }

    /**
     * Obtiene el porcentaje de descuento configurado.
     *
     * @return porcentaje de descuento.
     */
    public double getPorcentaje() {
        return porcentaje;
    }
}
