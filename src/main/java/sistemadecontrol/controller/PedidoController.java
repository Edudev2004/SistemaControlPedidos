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
import sistemadecontrol.estructuras.ColaPedidos;
import sistemadecontrol.estructuras.PilaAcciones;
import sistemadecontrol.estructuras.Accion;

// Controlador principal que conecta la vista con la logica de datos
public class PedidoController {
    private PanelPrincipal vista;
    private PedidoDAO pedidoDAO;
    private RepartidorDAO repartidorDAO;
    private ColaPedidos colaPendientes;
    private PilaAcciones pilaHistorial;

    public PedidoController(PanelPrincipal vista) {
        this.vista = vista;
        this.pedidoDAO = new PedidoDAO();
        this.repartidorDAO = new RepartidorDAO();
        this.colaPendientes = new ColaPedidos();
        this.pilaHistorial = new PilaAcciones();

        // Conectamos cada boton con su accion
        this.vista.btnRegistrar.addActionListener(e -> registrarPedido());
        this.vista.btnAsignar.addActionListener(e -> asignarRepartidor());
        this.vista.btnEntregado.addActionListener(e -> marcarEntregado());
        this.vista.btnBuscar.addActionListener(e -> buscarPedidos());
        this.vista.btnRefrescar.addActionListener(e -> cargarTabla());
        this.vista.btnDeshacer.addActionListener(e -> deshacerUltimaAccion());

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

    // Asigna un repartidor usando la COLA DINAMICA (FIFO)
    private void asignarRepartidor() {
        // Validar seleccion de repartidor
        Repartidor repartidor = (Repartidor) vista.cboRepartidores.getSelectedItem();
        if (repartidor == null) {
            JOptionPane.showMessageDialog(vista, "Seleccione un repartidor",
                "Sin repartidor", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- APLICACIÓN UNIDAD 3: COLAS ---
        // Profe, aquí es donde entra la utilidad real de la cola. En lugar de permitir
        // que el usuario asigne un repartidor a cualquier pedido al azar de la tabla,
        // lo obligo a hacer un "desencolar()".
        // Esto garantiza la regla de negocio FIFO: el sistema asume siempre el pedido más antiguo.
        Pedido pedidoAtender = colaPendientes.desencolar();
        
        if (pedidoAtender == null) {
            JOptionPane.showMessageDialog(vista, "La cola de pedidos esta vacia. No hay pedidos PENDIENTES.",
                "Cola Vacia", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Actualizar asignacion en BD
        boolean exito = pedidoDAO.asignarRepartidor(pedidoAtender.getId(), repartidor.getId());
        if (exito) {
            // --- APLICACIÓN UNIDAD 3: PUSH ---
            // Guardamos la acción en nuestra pila por si el usuario quiere deshacerla
            pilaHistorial.push(new Accion(pedidoAtender.getId(), "ASIGNACION"));
            
            JOptionPane.showMessageDialog(vista, "Pedido #" + pedidoAtender.getId() + " asignado a " + repartidor.getNombre() + "\nQuedan " + colaPendientes.getTamaño() + " en la cola.",
                "Exito (Cola Avanzó)", JOptionPane.INFORMATION_MESSAGE);
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
            // --- APLICACIÓN UNIDAD 3: PUSH ---
            pilaHistorial.push(new Accion(pedidoId, "ENTREGA"));
            
            cargarTabla();
        }
    }

    // --- APLICACIÓN UNIDAD 3: PILAS (Deshacer LIFO) ---
    private void deshacerUltimaAccion() {
        // Profe, aquí saco el último registro que se hizo (POP)
        Accion ultimaAccion = pilaHistorial.pop();

        if (ultimaAccion == null) {
            JOptionPane.showMessageDialog(vista, "No hay acciones recientes en el historial.",
                "Pila Vacía", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean exito = false;
        // Si lo último fue una ASIGNACIÓN, la revierto (lo pongo PENDIENTE de nuevo)
        if (ultimaAccion.getTipoAccion().equals("ASIGNACION")) {
            exito = pedidoDAO.deshacerAsignacion(ultimaAccion.getPedidoId());
        } 
        // Si lo último fue marcar ENTREGADO, lo regreso a EN_CAMINO
        else if (ultimaAccion.getTipoAccion().equals("ENTREGA")) {
            exito = pedidoDAO.actualizarEstado(ultimaAccion.getPedidoId(), "EN_CAMINO");
        }

        if (exito) {
            JOptionPane.showMessageDialog(vista, "Se deshizo la última acción: " + ultimaAccion.getTipoAccion() + ".",
                "Deshacer (Pila POP)", JOptionPane.INFORMATION_MESSAGE);
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

    // Carga todos los pedidos de la BD a la tabla y reconstruye la cola
    private void cargarTabla() {
        ArrayList<Pedido> pedidos = pedidoDAO.listarPedidos();
        
        // --- APLICACIÓN UNIDAD 3: LLENADO DE LA COLA ---
        // Aquí construyo mi Cola Dinámica basándome en los datos frescos de la BD.
        // La lista 'pedidos' viene con los más antiguos al principio (ASC). 
        // Por tanto, recorro la lista normalmente y los voy encolando (FIFO).
        colaPendientes = new ColaPedidos();
        for (Pedido p : pedidos) {
            if (p.getEstado().equals("PENDIENTE")) {
                colaPendientes.encolar(p); // Encolar() lo manda al final de la fila
            }
        }
        
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
