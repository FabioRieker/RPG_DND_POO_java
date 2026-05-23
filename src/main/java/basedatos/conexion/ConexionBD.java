package basedatos.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Proporciona y administra la conexión única con la base de datos MySQL,
 * garantizando un único punto de acceso para todas las operaciones del juego.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class ConexionBD {
    private static final String URL = "jdbc:mysql://127.0.0.1:3307/rpg_dnd";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    private static Connection conexion = null;

    private ConexionBD() {
    }

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                    System.out.println("Conexión cerrada.");
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}