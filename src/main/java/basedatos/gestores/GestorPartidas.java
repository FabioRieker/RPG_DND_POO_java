package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestorPartidas {

    // Comprueba si un usuario ya tiene una partida con el mismo nombre
    public boolean existeNombrePartida(String nombrePartida, int idUsuario) {
        String sql = "SELECT COUNT(*) FROM Partidas WHERE nombre_partida = ? AND usuario_id = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombrePartida);
            ps.setInt(2, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al comprobar nombre de partida: " + e.getMessage());
        }
        return false;
    }

    // Crea el registro inicial de una partida nueva
    public int crearNuevaPartida(String nombrePartida, int idUsuario, int idDificultad) {
        String sql = "INSERT INTO Partidas (nombre_partida, usuario_id, dificultad_id, estado) VALUES (?, ?, ?, 'activa')";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombrePartida);
            ps.setInt(2, idUsuario);
            ps.setInt(3, idDificultad);

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next())
                        return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creando partida: " + e.getMessage());
        }
        return -1;
    }

    // Guarda la sala, los puntos y el estado del héroe de un
    // solo golpe
    public boolean guardarPartidaCompleta(int idPartida, int idSalaActual, int puntuacion, int idHeroe, int vidaActual,
            int manaActual, int energiaActual) {
        Connection con = ConexionBD.getConexion();
        boolean exito = false;

        String updatePartida = "UPDATE Partidas SET sala_actual = ?, puntuacion = ?, fecha_ultimo_turno = CURRENT_TIMESTAMP WHERE ID_partida = ?";
        String upsertHeroe = "INSERT INTO Situacion_heroe (id_partida, id_personaje, vida_actual, mana_actual, energia_actual) "
                +
                "VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                "vida_actual = VALUES(vida_actual), mana_actual = VALUES(mana_actual), energia_actual = VALUES(energia_actual)";

        try {
            con.setAutoCommit(false);

            try (PreparedStatement psPartida = con.prepareStatement(updatePartida)) {
                psPartida.setInt(1, idSalaActual);
                psPartida.setInt(2, puntuacion);
                psPartida.setInt(3, idPartida);
                psPartida.executeUpdate();
            }

            try (PreparedStatement psHeroe = con.prepareStatement(upsertHeroe)) {
                psHeroe.setInt(1, idPartida);
                psHeroe.setInt(2, idHeroe);
                psHeroe.setInt(3, vidaActual);
                psHeroe.setInt(4, manaActual);
                psHeroe.setInt(5, energiaActual);
                psHeroe.executeUpdate();
            }

            con.commit(); // Si todo va bien, guardamos definitivamente
            exito = true;

        } catch (SQLException e) {
            System.err.println("Error crítico al guardar. Aplicando Rollback: " + e.getMessage());
            try {
                con.rollback(); // Si algo falla, deshacemos todo para evitar datos corruptos
            } catch (SQLException ex) {
                System.err.println("Error en el rollback: " + ex.getMessage());
            }
        } finally {
            try {
                con.setAutoCommit(true); // Devolvemos la conexión a su comportamiento normal
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exito;
    }

    // Registra logs del historial de acciones
    public void registrarLog(int idPartida, int turno, String accion) {
        String sql = "INSERT INTO Historial_Acciones (id_partida, turno, descripcion) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPartida);
            ps.setInt(2, turno);
            ps.setString(3, accion);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error guardando historial: " + e.getMessage());
        }
    }

    // Restaura la vida, maná y energía de los héroes cargando la base de datos
    public void restaurarEstadoHeroes(int idPartida, personajes.Personaje[] heroes) {
        String sql = "SELECT id_personaje, vida_actual, mana_actual, energia_actual FROM Situacion_heroe WHERE id_partida = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPartida);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id_personaje = rs.getInt("id_personaje"); // 1 al 5
                    int indice = id_personaje - 1;
                    if (indice >= 0 && indice < heroes.length) {
                        int vida = rs.getInt("vida_actual");
                        int mana = rs.getInt("mana_actual");
                        int energia = rs.getInt("energia_actual");
                        heroes[indice].cargarEstadoVital(vida, mana, energia);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error restaurando estado de los héroes: " + e.getMessage());
        }
    }
}