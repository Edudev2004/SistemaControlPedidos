package sistemadecontrol.dao;

import sistemadecontrol.model.Pedido;
import java.sql.*;
import java.util.ArrayList;

// Clase que maneja todas las operaciones de pedidos en la base de datos
public class PedidoDAO {

    // Registra un nuevo pedido en la base de datos
    public boolean insertarPedido(Pedido pedido) {
        String sql = "INSERT INTO pedidos (descripcion, direccion, estado) VALUES (?, ?, 'PENDIENTE')";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Seteamos los parametros del query
            ps.setString(1, pedido.getDescripcion());
            ps.setString(2, pedido.getDireccion());

            // Ejecutamos el query de insercion
            ps.executeUpdate();
            System.out.println("Pedido registrado correctamente");
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar pedido: " + e.getMessage());
            return false;
        }
    }

    // Obtiene todos los pedidos con el nombre del repartidor asignado
    public ArrayList<Pedido> listarPedidos() {
        ArrayList<Pedido> lista = new ArrayList<>();
        // JOIN para traer el nombre del repartidor junto con el pedido
        String sql = "SELECT p.id, p.descripcion, p.direccion, p.estado, "
                   + "p.repartidor_id, r.nombre AS nombre_repartidor, "
                   + "TO_CHAR(p.fecha_creacion, 'DD/MM/YYYY HH24:MI') AS fecha "
                   + "FROM pedidos p "
                   + "LEFT JOIN repartidores r ON p.repartidor_id = r.id "
                   + "ORDER BY p.id DESC";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Recorremos los resultados de la consulta
            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setDescripcion(rs.getString("descripcion"));
                pedido.setDireccion(rs.getString("direccion"));
                pedido.setEstado(rs.getString("estado"));
                pedido.setRepartidorId(rs.getInt("repartidor_id"));
                pedido.setNombreRepartidor(rs.getString("nombre_repartidor"));
                pedido.setFechaCreacion(rs.getString("fecha"));
                lista.add(pedido);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar pedidos: " + e.getMessage());
        }
        return lista;
    }

    // Asigna un repartidor a un pedido y cambia su estado a EN_CAMINO
    public boolean asignarRepartidor(int pedidoId, int repartidorId) {
        String sql = "UPDATE pedidos SET repartidor_id = ?, estado = 'EN_CAMINO' WHERE id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, repartidorId);
            ps.setInt(2, pedidoId);
            ps.executeUpdate();
            System.out.println("Repartidor asignado al pedido #" + pedidoId);
            return true;

        } catch (SQLException e) {
            System.out.println("Error al asignar repartidor: " + e.getMessage());
            return false;
        }
    }

    // Cambia el estado de un pedido (ej: de EN_CAMINO a ENTREGADO)
    public boolean actualizarEstado(int pedidoId, String nuevoEstado) {
        String sql = "UPDATE pedidos SET estado = ? WHERE id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, pedidoId);
            ps.executeUpdate();
            System.out.println("Pedido #" + pedidoId + " actualizado a " + nuevoEstado);
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar estado: " + e.getMessage());
            return false;
        }
    }

    // Busca pedidos que contengan el texto en la descripcion o direccion
    public ArrayList<Pedido> buscarPedidos(String texto) {
        ArrayList<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.descripcion, p.direccion, p.estado, "
                   + "p.repartidor_id, r.nombre AS nombre_repartidor, "
                   + "TO_CHAR(p.fecha_creacion, 'DD/MM/YYYY HH24:MI') AS fecha "
                   + "FROM pedidos p "
                   + "LEFT JOIN repartidores r ON p.repartidor_id = r.id "
                   + "WHERE LOWER(p.descripcion) LIKE ? OR LOWER(p.direccion) LIKE ? "
                   + "ORDER BY p.id DESC";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String busqueda = "%" + texto.toLowerCase() + "%";
            ps.setString(1, busqueda);
            ps.setString(2, busqueda);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setDescripcion(rs.getString("descripcion"));
                pedido.setDireccion(rs.getString("direccion"));
                pedido.setEstado(rs.getString("estado"));
                pedido.setRepartidorId(rs.getInt("repartidor_id"));
                pedido.setNombreRepartidor(rs.getString("nombre_repartidor"));
                pedido.setFechaCreacion(rs.getString("fecha"));
                lista.add(pedido);
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar pedidos: " + e.getMessage());
        }
        return lista;
    }
}
