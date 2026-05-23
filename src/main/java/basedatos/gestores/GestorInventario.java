package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Administra la mochila de consumibles de una partida específica,
 * permitiendo añadir nuevos objetos, consumirlos y listar el inventario actual.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class GestorInventario {

    /**
     * Inserta un consumible en la mochila de la partida, o incrementa su cantidad si ya existía.
     * 
     * @param idPartida      Identificador de la partida actual.
     * @param idConsumible   Identificador del objeto consumible a añadir.
     * @param cantidadAñadir Número de unidades a sumar al inventario.
     * @return true si la operación se completó correctamente, false en caso de error.
     */
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
            System.out.println("Error al añadir a la mochila: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reduce en 1 la cantidad de un consumible en la mochila. 
     * Si la cantidad llega a 0, elimina el registro completo para mantener la base de datos limpia.
     * 
     * @param idPartida    Identificador de la partida.
     * @param idConsumible Identificador del objeto a consumir.
     * @return true si se pudo consumir (había stock), false si ocurrió un error.
     */
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
            System.out.println("Error al consumir objeto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene la lista completa de consumibles disponibles para una partida concreta.
     * 
     * @param idPartida Identificador de la partida.
     * @return Un mapa (Map) donde la clave es el nombre del consumible y el valor es la cantidad disponible.
     */
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
            System.out.println("Error al leer inventario: " + e.getMessage());
        }
        return inventario;
    }
}