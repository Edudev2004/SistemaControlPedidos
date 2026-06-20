package sistemadecontrol.estructuras;

import sistemadecontrol.model.Pedido;

/**
 * ColaPedidos: Esta es mi implementación principal de una Cola Dinámica.
 * (Unidad 3: Colas mediante Listas Enlazadas y FIFO).
 * Decidí construirla desde cero con nodos y punteros para el curso, en lugar de
 * usar utilidades automáticas de Java, para tener control total de los datos.
 */
public class ColaPedidos {
    private NodoPedido frente; // Apunta al primer pedido de la fila (el que más ha esperado)
    private NodoPedido fin; // Apunta al último pedido en llegar a la fila
    private int tamaño; // Llevo el conteo para poder mostrar cuántos faltan

    public ColaPedidos() {
        this.frente = null;
        this.fin = null;
        this.tamaño = 0;
    }

    // Método ENCOLAR (Enqueue): Agregar un pedido al final de la fila
    public void encolar(Pedido pedido) {
        // Creo un nuevo "vagón" para este pedido
        NodoPedido nuevoNodo = new NodoPedido(pedido);

        if (estaVacia()) {
            // Si la cola está vacía, este nuevo nodo es tanto el primero como el último
            frente = nuevoNodo;
            fin = nuevoNodo;
        } else {
            // Si ya hay gente en la fila, al último actual le engancho este nuevo nodo
            // detrás
            fin.setSiguiente(nuevoNodo);
            // Y ahora actualizo mi puntero "fin" para que reconozca al nuevo último
            fin = nuevoNodo;
        }
        tamaño++;
    }

    // Método DESENCOLAR (Dequeue): Sacar y atender al pedido del inicio
    public Pedido desencolar() {

        if (estaVacia())
            return null;

        // Guardo la información del pedido que está al frente para poder devolverla
        Pedido pedidoAtendido = frente.getPedido();

        // El nuevo frente ahora será el que estaba segundo en la fila (avanzamos la
        // fila)
        frente = frente.getSiguiente();

        // Si después de avanzar resulta que ya no hay nadie, limpio el fin también
        if (frente == null) {
            fin = null;
        }

        tamaño--; // Restamos 1 al contador
        return pedidoAtendido; // Devuelvo el pedido para que el repartidor se lo lleve
    }

    public boolean estaVacia() {
        return frente == null; // Si no hay nadie al frente, la cola está vacía por lógica
    }

    public int getTamaño() {
        return tamaño;
    }
}
