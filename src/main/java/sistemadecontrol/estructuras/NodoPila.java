package sistemadecontrol.estructuras;

/**
 * NodoPila: Representa los elementos de nuestra Pila (Stack).
 * (Unidad 3: Pilas con Listas Enlazadas).
 * A diferencia de la Cola, la Pila crece hacia arriba.
 */
public class NodoPila {
    private Accion accion;
    private NodoPila siguiente; // Apunta al nodo que quedó DEBAJO de este en la pila

    public NodoPila(Accion accion) {
        this.accion = accion;
        this.siguiente = null;
    }

    public Accion getAccion() {
        return accion;
    }

    public NodoPila getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoPila siguiente) {
        this.siguiente = siguiente;
    }
}
