package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GestorEstadoHeroe {

    // Actualiza los atributos vitales de un personaje en combate
    public boolean actualizarAtributosVitales(int idPartida, int idPersonaje, int vida, int mana, int energia) {
        String sql = "UPDATE Situacion_heroe SET vida_actual = ?, mana_actual = ?, energia_actual = ? " +
                "WHERE id_partida = ? AND id_personaje = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, vida);
            ps.setInt(2, mana);
            ps.setInt(3, energia);
            ps.setInt(4, idPartida);
            ps.setInt(5, idPersonaje);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar atributos vitales: " + e.getMessage());
            return false;
        }
    }

    // Mata al personaje actualizando su estado
    public boolean matarPersonaje(int idPartida, int idPersonaje) {
        String sql = "UPDATE Situacion_heroe SET vivo = FALSE, vida_actual = 0 " +
                "WHERE id_partida = ? AND id_personaje = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPartida);
            ps.setInt(2, idPersonaje);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al matar personaje: " + e.getMessage());
            return false;
        }
    }

    // Aplica un estado alterado a un personaje
    public boolean aplicarEstadoAlterado(int idPersonaje, int idEstado) {
        String sql = "INSERT IGNORE INTO Gestor_estados (id_personaje, id_estado) VALUES (?, ?)";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPersonaje);
            ps.setInt(2, idEstado);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al aplicar estado alterado: " + e.getMessage());
            return false;
        }
    }

    // Limpia todos los estados de un personaje
    public boolean limpiarEstados(int idPersonaje) {
        String sql = "DELETE FROM Gestor_estados WHERE id_personaje = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPersonaje);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al limpiar estados: " + e.getMessage());
            return false;
        }
    }
}
