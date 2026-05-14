package sistemadecontrol.controller;

import sistemadecontrol.dao.PedidoDAO;
import sistemadecontrol.dao.RepartidorDAO;
import sistemadecontrol.model.Pedido;
import sistemadecontrol.model.Repartidor;
import sistemadecontrol.view.PanelPrincipal;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

// Controlador principal que conecta la vista con la logica de datos
public class PedidoController {
    private PanelPrincipal vista;
    private PedidoDAO pedidoDAO;
    private RepartidorDAO repartidorDAO;

    public PedidoController(PanelPrincipal vista) {
        this.vista = vista;
        this.pedidoDAO = new PedidoDAO();
        this.repartidorDAO = new RepartidorDAO();

        // Conectamos cada boton con su accion
        this.vista.btnRegistrar.addActionListener(e -> registrarPedido());
        this.vista.btnAsignar.addActionListener(e -> asignarRepartidor());
        this.vista.btnEntregado.addActionListener(e -> marcarEntregado());
        this.vista.btnBuscar.addActionListener(e -> buscarPedidos());
        this.vista.btnRefrescar.addActionListener(e -> cargarTabla());

        // Enter en el campo de descripcion para registrar rapido
        this.vista.txtDescripcion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    registrarPedido();
                }
            }
        });

        // Enter en el campo de busqueda para buscar rapido
        this.vista.txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarPedidos();
                }
            }
        });

        // Doble clic en una fila para ver detalle
        this.vista.tablaPedidos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    mostrarDetalle();
                }
            }
        });

        // Cargamos los datos iniciales
        cargarRepartidores();
        cargarTabla();
    }

    // Registra un nuevo pedido en la base de datos
    private void registrarPedido() {
        // Capturar datos del formulario
        String descripcion = vista.txtDescripcion.getText().trim();
        String direccion = vista.txtDireccion.getText().trim();

        // Validar campos vacios
        if (descripcion.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Complete la descripcion y la direccion",
                "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Crear objeto y guardar en BD
        Pedido nuevoPedido = new Pedido(descripcion, direccion);
        boolean exito = pedidoDAO.insertarPedido(nuevoPedido);

        if (exito) {
            // Limpiar campos del formulario
            vista.txtDescripcion.setText("");
            vista.txtDireccion.setText("");
            vista.txtDescripcion.requestFocus();

            // Refrescar lista de pedidos
            cargarTabla();
            JOptionPane.showMessageDialog(vista, "Pedido registrado exitosamente",
                "Exito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(vista, "Error al registrar el pedido",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Asigna un repartidor al pedido seleccionado
    private void asignarRepartidor() {
        // Obtener fila seleccionada
        int filaSeleccionada = vista.tablaPedidos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un pedido de la tabla",
                "Sin seleccion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener datos del pedido seleccionado
        int pedidoId = (int) vista.modeloTabla.getValueAt(filaSeleccionada, 0);
        String estadoActual = (String) vista.modeloTabla.getValueAt(filaSeleccionada, 3);

        // Validamos que el pedido este PENDIENTE
        if (!estadoActual.equals("PENDIENTE")) {
            JOptionPane.showMessageDialog(vista, "Solo se puede asignar a pedidos PENDIENTES",
                "Estado invalido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar seleccion de repartidor
        Repartidor repartidor = (Repartidor) vista.cboRepartidores.getSelectedItem();
        if (repartidor == null) {
            JOptionPane.showMessageDialog(vista, "Seleccione un repartidor",
                "Sin repartidor", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Actualizar asignacion en BD
        boolean exito = pedidoDAO.asignarRepartidor(pedidoId, repartidor.getId());
        if (exito) {
            cargarTabla();
        }
    }

    // Marca un pedido como entregado
    private void marcarEntregado() {
        // Paso 1: Verificamos que haya una fila seleccionada
        int filaSeleccionada = vista.tablaPedidos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un pedido de la tabla",
                "Sin seleccion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Paso 2: Verificamos que el pedido este EN_CAMINO
        int pedidoId = (int) vista.modeloTabla.getValueAt(filaSeleccionada, 0);
        String estadoActual = (String) vista.modeloTabla.getValueAt(filaSeleccionada, 3);

        if (!estadoActual.equals("EN_CAMINO")) {
            JOptionPane.showMessageDialog(vista, "Solo se puede entregar pedidos EN CAMINO",
                "Estado invalido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Paso 3: Actualizamos el estado en la BD
        boolean exito = pedidoDAO.actualizarEstado(pedidoId, "ENTREGADO");
        if (exito) {
            cargarTabla();
        }
    }

    // Busca pedidos por texto en la descripcion o direccion
    private void buscarPedidos() {
        String texto = vista.txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            cargarTabla();
            return;
        }

        ArrayList<Pedido> resultados = pedidoDAO.buscarPedidos(texto);
        llenarTabla(resultados);
    }

    // Muestra un detalle del pedido al hacer doble clic
    private void mostrarDetalle() {
        int fila = vista.tablaPedidos.getSelectedRow();
        if (fila == -1) return;

        String detalle = "Pedido #" + vista.modeloTabla.getValueAt(fila, 0) + "\n"
            + "Descripcion: " + vista.modeloTabla.getValueAt(fila, 1) + "\n"
            + "Direccion: " + vista.modeloTabla.getValueAt(fila, 2) + "\n"
            + "Estado: " + vista.modeloTabla.getValueAt(fila, 3) + "\n"
            + "Repartidor: " + (vista.modeloTabla.getValueAt(fila, 4) != null
                ? vista.modeloTabla.getValueAt(fila, 4) : "Sin asignar") + "\n"
            + "Fecha: " + vista.modeloTabla.getValueAt(fila, 5);

        JOptionPane.showMessageDialog(vista, detalle, "Detalle del Pedido",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // Carga los repartidores de la BD al JComboBox
    private void cargarRepartidores() {
        ArrayList<Repartidor> repartidores = repartidorDAO.listarRepartidores();
        vista.cboRepartidores.removeAllItems();
        for (Repartidor rep : repartidores) {
            vista.cboRepartidores.addItem(rep);
        }
    }

    // Carga todos los pedidos de la BD a la tabla
    private void cargarTabla() {
        ArrayList<Pedido> pedidos = pedidoDAO.listarPedidos();
        llenarTabla(pedidos);
    }

    // Metodo auxiliar que llena la tabla con una lista de pedidos
    private void llenarTabla(ArrayList<Pedido> pedidos) {
        // Limpiamos las filas actuales
        vista.modeloTabla.setRowCount(0);

        // Contadores para la barra de estado
        int pendientes = 0, enCamino = 0, entregados = 0;

        // Agregamos cada pedido como una fila
        for (Pedido p : pedidos) {
            Object[] fila = {
                p.getId(),
                p.getDescripcion(),
                p.getDireccion(),
                p.getEstado(),
                p.getNombreRepartidor() != null ? p.getNombreRepartidor() : "Sin asignar",
                p.getFechaCreacion()
            };
            vista.modeloTabla.addRow(fila);

            // Contamos por estado
            switch (p.getEstado()) {
                case "PENDIENTE": pendientes++; break;
                case "EN_CAMINO": enCamino++; break;
                case "ENTREGADO": entregados++; break;
            }
        }

        // Actualizamos la barra de estado
        vista.lblEstado.setText("  Total: " + pedidos.size()
            + " | Pendientes: " + pendientes
            + " | En camino: " + enCamino
            + " | Entregados: " + entregados);
    }
}
