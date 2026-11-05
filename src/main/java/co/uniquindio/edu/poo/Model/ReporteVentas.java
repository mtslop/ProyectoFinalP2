package co.uniquindio.edu.poo.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReporteVentas extends ReporteOperativo{


    /**
     * Crea el reporte de ventas vacío. Se llena al invocar generar(...).
     */
    public ReporteVentas() {
        super();
    }

    /**
     * Crea un reporte de ventas vacío.
     */

    @Override
    public String toString() {
        return "ReporteVentas{" + super.toString() + '}';
    }

    @Override
    protected String definirTitulo() {
        return "Reporte de ventas por periodo";
    }

    @Override
    protected String[] definirEncabezados() {
        return new String[]{"Fecha", "Cliente", "Película", "Entradas", "Total"};
    }

    @Override
    protected List<String[]> obtenerRegistros(CinemaUQ cinema, LocalDate inicio, LocalDate fin) {
        List<String[]> filas = new ArrayList<>();
        for (Compra c : cinema.getCompras()) {
            if ("PAGADA".equals(c.getEstado().nombre())
                    && estaEnPeriodo(c.getFecha().toLocalDate(), inicio, fin)) {
                filas.add(new String[]{
                        c.getFecha().toLocalDate().toString(),
                        c.getCliente().getNombre(),
                        c.getFuncion() != null ? c.getFuncion().getPelicula().getTitulo() : "Solo confitería",
                        String.valueOf(c.getEntradas().size()),
                        String.format("%.0f", c.getTotal())
                });
            }
        }
        return filas;
    }

    /**
     * Agrega el número de ventas y el valor total vendido.
     */
    @Override
    protected void calcularTotales() {
        double total = 0;
        for (String[] fila : getRegistros()) {
            total += Double.parseDouble(fila[4]);
        }
        getTotales().put("Número de ventas", (double) getRegistros().size());
        getTotales().put("Total vendido", total);
    }
}
