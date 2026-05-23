package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Centraliza la extracción de estadísticas base y atributos de los personajes
 * desde el catálogo en base de datos hacia la memoria del programa.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class GestorPersonajes {

    /**
     * Carga del catálogo de personajes todas las estadísticas base de un personaje concreto.
     *
     * @param idPersonaje Identificador del personaje en la tabla Personajes.
     * @return Mapa con las estadísticas clave (fuerza, destreza, vida_max, etc.) y sus valores.
     */
    public Map<String, Integer> cargarEstadisticas(int idPersonaje) {
        String sql = "SELECT fuerza, destreza, constitucion, inteligencia, vida_max, mana_max, energia_max, defensa_base "
                +
                "FROM Personajes WHERE ID_personaje = ?";
        Map<String, Integer> stats = new HashMap<>();
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPersonaje);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("fuerza", rs.getInt("fuerza"));
                    stats.put("destreza", rs.getInt("destreza"));
                    stats.put("constitucion", rs.getInt("constitucion"));
                    stats.put("inteligencia", rs.getInt("inteligencia"));
                    stats.put("vida_max", rs.getInt("vida_max"));
                    stats.put("mana_max", rs.getInt("mana_max"));
                    stats.put("energia_max", rs.getInt("energia_max"));
                    stats.put("defensa_base", rs.getInt("defensa_base"));
                }
            }
        } catch (SQLException e) {
            System.out.println(" Error al cargar estadísticas: " + e.getMessage());
        }
        return stats;
    }

    /**
     * Imprime por consola la lista de héroes disponibles para seleccionar al inicio de la partida,
     * mostrando su ID, nombre y clase.
     */
    public void listarHeroesSeleccionables() {
        String sql = "SELECT ID_personaje, nombre, tipo_clase FROM Personajes WHERE es_monstruo = FALSE";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            System.out.println("\nHEROES DISPONIBLES");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("ID_personaje") +
                        " | Nombre: " + rs.getString("nombre") +
                        " | Clase: " + rs.getString("tipo_clase"));
            }
            System.out.println("--------------------------------\n");
        } catch (SQLException e) {
            System.out.println("Error al listar héroes: " + e.getMessage());
        }
    }

    /**
     * Comprueba si un personaje del catálogo es un monstruo o un héroe jugable.
     *
     * @param idPersonaje Identificador del personaje.
     * @return true si el personaje es un monstruo, false si es un héroe.
     */
    public boolean esMonstruo(int idPersonaje) {
        String sql = "SELECT es_monstruo FROM Personajes WHERE ID_personaje = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPersonaje);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getBoolean("es_monstruo");
            }
        } catch (SQLException e) {
            System.out.println(" Error al verificar monstruo: " + e.getMessage());
        }
        return false;
    }
}