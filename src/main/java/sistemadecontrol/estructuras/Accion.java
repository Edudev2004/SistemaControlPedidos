package sistemadecontrol.estructuras;

/**
 * Accion: Representa el dato que vamos a guardar en nuestra Pila de historial.
 * Solo necesitamos recordar qué pedido se alteró y qué tipo de acción fue.
 */
public class Accion {
    private int pedidoId;
    private String tipoAccion; // Ej: "ASIGNACION" o "ENTREGA"

    public Accion(int pedidoId, String tipoAccion) {
        this.pedidoId = pedidoId;
        this.tipoAccion = tipoAccion;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public String getTipoAccion() {
        return tipoAccion;
    }
}
