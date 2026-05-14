package sistemadecontrol.model;

// Clase que representa un repartidor
public class Repartidor {
    private int id;
    private String nombre;
    private String telefono;
    private boolean activo;

    // Constructor vacio
    public Repartidor() {}

    // Constructor con datos
    public Repartidor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // --- Getters y Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    // Esto es lo que se muestra en el JComboBox automaticamente
    @Override
    public String toString() {
        return nombre;
    }
}
