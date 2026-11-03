package co.uniquindio.edu.poo.Model;

import java.time.LocalDate;
import java.util.List;

public class ServicioAdmin implements IServicioAdmin {

    private CinemaUQ cinema;
    private NotificacionCentro notificador;
    private CompraFacade compraFacade;
    private ReporteFactory reporteFactory;

    /**
     * Crea el servicio del administrador.
     *
     * @param cinema         datos del cine
     * @param notificador    centro de notificaciones para avisar a los clientes
     * @param compraFacade   fachada de compras, usada para reembolsar
     * @param reporteFactory fábrica de reportes
     */

    public ServicioAdmin(CinemaUQ cinema, NotificacionCentro notificador, CompraFacade compraFacade, ReporteFactory reporteFactory) {
        this.cinema = cinema;
        this.notificador = notificador;
        this.compraFacade = compraFacade;
        this.reporteFactory = reporteFactory;
    }


    public CinemaUQ getCinema() {
        return cinema;
    }

    public void setCinema(CinemaUQ cinema) {
        this.cinema = cinema;
    }

    public NotificacionCentro getNotificador() {
        return notificador;
    }

    public void setNotificador(NotificacionCentro notificador) {
        this.notificador = notificador;
    }

    public CompraFacade getCompraFacade() {
        return compraFacade;
    }

    public void setCompraFacade(CompraFacade compraFacade) {
        this.compraFacade = compraFacade;
    }

    public ReporteFactory getReporteFactory() {
        return reporteFactory;
    }

    public void setReporteFactory(ReporteFactory reporteFactory) {
        this.reporteFactory = reporteFactory;
    }

    @Override
    public String toString() {
        return "ServicioAdmin{" +
                "cinema=" + cinema +
                ", notificador=" + notificador +
                ", compraFacade=" + compraFacade +
                ", reporteFactory=" + reporteFactory +
                '}';
    }

    @Override
    public void registrarPelicula(Pelicula pelicula) {

    }

    @Override
    public void registrarSala(Sala sala) {

    }

    @Override
    public void registrarFuncion(Funcion funcion) {

    }

    @Override
    public void registrarItemConfiteria(ItemConfiteria item) {

    }

    @Override
    public void registrarPromocion(Promocion promocion) {

    }

    @Override
    public void cancelarFuncion(Funcion funcion) {

    }

    @Override
    public void recargarTarjeta(SolicitudRecarga solicitud) {

    }

    @Override
    public void rechazarRecarga(SolicitudRecarga solicitud) {

    }

    @Override
    public List<Cliente> consultarClientes() {
        return List.of();
    }

    @Override
    public List<Compra> consultarCompras(LocalDate inicio, LocalDate fin) {
        return List.of();
    }

    @Override
    public ReporteOperativo generarReporte(TipoReporte tipo, LocalDate inicio, LocalDate fin) {
        return null;
    }

    @Override
    public void exportarReporte(ReporteOperativo reporte, String ruta) {

    }
}
