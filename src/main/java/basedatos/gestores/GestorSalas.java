package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestorSalas {

    // Obtiene el tipo de sala basado en su ID
    public String obtenerTipoSala(int idSala) {
        String sql = "SELECT tipo FROM Salas WHERE ID_sala = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSala);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tipo");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener tipo de sala: " + e.getMessage());
        }
        return null;
    }

    // Avanza la sala actual de la partida
    public boolean avanzarSala(int idPartida, int nuevaSala) {
        String sql = "UPDATE Partidas SET sala_actual = ? WHERE ID_partida = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevaSala);
            ps.setInt(2, idPartida);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al avanzar sala: " + e.getMessage());
            return false;
        }
    }

    // Obtiene la lista de ID de enemigos en una sala especifica
    public List<Integer> obtenerEnemigosSala(int idSala) {
        List<Integer> enemigos = new ArrayList<>();
        String sql = "SELECT id_personaje FROM Gestor_Personajes WHERE id_sala = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSala);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enemigos.add(rs.getInt("id_personaje"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener enemigos de sala: " + e.getMessage());
        }
        return enemigos;
    }
}
