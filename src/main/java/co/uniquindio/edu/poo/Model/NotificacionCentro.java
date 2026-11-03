package co.uniquindio.edu.poo.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificacionCentro {

    // Por cada función, guarda los clientes que compraron entradas para ella (a quiénes avisar).

    private Map<Funcion, List<IClienteObserver>> observadores;

    /**
     * Crea un centro de notificaciones sin observadores.
     */
    public NotificacionCentro() {
        this.observadores = new HashMap<>(); // Se crea el mapa vacío; al inicio ningún cliente está suscrito.
    }

    public Map<Funcion, List<IClienteObserver>> getObservadores() {
        return observadores;
    }

    public void setObservadores(Map<Funcion, List<IClienteObserver>> observadores) {
        this.observadores = observadores;
    }

    @Override
    public String toString() {
        return "NotificacionCentro{" +
                "funcionesObservadas=" + observadores.size() +
                '}';
    }

    /**
     * Suscribe un cliente a una función para que reciba sus avisos.
     *
     * @param funcion    función a la que se suscribe
     * @param observador cliente que recibirá los avisos
     */
    public void suscribir(Funcion funcion, IClienteObserver observador) {
        List<IClienteObserver> lista = observadores.get(funcion);
        if (lista == null) {
            lista = new ArrayList<>();
            observadores.put(funcion, lista);
        }
        lista.add(observador);
    }


    /**
     * Retira un observador de los avisos de una función.
     *
     * @param funcion    función de la que se dejan de recibir avisos
     * @param observador observador que se retira
     */
    public void desuscribir(Funcion funcion, IClienteObserver observador) {
        List<IClienteObserver> lista = observadores.get(funcion);
        if (lista != null) {
            lista.remove(observador);
        }
    }

    /**
     * Envía un aviso a todos los observadores suscritos a una función.
     *
     * @param funcion función sobre la que se avisa
     * @param mensaje texto del aviso
     */
    public void notificar(Funcion funcion, String mensaje) {
        List<IClienteObserver> lista = observadores.get(funcion);
        if (lista != null) {
            for (IClienteObserver o : lista) {
                o.actualizar(mensaje);
            }
        }
    }
}
