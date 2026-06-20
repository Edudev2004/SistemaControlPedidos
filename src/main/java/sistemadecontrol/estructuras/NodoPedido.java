package sistemadecontrol.estructuras;

import sistemadecontrol.model.Pedido;

/**
 * NodoPedido: Esta clase representa los "vagones" de nuestra estructura de
 * datos dinámica.
 * (Unidad 2: Listas Enlazadas).
 * Yo la diseñé para que contenga un dato real (el Pedido) y el puntero al
 * siguiente nodo,
 * demostrando cómo se maneja la memoria sin usar librerías prefabricadas.
 */
public class NodoPedido {
    // El dato principal de nuestro nodo: la información del pedido de la base de
    // datos
    private Pedido pedido;

    // El "enganche" o puntero al siguiente nodo en la memoria (el siguiente en la
    // fila)
    private NodoPedido siguiente;

    public NodoPedido(Pedido pedido) {
        this.pedido = pedido;
        // Cuando creo un nodo nuevo, siempre nace "desenganchado" (apuntando a null)
        this.siguiente = null;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public NodoPedido getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoPedido siguiente) {
        this.siguiente = siguiente;
    }
}
