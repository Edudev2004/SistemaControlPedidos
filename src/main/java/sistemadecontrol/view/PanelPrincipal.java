/**
 *
 * @author 2004rend
 */
package sistemadecontrol.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Ventana principal del sistema con formulario, tabla y acciones
public class PanelPrincipal extends JFrame {

    // --- Componentes del formulario ---
    public JTextField txtDescripcion;
    public JTextField txtDireccion;
    public JButton btnRegistrar;

    // --- Componentes de la tabla ---
    public JTable tablaPedidos;
    public DefaultTableModel modeloTabla;

    // --- Componentes de acciones ---
    public JComboBox<Object> cboRepartidores;
    public JButton btnAsignar;
    public JButton btnEntregado;
    public JTextField txtBuscar;
    public JButton btnBuscar;
    public JButton btnRefrescar;
    public JButton btnDeshacer;

    // --- Barra de estado ---
    public JLabel lblEstado;

    public PanelPrincipal() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        // Configuracion de la ventana principal
        setTitle("Sistema de Control de Pedidos y Repartidores");
        setSize(950, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal
        JPanel contenedor = new JPanel(new BorderLayout(10, 10));
        contenedor.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        contenedor.setBackground(new Color(240, 242, 245));

        // =========================================
        // PANEL SUPERIOR - Formulario de registro
        // =========================================
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210)),
            " Registrar Nuevo Pedido ",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 13),
            new Color(50, 50, 70)
        ));
        panelFormulario.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: Descripcion
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelFormulario.add(crearLabel("Descripcion:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtDescripcion = new JTextField(20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelFormulario.add(txtDescripcion, gbc);

        // Fila 1: Direccion
        gbc.gridx = 2; gbc.weightx = 0;
        panelFormulario.add(crearLabel("Direccion:"), gbc);

        gbc.gridx = 3; gbc.weightx = 1;
        txtDireccion = new JTextField(20);
        txtDireccion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelFormulario.add(txtDireccion, gbc);

        // Fila 1: Boton registrar
        gbc.gridx = 4; gbc.weightx = 0;
        btnRegistrar = new JButton("Registrar Pedido");
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegistrar.setBackground(new Color(46, 139, 87));
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelFormulario.add(btnRegistrar, gbc);

        contenedor.add(panelFormulario, BorderLayout.NORTH);

        // =========================================
        // PANEL CENTRAL - Tabla de pedidos
        // =========================================
        String[] columnas = {"ID", "Descripcion", "Direccion", "Estado", "Repartidor", "Fecha"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            // Hacemos que la tabla NO sea editable directamente
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPedidos = new JTable(modeloTabla);
        tablaPedidos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaPedidos.setRowHeight(28);
        tablaPedidos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaPedidos.getTableHeader().setBackground(new Color(50, 50, 70));
        tablaPedidos.getTableHeader().setForeground(Color.BLACK);
        tablaPedidos.setSelectionBackground(new Color(200, 220, 240));
        tablaPedidos.setGridColor(new Color(220, 220, 230));
        tablaPedidos.setForeground(Color.BLACK);

        // Ajustamos el ancho de las columnas
        tablaPedidos.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        tablaPedidos.getColumnModel().getColumn(1).setPreferredWidth(200);  // Descripcion
        tablaPedidos.getColumnModel().getColumn(2).setPreferredWidth(180);  // Direccion
        tablaPedidos.getColumnModel().getColumn(3).setPreferredWidth(90);   // Estado
        tablaPedidos.getColumnModel().getColumn(4).setPreferredWidth(120);  // Repartidor
        tablaPedidos.getColumnModel().getColumn(5).setPreferredWidth(110);  // Fecha

        // Colores por estado en la columna "Estado" (columna 3)
        tablaPedidos.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component celda = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);

                if (!isSelected && value != null) {
                    String estado = value.toString();
                    switch (estado) {
                        case "PENDIENTE":
                            celda.setBackground(new Color(255, 243, 205)); // Amarillo suave
                            break;
                        case "EN_CAMINO":
                            celda.setBackground(new Color(209, 236, 241)); // Azul suave
                            break;
                        case "ENTREGADO":
                            celda.setBackground(new Color(212, 237, 218)); // Verde suave
                            break;
                        default:
                            celda.setBackground(Color.WHITE);
                    }
                    celda.setForeground(Color.BLACK);
                } else if (isSelected) {
                    celda.setBackground(new Color(200, 220, 240));
                    celda.setForeground(Color.BLACK);
                }
                return celda;
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tablaPedidos);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210)));
        contenedor.add(scrollTabla, BorderLayout.CENTER);

        // =========================================
        // PANEL INFERIOR - Acciones y busqueda
        // =========================================
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panelAcciones.setBackground(new Color(240, 242, 245));

        // ComboBox de repartidores
        panelAcciones.add(crearLabel("Repartidor:"));
        cboRepartidores = new JComboBox<>();
        cboRepartidores.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cboRepartidores.setPreferredSize(new Dimension(160, 28));
        panelAcciones.add(cboRepartidores);

        // Boton asignar (Internamente usa la Cola)
        btnAsignar = new JButton("Asignar");
        btnAsignar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAsignar.setBackground(new Color(70, 130, 180));
        btnAsignar.setForeground(Color.BLACK);
        btnAsignar.setFocusPainted(false);
        btnAsignar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelAcciones.add(btnAsignar);

        // Boton entregado
        btnEntregado = new JButton("Marcar Entregado");
        btnEntregado.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEntregado.setBackground(new Color(40, 167, 69));
        btnEntregado.setForeground(Color.BLACK);
        btnEntregado.setFocusPainted(false);
        btnEntregado.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelAcciones.add(btnEntregado);

        // Boton Deshacer (Pila)
        btnDeshacer = new JButton("Deshacer (Pila)");
        btnDeshacer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDeshacer.setBackground(new Color(220, 53, 69)); // Rojo
        btnDeshacer.setForeground(Color.WHITE);
        btnDeshacer.setFocusPainted(false);
        btnDeshacer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelAcciones.add(btnDeshacer);

        // Separador visual
        panelAcciones.add(new JLabel("    |   "));

        // Campo de busqueda
        panelAcciones.add(crearLabel("Buscar:"));
        txtBuscar = new JTextField(12);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelAcciones.add(txtBuscar);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelAcciones.add(btnBuscar);

        btnRefrescar = new JButton("Mostrar Todos");
        btnRefrescar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelAcciones.add(btnRefrescar);

        // Panel que contiene acciones + barra de estado
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBackground(new Color(240, 242, 245));
        panelSur.add(panelAcciones, BorderLayout.NORTH);

        // Barra de estado
        lblEstado = new JLabel("  Total: 0 | Pendientes: 0 | En camino: 0 | Entregados: 0");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEstado.setForeground(new Color(100, 100, 120));
        lblEstado.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 210)));
        panelSur.add(lblEstado, BorderLayout.SOUTH);

        contenedor.add(panelSur, BorderLayout.SOUTH);

        add(contenedor);
        
        // TODO: Agregar validacion de permisos para botones de edicion
    }
    // </editor-fold>

    // Metodo auxiliar para crear labels con estilo consistente
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(50, 50, 70));
        return label;
    }
}
