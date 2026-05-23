package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maneja el ciclo de vida de las partidas, desde su creación inicial hasta
 * el guardado de progreso y la restauración del estado de los héroes.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class GestorPartidas {

    /**
     * Comprueba si un usuario ya tiene una partida con el mismo nombre.
     * 
     * @param nombrePartida Nombre propuesto para la nueva partida.
     * @param idUsuario     Identificador del usuario creador.
     * @return true si el nombre ya existe para ese usuario, false si está disponible.
     */
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

    /**
     * Crea una nueva partida en la BD inicializándola en la sala 1 y en estado 'activa'.
     * 
     * @param nombrePartida Nombre asignado a la partida.
     * @param idUsuario     Identificador del usuario al que pertenece.
     * @param idDificultad  Nivel de dificultad elegido.
     * @return El ID de la partida recién creada, o -1 en caso de error.
     */
    public int crearNuevaPartida(String nombrePartida, int idUsuario, int idDificultad) {
        String sql = "INSERT INTO Partidas (nombre_partida, usuario_id, dificultad_id, estado, sala_actual) VALUES (?, ?, ?, 'activa', 1)";
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

    /**
     * Guarda el progreso actual del jugador empleando una transacción segura.
     * Actualiza tanto la sala actual como el estado vital del héroe para evitar
     * pérdida de datos en caso de desconexión.
     * 
     * @param idPartida    Identificador único de la partida en curso.
     * @param idSalaActual Número de la sala (1-20) en la que se encuentra el jugador.
     * @param puntuacion   Puntos acumulados en esta partida.
     * @param idHeroe      Identificador de la clase del personaje principal.
     * @param vidaActual   Puntos de vida restantes.
     * @param manaActual   Puntos de maná restantes.
     * @param energiaActual Puntos de energía restantes.
     * @return true si la transacción se consolida en la BD, false si se aplicó un rollback.
     */
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

    /**
     * Registra eventos importantes de la partida en el historial de acciones.
     * 
     * @param idPartida   Identificador de la partida en curso.
     * @param turno       Número del turno actual del combate o evento.
     * @param accion      Descripción de la acción a registrar.
     */
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

    /**
     * Consulta la base de datos para recuperar y aplicar el estado vital de los héroes de una partida.
     * 
     * @param idPartida Identificador de la partida a cargar.
     * @param heroes    Array de héroes cuyo estado se actualizará en memoria.
     */
    public void restaurarEstadoHeroes(int idPartida, personajes.Personaje[] heroes) {
        String sql = "SELECT id_personaje, vida_actual, mana_actual, energia_actual FROM Situacion_heroe WHERE id_partida = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPartida);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id_personaje = rs.getInt("id_personaje"); // El ID va del 1 al 5 según el orden del equipo.
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