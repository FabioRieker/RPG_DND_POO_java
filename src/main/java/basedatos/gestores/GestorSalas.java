package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Facilita la consulta de la configuración de las 20 salas del juego,
 * obteniendo el tipo de sala y recuperando los identificadores de los enemigos.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class GestorSalas {

    /**
     * Consulta el tipo de una sala por su ID (por ejemplo: 'combate', 'descanso', 'jefe').
     *
     * @param idSala Identificador de la sala.
     * @return El tipo de sala como texto, o null si no se encuentra.
     */
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

    /**
     * Actualiza el número de la sala actual de una partida para registrar el avance del jugador.
     *
     * @param idPartida  Identificador de la partida.
     * @param nuevaSala  Número de la sala a la que avanza el jugador.
     * @return true si la actualización fue correcta, false en caso de error.
     */
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

    /**
     * Obtiene la lista de identificadores de los personajes enemigos presentes en una sala.
     *
     * @param idSala Identificador de la sala.
     * @return Lista de IDs de enemigos, o una lista vacía si la sala no tiene ninguno.
     */
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
