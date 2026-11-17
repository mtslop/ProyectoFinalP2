package co.uniquindio.edu.poo.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio real con las operaciones del administrador. No verifica permisos:
 * de eso se encarga {@link ServicioAdminProxy}.
 */
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
    public ServicioAdmin(CinemaUQ cinema, NotificacionCentro notificador,
                         CompraFacade compraFacade, ReporteFactory reporteFactory) {
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
                "notificador=" + notificador +
                ", reporteFactory=" + reporteFactory +
                '}';
    }

    @Override
    public void registrarPelicula(Pelicula pelicula) {
        cinema.getPeliculas().add(pelicula);
    }

    @Override
    public void registrarSala(Sala sala) {
        cinema.getSalas().add(sala);
    }

    @Override
    public void registrarFuncion(Funcion funcion) {
        cinema.getFunciones().add(funcion);
    }

    @Override
    public void registrarItemConfiteria(ItemConfiteria item) {
        cinema.getProductos().add(item);
    }

    @Override
    public void registrarPromocion(Promocion promocion) {
        cinema.getPromociones().add(promocion);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Solo se reembolsan las compras pagadas de esa función.
     */
    @Override
    public void cancelarFuncion(Funcion funcion) {
        funcion.cancelar();
        for (Compra c : cinema.getCompras()) {
            if (c.getFuncion() == funcion && "PAGADA".equals(c.getEstado().nombre())) {
                compraFacade.reembolsarPorCine(c);
            }
        }
        notificador.notificar(funcion, "La función de " + funcion.getPelicula().getTitulo()
                + " del " + funcion.getFechaHora().toLocalDate()
                + " fue cancelada. Se reembolsó el 100 % a su tarjeta.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Primero se aprueba la solicitud, para que una solicitud ya atendida no recargue dos veces.
     */
    @Override
    public void recargarTarjeta(SolicitudRecarga solicitud) {
        solicitud.aprobar();
        solicitud.getCliente().getTarjeta().recargar(solicitud.getMonto());
    }

    @Override
    public void rechazarRecarga(SolicitudRecarga solicitud) {
        solicitud.rechazar();
    }

    @Override
    public List<Cliente> consultarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        for (Persona p : cinema.getUsuarios()) {
            if (p instanceof Cliente cliente) {
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    @Override
    public List<Compra> consultarCompras(LocalDate inicio, LocalDate fin) {
        List<Compra> resultado = new ArrayList<>();
        for (Compra c : cinema.getCompras()) {
            LocalDate fecha = c.getFecha().toLocalDate();
            if (!fecha.isBefore(inicio) && !fecha.isAfter(fin)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    @Override
    public ReporteOperativo generarReporte(TipoReporte tipo, LocalDate inicio, LocalDate fin) {
        ReporteOperativo reporte = reporteFactory.crearReporte(tipo);
        reporte.generar(cinema, inicio, fin);
        return reporte;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException si el archivo no se puede escribir
     */
    @Override
    public void exportarReporte(ReporteOperativo reporte, String ruta) {
        try {
            Files.write(Path.of(ruta), reporte.generarLineas());
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo exportar el reporte en " + ruta, e);
        }
    }
}
