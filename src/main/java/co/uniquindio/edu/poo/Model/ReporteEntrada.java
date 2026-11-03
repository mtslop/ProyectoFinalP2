package co.uniquindio.edu.poo.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReporteEntrada extends ReporteOperativo {


    /**
     * Crea el reporte de entradas vacío. Se llena al invocar generar(...).
     */
    public ReporteEntrada() {
        super();
    }

    @Override
    public String toString() {
        return "ReporteEntradas{" + super.toString() + '}';
    }

    @Override
    protected String definirTitulo() {
        return "Reporte de entradas vendidas por película";
    }

    @Override
    protected String[] definirEncabezados() {
        return new String[]{"Película", "Entradas vendidas"};
    }



    @Override
    protected List<String[]> obtenerRegistros(CinemaUQ cinema, LocalDate inicio, LocalDate fin) {
        Map<String, Integer> porPelicula = new LinkedHashMap<>();
        for (Compra c : cinema.getCompras()) {
            if ("PAGADA".equals(c.getEstado().nombre())
                    && estaEnPeriodo(c.getFecha().toLocalDate(), inicio, fin)) {
                String titulo = c.getFuncion().getPelicula().getTitulo();
                porPelicula.merge(titulo, c.getEntradas().size(), Integer::sum);
            }
        }
        List<String[]> filas = new ArrayList<>();
        for (Map.Entry<String, Integer> e : porPelicula.entrySet()) {
            filas.add(new String[]{e.getKey(), String.valueOf(e.getValue())});
        }
        return filas;
    }

    /**
     * Agrega el total de entradas vendidas.
     */
    @Override
    protected void calcularTotales() {
        double total = 0;
        for (String[] fila : getRegistros()) {
            total += Double.parseDouble(fila[1]);
        }
        getTotales().put("Total entradas vendidas", total);
    }
}

