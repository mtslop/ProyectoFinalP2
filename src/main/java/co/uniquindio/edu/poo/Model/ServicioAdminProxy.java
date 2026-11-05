package co.uniquindio.edu.poo.Model;

import java.time.LocalDate;
import java.util.List;

/**
 * Proxy de protección del patrón Proxy. Tiene la misma interfaz que {@link ServicioAdmin},
 * pero antes de cada operación verifica que la persona que inició sesión sea administrador.
 */
public class ServicioAdminProxy implements IServicioAdmin {

    private ServicioAdmin servicioReal;
    private Persona personaActual;

    /**
     * Crea el proxy para la persona que inició sesión.
     *
     * @param servicioReal  servicio que realiza las operaciones
     * @param personaActual persona que inició sesión
     */
    public ServicioAdminProxy(ServicioAdmin servicioReal, Persona personaActual) {
        this.servicioReal = servicioReal;
        this.personaActual = personaActual;
    }

    public ServicioAdmin getServicioReal() {
        return servicioReal;
    }

    public void setServicioReal(ServicioAdmin servicioReal) {
        this.servicioReal = servicioReal;
    }

    public Persona getPersonaActual() {
        return personaActual;
    }

    public void setPersonaActual(Persona personaActual) {
        this.personaActual = personaActual;
    }

    @Override
    public String toString() {
        return "ServicioAdminProxy{" +
                "personaActual=" + (personaActual != null ? personaActual.getNombre() : "ninguna") +
                '}';
    }

    @Override
    public void registrarPelicula(Pelicula pelicula) {
        verificarPermiso();
        servicioReal.registrarPelicula(pelicula);
    }

    @Override
    public void registrarSala(Sala sala) {
        verificarPermiso();
        servicioReal.registrarSala(sala);
    }

    @Override
    public void registrarFuncion(Funcion funcion) {
        verificarPermiso();
        servicioReal.registrarFuncion(funcion);
    }

    @Override
    public void registrarItemConfiteria(ItemConfiteria item) {
        verificarPermiso();
        servicioReal.registrarItemConfiteria(item);
    }

    @Override
    public void registrarPromocion(Promocion promocion) {
        verificarPermiso();
        servicioReal.registrarPromocion(promocion);
    }

    @Override
    public void cancelarFuncion(Funcion funcion) {
        verificarPermiso();
        servicioReal.cancelarFuncion(funcion);
    }

    @Override
    public void recargarTarjeta(SolicitudRecarga solicitud) {
        verificarPermiso();
        servicioReal.recargarTarjeta(solicitud);
    }

    @Override
    public void rechazarRecarga(SolicitudRecarga solicitud) {
        verificarPermiso();
        servicioReal.rechazarRecarga(solicitud);
    }

    @Override
    public List<Cliente> consultarClientes() {
        verificarPermiso();
        return servicioReal.consultarClientes();
    }

    @Override
    public List<Compra> consultarCompras(LocalDate inicio, LocalDate fin) {
        verificarPermiso();
        return servicioReal.consultarCompras(inicio, fin);
    }

    @Override
    public ReporteOperativo generarReporte(TipoReporte tipo, LocalDate inicio, LocalDate fin) {
        verificarPermiso();
        return servicioReal.generarReporte(tipo, inicio, fin);
    }

    @Override
    public void exportarReporte(ReporteOperativo reporte, String ruta) {
        verificarPermiso();
        servicioReal.exportarReporte(reporte, ruta);
    }

    /**
     * Verifica que la persona actual sea administrador.
     *
     * @throws IllegalStateException si no hay sesión o la persona no es administrador
     */
    private void verificarPermiso() {
        if (personaActual == null || !personaActual.esAdministrador()) {
            throw new IllegalStateException("Solo el administrador puede realizar esta operación");
        }
    }
}
