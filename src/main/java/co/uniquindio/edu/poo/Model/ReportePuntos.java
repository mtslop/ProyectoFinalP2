package co.uniquindio.edu.poo.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportePuntos extends ReporteOperativo{

    /**
     * Crea el reporte de puntos vacío. Se llena al invocar generar(...).
     */
    public ReportePuntos() {
        super();
    }


    @Override
    public String toString() {
        return "ReportePuntos{" + super.toString() + '}';
    }

    @Override
    protected String definirTitulo() {
        return "Reporte de puntos CinemaUQ";
    }

    @Override
    protected String[] definirEncabezados() {
        return new String[]{"Cliente", "Acumulados", "Usados", "Disponibles"};
    }



    @Override
    protected List<String[]> obtenerRegistros(CinemaUQ cinema, LocalDate inicio, LocalDate fin) {
        List<String[]> filas = new ArrayList<>();
        for (Persona p : cinema.getUsuarios()) {
            if (p instanceof Cliente cliente) {
                int acumulados = 0;
                int usados = 0;
                for (RegistroPuntos r : cliente.getCuentaPuntos().getRegistros()) {
                    if (estaEnPeriodo(r.getFechaObtencion(), inicio, fin)) {
                        acumulados += r.getCantidad();
                        usados += r.calcularCantidadUsada();
                    }
                }
                if (acumulados > 0) {
                    filas.add(new String[]{
                            cliente.getNombre(),
                            String.valueOf(acumulados),
                            String.valueOf(usados),
                            String.valueOf(acumulados - usados)
                    });
                }
            }
        }
        return filas;
    }

    /**
     * Agrega el total de puntos acumulados y usados.
     */
    @Override
    protected void calcularTotales() {
        double acumulados = 0;
        double usados = 0;
        for (String[] fila : getRegistros()) {
            acumulados += Double.parseDouble(fila[1]);
            usados += Double.parseDouble(fila[2]);
        }
        getTotales().put("Puntos acumulados", acumulados);
        getTotales().put("Puntos usados", usados);
    }
}
