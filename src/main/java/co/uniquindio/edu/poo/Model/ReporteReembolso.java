package co.uniquindio.edu.poo.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReporteReembolso extends ReporteOperativo{

    /**
     * Crea el reporte de reembolso vacío. Se llena al invocar generar(...).
     */
    public ReporteReembolso() {
        super();
    }

    @Override
    public String toString() {
        return "ReporteReembolsos{" + super.toString() + '}';
    }

    @Override
    protected String definirTitulo() {
        return "Reporte de cancelaciones y reembolsos";
    }

    @Override
    protected String[] definirEncabezados() {
        return new String[]{"Fecha compra", "Cliente", "Película", "Estado", "Total pagado", "Reembolsado"};
    }

    @Override
    protected List<String[]> obtenerRegistros(CinemaUQ cinema, LocalDate inicio, LocalDate fin) {
        List<String[]> filas = new ArrayList<>();
        for (Compra c : cinema.getCompras()) {
            String estado = c.getEstado().nombre();
            if (("REEMBOLSADA".equals(estado) || "CANCELADA".equals(estado))
                    && estaEnPeriodo(c.getFecha().toLocalDate(), inicio, fin)) {
                filas.add(new String[]{
                        c.getFecha().toLocalDate().toString(),
                        c.getCliente().getNombre(),
                        c.getFuncion() != null ? c.getFuncion().getPelicula().getTitulo() : "Solo confitería",
                        estado,
                        String.format("%.0f", c.getTotal()),
                        String.format("%.0f", c.getMontoReembolsado())
                });
            }
        }
        return filas;
    }

    /**
     * Agrega el número de cancelaciones y el valor total reembolsado.
     */
    @Override
    protected void calcularTotales() {
        double total = 0;
        for (String[] fila : getRegistros()) {
            total += Double.parseDouble(fila[5]);
        }
        getTotales().put("Cancelaciones", (double) getRegistros().size());
        getTotales().put("Total reembolsado", total);
    }
}
