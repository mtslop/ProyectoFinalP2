package co.uniquindio.edu.poo.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReporteOcupacion extends ReporteOperativo{

    /**
     * Crea el reporte de ocupacion vacío. Se llena al invocar generar(...).
     */
    public ReporteOcupacion() {
        super();
    }


    @Override
    public String toString() {
        return "ReporteOcupacion{" + super.toString() + '}';
    }

    @Override
    protected String definirTitulo() {
        return "Reporte de ocupación de salas";
    }

    @Override
    protected String[] definirEncabezados() {
        return new String[]{"Fecha y hora", "Película", "Sala", "Ocupados", "Capacidad", "Ocupación %"};
    }

    @Override
    protected List<String[]> obtenerRegistros(CinemaUQ cinema, LocalDate inicio, LocalDate fin) {
        List<String[]> filas = new ArrayList<>();
        for (Funcion f : cinema.getFunciones()) {
            if (!f.isCancelada() && estaEnPeriodo(f.getFechaHora().toLocalDate(), inicio, fin)) {
                filas.add(new String[]{
                        f.getFechaHora().toString(),
                        f.getPelicula().getTitulo(),
                        String.valueOf(f.getSala().getNumero()),
                        String.valueOf(f.getAsientosOcupados().size()),
                        String.valueOf(f.getSala().getCapacidad()),
                        String.format("%.1f", f.ocupacion() * 100)
                });
            }
        }
        return filas;
    }

    /**
     * Agrega el número de funciones y la ocupación promedio.
     */
    @Override
    protected void calcularTotales() {
        double suma = 0;
        for (String[] fila : getRegistros()) {
            suma += Double.parseDouble(fila[5].replace(',', '.'));
        }
        double promedio = getRegistros().isEmpty() ? 0 : suma / getRegistros().size();
        getTotales().put("Funciones", (double) getRegistros().size());
        getTotales().put("Ocupación promedio %", promedio);
    }
}
