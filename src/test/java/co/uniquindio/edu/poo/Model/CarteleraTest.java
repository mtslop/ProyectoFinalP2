package co.uniquindio.edu.poo.Model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de las clases de cartelera, confitería y promociones.
 */
public class CarteleraTest {

    /**
     * Instancia para el manejo de logs.
     */
    private static final Logger LOG = Logger.getLogger(CarteleraTest.class.getName());

    /**
     * Compara el precio calculado de un combo con el valor esperado.
     */
    @Test
    public void testCompararPrecioCombo() {
        LOG.info("Inicio de prueba testCompararPrecioCombo...");
        Producto crispetas = new Producto("P01", "Crispetas", 10000, 5);
        Producto gaseosa = new Producto("P02", "Gaseosa", 5000, 3);
        Combo combo = new Combo("C01", "Combo pareja", 10, 8);
        combo.agregar(crispetas);
        combo.agregar(gaseosa);
        assertEquals(13500.0, combo.precio(), 0.001,
                "El combo debe aplicar el 10% de descuento sobre 15000.");
        LOG.info("Fin de prueba testCompararPrecioCombo...");
    }

    /**
     * Comprueba que un asiento se ocupa y que la ocupación de la sala cambia.
     */
    @Test
    public void testComprobarOcupacionDeAsiento() {
        LOG.info("Inicio de prueba testComprobarOcupacionDeAsiento...");
        Pelicula pelicula = new Pelicula("P01", "Matrix", 120, "Sinopsis",
                Genero.FICCION, Clasificacion.MAYORES_12, true);
        Sala sala = new Sala(1, 2);
        Asiento asiento = new Asiento("A", 1, TipoAsiento.GENERAL);
        sala.agregarAsiento(asiento);
        Funcion funcion = new Funcion("F01", LocalDateTime.now().plusDays(1),
                15000, pelicula, sala);
        assertTrue(funcion.ocuparAsiento(asiento), "El asiento debe ocuparse.");
        assertEquals(50.0, funcion.ocupacion(), 0.001,
                "La ocupación debe ser del 50% al ocupar 1 de 2 asientos.");
        LOG.info("Fin de prueba testComprobarOcupacionDeAsiento...");
    }

    /**
     * Verifica que una promoción con fechas inválidas lanza una excepción.
     */
    @Test
    public void testPromocionConFechasInvalidas() {
        LOG.info("Inicio de prueba testPromocionConFechasInvalidas...");
        LocalDate hoy = LocalDate.now();
        assertThrows(IllegalArgumentException.class, () -> new Promocion(
                "Promoción inválida", TipoPromocion.PORCENTAJE, 10,
                DayOfWeek.MONDAY, hoy, hoy.minusDays(1)),
                "La fecha de fin no puede ser anterior a la fecha de inicio.");
        LOG.info("Fin de prueba testPromocionConFechasInvalidas...");
    }
}
