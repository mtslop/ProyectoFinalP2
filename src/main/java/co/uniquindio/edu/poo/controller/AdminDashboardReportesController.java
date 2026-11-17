package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.Model.Cliente;
import co.uniquindio.edu.poo.Model.Compra;
import co.uniquindio.edu.poo.Model.DetalleConfiteria;
import co.uniquindio.edu.poo.Model.Entrada;
import co.uniquindio.edu.poo.Model.Funcion;
import co.uniquindio.edu.poo.Model.IServicioAdmin;
import co.uniquindio.edu.poo.Model.Movimiento;
import co.uniquindio.edu.poo.Model.ReporteOperativo;
import co.uniquindio.edu.poo.Model.Sala;
import co.uniquindio.edu.poo.Model.TipoMovimiento;
import co.uniquindio.edu.poo.Model.TipoReporte;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Controlador del panel del administrador: menú de módulos, indicadores, gráficas
 * y reportes operativos (Template Method) con exportación a TXT y PDF.
 * Todas las operaciones pasan por el proxy de administración (patrón Proxy).
 */
public class AdminDashboardReportesController {

    @FXML
    private BorderPane adminMainBorderPane;
    @FXML
    private Label lblAdminActivo;
    @FXML
    private Label lblKpiEntradas;
    @FXML
    private Label lblKpiIngresos;
    @FXML
    private Label lblKpiOcupacion;
    @FXML
    private Label lblKpiReembolsos;
    @FXML
    private ComboBox<TipoReporte> cmbTipoReporte;
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaFin;
    @FXML
    private BarChart<String, Number> barChartVentasPelicula;
    @FXML
    private PieChart pieChartOcupacionSalas;
    @FXML
    private LineChart<String, Number> lineChartComportamientoTiempo;
    @FXML
    private Label lblTituloReporteGenerado;
    @FXML
    private Label lblFechaGeneracionReporte;
    @FXML
    private Label lblTotalRegistrosReporte;
    @FXML
    private TextArea txtVistaPreviaReporte;

    private final SesionApp sesion = SesionApp.getInstance();
    private Node contenidoDashboard;
    private ReporteOperativo reporteActual;

    /**
     * Guarda el contenido del dashboard, llena los filtros y calcula indicadores y gráficas.
     */
    @FXML
    private void initialize() {
        contenidoDashboard = adminMainBorderPane.getCenter();
        lblAdminActivo.setText(sesion.getPersonaActual() != null ? sesion.getPersonaActual().getNombre() : "Administrador");
        cmbTipoReporte.getItems().setAll(TipoReporte.values());
        cmbTipoReporte.setValue(TipoReporte.VENTAS);
        dpFechaInicio.setValue(LocalDate.now().minusDays(30));
        dpFechaFin.setValue(LocalDate.now().plusDays(30));
        barChartVentasPelicula.setAnimated(false);
        lineChartComportamientoTiempo.setAnimated(false);
        try {
            actualizarIndicadores();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Cierra la sesión del administrador y vuelve al inicio de sesión.
     *
     * @param event evento del botón
     */
    @FXML
    private void onCerrarSesionAdmin(ActionEvent event) {
        sesion.cerrarSesion();
        Navegador.cambiarVentana(lblAdminActivo, "LoginRegistroView", "CinemaUQ");
    }

    /**
     * Vuelve a mostrar el dashboard y recalcula los indicadores.
     *
     * @param event evento del botón
     */
    @FXML
    private void onMostrarDashboardReportes(ActionEvent event) {
        adminMainBorderPane.setCenter(contenidoDashboard);
        try {
            actualizarIndicadores();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Muestra la gestión de películas, salas y funciones.
     *
     * @param event evento del botón
     */
    @FXML
    private void onMostrarGestionCartelera(ActionEvent event) {
        Navegador.mostrarEnCentro(adminMainBorderPane, "AdminCarteleraSalasView");
    }

    /**
     * Muestra la gestión de confitería, combos y promociones.
     *
     * @param event evento del botón
     */
    @FXML
    private void onMostrarGestionConfiteria(ActionEvent event) {
        Navegador.mostrarEnCentro(adminMainBorderPane, "AdminConfiteriaPromocionesView");
    }

    /**
     * Muestra la gestión de recargas, clientes y compras.
     *
     * @param event evento del botón
     */
    @FXML
    private void onMostrarGestionClientesRecargas(ActionEvent event) {
        Navegador.mostrarEnCentro(adminMainBorderPane, "AdminClientesRecargasView");
    }

    /**
     * Muestra el registro de clientes y administradores.
     *
     * @param event evento del botón
     */
    @FXML
    private void onMostrarRegistroUsuarios(ActionEvent event) {
        Navegador.mostrarEnCentro(adminMainBorderPane, "AdminRegistroUsuariosView");
    }

    /**
     * Genera el reporte del tipo y periodo seleccionados con {@code ReporteFactory}
     * y muestra su vista previa.
     *
     * @param event evento del botón
     */
    @FXML
    private void onGenerarReporte(ActionEvent event) {
        try {
            TipoReporte tipo = cmbTipoReporte.getValue();
            if (tipo == null) {
                VistaUtil.advertir("Seleccione el tipo de reporte");
                return;
            }
            LocalDate inicio = fechaInicio();
            LocalDate fin = fechaFin();
            reporteActual = admin().generarReporte(tipo, inicio, fin);
            lblTituloReporteGenerado.setText("Titulo: " + reporteActual.getTitulo());
            lblFechaGeneracionReporte.setText("Fecha de Generacion: "
                    + VistaUtil.fechaHora(reporteActual.getFechaGeneracion()));
            lblTotalRegistrosReporte.setText("Total Registros: " + reporteActual.totalRegistros());
            txtVistaPreviaReporte.setText(String.join("\n", reporteActual.generarLineas()));
            actualizarIndicadores();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Exporta el reporte generado a un archivo de texto.
     *
     * @param event evento del botón
     */
    @FXML
    private void onExportarTxt(ActionEvent event) {
        File archivo = elegirArchivo("Archivo de texto", "*.txt", ".txt");
        if (archivo == null) {
            return;
        }
        try {
            admin().exportarReporte(reporteActual, archivo.getAbsolutePath());
            VistaUtil.informar("Reporte exportado", "Se guardó el archivo " + archivo.getName());
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Exporta el reporte generado a un archivo PDF.
     *
     * @param event evento del botón
     */
    @FXML
    private void onExportarPdf(ActionEvent event) {
        File archivo = elegirArchivo("Documento PDF", "*.pdf", ".pdf");
        if (archivo == null) {
            return;
        }
        try {
            ExportadorPdf.exportar(reporteActual.generarLineas(), archivo.toPath());
            VistaUtil.informar("Reporte exportado", "Se guardó el archivo " + archivo.getName());
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Pide al administrador dónde guardar el reporte.
     *
     * @param descripcion descripción del tipo de archivo
     * @param patron      patrón de la extensión, por ejemplo {@code *.txt}
     * @param extension   extensión del archivo, por ejemplo {@code .txt}
     * @return archivo elegido, o {@code null} si se canceló o no hay reporte generado
     */
    private File elegirArchivo(String descripcion, String patron, String extension) {
        if (reporteActual == null) {
            VistaUtil.advertir("Primero genere un reporte");
            return null;
        }
        FileChooser selector = new FileChooser();
        selector.setTitle("Guardar reporte");
        selector.setInitialFileName(cmbTipoReporte.getValue().name().toLowerCase() + "_" + LocalDate.now() + extension);
        selector.getExtensionFilters().add(new FileChooser.ExtensionFilter(descripcion, patron));
        return selector.showSaveDialog(adminMainBorderPane.getScene().getWindow());
    }

    /**
     * Calcula los indicadores y las gráficas con las compras del periodo seleccionado.
     */
    private void actualizarIndicadores() {
        List<Compra> compras = admin().consultarCompras(fechaInicio(), fechaFin());
        int entradas = 0;
        int reembolsos = 0;
        double ingresos = 0;
        Map<String, Double> ventasPorCategoria = new TreeMap<>();
        Map<LocalDate, Double> ventasPorDia = new TreeMap<>();
        for (Compra compra : compras) {
            String estado = compra.getEstado().nombre();
            if ("REEMBOLSADA".equals(estado)) {
                reembolsos++;
            }
            if (!"PAGADA".equals(estado)) {
                continue;
            }
            entradas += compra.getEntradas().size();
            ingresos += compra.getTotal();
            ventasPorDia.merge(compra.getFecha().toLocalDate(), compra.getTotal(), Double::sum);
            for (Entrada entrada : compra.getEntradas()) {
                ventasPorCategoria.merge(entrada.getFuncion().getPelicula().getTitulo(), entrada.getPrecio(), Double::sum);
            }
            for (DetalleConfiteria detalle : compra.getDetallesConfiteria()) {
                ventasPorCategoria.merge("Confitería", detalle.subtotal(), Double::sum);
            }
        }
        lblKpiEntradas.setText(String.valueOf(entradas));
        lblKpiIngresos.setText(VistaUtil.moneda(ingresos));
        lblKpiReembolsos.setText(String.valueOf(reembolsos));
        lblKpiOcupacion.setText(String.format("%.1f %%", ocupacionPromedio()));

        XYChart.Series<String, Number> serieVentas = new XYChart.Series<>();
        serieVentas.setName("Ventas brutas ($)");
        ventasPorCategoria.forEach((categoria, valor) -> serieVentas.getData().add(new XYChart.Data<>(categoria, valor)));
        barChartVentasPelicula.getData().setAll(List.of(serieVentas));

        actualizarGraficaOcupacion();
        actualizarGraficaTiempo(ventasPorDia);
    }

    /**
     * Muestra en la gráfica de torta la ocupación promedio de cada sala.
     */
    private void actualizarGraficaOcupacion() {
        pieChartOcupacionSalas.getData().clear();
        for (Sala sala : sesion.getCinema().getSalas()) {
            double total = 0;
            int funciones = 0;
            for (Funcion funcion : sesion.getCinema().getFunciones()) {
                if (funcion.getSala() == sala && !funcion.estaCancelada()) {
                    total += funcion.ocupacion();
                    funciones++;
                }
            }
            double promedio = funciones == 0 ? 0 : total / funciones;
            if (promedio > 0) {
                pieChartOcupacionSalas.getData().add(new PieChart.Data(
                        String.format("Sala %d (%.1f %%)", sala.getNumero(), promedio), promedio));
            }
        }
        if (pieChartOcupacionSalas.getData().isEmpty()) {
            pieChartOcupacionSalas.getData().add(new PieChart.Data("Sin asientos ocupados", 1));
        }
    }

    /**
     * Muestra en la gráfica de líneas las ventas y recargas de cada día del periodo.
     *
     * @param ventasPorDia ventas pagadas agrupadas por día
     */
    private void actualizarGraficaTiempo(Map<LocalDate, Double> ventasPorDia) {
        Map<LocalDate, Double> recargasPorDia = new TreeMap<>();
        LocalDate inicio = fechaInicio();
        LocalDate fin = fechaFin();
        for (Cliente cliente : admin().consultarClientes()) {
            for (Movimiento movimiento : cliente.getTarjeta().getMovimientos()) {
                LocalDate dia = movimiento.getFecha().toLocalDate();
                if (movimiento.getTipo() == TipoMovimiento.RECARGA && !dia.isBefore(inicio) && !dia.isAfter(fin)) {
                    recargasPorDia.merge(dia, movimiento.getValor(), Double::sum);
                }
            }
        }
        TreeMap<LocalDate, Boolean> dias = new TreeMap<>();
        ventasPorDia.keySet().forEach(d -> dias.put(d, true));
        recargasPorDia.keySet().forEach(d -> dias.put(d, true));

        XYChart.Series<String, Number> serieVentas = new XYChart.Series<>();
        serieVentas.setName("Ventas");
        XYChart.Series<String, Number> serieRecargas = new XYChart.Series<>();
        serieRecargas.setName("Recargas");
        for (LocalDate dia : dias.keySet()) {
            String etiqueta = VistaUtil.fecha(dia);
            serieVentas.getData().add(new XYChart.Data<>(etiqueta, ventasPorDia.getOrDefault(dia, 0.0)));
            serieRecargas.getData().add(new XYChart.Data<>(etiqueta, recargasPorDia.getOrDefault(dia, 0.0)));
        }
        lineChartComportamientoTiempo.getData().setAll(List.of(serieVentas, serieRecargas));
    }

    /**
     * Calcula la ocupación promedio de las funciones que no están canceladas.
     *
     * @return porcentaje de ocupación promedio
     */
    private double ocupacionPromedio() {
        double total = 0;
        int funciones = 0;
        for (Funcion funcion : sesion.getCinema().getFunciones()) {
            if (!funcion.estaCancelada()) {
                total += funcion.ocupacion();
                funciones++;
            }
        }
        return funciones == 0 ? 0 : total / funciones;
    }

    /**
     * Obtiene la fecha inicial del periodo seleccionado.
     *
     * @return fecha inicial
     */
    private LocalDate fechaInicio() {
        return dpFechaInicio.getValue() != null ? dpFechaInicio.getValue() : LocalDate.now().minusDays(30);
    }

    /**
     * Obtiene la fecha final del periodo y verifica que no sea anterior a la inicial.
     *
     * @return fecha final
     * @throws IllegalArgumentException si la fecha final es anterior a la inicial
     */
    private LocalDate fechaFin() {
        LocalDate fin = dpFechaFin.getValue() != null ? dpFechaFin.getValue() : LocalDate.now().plusDays(30);
        if (fin.isBefore(fechaInicio())) {
            throw new IllegalArgumentException("La fecha final no puede ser anterior a la fecha inicial");
        }
        return fin;
    }

    /**
     * Obtiene el servicio de administración protegido por el proxy.
     *
     * @return proxy de administración
     */
    private IServicioAdmin admin() {
        return sesion.getServicioAdmin();
    }
}
