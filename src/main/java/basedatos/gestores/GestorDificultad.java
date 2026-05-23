package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Proporciona el escalado estadístico correspondiente a la configuración
 * elegida por el jugador (Fácil, Normal, Difícil).
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class GestorDificultad {

    /**
     * Obtiene el multiplicador de estadísticas asociado a un nivel de dificultad.
     * Este valor escalará tanto el daño como la vida máxima de los enemigos.
     * 
     * @param idDificultad ID de la tabla Dificultades (Ej: 1=Normal, 2=Fácil).
     * @return El factor de multiplicación tipo double (por defecto 1.0).
     */
    public double obtenerMultiplicador(int idDificultad) {
        String sql = "SELECT mult_vida FROM Dificultades WHERE ID_dificultad = ?";
        double multiplicador = 1.0; // Valor base por defecto (Normal)

        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDificultad);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    multiplicador = rs.getDouble("mult_vida");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener dificultad: " + e.getMessage());
        }
        return multiplicador;
    }
}