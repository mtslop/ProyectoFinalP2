package co.uniquindio.edu.poo.Model;

public interface IEstrategiaPuntos {

    /**
     * Calcula los puntos que corresponden a un valor pagado.
     *
     * @param totalPagado valor total pagado en la compra
     * @return puntos obtenidos
     */
    int calcularPuntos(double totalPagado);

}
