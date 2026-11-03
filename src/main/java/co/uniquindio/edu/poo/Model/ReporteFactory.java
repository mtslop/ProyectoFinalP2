package co.uniquindio.edu.poo.Model;


public class ReporteFactory {

    /**
     * Crea la fábrica de reportes.
     */
    public ReporteFactory() {
    }

    @Override
    public String toString() {
        return "ReporteFactory{}";
    }

    /**
     * Crea un reporte vacío del tipo indicado. Para llenarlo se debe invocar
     * {@link ReporteOperativo#generar(CinemaUQ, java.time.LocalDate, java.time.LocalDate)}.
     *
     * @param tipo tipo de reporte
     * @return reporte del tipo indicado, listo para generarse
     */
    /**
     * Crea el reporte que corresponde al tipo indicado.
     *
     * @param tipo tipo de reporte
     * @return reporte del tipo indicado
     */
    public ReporteOperativo crearReporte(TipoReporte tipo) {
        switch (tipo) {
            case VENTAS:
                return new ReporteVentas();
            case ENTRADAS:
                return new ReporteEntrada();
            case OCUPACION:
                return new ReporteOcupacion();
            case PRODUCTOS:
                return new ReporteProductos();
            case RECARGAS:
                return new ReporteRecarga();
            case REEMBOLSOS:
                return new ReporteReembolso();
            case PUNTOS:
                return new ReportePuntos();
            default:
                throw new IllegalArgumentException("Tipo de reporte no válido: " + tipo);
        }
    }
}
