package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.Color;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import motor.MotorCombate;

/**
 * Controla el desbloqueo de logros, la suma de puntos y
 * la gestión del arsenal de armas que el jugador encuentra en su aventura.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class GestorRecompensas {

    public boolean usuarioYaTieneLogro(int idPartida, int idLogro) {
        String sql = "SELECT 1 FROM Partida_Logros pl JOIN Partidas p ON pl.partida_id = p.ID_partida " +
                     "WHERE p.usuario_id = (SELECT usuario_id FROM Partidas WHERE ID_partida = ?) AND pl.logro_id = ?";
        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPartida);
            ps.setInt(2, idLogro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { return false; }
    }

    /**
     * Intenta desbloquear un logro para la partida actual.
     * 
     * @param idPartida El ID de la partida.
     * @param idLogro   El ID del logro a desbloquear.
     * @return true si se desbloqueó por primera vez para este usuario, false en caso
     *         contrario o si hubo un error.
     */
    public boolean desbloquearLogro(int idPartida, int idLogro) {
        boolean yaLoTenia = usuarioYaTieneLogro(idPartida, idLogro);

        String sql = "INSERT IGNORE INTO Partida_Logros (partida_id, logro_id) VALUES (?, ?)";
        boolean insertado = false;
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPartida);
            ps.setInt(2, idLogro);

            insertado = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al desbloquear logro: " + e.getMessage());
            return false;
        }

        // Comprobar logro 15 (Platinado) por código para evitar error 1442 de triggers
        // en MySQL
        if (insertado && idLogro != 15) {
            String sqlCheck = "SELECT COUNT(DISTINCT pl.logro_id) FROM Partida_Logros pl " +
                    "JOIN Partidas p ON pl.partida_id = p.ID_partida " +
                    "WHERE p.usuario_id = (SELECT usuario_id FROM Partidas WHERE ID_partida = ?) " +
                    "AND pl.logro_id != 15";
            try (Connection con = ConexionBD.getConexion();
                    PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, idPartida);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) >= 21) {
                        desbloquearLogro(idPartida, 15);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error al comprobar logro platino: " + e.getMessage());
            }
        }

        if (yaLoTenia) {
            return false; // Retornamos false para que no salte el pop-up por pantalla otra vez
        }

        return insertado;
    }

    /**
     * Inserta un arma en la mochila de la partida, o incrementa su cantidad si ya
     * la poseía.
     * 
     * @param idPartida Identificador de la partida.
     * @param idArma    Identificador del arma obtenida.
     * @param cantidad  Número de copias de ese arma a añadir.
     * @return true si la operación en la BD tuvo éxito, false en caso de error.
     */
    public boolean anadirArma(int idPartida, int idArma, int cantidad) {
        String sql = "INSERT INTO Mochila_Armas (id_partida, id_arma, cantidad) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE cantidad = cantidad + ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPartida);
            ps.setInt(2, idArma);
            ps.setInt(3, cantidad);
            ps.setInt(4, cantidad);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(" Error al añadir arma a la mochila: " + e.getMessage());
            return false;
        }
    }

    /**
     * Consulta cuántos puntos de galardón otorga un logro específico.
     * 
     * @param idLogro Identificador del logro.
     * @return Cantidad de puntos que otorga, o 0 si no se encuentra.
     */
    public int obtenerPuntosLogro(int idLogro) {
        String sql = "SELECT puntos FROM Logros WHERE ID_logro = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLogro);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("puntos");
                }
            }
        } catch (SQLException e) {
            System.out.println(" Error al obtener puntos del logro: " + e.getMessage());
        }
        return 0; // Por defecto retorna 0 si no se encuentra
    }

    /**
     * Comprueba el número de armas distintas en la mochila. Si el jugador ha
     * reunido 4 o más armas diferentes, le otorga el logro "Coleccionista de
     * Arsenal".
     * 
     * @param idPartida Identificador de la partida actual.
     */
    public void verificarColeccionistaArmas(int idPartida) {
        String sql = "SELECT COUNT(DISTINCT id_arma) AS num_armas FROM Mochila_Armas WHERE id_partida = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPartida);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt("num_armas") >= 4) {
                    desbloquearLogro(idPartida, 14); // Logro 14: Coleccionista de Arsenal
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar coleccionista de armas: " + e.getMessage());
        }
    }

    /**
     * Busca el identificador numérico de un arma por su nombre en texto.
     * 
     * @param nombre Nombre exacto del arma a buscar.
     * @return El ID numérico del arma, o 1 (Arma base por defecto) si no se
     *         encuentra.
     */
    public int obtenerIdArmaPorNombre(String nombre) {
        String sql = "SELECT ID_arma FROM Armas WHERE nombre = ?";
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt("ID_arma");
            }
        } catch (SQLException e) {
        }
        return 1;
    }

    // Clase auxiliar para agrupar la información de cada logro (estilo 1º de DAM)
    private static class InfoLogro {
        int id;
        String nombre;
        String descripcion;
        boolean obtenido;

        public InfoLogro(int id, String nombre, String descripcion, boolean obtenido) {
            this.id = id;
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.obtenido = obtenido;
        }
    }

    public void mostrarMenuLogros(int idUsuarioLogueado) {
        // Colores para cada tier de logros
        String colorBronce = "\033[38;5;130m"; // Marron / Bronce
        String colorPlata = "\033[37m"; // Gris claro -> Plata
        String colorOro = "\033[93m"; // Amarillo brillante -> Oro
        String colorPlatino = "\033[96m"; // Cian brillante -> Platino
        String colorGris = "\033[90m"; // Gris oscuro -> no obtenido
        String reset = MotorCombate.ANSI_RESET;

        // IDs de cada logro agrupados por tier de dificultad
        int[] idsBronce = { 2, 3, 4, 5, 6 };
        int[] idsPlata = { 7, 8, 13, 14, 16, 18, 19 };
        int[] idsOro = { 1, 9, 10, 11, 12, 17, 20, 21, 22 };
        int[] idsPlatino = { 15 };

        // Consulta: trae TODOS los logros y marca cuales tiene el usuario
        // Se usa LEFT JOIN para que aparezcan también los no obtenidos
        String sql = "SELECT l.ID_logro, l.nombre, l.descripcion, " +
                "       CASE WHEN pl.logro_id IS NOT NULL THEN 1 ELSE 0 END AS desbloqueado " +
                "FROM Logros l " +
                "LEFT JOIN Partida_Logros pl " +
                "       ON l.ID_logro = pl.logro_id " +
                "      AND pl.partida_id IN (SELECT ID_partida FROM Partidas WHERE usuario_id = ?) " +
                "GROUP BY l.ID_logro " +
                "ORDER BY l.ID_logro";

        System.out.println(MotorCombate.ANSI_MORADO + "\n=== MIS LOGROS ===" + reset);

        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuarioLogueado);
            try (ResultSet rs = ps.executeQuery()) {

                // Guardamos los logros en un ArrayList de objetos
                ArrayList<InfoLogro> todosLosLogros = new ArrayList<>();

                while (rs.next()) {
                    InfoLogro logro = new InfoLogro(
                            rs.getInt("ID_logro"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getInt("desbloqueado") == 1);
                    todosLosLogros.add(logro);
                }

                // Contar cuantos ha desbloqueado el usuario
                int desbloqueados = 0;
                for (InfoLogro l : todosLosLogros) {
                    if (l.obtenido)
                        desbloqueados++;
                }

                // Imprimir cada tier en su color
                this.imprimirTierLogros("BRONCE", colorBronce, idsBronce, todosLosLogros, colorGris, reset);
                this.imprimirTierLogros("PLATA", colorPlata, idsPlata, todosLosLogros, colorGris, reset);
                this.imprimirTierLogros("ORO", colorOro, idsOro, todosLosLogros, colorGris, reset);
                this.imprimirTierLogros("PLATINO", colorPlatino, idsPlatino, todosLosLogros, colorGris, reset);

                // Contador final
                System.out.println();
                System.out.println("Desbloqueados: " + desbloqueados + " / " + todosLosLogros.size());

                // Mostrar gráficos en ventana flotante
                this.mostrarGraficosLogros(todosLosLogros, idsBronce, idsPlata, idsOro, idsPlatino);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar logros: " + e.getMessage());
        }

        System.out.println("\nPresiona ENTER para volver...");
        MotorCombate.sc.nextLine();
    }

    /**
     * Imprime una seccion de logros de un tier concreto usando la lista de objetos.
     * Los logros obtenidos aparecen en el color del tier con simbolo [v].
     * Los no obtenidos aparecen en gris con [ ].
     *
     * @param nombreTier Nombre del tier (ej: "ORO").
     * @param colorTier  Codigo ANSI del color del tier.
     * @param idsTier    IDs de logros que pertenecen a este tier.
     * @param logros     Lista de todos los logros como objetos.
     * @param colorGris  Color para logros no obtenidos.
     * @param reset      Codigo ANSI de reset.
     */
    private void imprimirTierLogros(String nombreTier, String colorTier, int[] idsTier,
            ArrayList<InfoLogro> logros, String colorGris, String reset) {

        System.out.println("\n" + colorTier + "── " + nombreTier
                + " ──────────────────────────────────" + reset);

        // Buscar cada ID del tier en la lista de objetos
        for (int t = 0; t < idsTier.length; t++) {
            int idBuscado = idsTier[t];

            for (InfoLogro logro : logros) {
                if (logro.id == idBuscado) {
                    if (logro.obtenido) {
                        // Logro obtenido: color del tier y check
                        System.out.println(colorTier + "  [v] " + logro.nombre
                                + " (" + logro.descripcion + ")" + reset);
                    } else {
                        // Logro no obtenido: en gris para diferenciarlo
                        System.out.println(colorGris + "  [ ] " + logro.nombre
                                + " (" + logro.descripcion + ")" + reset);
                    }
                    break;
                }
            }
        }
    }

    /**
     * Muestra una matriz de gráficos circulares (PieChart) usando la librería
     * XChart.
     * Calcula cuántos logros de cada tier se han obtenido para dibujar los
     * porcentajes.
     */
    private void mostrarGraficosLogros(ArrayList<InfoLogro> todosLosLogros, int[] idsBronce, int[] idsPlata,
            int[] idsOro, int[] idsPlatino) {
        // Contar obtenidos por tier usando un método de ayuda
        int obtBronce = this.contarLogrosObtenidos(idsBronce, todosLosLogros);
        int obtPlata = this.contarLogrosObtenidos(idsPlata, todosLosLogros);
        int obtOro = this.contarLogrosObtenidos(idsOro, todosLosLogros);
        int obtPlatino = this.contarLogrosObtenidos(idsPlatino, todosLosLogros);

        // Crear los graficos de XChart
        ArrayList<PieChart> graficos = new ArrayList<>();
        Color colorBloqueado = new Color(60, 60, 60); // Gris oscuro para porciones no obtenidas

        // Grafico Bronce
        PieChart chartBronce = new PieChartBuilder().width(400).height(300).title("Logros Bronce").build();
        chartBronce.addSeries("Obtenidos", obtBronce);
        chartBronce.addSeries("Bloqueados", idsBronce.length - obtBronce);
        chartBronce.getStyler().setSeriesColors(new Color[] { new Color(139, 69, 19), colorBloqueado });
        chartBronce.getStyler().setLegendVisible(false);
        graficos.add(chartBronce);

        // Grafico Plata
        PieChart chartPlata = new PieChartBuilder().width(400).height(300).title("Logros Plata").build();
        chartPlata.addSeries("Obtenidos", obtPlata);
        chartPlata.addSeries("Bloqueados", idsPlata.length - obtPlata);
        chartPlata.getStyler().setSeriesColors(new Color[] { new Color(192, 192, 192), colorBloqueado });
        chartPlata.getStyler().setLegendVisible(false);
        graficos.add(chartPlata);

        // Grafico Oro
        PieChart chartOro = new PieChartBuilder().width(400).height(300).title("Logros Oro").build();
        chartOro.addSeries("Obtenidos", obtOro);
        chartOro.addSeries("Bloqueados", idsOro.length - obtOro);
        chartOro.getStyler().setSeriesColors(new Color[] { new Color(255, 215, 0), colorBloqueado });
        chartOro.getStyler().setLegendVisible(false);
        graficos.add(chartOro);

        // Grafico Platino
        PieChart chartPlatino = new PieChartBuilder().width(400).height(300).title("Logros Platino").build();
        chartPlatino.addSeries("Obtenidos", obtPlatino);
        chartPlatino.addSeries("Bloqueados", idsPlatino.length - obtPlatino);
        chartPlatino.getStyler().setSeriesColors(new Color[] { new Color(0, 255, 255), colorBloqueado });
        chartPlatino.getStyler().setLegendVisible(false);
        graficos.add(chartPlatino);

        // Mostrar en ventana flotante de Swing
        new SwingWrapper<>(graficos).displayChartMatrix();

    }

    /**
     * Cuenta cuántos logros de un tier específico han sido obtenidos.
     */
    private int contarLogrosObtenidos(int[] idsTier, ArrayList<InfoLogro> logros) {
        int contador = 0;
        for (int id : idsTier) {
            for (InfoLogro l : logros) {
                if (l.id == id && l.obtenido) {
                    contador++;
                }
            }
        }
        return contador;
    }
}
