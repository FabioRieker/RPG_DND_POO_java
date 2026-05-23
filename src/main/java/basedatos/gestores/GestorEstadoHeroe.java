package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Actualiza dinámicamente la situación vital y los estados alterados de un 
 * personaje dentro de una partida activa.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class GestorEstadoHeroe {

    /**
     * Actualiza los puntos de vida, maná y energía de un personaje dentro de una partida activa.
     *
     * @param idPartida   Identificador de la partida.
     * @param idPersonaje Identificador del personaje a actualizar.
     * @param vida        Puntos de vida actuales.
     * @param mana        Puntos de maná actuales.
     * @param energia     Puntos de energía actuales.
     * @return true si la actualización fue correcta, false si hubo un error.
     */
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

    /**
     * Marca al personaje como muerto en la base de datos, poniendo su vida a 0.
     *
     * @param idPartida   Identificador de la partida.
     * @param idPersonaje Identificador del personaje a eliminar.
     * @return true si la operación tuvo éxito, false en caso de error.
     */
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

    /**
     * Registra un estado alterado activo sobre un personaje usando INSERT IGNORE
     * para no duplicar si ya lo tenía aplicado.
     *
     * @param idPersonaje Identificador del personaje afectado.
     * @param idEstado    Identificador del estado alterado a aplicar.
     * @return true si se insertó el estado, false si ya existía o hubo error.
     */
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

    /**
     * Elimina todos los estados alterados activos de un personaje.
     *
     * @param idPersonaje Identificador del personaje al que se le limpian los estados.
     * @return true si la operación fue exitosa, false en caso de error.
     */
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
