package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestorDificultad {

    // Devuelve el multiplicador de dificultad buscando por el ID
    // Como en la BD mult_vida y mult_dano son iguales, nos vale con coger uno
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
            System.err.println("error al obtener dificultad: " + e.getMessage());
        }
        return multiplicador;
    }
}