package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.Model.Clasificacion;
import co.uniquindio.edu.poo.Model.Cliente;
import co.uniquindio.edu.poo.Model.Funcion;
import co.uniquindio.edu.poo.Model.Genero;
import co.uniquindio.edu.poo.Model.Pelicula;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador del panel principal del cliente: encabezado con saldo y puntos,
 * menú de navegación y consulta de la cartelera con sus funciones.
 */
public class ClienteDashboardController {

    @FXML
    private BorderPane mainClienteBorderPane;
    @FXML
    private Label lblSaldoTarjeta;
    @FXML
    private Label lblPuntosCliente;
    @FXML
    private Button btnNotificaciones;
    @FXML
    private Label lblNombreCliente;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private Button btnNavCartelera;
    @FXML
    private Button btnNavCompraConfiteria;
    @FXML
    private Button btnNavMonedero;
    @FXML
    private Button btnNavHistorial;

    @FXML
    private TextField txtBuscarPelicula;
    @FXML
    private ComboBox<Genero> cmbFiltroGenero;
    @FXML
    private ComboBox<Clasificacion> cmbFiltroClasificacion;
    @FXML
    private TableView<Pelicula> tblPeliculasCartelera;
    @FXML
    private TableColumn<Pelicula, String> colPeliCodigo;
    @FXML
    private TableColumn<Pelicula, String> colPeliTitulo;
    @FXML
    private TableColumn<Pelicula, String> colPeliGenero;
    @FXML
    private TableColumn<Pelicula, String> colPeliClasificacion;
    @FXML
    private TableColumn<Pelicula, String> colPeliDuracion;
    @FXML
    private TextArea txtSinopsisPelicula;
    @FXML
    private TableView<Funcion> tblFuncionesPelicula;
    @FXML
    private TableColumn<Funcion, String> colFuncId;
    @FXML
    private TableColumn<Funcion, String> colFuncFechaHora;
    @FXML
    private TableColumn<Funcion, String> colFuncSala;
    @FXML
    private TableColumn<Funcion, String> colFuncPrecio;
    @FXML
    private Button btnIrSeleccionAsientos;

    private final SesionApp sesion = SesionApp.getInstance();
    private Node contenidoCartelera;

    /**
     * Configura las tablas y filtros, carga la cartelera y el encabezado del cliente.
     */
    @FXML
    private void initialize() {
        sesion.setDashboardCliente(this);
        contenidoCartelera = mainClienteBorderPane.getCenter();

        cmbFiltroGenero.getItems().setAll(Genero.values());
        cmbFiltroClasificacion.getItems().setAll(Clasificacion.values());

        VistaUtil.columna(colPeliCodigo, Pelicula::getCodigo);
        VistaUtil.columna(colPeliTitulo, Pelicula::getTitulo);
        VistaUtil.columna(colPeliGenero, p -> p.getGenero().name());
        VistaUtil.columna(colPeliClasificacion, p -> p.getClasificacion().name());
        VistaUtil.columna(colPeliDuracion, p -> String.valueOf(p.getDuracionMin()));

        VistaUtil.columna(colFuncId, Funcion::getId);
        VistaUtil.columna(colFuncFechaHora, f -> VistaUtil.fechaHora(f.getFechaHora()));
        VistaUtil.columna(colFuncSala, f -> "Sala " + f.getSala().getNumero());
        VistaUtil.columna(colFuncPrecio, f -> VistaUtil.moneda(f.getPrecioBase()));

        tblPeliculasCartelera.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, pelicula) -> mostrarPelicula(pelicula));

        cargarCartelera(sesion.getServicioCliente().consultarCartelera());
        actualizarEncabezado();
    }

    /**
     * Actualiza el nombre, el saldo de la tarjeta, los puntos y el contador de notificaciones.
     * Lo llaman otros controladores cuando cambian esos datos.
     */
    public void actualizarEncabezado() {
        Cliente cliente = sesion.getClienteActual();
        lblNombreCliente.setText(cliente.getNombre());
        lblSaldoTarjeta.setText(VistaUtil.moneda(cliente.getTarjeta().getSaldo()));
        lblPuntosCliente.setText(sesion.getServicioCliente().consultarPuntos(cliente) + " pts");
        btnNotificaciones.setText("Notificaciones (" + cliente.getNotificaciones().size() + ")");
    }

    /**
     * Muestra una vista en el centro del panel del cliente.
     *
     * @param vista nombre de la vista
     * @param <T>   tipo del controlador de la vista
     * @return controlador de la vista mostrada
     */
    public <T> T mostrar(String vista) {
        actualizarEncabezado();
        return Navegador.mostrarEnCentro(mainClienteBorderPane, vista);
    }

    /**
     * Vuelve a mostrar la cartelera en el centro del panel.
     */
    public void mostrarCartelera() {
        mainClienteBorderPane.setCenter(contenidoCartelera);
        cargarCartelera(filtrar());
        actualizarEncabezado();
    }

    /**
     * Abre la bandeja de notificaciones, que está en el historial de compras.
     *
     * @param event evento del botón
     */
    @FXML
    private void onAbrirNotificaciones(ActionEvent event) {
        mostrar("HistorialComprasClienteView");
    }

    /**
     * Cierra la sesión y vuelve al inicio de sesión.
     *
     * @param event evento del botón
     */
    @FXML
    private void onCerrarSesion(ActionEvent event) {
        sesion.cerrarSesion();
        Navegador.cambiarVentana(btnCerrarSesion, "LoginRegistroView", "CinemaUQ");
    }

    /**
     * Muestra la cartelera y sus funciones.
     *
     * @param event evento del botón
     */
    @FXML
    private void onMostrarCartelera(ActionEvent event) {
        mostrarCartelera();
    }

    /**
     * Muestra la compra de confitería y combos.
     *
     * @param event evento del botón
     */
    @FXML
    private void onMostrarCompraConfiteria(ActionEvent event) {
        mostrar("CompraConfiteriaView");
    }

    /**
     * Muestra la tarjeta virtual, las recargas y los puntos del cliente.
     *
     * @param event evento del botón
     */
    @FXML
    private void onMostrarMonedero(ActionEvent event) {
        mostrar("MonederoPuntosClienteView");
    }

    /**
     * Muestra el historial de compras y las notificaciones.
     *
     * @param event evento del botón
     */
    @FXML
    private void onMostrarHistorial(ActionEvent event) {
        mostrar("HistorialComprasClienteView");
    }

    /**
     * Filtra la cartelera por texto, género y clasificación.
     *
     * @param event evento del botón
     */
    @FXML
    private void onFiltrarCartelera(ActionEvent event) {
        cargarCartelera(filtrar());
    }

    /**
     * Quita los filtros y muestra toda la cartelera.
     *
     * @param event evento del botón
     */
    @FXML
    private void onLimpiarFiltros(ActionEvent event) {
        txtBuscarPelicula.clear();
        cmbFiltroGenero.setValue(null);
        cmbFiltroClasificacion.setValue(null);
        cargarCartelera(sesion.getServicioCliente().consultarCartelera());
    }

    /**
     * Guarda la función seleccionada en la sesión y abre el mapa de asientos.
     *
     * @param event evento del botón
     */
    @FXML
    private void onSeleccionarAsientos(ActionEvent event) {
        Funcion funcion = tblFuncionesPelicula.getSelectionModel().getSelectedItem();
        if (funcion == null) {
            VistaUtil.advertir("Seleccione una función de la tabla de funciones programadas");
            return;
        }
        if (!funcion.estaDisponible()) {
            VistaUtil.advertir("La función seleccionada ya no está disponible");
            return;
        }
        sesion.seleccionarFuncion(funcion);
        mostrar("SeleccionAsientosView");
    }

    /**
     * Aplica los filtros escritos por el cliente sobre la cartelera.
     *
     * @return películas que cumplen los filtros
     */
    private List<Pelicula> filtrar() {
        String texto = txtBuscarPelicula.getText() == null ? "" : txtBuscarPelicula.getText().trim().toLowerCase();
        Genero genero = cmbFiltroGenero.getValue();
        Clasificacion clasificacion = cmbFiltroClasificacion.getValue();
        List<Pelicula> resultado = new ArrayList<>();
        for (Pelicula p : sesion.getServicioCliente().consultarCartelera()) {
            boolean coincideTexto = texto.isEmpty()
                    || p.getTitulo().toLowerCase().contains(texto)
                    || p.getCodigo().toLowerCase().contains(texto);
            boolean coincideGenero = genero == null || p.getGenero() == genero;
            boolean coincideClasificacion = clasificacion == null || p.getClasificacion() == clasificacion;
            if (coincideTexto && coincideGenero && coincideClasificacion) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    /**
     * Muestra las películas en la tabla y limpia la sinopsis y las funciones.
     *
     * @param peliculas películas a mostrar
     */
    private void cargarCartelera(List<Pelicula> peliculas) {
        tblPeliculasCartelera.getItems().setAll(peliculas);
        txtSinopsisPelicula.clear();
        tblFuncionesPelicula.getItems().clear();
    }

    /**
     * Muestra la sinopsis y las funciones disponibles de la película seleccionada.
     *
     * @param pelicula película seleccionada, o {@code null}
     */
    private void mostrarPelicula(Pelicula pelicula) {
        if (pelicula == null) {
            txtSinopsisPelicula.clear();
            tblFuncionesPelicula.getItems().clear();
            return;
        }
        txtSinopsisPelicula.setText(pelicula.getSinopsis());
        tblFuncionesPelicula.getItems().setAll(sesion.getServicioCliente().consultarFunciones(pelicula));
    }
}
