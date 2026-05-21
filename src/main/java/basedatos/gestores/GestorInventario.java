package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GestorInventario {

    public boolean anadirConsumible(int idPartida, int idConsumible, int cantidadAñadir) {
        String sql = "INSERT INTO Mochila_Consumibles (id_partida, id_consumible, cantidad) " +
                "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE cantidad = cantidad + ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPartida);
            ps.setInt(2, idConsumible);
            ps.setInt(3, cantidadAñadir);
            ps.setInt(4, cantidadAñadir);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al añadir a la mochila: " + e.getMessage());
            return false;
        }
    }

    public boolean consumirObjeto(int idPartida, int idConsumible) {
        String sqlUpdate = "UPDATE Mochila_Consumibles SET cantidad = cantidad - 1 " +
                "WHERE id_partida = ? AND id_consumible = ? AND cantidad > 0";
        String sqlDelete = "DELETE FROM Mochila_Consumibles WHERE id_partida = ? AND cantidad <= 0";

        try (Connection con = ConexionBD.getConexion()) {
            try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {
                psUpdate.setInt(1, idPartida);
                psUpdate.setInt(2, idConsumible);
                psUpdate.executeUpdate();
            }
            try (PreparedStatement psDelete = con.prepareStatement(sqlDelete)) {
                psDelete.setInt(1, idPartida);
                psDelete.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error al consumir objeto: " + e.getMessage());
            return false;
        }
    }

    public Map<String, Integer> obtenerInventario(int idPartida) {
        String sql = "SELECT c.nombre, m.cantidad FROM Mochila_Consumibles m " +
                "JOIN Consumibles c ON m.id_consumible = c.ID_consumible " +
                "WHERE m.id_partida = ?";
        Map<String, Integer> inventario = new HashMap<>();
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPartida);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inventario.put(rs.getString("nombre"), rs.getInt("cantidad"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al leer inventario: " + e.getMessage());
        }
        return inventario;
    }
}