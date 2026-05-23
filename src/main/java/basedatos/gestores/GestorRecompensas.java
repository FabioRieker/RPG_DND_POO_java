package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controla el desbloqueo de logros, la suma de puntos de galardón y 
 * la gestión del arsenal de armas que el jugador encuentra en su aventura.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class GestorRecompensas {

    /**
     * Desbloquea un logro para una partida específica empleando INSERT IGNORE
     * para evitar errores si el logro ya estaba desbloqueado previamente.
     * 
     * @param idPartida Identificador de la partida.
     * @param idLogro   Identificador del logro a desbloquear.
     * @return true si se ha insertado un nuevo logro, false si ya lo tenía o hubo error.
     */
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

    /**
     * Inserta un arma en la mochila de la partida, o incrementa su cantidad si ya la poseía.
     * 
     * @param idPartida Identificador de la partida.
     * @param idArma    Identificador del arma obtenida.
     * @param cantidad  Número de copias de ese arma a añadir.
     * @return true si la operación en la BD tuvo éxito, false en caso de error.
     */
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

    /**
     * Consulta cuántos puntos de galardón otorga un logro específico.
     * 
     * @param idLogro Identificador del logro.
     * @return Cantidad de puntos que otorga, o 0 si no se encuentra.
     */
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

    /**
     * Comprueba el número de armas distintas en la mochila. Si el jugador ha
     * reunido 4 o más armas diferentes, le otorga el logro "Coleccionista de Arsenal".
     * 
     * @param idPartida Identificador de la partida actual.
     */
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

    /**
     * Busca el identificador numérico de un arma por su nombre en texto.
     * 
     * @param nombre Nombre exacto del arma a buscar.
     * @return El ID numérico del arma, o 1 (Arma base por defecto) si no se encuentra.
     */
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
