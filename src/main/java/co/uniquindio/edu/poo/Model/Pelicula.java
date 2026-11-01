package co.uniquindio.edu.poo.Model;

import java.util.Objects;

/**
 * Representa una película que puede proyectarse en el cine.
 */
public class Pelicula {

    private String codigo;
    private String titulo;
    private int duracionMin;
    private String sinopsis;
    private Genero genero;
    private Clasificacion clasificacion;
    private boolean enCartelera;

    /**
     * Crea una película con todos sus datos.
     *
     * @param codigo identificador único de la película.
     * @param titulo título comercial.
     * @param duracionMin duración en minutos.
     * @param sinopsis resumen de la historia.
     * @param genero género cinematográfico.
     * @param clasificacion clasificación por edades.
     * @param enCartelera indica si la película está actualmente en cartelera.
     * @throws NullPointerException si el código, el título, el género o la clasificación son null.
     * @throws IllegalArgumentException si el código o el título están vacíos o la duración no es positiva.
     */
    public Pelicula(String codigo, String titulo, int duracionMin, String sinopsis,
                    Genero genero, Clasificacion clasificacion, boolean enCartelera) {
        this.codigo = Objects.requireNonNull(codigo, "El código no puede ser null.");
        if (codigo.isBlank()) {
            throw new IllegalArgumentException("El código de la película no puede estar vacío.");
        }
        this.titulo = Objects.requireNonNull(titulo, "El título no puede ser null.");
        if (titulo.isBlank()) {
            throw new IllegalArgumentException("El título de la película no puede estar vacío.");
        }
        if (duracionMin <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor que cero.");
        }
        this.duracionMin = duracionMin;
        this.sinopsis = sinopsis;
        this.genero = Objects.requireNonNull(genero, "El género no puede ser null.");
        this.clasificacion = Objects.requireNonNull(clasificacion, "La clasificación no puede ser null.");
        this.enCartelera = enCartelera;
    }

    /**
     * Obtiene el código de la película.
     *
     * @return código único.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Actualiza el código de la película.
     *
     * @param codigo nuevo código.
     */
    public void setCodigo(String codigo) {
        this.codigo = Objects.requireNonNull(codigo, "El código no puede ser null.");
        if (codigo.isBlank()) {
            throw new IllegalArgumentException("El código de la película no puede estar vacío.");
        }
    }

    /**
     * Obtiene el título de la película.
     *
     * @return título comercial.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Actualiza el título de la película.
     *
     * @param titulo nuevo título.
     */
    public void setTitulo(String titulo) {
        this.titulo = Objects.requireNonNull(titulo, "El título no puede ser null.");
        if (titulo.isBlank()) {
            throw new IllegalArgumentException("El título de la película no puede estar vacío.");
        }
    }

    /**
     * Obtiene la duración de la película.
     *
     * @return duración en minutos.
     */
    public int getDuracionMin() {
        return duracionMin;
    }

    /**
     * Actualiza la duración de la película.
     *
     * @param duracionMin nueva duración en minutos.
     */
    public void setDuracionMin(int duracionMin) {
        if (duracionMin <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor que cero.");
        }
        this.duracionMin = duracionMin;
    }

    /**
     * Obtiene la sinopsis de la película.
     *
     * @return sinopsis.
     */
    public String getSinopsis() {
        return sinopsis;
    }

    /**
     * Actualiza la sinopsis de la película.
     *
     * @param sinopsis nueva sinopsis.
     */
    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    /**
     * Obtiene el género de la película.
     *
     * @return género cinematográfico.
     */
    public Genero getGenero() {
        return genero;
    }

    /**
     * Actualiza el género de la película.
     *
     * @param genero nuevo género.
     */
    public void setGenero(Genero genero) {
        this.genero = Objects.requireNonNull(genero, "El género no puede ser null.");
    }

    /**
     * Obtiene la clasificación por edades.
     *
     * @return clasificación de la película.
     */
    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    /**
     * Actualiza la clasificación por edades.
     *
     * @param clasificacion nueva clasificación.
     */
    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = Objects.requireNonNull(clasificacion, "La clasificación no puede ser null.");
    }

    /**
     * Indica si la película está en cartelera.
     *
     * @return true si está en cartelera.
     */
    public boolean isEnCartelera() {
        return enCartelera;
    }

    /**
     * Actualiza el estado de cartelera de la película.
     *
     * @param enCartelera nuevo estado.
     */
    public void setEnCartelera(boolean enCartelera) {
        this.enCartelera = enCartelera;
    }
}
