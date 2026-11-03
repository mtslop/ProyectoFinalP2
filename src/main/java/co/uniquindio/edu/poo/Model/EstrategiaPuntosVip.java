package co.uniquindio.edu.poo.Model;

public class EstrategiaPuntosVip implements IEstrategiaPuntos{

    private double valorPorPunto;
    private double multiplicador;

    /**
     * Crea la estrategia de puntos para clientes VIP.
     *
     * @param valorPorPunto valor en pesos que se debe pagar para obtener un punto
     * @param multiplicador factor por el que se multiplican los puntos del cliente VIP
     */

    public EstrategiaPuntosVip(double valorPorPunto, double multiplicador) {
        this.valorPorPunto = valorPorPunto;
        this.multiplicador = multiplicador;
    }


    public double getValorPorPunto() {
        return valorPorPunto;
    }

    public void setValorPorPunto(double valorPorPunto) {
        this.valorPorPunto = valorPorPunto;
    }

    public double getMultiplicador() {
        return multiplicador;
    }

    public void setMultiplicador(double multiplicador) {
        this.multiplicador = multiplicador;
    }

    @Override
    public String toString() {
        return "EstrategiaPuntosVIP{" +
                "valorPorPunto=" + valorPorPunto +
                ", multiplicador=" + multiplicador +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int calcularPuntos(double totalPagado) {
        return (int) (totalPagado / valorPorPunto * multiplicador);
    }
}
