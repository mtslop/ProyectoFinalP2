package co.uniquindio.edu.poo.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Exporta las líneas de un reporte a un archivo PDF sencillo (texto en fuente Courier),
 * sin usar librerías externas. Cada página tiene hasta {@link #LINEAS_POR_PAGINA} líneas.
 */
public final class ExportadorPdf {

    private static final int LINEAS_POR_PAGINA = 52;
    private static final Charset LATIN1 = Charset.forName("windows-1252");

    /**
     * Evita que se creen objetos de esta clase de utilidades.
     */
    private ExportadorPdf() {
    }

    /**
     * Escribe las líneas en un archivo PDF.
     *
     * @param lineas líneas de texto del reporte
     * @param ruta   archivo de destino
     * @throws IllegalStateException si el archivo no se puede escribir
     */
    public static void exportar(List<String> lineas, Path ruta) {
        List<List<String>> paginas = new ArrayList<>();
        for (int i = 0; i < lineas.size(); i += LINEAS_POR_PAGINA) {
            paginas.add(lineas.subList(i, Math.min(i + LINEAS_POR_PAGINA, lineas.size())));
        }
        if (paginas.isEmpty()) {
            paginas.add(List.of(""));
        }

        List<String> objetos = new ArrayList<>();
        objetos.add("<< /Type /Catalog /Pages 2 0 R >>");
        StringBuilder kids = new StringBuilder();
        for (int p = 0; p < paginas.size(); p++) {
            kids.append(4 + p * 2).append(" 0 R ");
        }
        objetos.add("<< /Type /Pages /Kids [" + kids + "] /Count " + paginas.size() + " >>");
        objetos.add("<< /Type /Font /Subtype /Type1 /BaseFont /Courier /Encoding /WinAnsiEncoding >>");
        for (int p = 0; p < paginas.size(); p++) {
            int contenido = 5 + p * 2;
            objetos.add("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] "
                    + "/Resources << /Font << /F1 3 0 R >> >> /Contents " + contenido + " 0 R >>");
            String flujo = crearFlujo(paginas.get(p));
            objetos.add("<< /Length " + flujo.getBytes(LATIN1).length + " >>\nstream\n" + flujo + "\nendstream");
        }

        try {
            Files.write(ruta, construirArchivo(objetos));
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo exportar el PDF en " + ruta, e);
        }
    }

    /**
     * Crea las instrucciones PDF que dibujan las líneas de una página.
     *
     * @param lineas líneas de la página
     * @return contenido de la página
     */
    private static String crearFlujo(List<String> lineas) {
        StringBuilder sb = new StringBuilder("BT\n/F1 9 Tf\n12 TL\n40 752 Td\n");
        for (String linea : lineas) {
            sb.append('(').append(escapar(linea)).append(") Tj T*\n");
        }
        return sb.append("ET").toString();
    }

    /**
     * Arma el archivo PDF completo con su tabla de referencias.
     *
     * @param objetos objetos PDF en orden (el primero es el número 1)
     * @return bytes del archivo
     */
    private static byte[] construirArchivo(List<String> objetos) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        List<Integer> posiciones = new ArrayList<>();
        escribir(salida, "%PDF-1.4\n");
        for (int i = 0; i < objetos.size(); i++) {
            posiciones.add(salida.size());
            escribir(salida, (i + 1) + " 0 obj\n" + objetos.get(i) + "\nendobj\n");
        }
        int inicioXref = salida.size();
        StringBuilder xref = new StringBuilder("xref\n0 " + (objetos.size() + 1) + "\n0000000000 65535 f \n");
        for (int posicion : posiciones) {
            xref.append(String.format("%010d 00000 n \n", posicion));
        }
        xref.append("trailer\n<< /Size ").append(objetos.size() + 1).append(" /Root 1 0 R >>\n")
                .append("startxref\n").append(inicioXref).append("\n%%EOF\n");
        escribir(salida, xref.toString());
        return salida.toByteArray();
    }

    /**
     * Escapa los caracteres especiales de un texto PDF y cambia los que la fuente no puede mostrar.
     *
     * @param texto texto original
     * @return texto seguro para el PDF
     */
    private static String escapar(String texto) {
        StringBuilder sb = new StringBuilder();
        for (char c : texto.toCharArray()) {
            if (c == '(' || c == ')' || c == '\\') {
                sb.append('\\').append(c);
            } else if (c == '\t') {
                sb.append("    ");
            } else if (LATIN1.newEncoder().canEncode(c)) {
                sb.append(c);
            } else {
                sb.append('?');
            }
        }
        return sb.toString();
    }

    /**
     * Escribe un texto en la salida con la codificación de la fuente.
     *
     * @param salida salida de bytes
     * @param texto  texto a escribir
     */
    private static void escribir(ByteArrayOutputStream salida, String texto) {
        byte[] bytes = texto.getBytes(LATIN1);
        salida.write(bytes, 0, bytes.length);
    }
}
