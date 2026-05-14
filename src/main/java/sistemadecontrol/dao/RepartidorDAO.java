package sistemadecontrol.dao;

import sistemadecontrol.model.Repartidor;
import java.sql.*;
import java.util.ArrayList;

// Clase que maneja las consultas de repartidores y la autenticacion
public class RepartidorDAO {

    // Obtiene todos los repartidores activos para el JComboBox
    public ArrayList<Repartidor> listarRepartidores() {
        ArrayList<Repartidor> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, telefono FROM repartidores WHERE activo = TRUE ORDER BY nombre";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Repartidor rep = new Repartidor();
                rep.setId(rs.getInt("id"));
                rep.setNombre(rs.getString("nombre"));
                rep.setTelefono(rs.getString("telefono"));
                rep.setActivo(true);
                lista.add(rep);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar repartidores: " + e.getMessage());
        }
        return lista;
    }

    // Valida si el usuario y clave existen en la tabla usuarios
    public boolean validarUsuario(String usuario, String clave) {
        String sql = "SELECT id FROM usuarios WHERE usuario = ? AND clave = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, clave);
            ResultSet rs = ps.executeQuery();

            // Si encontramos al menos una fila, las credenciales son validas
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Error al validar usuario: " + e.getMessage());
            return false;
        }
    }
}
