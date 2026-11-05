package co.uniquindio.edu.poo.Model;

import java.util.ArrayList;
import java.util.List;

public class ServicioCliente {

    private CinemaUQ cinema;

    /**
     * Crea el servicio del cliente.
     *
     * @param cinema datos del cine
     */
    public ServicioCliente(CinemaUQ cinema) {
        this.cinema = cinema;
    }

    public CinemaUQ getCinema() {
        return cinema;
    }

    public void setCinema(CinemaUQ cinema) {
        this.cinema = cinema;
    }

    @Override
    public String toString() {
        return "ServicioCliente{}";
    }

    /**
     * Consulta las películas que están en cartelera.
     *
     * @return películas en cartelera
     */
    public List<Pelicula> consultarCartelera() {
        List<Pelicula> cartelera = new ArrayList<>();
        for (Pelicula p : cinema.getPeliculas()) {
            if (p.isEnCartelera()) {
                cartelera.add(p);
            }
        }
        return cartelera;
    }

    /**
     * Consulta las funciones disponibles de una película (no canceladas ni finalizadas).
     *
     * @param pelicula película consultada
     * @return funciones disponibles
     */
    public List<Funcion> consultarFunciones(Pelicula pelicula) {
        List<Funcion> resultado = new ArrayList<>();
        for (Funcion f : cinema.getFunciones()) {
            if (f.getPelicula() == pelicula && !f.estaCancelada() && !f.estaFinalizada()) {
                resultado.add(f);
            }
        }
        return resultado;
    }

    /**
     * Crea una solicitud de recarga en estado pendiente y la deja para que el administrador la atienda.
     *
     * @param cliente cliente que solicita la recarga
     * @param monto   valor a recargar
     * @return solicitud creada
     */
    public SolicitudRecarga solicitarRecarga(Cliente cliente, double monto) {
        String id = "SOL-" + (cinema.getSolicitudes().size() + 1);
        SolicitudRecarga solicitud = new SolicitudRecarga(id, cliente, monto);
        cinema.getSolicitudes().add(solicitud);
        return solicitud;
    }

    /**
     * Consulta los movimientos de la tarjeta de un cliente.
     *
     * @param cliente cliente consultado
     * @return movimientos de su tarjeta
     */
    public List<Movimiento> consultarMovimientos(Cliente cliente) {
        return cliente.getTarjeta().getMovimientos();
    }

    /**
     * Consulta el historial de compras de un cliente.
     *
     * @param cliente cliente consultado
     * @return compras del cliente
     */
    public List<Compra> consultarHistorial(Cliente cliente) {
        return cliente.getHistorialCompras();
    }

    /**
     * Consulta los puntos vigentes de un cliente.
     *
     * @param cliente cliente consultado
     * @return puntos disponibles
     */
    public int consultarPuntos(Cliente cliente) {
        return cliente.getCuentaPuntos().puntosDisponibles();
    }
}