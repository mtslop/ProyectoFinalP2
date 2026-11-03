package co.uniquindio.edu.poo;

import co.uniquindio.edu.poo.Model.Asiento;
import co.uniquindio.edu.poo.Model.Clasificacion;
import co.uniquindio.edu.poo.Model.Combo;
import co.uniquindio.edu.poo.Model.Funcion;
import co.uniquindio.edu.poo.Model.Genero;
import co.uniquindio.edu.poo.Model.Pelicula;
import co.uniquindio.edu.poo.Model.Producto;
import co.uniquindio.edu.poo.Model.Promocion;
import co.uniquindio.edu.poo.Model.Sala;
import co.uniquindio.edu.poo.Model.TipoAsiento;
import co.uniquindio.edu.poo.Model.TipoPromocion;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Punto de entrada del módulo de cartelera, confitería y promociones.
 */
public class Main {

    /**
     * Ejecuta una demostración básica de las clases del módulo.
     *
     * @param args argumentos de la aplicación.
     */
    public static void main(String[] args) {
        Asiento asientoA1 = new Asiento("A", 1, TipoAsiento.GENERAL);
        Asiento asientoA2 = new Asiento("A", 2, TipoAsiento.PREFERENCIAL);
        Sala sala = new Sala(1, List.of(asientoA1, asientoA2));

        Pelicula pelicula = new Pelicula("P001", "Interestelar", 169,
                "Un grupo de exploradores viaja a través de un agujero de gusano.",
                Genero.FICCION, Clasificacion.MAYORES_12, true);

        Funcion funcion = new Funcion("F001", LocalDateTime.now().plusHours(3), 15000,
                pelicula, sala);

        System.out.println("Película: " + pelicula.getTitulo());
        System.out.println("Sala: " + sala.getNumero());
        System.out.println("Función disponible: " + funcion.estaDisponible());
        System.out.println("Asiento " + asientoA1.codigo() + " ocupado: "
                + funcion.ocuparAsiento(asientoA1));
        System.out.println("Ocupación de la sala: " + funcion.ocupacion() + "%");

        Producto palomitas = new Producto("C001", "Palomitas grandes", 12000, 10);
        Producto bebida = new Producto("C002", "Bebida", 6000, 5);
        Combo combo = new Combo("K001", "Combo pareja", 10, 20);
        combo.agregar(palomitas);
        combo.agregar(bebida);

        System.out.println("Combo: " + combo.nombre() + " - Precio: " + combo.precio());
        System.out.println("Puntos del combo: " + combo.puntosRedencion());

        Promocion promocion = new Promocion("Martes de cine", TipoPromocion.PORCENTAJE, 25,
                DayOfWeek.TUESDAY, LocalDate.now().minusDays(1), LocalDate.now().plusDays(7));
        System.out.println("Promoción vigente: " + promocion.estaVigente());
        System.out.println("Monto con descuento: " + promocion.aplicar(20000));
    }
}
