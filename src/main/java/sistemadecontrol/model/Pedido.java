package sistemadecontrol.model;

// Clase que representa un pedido del sistema
public class Pedido {
    private int id;
    private String descripcion;
    private String direccion;
    private String estado;              // PENDIENTE, EN_CAMINO, ENTREGADO
    private int repartidorId;
    private String nombreRepartidor;    // Para mostrar en la tabla
    private String fechaCreacion;

    // Constructor vacio (necesario para crear objetos antes de llenarlos)
    public Pedido() {}

    // Constructor para registrar un pedido nuevo
    public Pedido(String descripcion, String direccion) {
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.estado = "PENDIENTE";
    }

    // --- Getters y Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getRepartidorId() { return repartidorId; }
    public void setRepartidorId(int repartidorId) { this.repartidorId = repartidorId; }

    public String getNombreRepartidor() { return nombreRepartidor; }
    public void setNombreRepartidor(String nombreRepartidor) { this.nombreRepartidor = nombreRepartidor; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
