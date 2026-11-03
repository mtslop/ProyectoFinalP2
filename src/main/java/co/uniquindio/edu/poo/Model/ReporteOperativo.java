package co.uniquindio.edu.poo.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public abstract class  ReporteOperativo {

    private String titulo;
    private LocalDateTime fechaGeneracion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String[] encabezados;
    private List<String[]> registros;
    private Map<String, Double> totales;

    public ReporteOperativo(String titulo, LocalDateTime fechaGeneracion, LocalDate fechaInicio, LocalDate fechaFin, String[] encabezados, List<String[]> registros, Map<String, Double> totales) {
        this.titulo = titulo;
        this.fechaGeneracion = fechaGeneracion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.encabezados = encabezados;
        this.registros = registros;
        this.totales = totales;
    }

    protected ReporteOperativo() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String[] getEncabezados() {
        return encabezados;
    }

    public void setEncabezados(String[] encabezados) {
        this.encabezados = encabezados;
    }

    public List<String[]> getRegistros() {
        return registros;
    }

    public void setRegistros(List<String[]> registros) {
        this.registros = registros;
    }

    public Map<String, Double> getTotales() {
        return totales;
    }

    public void setTotales(Map<String, Double> totales) {
        this.totales = totales;
    }

    @Override
    public String toString() {
        return "ReporteOperativo{" +
                "titulo='" + titulo + '\'' +
                ", fechaGeneracion=" + fechaGeneracion +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", encabezados=" + Arrays.toString(encabezados) +
                ", registros=" + registros +
                ", totales=" + totales +
                '}';
    }

    /**
     * Método plantilla: genera el reporte siguiendo siempre los mismos pasos.
     * Es final para que ninguna subclase cambie el orden.
     *
     * @param cinema fuente de los datos
     * @param inicio fecha inicial del periodo
     * @param fin    fecha final del periodo
     */
    public final void generar(CinemaUQ cinema, LocalDate inicio, LocalDate fin) {
        this.fechaGeneracion = LocalDateTime.now();
        this.fechaInicio = inicio;
        this.fechaFin = fin;
        this.titulo = definirTitulo();
        this.encabezados = definirEncabezados();
        this.registros = obtenerRegistros(cinema, inicio, fin);
        this.totales = new LinkedHashMap<>();
        calcularTotales();
    }

    /**
     * Define el título del reporte.
     *
     * @return título del reporte
     */
    protected abstract String definirTitulo();

    /**
     * Define los nombres de las columnas del reporte.
     *
     * @return nombres de las columnas
     */
    protected abstract String[] definirEncabezados();

    /**
     * Obtiene las filas del reporte a partir de los datos del cine.
     *
     * @param cinema fuente de los datos
     * @param inicio fecha inicial del periodo
     * @param fin    fecha final del periodo
     * @return filas del reporte
     */
    protected abstract List<String[]> obtenerRegistros(CinemaUQ cinema, LocalDate inicio, LocalDate fin);

    /**
     * Calcula los totales del reporte. Por defecto registra la cantidad de filas;
     * las subclases pueden sobrescribirlo para agregar otros totales.
     */
    protected void calcularTotales() {
        totales.put("Total registros", (double) registros.size());
    }

    /**
     * Retorna la cantidad de filas del reporte.
     *
     * @return cantidad de registros
     */
    public int totalRegistros() {
        return registros.size();
    }

    /**
     * Convierte el reporte en líneas de texto, listas para exportarse a un archivo TXT.
     *
     * @return líneas del reporte
     */
    public List<String> generarLineas() {
        List<String> lineas = new ArrayList<>();
        lineas.add(titulo);
        lineas.add("Generado: " + fechaGeneracion.withNano(0));
        lineas.add("Periodo: " + fechaInicio + " a " + fechaFin);
        lineas.add("");
        lineas.add(String.join(" | ", encabezados));
        for (String[] fila : registros) {
            lineas.add(String.join(" | ", fila));
        }
        lineas.add("");
        for (Map.Entry<String, Double> total : totales.entrySet()) {
            lineas.add(total.getKey() + ": " + String.format("%,.0f", total.getValue()));
        }
        return lineas;
    }

    /**
     * Indica si una fecha está dentro del periodo del reporte (incluye los extremos).
     *
     * @param fecha  fecha a evaluar
     * @param inicio fecha inicial del periodo
     * @param fin    fecha final del periodo
     * @return {@code true} si la fecha está dentro del periodo
     */
    protected boolean estaEnPeriodo(LocalDate fecha, LocalDate inicio, LocalDate fin) {
        return !fecha.isBefore(inicio) && !fecha.isAfter(fin);
    }
}
