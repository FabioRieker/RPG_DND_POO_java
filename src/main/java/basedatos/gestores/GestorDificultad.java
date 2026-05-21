package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestorDificultad {

    // Devuelve los multiplicadores [vida, daño] buscando por el ID de dificultad
    public float[] obtenerMultiplicadores(int idDificultad) {
        String sql = "SELECT mult_vida, mult_dano FROM Dificultades WHERE ID_dificultad = ?";
        float[] multiplicadores = { 1.0f, 1.0f }; // Valores base por defecto

        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDificultad);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    multiplicadores[0] = rs.getFloat("mult_vida");
                    multiplicadores[1] = rs.getFloat("mult_dano");
                }
            }
        } catch (SQLException e) {
            System.err.println("error al obtener dificultad: " + e.getMessage());
        }
        return multiplicadores;
    }
}