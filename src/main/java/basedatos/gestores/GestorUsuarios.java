package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestorUsuarios {

    // Comprueba si un usuario ya existe en la base de datos
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

    // Comprueba si un email ya existe en la base de datos
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

    // Registra un nuevo usuario en la base de datos
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

    // Valida las credenciales de login y devuelve el ID del usuario
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

    // Actualiza la contrasena de un usuario
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

    // Borra la cuenta de un usuario (elimina en cascada sus partidas)
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

    // Muestra por consola el Top 10 de mejores puntuaciones historicas
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