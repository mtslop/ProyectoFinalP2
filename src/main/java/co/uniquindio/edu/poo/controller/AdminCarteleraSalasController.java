package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.Model.CinemaUQ;
import co.uniquindio.edu.poo.Model.Clasificacion;
import co.uniquindio.edu.poo.Model.Funcion;
import co.uniquindio.edu.poo.Model.Genero;
import co.uniquindio.edu.poo.Model.Pelicula;
import co.uniquindio.edu.poo.Model.Sala;
import co.uniquindio.edu.poo.Model.TipoAsiento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

/**
 * Controlador de la gestión de películas, salas con sus asientos y funciones,
 * incluida la cancelación de funciones por parte del cine.
 */
public class AdminCarteleraSalasController {

    @FXML
    private TextField txtPeliCodigo;
    @FXML
    private TextField txtPeliTitulo;
    @FXML
    private TextField txtPeliDuracion;
    @FXML
    private ComboBox<Genero> cmbPeliGenero;
    @FXML
    private ComboBox<Clasificacion> cmbPeliClasificacion;
    @FXML
    private TextArea txtPeliSinopsis;
    @FXML
    private CheckBox chkPeliEnCartelera;
    @FXML
    private TableView<Pelicula> tblAdminPeliculas;
    @FXML
    private TableColumn<Pelicula, String> colAdmPeliCod;
    @FXML
    private TableColumn<Pelicula, String> colAdmPeliTit;
    @FXML
    private TableColumn<Pelicula, String> colAdmPeliGen;
    @FXML
    private TableColumn<Pelicula, String> colAdmPeliClas;
    @FXML
    private TableColumn<Pelicula, String> colAdmPeliDur;
    @FXML
    private TableColumn<Pelicula, String> colAdmPeliActiva;

    @FXML
    private TextField txtSalaNumero;
    @FXML
    private TextField txtSalaFilas;
    @FXML
    private TextField txtSalaAsientosPorFila;
    @FXML
    private ComboBox<TipoAsiento> cmbTipoAsientoDefecto;
    @FXML
    private TableView<Sala> tblAdminSalas;
    @FXML
    private TableColumn<Sala, String> colAdmSalaNum;
    @FXML
    private TableColumn<Sala, String> colAdmSalaCap;
    @FXML
    private TableColumn<Sala, String> colAdmSalaAsientos;

    @FXML
    private TextField txtFuncionId;
    @FXML
    private ComboBox<Pelicula> cmbFuncionPelicula;
    @FXML
    private ComboBox<Sala> cmbFuncionSala;
    @FXML
    private DatePicker dpFuncionFecha;
    @FXML
    private TextField txtFuncionHora;
    @FXML
    private TextField txtFuncionPrecioBase;
    @FXML
    private TableView<Funcion> tblAdminFunciones;
    @FXML
    private TableColumn<Funcion, String> colAdmFuncId;
    @FXML
    private TableColumn<Funcion, String> colAdmFuncPeli;
    @FXML
    private TableColumn<Funcion, String> colAdmFuncSala;
    @FXML
    private TableColumn<Funcion, String> colAdmFuncFecha;
    @FXML
    private TableColumn<Funcion, String> colAdmFuncOcup;
    @FXML
    private TableColumn<Funcion, String> colAdmFuncEstado;

    private final SesionApp sesion = SesionApp.getInstance();
    private final CinemaUQ cinema = sesion.getCinema();

    /**
     * Llena las listas desplegables, configura las tablas y carga los datos.
     */
    @FXML
    private void initialize() {
        cmbPeliGenero.getItems().setAll(Genero.values());
        cmbPeliClasificacion.getItems().setAll(Clasificacion.values());
        cmbTipoAsientoDefecto.getItems().setAll(TipoAsiento.values());
        cmbTipoAsientoDefecto.setValue(TipoAsiento.GENERAL);
        cmbFuncionPelicula.setConverter(convertidor(Pelicula::getTitulo));
        cmbFuncionSala.setConverter(convertidor(s -> "Sala " + s.getNumero()));

        VistaUtil.columna(colAdmPeliCod, Pelicula::getCodigo);
        VistaUtil.columna(colAdmPeliTit, Pelicula::getTitulo);
        VistaUtil.columna(colAdmPeliGen, p -> p.getGenero().name());
        VistaUtil.columna(colAdmPeliClas, p -> p.getClasificacion().name());
        VistaUtil.columna(colAdmPeliDur, p -> String.valueOf(p.getDuracionMin()));
        VistaUtil.columna(colAdmPeliActiva, p -> p.isEnCartelera() ? "Sí" : "No");

        VistaUtil.columna(colAdmSalaNum, s -> String.valueOf(s.getNumero()));
        VistaUtil.columna(colAdmSalaCap, s -> String.valueOf(s.getCapacidad()));
        VistaUtil.columna(colAdmSalaAsientos, s -> String.valueOf(s.getAsientos().size()));

        VistaUtil.columna(colAdmFuncId, Funcion::getId);
        VistaUtil.columna(colAdmFuncPeli, f -> f.getPelicula().getTitulo());
        VistaUtil.columna(colAdmFuncSala, f -> String.valueOf(f.getSala().getNumero()));
        VistaUtil.columna(colAdmFuncFecha, f -> VistaUtil.fechaHora(f.getFechaHora()));
        VistaUtil.columna(colAdmFuncOcup, f -> String.format("%.1f %%", f.ocupacion()));
        VistaUtil.columna(colAdmFuncEstado, f -> f.estaCancelada() ? "Sí" : "No");

        cargarDatos();
    }

    /**
     * Registra una película nueva en el catálogo.
     *
     * @param event evento del botón
     */
    @FXML
    private void onRegistrarPelicula(ActionEvent event) {
        try {
            String codigo = VistaUtil.requerido(txtPeliCodigo.getText(), "código");
            for (Pelicula p : cinema.getPeliculas()) {
                if (p.getCodigo().equalsIgnoreCase(codigo)) {
                    throw new IllegalArgumentException("Ya existe una película con el código " + codigo);
                }
            }
            if (cmbPeliGenero.getValue() == null || cmbPeliClasificacion.getValue() == null) {
                throw new IllegalArgumentException("Seleccione el género y la clasificación");
            }
            Pelicula pelicula = new Pelicula(codigo,
                    VistaUtil.requerido(txtPeliTitulo.getText(), "título"),
                    VistaUtil.leerEntero(txtPeliDuracion.getText(), "duración"),
                    txtPeliSinopsis.getText(),
                    cmbPeliGenero.getValue(),
                    cmbPeliClasificacion.getValue(),
                    chkPeliEnCartelera.isSelected());
            sesion.getServicioAdmin().registrarPelicula(pelicula);
            VistaUtil.informar("Película registrada", pelicula.getTitulo() + " quedó registrada.");
            txtPeliCodigo.clear();
            txtPeliTitulo.clear();
            txtPeliDuracion.clear();
            txtPeliSinopsis.clear();
            cargarDatos();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Registra una sala y genera sus asientos por filas.
     *
     * @param event evento del botón
     */
    @FXML
    private void onRegistrarSala(ActionEvent event) {
        try {
            int numero = VistaUtil.leerEntero(txtSalaNumero.getText(), "número de sala");
            for (Sala s : cinema.getSalas()) {
                if (s.getNumero() == numero) {
                    throw new IllegalArgumentException("Ya existe la sala " + numero);
                }
            }
            TipoAsiento tipo = cmbTipoAsientoDefecto.getValue() != null ? cmbTipoAsientoDefecto.getValue() : TipoAsiento.GENERAL;
            Sala sala = DatosIniciales.crearSala(numero,
                    VistaUtil.leerEntero(txtSalaFilas.getText(), "cantidad de filas"),
                    VistaUtil.leerEntero(txtSalaAsientosPorFila.getText(), "asientos por fila"),
                    tipo);
            sesion.getServicioAdmin().registrarSala(sala);
            VistaUtil.informar("Sala registrada", "Sala " + numero + " con " + sala.getAsientos().size() + " asientos.");
            txtSalaNumero.clear();
            txtSalaFilas.clear();
            txtSalaAsientosPorFila.clear();
            cargarDatos();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Programa una función verificando que la sala no tenga otra función a la misma hora.
     *
     * @param event evento del botón
     */
    @FXML
    private void onRegistrarFuncion(ActionEvent event) {
        try {
            String id = VistaUtil.requerido(txtFuncionId.getText(), "ID de la función");
            for (Funcion f : cinema.getFunciones()) {
                if (f.getId().equalsIgnoreCase(id)) {
                    throw new IllegalArgumentException("Ya existe una función con el ID " + id);
                }
            }
            Pelicula pelicula = cmbFuncionPelicula.getValue();
            Sala sala = cmbFuncionSala.getValue();
            LocalDate fecha = dpFuncionFecha.getValue();
            if (pelicula == null || sala == null || fecha == null) {
                throw new IllegalArgumentException("Seleccione la película, la sala y la fecha");
            }
            LocalDateTime fechaHora = LocalDateTime.of(fecha, leerHora(txtFuncionHora.getText()));
            if (!fechaHora.isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException("La función debe programarse en una fecha y hora futuras");
            }
            validarSalaLibre(sala, fechaHora, pelicula.getDuracionMin());
            Funcion funcion = new Funcion(id, fechaHora,
                    VistaUtil.leerDecimal(txtFuncionPrecioBase.getText(), "precio base"), pelicula, sala);
            sesion.getServicioAdmin().registrarFuncion(funcion);
            VistaUtil.informar("Función programada", pelicula.getTitulo() + " - " + VistaUtil.fechaHora(fechaHora));
            txtFuncionId.clear();
            txtFuncionHora.clear();
            txtFuncionPrecioBase.clear();
            cargarDatos();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Cancela la función seleccionada: reembolsa el 100 % a los compradores
     * y les envía una notificación (patrón Observer).
     *
     * @param event evento del botón
     */
    @FXML
    private void onCancelarFuncionPorCine(ActionEvent event) {
        Funcion funcion = tblAdminFunciones.getSelectionModel().getSelectedItem();
        if (funcion == null) {
            VistaUtil.advertir("Seleccione una función de la tabla");
            return;
        }
        if (funcion.estaCancelada()) {
            VistaUtil.advertir("La función " + funcion.getId() + " ya está cancelada");
            return;
        }
        if (!VistaUtil.confirmar("¿Cancelar la función " + funcion.getId() + " de " + funcion.getPelicula().getTitulo()
                + "? Se reembolsará el 100 % a todos los compradores.")) {
            return;
        }
        try {
            sesion.getServicioAdmin().cancelarFuncion(funcion);
            VistaUtil.informar("Función cancelada", "Se reembolsaron las compras y se notificó a los clientes.");
            cargarDatos();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Verifica que la sala no tenga otra función que se cruce con el horario nuevo.
     *
     * @param sala        sala de la función
     * @param inicio      fecha y hora de inicio de la función nueva
     * @param duracionMin duración de la película en minutos
     * @throws IllegalArgumentException si el horario se cruza con otra función
     */
    private void validarSalaLibre(Sala sala, LocalDateTime inicio, int duracionMin) {
        LocalDateTime fin = inicio.plusMinutes(duracionMin);
        for (Funcion f : cinema.getFunciones()) {
            if (f.getSala() != sala || f.estaCancelada()) {
                continue;
            }
            LocalDateTime otraInicio = f.getFechaHora();
            LocalDateTime otraFin = otraInicio.plusMinutes(f.getPelicula().getDuracionMin());
            if (inicio.isBefore(otraFin) && otraInicio.isBefore(fin)) {
                throw new IllegalArgumentException("La sala " + sala.getNumero() + " ya tiene la función "
                        + f.getId() + " en ese horario");
            }
        }
    }

    /**
     * Convierte el texto de la hora con formato HH:mm.
     *
     * @param texto hora escrita por el administrador
     * @return hora leída
     * @throws IllegalArgumentException si el formato no es válido
     */
    private LocalTime leerHora(String texto) {
        try {
            return LocalTime.parse(VistaUtil.requerido(texto, "hora"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("La hora debe tener el formato HH:mm, por ejemplo 19:30");
        }
    }

    /**
     * Recarga las tablas y las listas de películas y salas.
     */
    private void cargarDatos() {
        tblAdminPeliculas.getItems().setAll(cinema.getPeliculas());
        tblAdminSalas.getItems().setAll(cinema.getSalas());
        tblAdminFunciones.getItems().setAll(cinema.getFunciones());
        cmbFuncionPelicula.getItems().setAll(cinema.getPeliculas());
        cmbFuncionSala.getItems().setAll(cinema.getSalas());
    }

    /**
     * Crea un convertidor para mostrar objetos en una lista desplegable.
     *
     * @param texto función que obtiene el texto a mostrar
     * @param <T>   tipo de los objetos
     * @return convertidor de la lista desplegable
     */
    static <T> StringConverter<T> convertidor(Function<T, String> texto) {
        return new StringConverter<>() {
            @Override
            public String toString(T objeto) {
                return objeto == null ? "" : texto.apply(objeto);
            }

            @Override
            public T fromString(String cadena) {
                return null;
            }
        };
    }
}
