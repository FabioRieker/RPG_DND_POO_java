package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestorRecompensas {

    // Desbloquea un logro para una partida especifica
    public boolean desbloquearLogro(int idPartida, int idLogro) {
        String sql = "INSERT IGNORE INTO Partida_Logros (partida_id, logro_id) VALUES (?, ?)";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPartida);
            ps.setInt(2, idLogro);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al desbloquear logro: " + e.getMessage());
            return false;
        }
    }

    // añade un arma a la mochila, si ya existe en esa partida, suma la cantidad
    public boolean anadirArma(int idPartida, int idArma, int cantidad) {
        String sql = "INSERT INTO Mochila_Armas (id_partida, id_arma, cantidad) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE cantidad = cantidad + ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPartida);
            ps.setInt(2, idArma);
            ps.setInt(3, cantidad);
            ps.setInt(4, cantidad);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Error al añadir arma a la mochila: " + e.getMessage());
            return false;
        }
    }

    // Obtiene los puntos de un logro a traves de su ID
    public int obtenerPuntosLogro(int idLogro) {
        String sql = "SELECT puntos FROM Logros WHERE ID_logro = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLogro);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("puntos");
                }
            }
        } catch (SQLException e) {
            System.err.println(" Error al obtener puntos del logro: " + e.getMessage());
        }
        return 0; // Por defecto retorna 0 si no se encuentra
    }

    // Verifica si el jugador tiene 4 o mas armas distintas y le da el logro
    public void verificarColeccionistaArmas(int idPartida) {
        String sql = "SELECT COUNT(DISTINCT id_arma) AS num_armas FROM Mochila_Armas WHERE id_partida = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idPartida);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt("num_armas") >= 4) {
                    desbloquearLogro(idPartida, 14); // Logro 14: Coleccionista de Arsenal
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar coleccionista de armas: " + e.getMessage());
        }
    }

    public int obtenerIdArmaPorNombre(String nombre) {
        String sql = "SELECT ID_arma FROM Armas WHERE nombre = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("ID_arma");
            }
        } catch (SQLException e) {}
        return 1;
    }
}
