package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Gestiona el alta, validación y eliminación de usuarios en la base de datos,
 * así como la visualización del ranking global de puntuaciones.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class GestorUsuarios {

    /**
     * Comprueba si un nombre de usuario ya está registrado en la base de datos.
     * 
     * @param nombre El nombre de usuario a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    public boolean existeUsuario(String nombre) {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE nombre_usuario = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al comprobar usuario: " + e.getMessage());
        }
        return false;
    }

    /**
     * Comprueba si una dirección de correo electrónico ya está registrada.
     * 
     * @param email El email a verificar.
     * @return true si el email ya está en uso, false en caso contrario.
     */
    public boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE email = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al comprobar email: " + e.getMessage());
        }
        return false;
    }

    /**
     * Registra un nuevo jugador en la base de datos.
     * 
     * @param nombre   Nombre de usuario deseado (debe ser único).
     * @param password Contraseña de la cuenta.
     * @param email    Correo electrónico (debe ser único).
     * @return El ID autogenerado por MySQL si el registro es exitoso, o -1 si hubo un error.
     */
    public int registrarUsuario(String nombre, String password, String email) {
        String sql = "INSERT INTO Usuarios (nombre_usuario, contraseña, email) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);
            ps.setString(2, password);
            ps.setString(3, email);

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next())
                        return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Comprueba las credenciales del usuario para permitir el acceso al juego.
     * 
     * @param nombre   Nombre de usuario.
     * @param password Contraseña asociada a la cuenta.
     * @return El ID del usuario si las credenciales son correctas, o -1 si fallan.
     */
    public int validarLogin(String nombre, String password) {

        String sql = "SELECT ID_usuario FROM Usuarios WHERE nombre_usuario = ? AND contraseña = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt("ID_usuario");
            }
        } catch (SQLException e) {
            System.err.println("Error en login: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Modifica la contraseña de un usuario existente buscando por su ID.
     * 
     * @param idUsuario     Identificador del usuario en la base de datos.
     * @param nuevaPassword Nueva contraseña a establecer.
     * @return true si la actualización fue exitosa, false si falló.
     */
    public boolean actualizarContraseña(int idUsuario, String nuevaPassword) {
        String sql = "UPDATE Usuarios SET contraseña = ? WHERE ID_usuario = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevaPassword);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina permanentemente la cuenta de un usuario.
     * Esta acción también elimina en cascada todas sus partidas asociadas por integridad referencial.
     * 
     * @param idUsuario Identificador del usuario a borrar.
     * @return true si el borrado fue exitoso, false en caso de error.
     */
    public boolean borrarCuenta(int idUsuario) {
        String sql = "DELETE FROM Usuarios WHERE ID_usuario = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al borrar cuenta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extrae y muestra por consola las 10 mejores puntuaciones históricas
     * agrupadas por usuario para evitar duplicidades del mismo jugador.
     */
    public void mostrarRankingGlobal() {
        String sql = "SELECT u.nombre_usuario, MAX(p.puntuacion) as max_puntos " +
                "FROM Partidas p JOIN Usuarios u ON p.usuario_id = u.ID_usuario " +
                "GROUP BY u.ID_usuario ORDER BY max_puntos DESC LIMIT 10";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            System.out.println("\n=== RANKING GLOBAL TOP 10 ===");
            int pos = 1;
            while (rs.next()) {
                System.out.println(
                        pos + ". " + rs.getString("nombre_usuario") + " - " + rs.getInt("max_puntos") + " pts");
                pos++;
            }
            System.out.println("===================================\n");
        } catch (SQLException e) {
            System.err.println("Error al cargar ranking: " + e.getMessage());
        }
    }
}