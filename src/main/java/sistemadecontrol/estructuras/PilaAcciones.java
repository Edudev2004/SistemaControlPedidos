package sistemadecontrol.estructuras;

/**
 * PilaAcciones: Implementación de una Pila Dinámica (LIFO - Last In, First
 * Out).
 * (Unidad 3: Pilas).
 * Se usa para guardar el historial y poder "Deshacer" la última acción (como
 * Ctrl+Z).
 */
public class PilaAcciones {
    private NodoPila tope; // El plato que está hasta arriba de la pila

    public PilaAcciones() {
        this.tope = null;
    }

    // Método PUSH: Poner una nueva acción encima de la pila
    public void push(Accion accion) {
        NodoPila nuevoNodo = new NodoPila(accion);

        // El nuevo nodo se pone arriba, así que lo que haya abajo será su "siguiente"
        nuevoNodo.setSiguiente(tope);
        // Ahora el nuevo "tope" oficial es este nodo que acabamos de meter
        tope = nuevoNodo;
    }

    // Método POP: Sacar la última acción que pusimos en la pila
    public Accion pop() {
        // Si no hay nada para deshacer
        if (estaVacia())
            return null;

        // Agarramos la información del nodo que está arriba
        Accion accionDeshacer = tope.getAccion();

        // El nuevo tope pasa a ser el nodo que estaba justo debajo de este
        tope = tope.getSiguiente();

        // Devolvemos la acción para poder revertirla en la base de datos
        return accionDeshacer;
    }

    public boolean estaVacia() {
        return tope == null;
    }
}
