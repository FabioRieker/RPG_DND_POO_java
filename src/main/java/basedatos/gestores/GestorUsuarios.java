package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.Color;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;
import motor.MotorCombate;
import motor.Main;

/**
 * Gestiona el alta, validación y eliminación de usuarios en la base de datos,
 * así como la visualización del ranking global de puntuaciones.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class GestorUsuarios {

    /**
     * Gestiona el bucle de interacción por consola para Iniciar Sesión o Registrarse.
     * @return El ID del usuario una vez que ha iniciado sesión correctamente.
     */
    public int menuAcceso() {
        int idUsuarioLogueado = -1;

        System.out.println(MotorCombate.ANSI_AZUL_MARINO + "===========================================");
        System.out.println("    [SISTEMA] IDENTIFICACIÓN");
        System.out.println("===========================================" + MotorCombate.ANSI_RESET);

        while (idUsuarioLogueado == -1) {
            System.out.println("\n1. Iniciar Sesión");
            System.out.println("2. Registrarse");
            System.out.print(MotorCombate.ANSI_BEIGE + "> Elige una opción: " + MotorCombate.ANSI_RESET);

            int optAcceso = 0;
            if (MotorCombate.sc.hasNextInt()) {
                optAcceso = MotorCombate.sc.nextInt();
                MotorCombate.sc.nextLine();
            } else {
                MotorCombate.sc.nextLine(); // Limpiar buffer
                System.out.println(MotorCombate.ANSI_ROJO + "[SISTEMA] Opción no válida." + MotorCombate.ANSI_RESET);
                continue;
            }

            if (optAcceso != 1 && optAcceso != 2) {
                System.out.println(MotorCombate.ANSI_ROJO + "[SISTEMA] Opción no válida." + MotorCombate.ANSI_RESET);
                continue;
            }

            if (optAcceso == 1) {
                System.out.println(MotorCombate.ANSI_AZUL_MARINO + "\n--- [LOGIN] ---" + MotorCombate.ANSI_RESET);
                System.out.print(MotorCombate.ANSI_BEIGE + "Usuario: " + MotorCombate.ANSI_RESET);
                String nombre = MotorCombate.sc.nextLine();
                System.out.print(MotorCombate.ANSI_BEIGE + "Contraseña: " + MotorCombate.ANSI_RESET);
                String pass = MotorCombate.sc.nextLine();

                idUsuarioLogueado = this.validarLogin(nombre, pass);
                if (idUsuarioLogueado == -1) {
                    System.out.println(MotorCombate.ANSI_ROJO + "Credenciales incorrectas." + MotorCombate.ANSI_RESET);
                } else {
                    Main.nombreUsuarioLogueado = nombre;
                    System.out.println(MotorCombate.ANSI_VERDE_OSCURO + "Acceso concedido. Bienvenido, " + nombre + "!"
                            + MotorCombate.ANSI_RESET);
                }
            } else if (optAcceso == 2) {
                System.out.println(MotorCombate.ANSI_AZUL_MARINO + "\n--- [REGISTRO] ---" + MotorCombate.ANSI_RESET);
                String nombre;
                while (true) {
                    System.out.print(MotorCombate.ANSI_BEIGE + "Nuevo Usuario: " + MotorCombate.ANSI_RESET);
                    nombre = MotorCombate.sc.nextLine();
                    if (nombre.trim().isEmpty() || nombre.length() > 20) {
                        System.out.println(MotorCombate.ANSI_ROJO
                                + "[SISTEMA] El nombre debe tener entre 1 y 20 caracteres." + MotorCombate.ANSI_RESET);
                    } else if (this.existeUsuario(nombre)) {
                        System.out.println(MotorCombate.ANSI_ROJO
                                + "[SISTEMA] Ese nombre de usuario ya está en uso." + MotorCombate.ANSI_RESET);
                    } else {
                        break;
                    }
                }

                String pass;
                while (true) {
                    System.out.print(MotorCombate.ANSI_BEIGE + "Contraseña: " + MotorCombate.ANSI_RESET);
                    pass = MotorCombate.sc.nextLine();
                    if (pass.trim().isEmpty() || pass.length() > 50) {
                        System.out.println(
                                MotorCombate.ANSI_ROJO + "[SISTEMA] La contraseña debe tener entre 1 y 50 caracteres."
                                        + MotorCombate.ANSI_RESET);
                    } else {
                        break;
                    }
                }

                String email;
                while (true) {
                    System.out.print(MotorCombate.ANSI_BEIGE + "Email: " + MotorCombate.ANSI_RESET);
                    email = MotorCombate.sc.nextLine();
                    if (email.trim().isEmpty() || email.length() > 100) {
                        System.out.println(MotorCombate.ANSI_ROJO
                                + "[SISTEMA] El email debe tener entre 1 y 100 caracteres." + MotorCombate.ANSI_RESET);
                    } else if (this.existeEmail(email)) {
                        System.out.println(MotorCombate.ANSI_ROJO
                                + "[SISTEMA] Ese email ya está registrado." + MotorCombate.ANSI_RESET);
                    } else {
                        break;
                    }
                }

                int nuevoId = this.registrarUsuario(nombre, pass, email);
                if (nuevoId != -1) {
                    System.out.println(MotorCombate.ANSI_VERDE_OSCURO
                            + "Cuenta creada con éxito. Ya puedes iniciar sesión." + MotorCombate.ANSI_RESET);
                }
            }
        }
        return idUsuarioLogueado;
    }

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
            System.out.println("Error al comprobar usuario: " + e.getMessage());
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
            System.out.println("Error al comprobar email: " + e.getMessage());
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
            System.out.println("Error al registrar usuario: " + e.getMessage());
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
            System.out.println("Error en login: " + e.getMessage());
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
            System.out.println("Error al actualizar contraseña: " + e.getMessage());
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
            System.out.println("Error al borrar cuenta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extrae y muestra por consola las 10 mejores puntuaciones del juego.
     * Cada entrada corresponde a una partida individual, por lo que un mismo
     * usuario puede aparecer varias veces si tiene varias partidas bien puntuadas.
     * Además, abre una ventana gráfica con un gráfico de barras coloreado por dificultad.
     */
    public void mostrarRankingGlobal() {
        // Obtenemos todas las partidas ordenadas por puntuacion para recorrerlas
        String sql = "SELECT u.nombre_usuario, p.nombre_partida, p.puntuacion, p.dificultad_id " +
                "FROM Partidas p JOIN Usuarios u ON p.usuario_id = u.ID_usuario " +
                "ORDER BY p.puntuacion DESC";

        // Listas separadas por dificultad para los 3 gráficos (Top 5 cada una)
        ArrayList<String> labelsFacil    = new ArrayList<>();
        ArrayList<Integer> valoresFacil  = new ArrayList<>();
        ArrayList<String> labelsNormal   = new ArrayList<>();
        ArrayList<Integer> valoresNormal = new ArrayList<>();
        ArrayList<String> labelsDificil   = new ArrayList<>();
        ArrayList<Integer> valoresDificil = new ArrayList<>();

        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            System.out.println("\n=== RANKING GLOBAL TOP 10 ===");
            int pos = 1;
            
            while (rs.next()) {
                int diffId = rs.getInt("dificultad_id");
                int puntos = rs.getInt("puntuacion");
                String colorDiff;
                String nombreDiff;
                
                if (diffId == 1) {
                    colorDiff = motor.MotorCombate.ANSI_VERDE_OSCURO;
                    nombreDiff = "Facil";
                } else if (diffId == 3) {
                    colorDiff = motor.MotorCombate.ANSI_ROJO;
                    nombreDiff = "Dificil";
                } else {
                    colorDiff = motor.MotorCombate.ANSI_CIAN;
                    nombreDiff = "Normal";
                }

                // Imprimir por consola solo el TOP 10 absoluto
                if (pos <= 10) {
                    System.out.println(pos + ". " + rs.getString("nombre_usuario")
                            + " | " + rs.getString("nombre_partida")
                            + " - " + puntos + " pts"
                            + " | " + colorDiff + nombreDiff + motor.MotorCombate.ANSI_RESET);
                }

                // Etiqueta para el eje X del grafico
                String etiquetaGrafico = rs.getString("nombre_usuario") + " | " + rs.getString("nombre_partida");

                // Clasificar en la lista de su dificultad (máximo 5)
                if (diffId == 1 && labelsFacil.size() < 5) {
                    labelsFacil.add(etiquetaGrafico);
                    valoresFacil.add(puntos);
                } else if (diffId == 3 && labelsDificil.size() < 5) {
                    labelsDificil.add(etiquetaGrafico);
                    valoresDificil.add(puntos);
                } else if ((diffId == 2 || diffId > 3 || diffId < 1) && labelsNormal.size() < 5) {
                    // diffId 2 es Normal
                    labelsNormal.add(etiquetaGrafico);
                    valoresNormal.add(puntos);
                }

                pos++;
            }
            if (pos == 1) {
                System.out.println("Aún no hay partidas registradas.");
            }
            System.out.println("===================================\n");

        } catch (SQLException e) {
            System.out.println("Error al cargar ranking: " + e.getMessage());
            return;
        }

        // Crear matriz de gráficos
        ArrayList<CategoryChart> graficos = new ArrayList<>();

        if (!labelsFacil.isEmpty()) {
            CategoryChart chartFacil = new CategoryChartBuilder().width(400).height(300).title("Top 5 - Fácil").xAxisTitle("Jugador | Partida").yAxisTitle("Puntos").theme(Styler.ChartTheme.GGPlot2).build();
            chartFacil.getStyler().setLegendVisible(false);
            chartFacil.getStyler().setXAxisLabelRotation(30);
            chartFacil.addSeries("Facil", labelsFacil, valoresFacil).setFillColor(new Color(50, 180, 50));
            graficos.add(chartFacil);
        }

        if (!labelsNormal.isEmpty()) {
            CategoryChart chartNormal = new CategoryChartBuilder().width(400).height(300).title("Top 5 - Normal").xAxisTitle("Jugador | Partida").yAxisTitle("Puntos").theme(Styler.ChartTheme.GGPlot2).build();
            chartNormal.getStyler().setLegendVisible(false);
            chartNormal.getStyler().setXAxisLabelRotation(30);
            chartNormal.addSeries("Normal", labelsNormal, valoresNormal).setFillColor(new Color(30, 144, 255));
            graficos.add(chartNormal);
        }

        if (!labelsDificil.isEmpty()) {
            CategoryChart chartDificil = new CategoryChartBuilder().width(400).height(300).title("Top 5 - Difícil").xAxisTitle("Jugador | Partida").yAxisTitle("Puntos").theme(Styler.ChartTheme.GGPlot2).build();
            chartDificil.getStyler().setLegendVisible(false);
            chartDificil.getStyler().setXAxisLabelRotation(30);
            chartDificil.addSeries("Dificil", labelsDificil, valoresDificil).setFillColor(new Color(200, 50, 50));
            graficos.add(chartDificil);
        }

        if (!graficos.isEmpty()) {
            new SwingWrapper<>(graficos).displayChartMatrix();
        }
    }
}