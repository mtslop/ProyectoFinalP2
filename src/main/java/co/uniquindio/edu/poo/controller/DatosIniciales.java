package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.Model.Asiento;
import co.uniquindio.edu.poo.Model.CinemaUQ;
import co.uniquindio.edu.poo.Model.Clasificacion;
import co.uniquindio.edu.poo.Model.Cliente;
import co.uniquindio.edu.poo.Model.ClienteFactory;
import co.uniquindio.edu.poo.Model.Combo;
import co.uniquindio.edu.poo.Model.Compra;
import co.uniquindio.edu.poo.Model.CompraFacade;
import co.uniquindio.edu.poo.Model.Funcion;
import co.uniquindio.edu.poo.Model.Genero;
import co.uniquindio.edu.poo.Model.NotificacionCentro;
import co.uniquindio.edu.poo.Model.Pelicula;
import co.uniquindio.edu.poo.Model.Producto;
import co.uniquindio.edu.poo.Model.Promocion;
import co.uniquindio.edu.poo.Model.Sala;
import co.uniquindio.edu.poo.Model.SolicitudRecarga;
import co.uniquindio.edu.poo.Model.TipoAsiento;
import co.uniquindio.edu.poo.Model.TipoPromocion;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Datos iniciales para probar la aplicación: películas, salas con asientos, funciones,
 * confitería, promociones, clientes con saldo y una compra ya pagada.
 * <p>
 * Usuarios de prueba: {@code admin@cinemauq.com / admin123} y {@code juan@correo.com / 1234}.
 */
public final class DatosIniciales {

    /**
     * Evita que se creen objetos de esta clase de utilidades.
     */
    private DatosIniciales() {
    }

    /**
     * Carga los datos de prueba si el cine todavía no tiene películas.
     *
     * @param cinema      cine donde se guardan los datos
     * @param facade      fachada de compras, para registrar una compra de ejemplo
     * @param notificador centro de notificaciones, para suscribir al cliente de la compra de ejemplo
     */
    public static void cargar(CinemaUQ cinema, CompraFacade facade, NotificacionCentro notificador) {
        if (!cinema.getPeliculas().isEmpty()) {
            return;
        }
        cinema.cargarDatosIniciales();

        Pelicula horizonte = new Pelicula("P001", "Horizonte Perdido", 128,
                "Una piloto queda atrapada en una tormenta que la lleva a una isla que no aparece en los mapas.",
                Genero.ACCION, Clasificacion.MAYORES_12, true);
        Pelicula eco = new Pelicula("P002", "La Casa del Eco", 102,
                "Una familia se muda a una casa donde cada palabra que se dice se repite al día siguiente.",
                Genero.TERROR, Clasificacion.MAYORES_15, true);
        Pelicula risas = new Pelicula("P003", "Risas en el Andén", 95,
                "Dos vendedores ambulantes compiten por el mejor puesto de la estación del tren.",
                Genero.COMEDIA, Clasificacion.TODO_PUBLICO, true);
        Pelicula gigantes = new Pelicula("P004", "Pequeños Gigantes", 88,
                "Un grupo de hormigas emprende un viaje para salvar su colonia de la sequía.",
                Genero.ANIMACION, Clasificacion.TODO_PUBLICO, true);
        Pelicula verano = new Pelicula("P005", "El Último Verano", 110,
                "Tres amigos se reencuentran en el pueblo donde crecieron. Próximo estreno.",
                Genero.DRAMA, Clasificacion.MAYORES_7, false);
        cinema.getPeliculas().add(horizonte);
        cinema.getPeliculas().add(eco);
        cinema.getPeliculas().add(risas);
        cinema.getPeliculas().add(gigantes);
        cinema.getPeliculas().add(verano);

        Sala sala1 = crearSala(1, 6, 10, TipoAsiento.GENERAL);
        Sala sala2 = crearSala(2, 5, 8, TipoAsiento.GENERAL);
        Sala sala3 = crearSala(3, 4, 8, TipoAsiento.GENERAL);
        cinema.getSalas().add(sala1);
        cinema.getSalas().add(sala2);
        cinema.getSalas().add(sala3);

        LocalDate hoy = LocalDate.now();
        Funcion f1 = new Funcion("F001", LocalDateTime.of(hoy.plusDays(1), LocalTime.of(19, 0)), 12000, horizonte, sala1);
        Funcion f2 = new Funcion("F002", LocalDateTime.now().plusHours(5).truncatedTo(ChronoUnit.HOURS), 10000, eco, sala2);
        Funcion f3 = new Funcion("F003", LocalDateTime.of(hoy.plusDays(3), LocalTime.of(16, 30)), 14000, horizonte, sala3);
        Funcion f4 = new Funcion("F004", LocalDateTime.of(hoy.plusDays(2), LocalTime.of(21, 0)), 11000, risas, sala1);
        Funcion f5 = new Funcion("F005", LocalDateTime.of(hoy.plusDays(1), LocalTime.of(15, 0)), 9000, gigantes, sala2);
        cinema.getFunciones().add(f1);
        cinema.getFunciones().add(f2);
        cinema.getFunciones().add(f3);
        cinema.getFunciones().add(f4);
        cinema.getFunciones().add(f5);

        Producto crispetas = new Producto("C001", "Crispetas medianas", 9000, 50);
        Producto gaseosa = new Producto("C002", "Gaseosa 16 oz", 6000, 30);
        Producto perro = new Producto("C003", "Perro caliente", 11000, 60);
        Producto nachos = new Producto("C004", "Nachos con queso", 12000, 60);
        Producto chocolatina = new Producto("C005", "Chocolatina", 4000, 20);
        Combo comboPareja = new Combo("K001", "Combo Pareja", 15, 120);
        comboPareja.agregar(crispetas);
        comboPareja.agregar(gaseosa);
        comboPareja.agregar(gaseosa);
        Combo comboIndividual = new Combo("K002", "Combo Individual", 10, 80);
        comboIndividual.agregar(crispetas);
        comboIndividual.agregar(gaseosa);
        cinema.getProductos().add(crispetas);
        cinema.getProductos().add(gaseosa);
        cinema.getProductos().add(perro);
        cinema.getProductos().add(nachos);
        cinema.getProductos().add(chocolatina);
        cinema.getProductos().add(comboPareja);
        cinema.getProductos().add(comboIndividual);

        cinema.getPromociones().add(new Promocion("Descuento Estudiantes UQ", TipoPromocion.PORCENTAJE,
                10, null, hoy.minusDays(7), hoy.plusDays(60)));
        cinema.getPromociones().add(new Promocion("Bono de $5.000", TipoPromocion.VALORFIJO,
                5000, null, hoy.minusDays(7), hoy.plusDays(30)));
        cinema.getPromociones().add(new Promocion("Miércoles de Cine", TipoPromocion.DIA_ESPECIAL,
                25, DayOfWeek.WEDNESDAY, hoy.minusDays(7), hoy.plusDays(90)));

        Cliente juan = (Cliente) cinema.buscarPersonaPorCorreo("juan@correo.com");
        Cliente maria = (Cliente) new ClienteFactory().crearPersona("1003", "Maria Gomez",
                "maria@correo.com", "1234", "3200000000");
        cinema.agregarPersona(maria);
        if (juan != null) {
            juan.getTarjeta().recargar(150000);
            juan.getCuentaPuntos().acumularCantidad(30);
            Compra compra = new Compra("CMP-0001")
                    .paraCliente(juan)
                    .paraFuncion(f1)
                    .agregarAsiento(sala1.buscarAsiento("C", 5), f1.getPrecioBase())
                    .agregarAsiento(sala1.buscarAsiento("C", 6), f1.getPrecioBase())
                    .agregarItem(comboPareja, 1);
            compra.setTarjeta(juan.getTarjeta());
            facade.realizarCompra(compra);
            notificador.suscribir(f1, juan);
        }
        maria.getTarjeta().recargar(40000);
        cinema.getSolicitudes().add(new SolicitudRecarga("SOL-1", maria, 50000));
    }

    /**
     * Crea una sala y genera sus asientos por filas (A, B, C...).
     * Los dos primeros asientos de la fila A son accesibles y la última fila es preferencial.
     *
     * @param numero          número de la sala
     * @param filas           cantidad de filas
     * @param asientosPorFila asientos en cada fila
     * @param tipo            tipo de asiento para el resto de la sala
     * @return sala con todos sus asientos creados
     * @throws IllegalArgumentException si los datos de la sala no son válidos
     */
    public static Sala crearSala(int numero, int filas, int asientosPorFila, TipoAsiento tipo) {
        if (filas <= 0 || filas > 26) {
            throw new IllegalArgumentException("La cantidad de filas debe estar entre 1 y 26");
        }
        if (asientosPorFila <= 0) {
            throw new IllegalArgumentException("Los asientos por fila deben ser mayores que cero");
        }
        Sala sala = new Sala(numero, filas * asientosPorFila);
        for (int f = 0; f < filas; f++) {
            String fila = String.valueOf((char) ('A' + f));
            for (int n = 1; n <= asientosPorFila; n++) {
                TipoAsiento tipoAsiento = tipo;
                if (f == 0 && n <= 2) {
                    tipoAsiento = TipoAsiento.ACCESIBLE;
                } else if (f == filas - 1 && filas > 1) {
                    tipoAsiento = TipoAsiento.PREFERENCIAL;
                }
                sala.agregarAsiento(new Asiento(fila, n, tipoAsiento));
            }
        }
        return sala;
    }
}
