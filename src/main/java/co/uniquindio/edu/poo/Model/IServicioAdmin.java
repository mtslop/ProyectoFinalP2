package co.uniquindio.edu.poo.Model;

import java.time.LocalDate;
import java.util.List;

public interface IServicioAdmin {

    /**
     * Registra una película en el catálogo.
     *
     * @param pelicula película a registrar
     */
    void registrarPelicula(Pelicula pelicula);

    /**
     * Registra una sala.
     *
     * @param sala sala a registrar
     */
    void registrarSala(Sala sala);

    /**
     * Programa una función.
     *
     * @param funcion función a registrar
     */
    void registrarFuncion(Funcion funcion);

    /**
     * Registra un producto o combo de confitería.
     *
     * @param item producto o combo a registrar
     */
    void registrarItemConfiteria(ItemConfiteria item);

    /**
     * Registra una promoción.
     *
     * @param promocion promoción a registrar
     */
    void registrarPromocion(Promocion promocion);

    /**
     * Cancela una función, reembolsa el 100 % de sus compras y avisa a los clientes.
     *
     * @param funcion función a cancelar
     */
    void cancelarFuncion(Funcion funcion);

    /**
     * Aprueba una solicitud de recarga y recarga la tarjeta del cliente.
     *
     * @param solicitud solicitud a aprobar
     */
    void recargarTarjeta(SolicitudRecarga solicitud);

    /**
     * Rechaza una solicitud de recarga sin modificar la tarjeta.
     *
     * @param solicitud solicitud a rechazar
     */
    void rechazarRecarga(SolicitudRecarga solicitud);

    /**
     * Consulta los clientes registrados.
     *
     * @return lista de clientes
     */
    List<Cliente> consultarClientes();

    /**
     * Consulta las compras realizadas en un periodo.
     *
     * @param inicio fecha inicial
     * @param fin    fecha final
     * @return compras del periodo
     */
    List<Compra> consultarCompras(LocalDate inicio, LocalDate fin);

    /**
     * Genera un reporte del tipo indicado para un periodo.
     *
     * @param tipo   tipo de reporte
     * @param inicio fecha inicial
     * @param fin    fecha final
     * @return reporte generado
     */
    ReporteOperativo generarReporte(TipoReporte tipo, LocalDate inicio, LocalDate fin);

    /**
     * Exporta un reporte a un archivo de texto.
     *
     * @param reporte reporte a exportar
     * @param ruta    ruta del archivo de destino
     */
    void exportarReporte(ReporteOperativo reporte, String ruta);
}
