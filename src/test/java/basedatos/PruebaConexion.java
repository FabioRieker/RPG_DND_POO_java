package basedatos;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;

public class PruebaConexion {
    public static void main(String[] args) {
        System.out.println("=== Lanzando test de conexión aislado ===");

        Connection con = ConexionBD.getConexion();

        if (con != null) {
            System.out.println("ConexionBD se comunica con XAMPP perfectamente.");

            ConexionBD.cerrarConexion();
        } else {
            System.out.println("ERROR: La conexión ha devuelto un valor nulo.");
        }
    }
}