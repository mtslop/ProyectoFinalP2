package co.uniquindio.edu.poo.Model;

import java.time.LocalDate;
import java.util.List;

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
                "servicioReal=" + servicioReal +
                ", personaActual=" + personaActual +
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
