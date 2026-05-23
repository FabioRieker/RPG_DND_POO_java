package motor;

import personajes.*;
import basedatos.gestores.*;
import basedatos.conexion.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;

/**
 * Clase principal que arranca el juego. Gestiona el Login, el Menu Principal,
 * la persistencia en base de datos y el bucle de las 20 salas.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Main {

    // --- VARIABLES GLOBALES DE PERSISTENCIA ---
    public static int idUsuarioLogueado = -1;
    public static String nombreUsuarioLogueado = "";
    public static int idPartidaActual = -1;
    public static int salaActual = 1;
    public static int puntuacionPartida = 0;
    public static int bajasTotales = 0; // Bajas acumuladas en toda la partida (para logro Impecable)

    public static void main(String[] args) {

        imprimirAsciiArt();

        GestorUsuarios gestorUsuarios = new GestorUsuarios();

        // 1. BUCLE DE AUTENTICACION (LOGIN / REGISTRO)
        idUsuarioLogueado = gestorUsuarios.menuAcceso();

        // 2. MENU PRINCIPAL
        boolean salirJuego = false;
        while (!salirJuego) {
            System.out
                    .println("\n" + MotorCombate.ANSI_AZUL_MARINO + "=== MENÚ PRINCIPAL ===" + MotorCombate.ANSI_RESET);
            System.out.println("1. Nueva Partida");
            System.out.println("2. Cargar Partida");
            System.out.println("3. Ver Ranking Global");
            System.out.println("4. Ver Mis Logros");
            System.out.println("5. Salir al Escritorio");
            System.out.print("> Acción: ");

            int optMenu = 0;
            if (MotorCombate.sc.hasNextInt()) {
                optMenu = MotorCombate.sc.nextInt();
            }
            MotorCombate.sc.nextLine();

            switch (optMenu) {
                case 1:
                    configurarNuevaPartida();
                    break;
                case 2:
                    cargarPartidaGuardada();
                    break;
                case 3:
                    gestorUsuarios.mostrarRankingGlobal();
                    break;
                case 4:
                    mostrarMisLogros();
                    break;
                case 5:
                    salirJuego = true;
                    System.out.println("Cerrando los portales... ¡Hasta la próxima aventura!");
                    break;
                default:
                    System.out
                            .println(MotorCombate.ANSI_ROJO + "[SISTEMA] Opción no válida." + MotorCombate.ANSI_RESET);
            }
        }
    }

    // --- METODOS DEL MENU ---

    /*
     * Flujo de creación: pide nombre de la partida, selecciona dificultad global
     * y, si el usuario es Admin, pregunta por la sala de inicio (modo debug).
     */
    private static void configurarNuevaPartida() {
        System.out.println(MotorCombate.ANSI_AZUL_MARINO + "\n=== [NUEVA PARTIDA] ===" + MotorCombate.ANSI_RESET);
        GestorPartidas gp = new GestorPartidas();
        String nombrePartida;
        while (true) {
            System.out
                    .print(MotorCombate.ANSI_BEIGE + "Introduce un nombre para tu partida: " + MotorCombate.ANSI_RESET);
            nombrePartida = MotorCombate.sc.nextLine();
            if (nombrePartida.trim().isEmpty() || nombrePartida.length() > 100) {
                System.out.println(MotorCombate.ANSI_ROJO + "[SISTEMA] El nombre debe tener entre 1 y 100 caracteres."
                        + MotorCombate.ANSI_RESET);
            } else if (gp.existeNombrePartida(nombrePartida, idUsuarioLogueado)) {
                System.out.println(MotorCombate.ANSI_ROJO + "[SISTEMA] Ya tienes una partida con ese nombre."
                        + MotorCombate.ANSI_RESET);
            } else {
                break;
            }
        }

        int dif = 0;
        while (true) {
            System.out.println(
                    MotorCombate.ANSI_AZUL_MARINO + "\n=== [SELECCIÓN DE DIFICULTAD] ===" + MotorCombate.ANSI_RESET);
            System.out.println("1. Fácil   (Vida y Daño de enemigos x0.6)");
            System.out.println("2. Normal  (Vida y Daño de enemigos x1.0)");
            System.out.println("3. Difícil (Vida y Daño de enemigos x1.5)");
            System.out.print(MotorCombate.ANSI_BEIGE + "> Elige una dificultad: " + MotorCombate.ANSI_RESET);
            if (MotorCombate.sc.hasNextInt()) {
                dif = MotorCombate.sc.nextInt();
                MotorCombate.sc.nextLine();
                if (dif >= 1 && dif <= 3) {
                    break;
                }
            } else {
                MotorCombate.sc.nextLine(); // Limpiar el buffer del Scanner.
            }
            System.out.println(MotorCombate.ANSI_ROJO + "[SISTEMA] Opción no válida." + MotorCombate.ANSI_RESET);
        }

        int salaInicio = 1;

        // Modo debug solo para admin
        if (nombreUsuarioLogueado.equalsIgnoreCase("Admin")) {
            while (true) {
                System.out.print(MotorCombate.ANSI_MORADO
                        + "> [MODO DIOS] ¿En qué sala quieres empezar, Admin? (1-20): " + MotorCombate.ANSI_RESET);
                if (MotorCombate.sc.hasNextInt()) {
                    salaInicio = MotorCombate.sc.nextInt();
                    MotorCombate.sc.nextLine();
                    if (salaInicio >= 1 && salaInicio <= 20) {
                        break;
                    }
                } else {
                    MotorCombate.sc.nextLine(); // Limpiar el buffer del Scanner.
                }
                System.out.println(MotorCombate.ANSI_ROJO
                        + "[SISTEMA] Opción no válida. Introduce un número del 1 al 20." + MotorCombate.ANSI_RESET);
            }
        }

        idPartidaActual = gp.crearNuevaPartida(nombrePartida, idUsuarioLogueado, dif);

        if (idPartidaActual != -1) {
            double multiplicador = new GestorDificultad().obtenerMultiplicador(dif);
            MotorCombate.multiplicadorDificultad = multiplicador;

            salaActual = salaInicio;
            puntuacionPartida = 0;
            bajasTotales = 0;
            System.out.println(MotorCombate.ANSI_VERDE_OSCURO + "\n¡Partida '" + nombrePartida + "' creada con éxito!"
                    + MotorCombate.ANSI_RESET);
            iniciarAventura();
        }
    }

    /**
     * Consulta las partidas activas del usuario logueado, permite seleccionar una
     * mediante su ID y restaura la sala, puntuación y dificultad antes de saltar a
     * la aventura.
     */
    private static void cargarPartidaGuardada() {
        System.out.println(MotorCombate.ANSI_AZUL_MARINO + "\n=== [CARGAR PARTIDA] ===" + MotorCombate.ANSI_RESET);
        System.out.println("--- TUS PARTIDAS GUARDADAS ---");
        String sql = "SELECT ID_partida, nombre_partida, sala_actual, puntuacion, estado FROM Partidas WHERE usuario_id = ?";

        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuarioLogueado);
            try (ResultSet rs = ps.executeQuery()) {
                boolean hayPartidas = false;
                while (rs.next()) {
                    hayPartidas = true;
                    String estadoP = rs.getString("estado");
                    String tag = estadoP.equals("completada")
                            ? " " + MotorCombate.ANSI_ROJO + "(COMPLETADA)" + MotorCombate.ANSI_RESET
                            : "";
                    System.out.println("ID: " + rs.getInt("ID_partida") +
                            " | Nombre: " + rs.getString("nombre_partida") +
                            " | Sala: " + rs.getInt("sala_actual") +
                            " | Puntos: " + rs.getInt("puntuacion") + tag);
                }
                if (!hayPartidas) {
                    System.out.println(
                            MotorCombate.ANSI_ROJO + "No tienes partidas guardadas." + MotorCombate.ANSI_RESET);
                    return;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar partidas: " + e.getMessage());
        }

        int idElegido = 0;
        while (true) {
            System.out.print(MotorCombate.ANSI_BEIGE + "\n> Escribe el ID de la partida a cargar (0 para cancelar): "
                    + MotorCombate.ANSI_RESET);
            if (MotorCombate.sc.hasNextInt()) {
                idElegido = MotorCombate.sc.nextInt();
                MotorCombate.sc.nextLine();
                break;
            } else {
                MotorCombate.sc.nextLine();
                System.out.println(MotorCombate.ANSI_ROJO + "[SISTEMA] Opción no válida." + MotorCombate.ANSI_RESET);
            }
        }

        if (idElegido > 0) {
            // Recuperar datos de la partida elegida
            String sqlCarga = "SELECT sala_actual, puntuacion, dificultad_id, estado FROM Partidas WHERE ID_partida = ? AND usuario_id = ?";
            try (PreparedStatement ps2 = con.prepareStatement(sqlCarga)) {
                ps2.setInt(1, idElegido);
                ps2.setInt(2, idUsuarioLogueado);
                try (ResultSet rs2 = ps2.executeQuery()) {
                    if (rs2.next()) {
                        if (rs2.getString("estado").equals("completada")) {
                            System.out.println(MotorCombate.ANSI_ROJO
                                    + "[SISTEMA] Esta aventura ya se completó. Enhorabuena!" + MotorCombate.ANSI_RESET);
                            return;
                        }
                        idPartidaActual = idElegido;
                        salaActual = rs2.getInt("sala_actual");
                        puntuacionPartida = rs2.getInt("puntuacion");
                        int diffId = rs2.getInt("dificultad_id");
                        double multiplicador = new GestorDificultad().obtenerMultiplicador(diffId);
                        MotorCombate.multiplicadorDificultad = multiplicador;

                        System.out.println(MotorCombate.ANSI_VERDE_OSCURO + "Partida cargada. Retomando desde la sala "
                                + salaActual + "..." + MotorCombate.ANSI_RESET);
                        iniciarAventura();
                    } else {
                        System.out.println(MotorCombate.ANSI_ROJO + "[SISTEMA] ID de partida no válido."
                                + MotorCombate.ANSI_RESET);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
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

    private static void mostrarMisLogros() {
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
                imprimirTierLogros("BRONCE", colorBronce, idsBronce, todosLosLogros, colorGris, reset);
                imprimirTierLogros("PLATA", colorPlata, idsPlata, todosLosLogros, colorGris, reset);
                imprimirTierLogros("ORO", colorOro, idsOro, todosLosLogros, colorGris, reset);
                imprimirTierLogros("PLATINO", colorPlatino, idsPlatino, todosLosLogros, colorGris, reset);

                // Contador final
                System.out.println();
                System.out.println("Desbloqueados: " + desbloqueados + " / " + todosLosLogros.size());

                // Mostrar gráficos en ventana flotante
                mostrarGraficosLogros(todosLosLogros, idsBronce, idsPlata, idsOro, idsPlatino);
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
    private static void imprimirTierLogros(String nombreTier, String colorTier, int[] idsTier,
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
     * Muestra una matriz de gráficos circulares (PieChart) usando la librería XChart.
     * Calcula cuántos logros de cada tier se han obtenido para dibujar los porcentajes.
     */
    private static void mostrarGraficosLogros(ArrayList<InfoLogro> todosLosLogros, int[] idsBronce, int[] idsPlata, int[] idsOro, int[] idsPlatino) {
        // Contar obtenidos por tier usando un método de ayuda
        int obtBronce = contarLogrosObtenidos(idsBronce, todosLosLogros);
        int obtPlata = contarLogrosObtenidos(idsPlata, todosLosLogros);
        int obtOro = contarLogrosObtenidos(idsOro, todosLosLogros);
        int obtPlatino = contarLogrosObtenidos(idsPlatino, todosLosLogros);

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
    private static int contarLogrosObtenidos(int[] idsTier, ArrayList<InfoLogro> logros) {
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

    // --- BUCLE PRINCIPAL DEL JUEGO ---

    /*
     * Bucle principal del juego. Gestiona eventos de historia estáticos (salas 2,
     * 5, 7, etc.),
     * eventos de curación, reclutamientos y genera combates en el resto de salas.
     * También controla los puntos de guardado y los reemplazos del equipo de
     * reserva.
     */
    private static void iniciarAventura() {
        boolean guardadoAuto = false;
        boolean guardadoManual = true;

        System.out.println(MotorCombate.ANSI_AZUL_MARINO + "\n=== [MODO DE JUEGO] ===" + MotorCombate.ANSI_RESET);
        System.out.println("1. Modo Automático (La IA controla todo)");
        System.out.println("2. Modo Manual (Control total de Héroes)");

        int opt = 0;
        while (true) {
            System.out.print(MotorCombate.ANSI_BEIGE + "> Elige una opción: " + MotorCombate.ANSI_RESET);
            if (MotorCombate.sc.hasNextInt()) {
                opt = MotorCombate.sc.nextInt();
                MotorCombate.sc.nextLine();
                if (opt == 1 || opt == 2)
                    break;
            } else {
                MotorCombate.sc.nextLine(); // Limpiar el buffer del Scanner.
            }
            System.out.println(MotorCombate.ANSI_ROJO + "[SISTEMA] Opción no válida." + MotorCombate.ANSI_RESET);
        }

        if (opt == 2) {
            MotorCombate.modoManual = true;
        } else {
            MotorCombate.modoManual = false;
        }
        // Dar 5 pociones al grupo en ambos modos
        for (int j = 0; j < 5; j++) {
            MotorCombate.inventarioGrupo.add(new consumibles.PocionCuracion(1));
        }

        if (MotorCombate.modoManual) {
            System.out
                    .println(MotorCombate.ANSI_AZUL_MARINO + "\n=== [TIPO DE GUARDADO] ===" + MotorCombate.ANSI_RESET);
            System.out.println("1. Guardado Automático (Tras combates y campamentos)");
            System.out.println("2. Guardado Manual (Pregunta tras combates y campamentos)");

            int optGuardado = 0;
            while (true) {
                System.out.print(MotorCombate.ANSI_BEIGE + "> Elige una opción: " + MotorCombate.ANSI_RESET);
                if (MotorCombate.sc.hasNextInt()) {
                    optGuardado = MotorCombate.sc.nextInt();
                    MotorCombate.sc.nextLine();
                    if (optGuardado == 1 || optGuardado == 2)
                        break;
                } else {
                    MotorCombate.sc.nextLine(); // Limpiar el buffer del Scanner.
                }
                System.out.println(MotorCombate.ANSI_ROJO + "[SISTEMA] Opción no válida." + MotorCombate.ANSI_RESET);
            }

            if (optGuardado == 1) {
                guardadoAuto = true;
                guardadoManual = false;
            } else {
                guardadoAuto = false;
                guardadoManual = true;
            }
        } else {
            // Modo Automático (Combate)
            // Se guardará automáticamente tras combates, pero preguntará en campamentos
            guardadoAuto = false;
            guardadoManual = true;
        }

        // Banner de inicio
        System.out.println(MotorCombate.ANSI_VERDE_OSCURO + "\n=== [SISTEMA] "
                + (salaActual > 1 ? "REANUDANDO" : "COMENZANDO") + " AVENTURA ===" + MotorCombate.ANSI_RESET);

        // Crear el equipo que lucha y preparar la reserva
        System.out.println();
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
        }

        List<Personaje> listaHeroes = FabricaHeroes.crearEquipoInicial();
        List<Personaje> reserva = new ArrayList<>();
        Personaje[] heroes = listaHeroes.toArray(new Personaje[0]);

        if (idPartidaActual != -1) {
            new basedatos.gestores.GestorPartidas().restaurarEstadoHeroes(idPartidaActual, heroes);
        }

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
        }

        // Bucle que recorre las salas de la mazmorra (EMPIEZA DESDE salaActual)
        for (int i = salaActual; i <= 20; i++) {
            salaActual = i; // Sincronizamos la global

            if (!MotorCombate.hayVivos(heroes)) {
                System.out.println(
                        MotorCombate.ANSI_ROJO + "EL EQUIPO HA MUERTO. FIN DE LA PARTIDA." + MotorCombate.ANSI_RESET);
                System.out.println(MotorCombate.ANSI_AMARILLO + "Puedes reintentarlo cargando la partida en el menú."
                        + MotorCombate.ANSI_RESET);
                break;
            }

            System.out.println(
                    MotorCombate.ANSI_AZUL_MARINO + "\n>>> ENTRANDO EN LA SALA " + i + "..." + MotorCombate.ANSI_RESET);

            // Eventos de historia (curas, trampas y reclutamientos)
            if (i == 2) {
                System.out.println("Encontráis suministros en una caravana saqueada.");
                for (Personaje h : heroes) {
                    if (h.estaVivo())
                        h.recuperarRecursos(30);
                }
            } else if (i == 5) {
                System.out.println(MotorCombate.ANSI_MORADO
                        + "[EVENTO] ¡Rescatáis a Kallista! Se une a vuestra reserva." + MotorCombate.ANSI_RESET);
                reserva.add(FabricaHeroes.crearKallista());
                new basedatos.gestores.GestorRecompensas().desbloquearLogro(idPartidaActual, 4);
                System.out.println(MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Un Nuevo Aliado."
                        + " (Kallista se ha unido a la reserva)" + MotorCombate.ANSI_RESET);
            } else if (i == 7) {
                System.out.println("¡BOOM! Una trampa de fuego estalla.");
                int vivosAntesTrampa = 0;
                for (int j = 0; j < heroes.length; j++) {
                    if (heroes[j].estaVivo())
                        vivosAntesTrampa++;
                }
                for (Personaje h : heroes) {
                    if (h.estaVivo())
                        h.recibirDaño(12, true);
                }
                int vivosTrampa = 0;
                for (int j = 0; j < heroes.length; j++) {
                    if (heroes[j].estaVivo())
                        vivosTrampa++;
                }
                bajasTotales += vivosAntesTrampa - vivosTrampa;
            } else if (i == 9) {
                System.out.println(MotorCombate.ANSI_MORADO
                        + "[EVENTO] Llegáis a una fuente curativa. El grupo descansa." + MotorCombate.ANSI_RESET);
                for (Personaje h : heroes) {
                    if (h.estaVivo())
                        h.curar(50);
                }
                new basedatos.gestores.GestorRecompensas().desbloquearLogro(idPartidaActual, 3);
                System.out.println(MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Campista Novato."
                        + " (Fuente curativa de la sala 9 alcanzada)" + MotorCombate.ANSI_RESET);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                }
                if (!MotorCombate.gestionarCampamento(heroes, reserva, guardadoAuto, guardadoManual, i)) {
                    break;
                }
            } else if (i == 12) {
                System.out.println(MotorCombate.ANSI_MORADO
                        + "[EVENTO] Un monje llamado Kwai Chang se une a vuestra reserva." + MotorCombate.ANSI_RESET);
                reserva.add(FabricaHeroes.crearMonjeKwai());
            } else if (i == 14) {
                System.out.println("Sala vacía... un silencio sepulcral inunda el lugar.");
            } else if (i == 17) {
                System.out.println(MotorCombate.ANSI_MORADO
                        + "[EVENTO] Lulu Nightingale, la barda, se une a vuestra reserva." + MotorCombate.ANSI_RESET);
                reserva.add(FabricaHeroes.crearBardoLulu());
            } else if (i == 19) {
                System.out.println("Último descanso antes del gran final. Salud y recursos al máximo.");
                for (Personaje h : heroes) {
                    if (h.estaVivo()) {
                        h.curar(100);
                        h.recuperarRecursos(100);
                    }
                }
                if (!MotorCombate.gestionarCampamento(heroes, reserva, guardadoAuto, guardadoManual, i)) {
                    break;
                }
            } else {
                // Cargar los enemigos que toquen en esta sala
                Sala salaLucha = FabricaSalas.generarSala(i);
                List<Personaje> listaEnemigos = salaLucha.getEnemigos();
                Personaje[] enemigos = listaEnemigos.toArray(new Personaje[0]);

                MotorCombate.iniciarCombate(heroes, enemigos);
            }

            // Sustitucion automatica de heroes caidos por miembros de la reserva.
            for (int j = 0; j < heroes.length; j++) {
                if (!heroes[j].estaVivo() && !reserva.isEmpty()) {
                    Personaje caido = heroes[j];
                    for (int r = 0; r < reserva.size(); r++) {
                        if (reserva.get(r).estaVivo()) {
                            Personaje sustituto = reserva.remove(r);
                            System.out.println(MotorCombate.ANSI_ROJO + "\n[SISTEMA] " + caido.getNombre()
                                    + " ha muerto en acto de servicio." + MotorCombate.ANSI_RESET);
                            System.out.println(MotorCombate.ANSI_MORADO + "[RESERVA] " + sustituto.getNombre()
                                    + " entra al equipo principal para ocupar su lugar." + MotorCombate.ANSI_RESET);
                            heroes[j] = sustituto;
                            break;
                        }
                    }
                }

                if ((i == 9 || i == 19) && !reserva.isEmpty() && heroes[j].estaVivo()) {
                    if (heroes[j].getVidaActual() < (heroes[j].getVidaMax() * 0.5)) {
                        for (int r = 0; r < reserva.size(); r++) {
                            Personaje candidato = reserva.get(r);
                            if (candidato.estaVivo() && candidato.getVidaActual() >= (candidato.getVidaMax() * 0.8)) {
                                Personaje herido = heroes[j];
                                heroes[j] = reserva.remove(r);
                                reserva.add(herido);
                                System.out.println("[IA DESCANSO] " + herido.getNombre()
                                        + " está herido. Se va a la reserva a recuperarse y entra "
                                        + heroes[j].getNombre());
                                break;
                            }
                        }
                    }
                }
            }

            if (i == 20 && MotorCombate.hayVivos(heroes)) {
                System.out.println(MotorCombate.ANSI_AZUL_MARINO + "\n===========================================");
                System.out.println("    [SISTEMA] ¡AVENTURA COMPLETADA CON ÉXITO!");
                System.out.println("===========================================" + MotorCombate.ANSI_RESET);

                basedatos.gestores.GestorRecompensas gr = new basedatos.gestores.GestorRecompensas();
                gr.desbloquearLogro(idPartidaActual, 10);
                System.out.println(MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Matadragones."
                        + " (Aventura completada)" + MotorCombate.ANSI_RESET);

                // Verificar dificultad para Locura Absoluta (11)
                int diffId = 2;
                try (java.sql.Connection con = basedatos.conexion.ConexionBD.getConexion();
                        java.sql.PreparedStatement ps = con
                                .prepareStatement("SELECT dificultad_id FROM Partidas WHERE ID_partida = ?")) {
                    ps.setInt(1, idPartidaActual);
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        if (rs.next())
                            diffId = rs.getInt("dificultad_id");
                    }
                } catch (java.sql.SQLException e) {
                }

                if (diffId == 1) {
                    gr.desbloquearLogro(idPartidaActual, 18);
                    System.out.println(MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Paseo por el Parque."
                            + " (Aventura completada en dificultad Facil)" + MotorCombate.ANSI_RESET);
                } else if (diffId == 2) {
                    gr.desbloquearLogro(idPartidaActual, 19);
                    System.out.println(MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] El Camino del Heroe."
                            + " (Aventura completada en dificultad Normal)" + MotorCombate.ANSI_RESET);
                } else if (diffId == 3) {
                    gr.desbloquearLogro(idPartidaActual, 11);
                    System.out.println(MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Elite de la Elite."
                            + " (Aventura completada en dificultad Dificil)" + MotorCombate.ANSI_RESET);
                }

                // Logro Impecable: cero bajas durante toda la aventura
                if (bajasTotales == 0) {
                    gr.desbloquearLogro(idPartidaActual, 20);
                    System.out.println(MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Impecable."
                            + " (Ningún héroe cayó en toda la aventura)" + MotorCombate.ANSI_RESET);
                }

                // Logro Con Refuerzos: Kallista y Kwai Chang en el equipo al final
                boolean hayKallista = false;
                boolean hayKwai = false;
                for (int j = 0; j < heroes.length; j++) {
                    if (heroes[j].estaVivo() && heroes[j].getNombre().equals("Kallista"))
                        hayKallista = true;
                    if (heroes[j].estaVivo() && heroes[j].getNombre().equals("Kwai Chang"))
                        hayKwai = true;
                }
                if (hayKallista && hayKwai) {
                    gr.desbloquearLogro(idPartidaActual, 21);
                    System.out.println(MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Con Refuerzos."
                            + " (Kallista y Kwai Chang terminaron la aventura en el equipo)" + MotorCombate.ANSI_RESET);
                }

                // Logro El Ultimo Superviviente: exactamente 1 heroe vivo al final
                int vivosFinales = 0;
                for (int j = 0; j < heroes.length; j++) {
                    if (heroes[j].estaVivo())
                        vivosFinales++;
                }
                if (vivosFinales == 1) {
                    gr.desbloquearLogro(idPartidaActual, 22);
                    System.out.println(MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] El Ultimo Superviviente."
                            + " (Solo un heroe llegó vivo al final)" + MotorCombate.ANSI_RESET);
                }

                // Actualizar DB para marcar partida como terminada
                String sqlFin = "UPDATE Partidas SET estado = 'completada' WHERE ID_partida = ?";
                try (java.sql.Connection con = basedatos.conexion.ConexionBD.getConexion();
                        java.sql.PreparedStatement ps = con.prepareStatement(sqlFin)) {
                    ps.setInt(1, idPartidaActual);
                    ps.executeUpdate();
                } catch (java.sql.SQLException e) {
                    /* Ignore */ }
            }

            // Logros de progreso
            if (i == 1 && MotorCombate.hayVivos(heroes)) {
                new basedatos.gestores.GestorRecompensas().desbloquearLogro(idPartidaActual, 2);
                System.out.println(MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Primeros Pasos."
                        + " (Primera sala superada)" + MotorCombate.ANSI_RESET);
            }
            // Logros de puntuacion (se comprueban cada sala, desbloquearLogro evita
            // duplicados)
            if (puntuacionPartida > 500) {
                if (new basedatos.gestores.GestorRecompensas().desbloquearLogro(idPartidaActual, 6)) {
                    System.out.println(MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Verdugo de Monstruos."
                            + " (Superados los 500 puntos en esta partida)" + MotorCombate.ANSI_RESET);
                }
            }
            if (puntuacionPartida > 1500) {
                if (new basedatos.gestores.GestorRecompensas().desbloquearLogro(idPartidaActual, 16)) {
                    System.out.println(MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Veterano."
                            + " (Superados los 1500 puntos en esta partida)" + MotorCombate.ANSI_RESET);
                }
            }
            if (puntuacionPartida > 3000) {
                if (new basedatos.gestores.GestorRecompensas().desbloquearLogro(idPartidaActual, 17)) {
                    System.out.println(MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Leyenda."
                            + " (Superados los 3000 puntos en esta partida)" + MotorCombate.ANSI_RESET);
                }
            }
            // Pausa para leer eventos que no son de combate (los combates ya pausan por sí
            // solos).
            boolean esEventoPequeno = (i == 2 || i == 5 || i == 7 || i == 12 || i == 14 || i == 17);
            if (esEventoPequeno) {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                }
            }

            // Solo guardar tras combates; salas de eventos no actualizan la sala en BD.
            boolean esCombate = (i != 2 && i != 5 && i != 7 && i != 9 && i != 12 && i != 14 && i != 17 && i != 19);
            if (MotorCombate.hayVivos(heroes) && i < 20 && esCombate) {
                if (!MotorCombate.modoManual || guardadoAuto) {
                    System.out.println(MotorCombate.ANSI_VERDE_OSCURO + "Guardando partida automáticamente..."
                            + MotorCombate.ANSI_RESET);
                    MotorCombate.ejecutarGuardado(heroes, i + 1);
                } else if (guardadoManual) {
                    System.out.println(MotorCombate.ANSI_AZUL_MARINO + "\n¿Deseas guardar la partida? (1. Sí / 2. No)"
                            + MotorCombate.ANSI_RESET);
                    System.out.print(MotorCombate.ANSI_BEIGE + "> Elige: " + MotorCombate.ANSI_RESET);
                    int optG = 2;
                    if (MotorCombate.sc.hasNextInt()) {
                        optG = MotorCombate.sc.nextInt();
                    }
                    MotorCombate.sc.nextLine();
                    if (optG == 1) {
                        MotorCombate.ejecutarGuardado(heroes, i + 1);
                        System.out.println(MotorCombate.ANSI_VERDE_OSCURO + "¡Partida guardada con éxito!"
                                + MotorCombate.ANSI_RESET);
                    }
                }
            }
        }

        // Al terminar el bucle (o morir), resetear partida
        idPartidaActual = -1;
    }

    private static void imprimirAsciiArt() {
        System.out.println(MotorCombate.ANSI_AZUL_MARINO +
                "\n=======================================================================================================");
        System.out.println(" █████╗ ██╗   ██╗███████╗███╗   ██╗████████╗██╗   ██╗██████╗  █████╗                    \n"
                + "██╔══██╗██║   ██║██╔════╝████╗  ██║╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██╗                \n"
                + "███████║██║   ██║█████╗  ██╔██╗ ██║   ██║   ██║   ██║██████╔╝███████║╚═╝                \n"
                + "██╔══██║╚██╗ ██╔╝██╔══╝  ██║╚██╗██║   ██║   ██║   ██║██╔══██╗██╔══██║██╗                \n"
                + "██║  ██║ ╚████╔╝ ███████╗██║ ╚████║   ██║   ╚██████╔╝██║  ██║██║  ██║╚═╝                \n"
                + "╚═╝  ╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═══╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝                   \n"
                + "                                                                                        \n"
                + "███████╗██╗         ██████╗ ███████╗███████╗ ██████╗███████╗███╗   ██╗███████╗ ██████╗  \n"
                + "██╔════╝██║         ██╔══██╗██╔════╝██╔════╝██╔════╝██╔════╝████╗  ██║██╔════╝██╔═══██╗ \n"
                + "█████╗  ██║         ██║  ██║█████╗  ███████╗██║     █████╗  ██╔██╗ ██║███████╗██║   ██║ \n"
                + "██╔══╝  ██║         ██║  ██║██╔══╝  ╚════██║██║     ██╔══╝  ██║╚██╗██║╚════██║██║   ██║ \n"
                + "███████╗███████╗    ██████╔╝███████╗███████║╚██████╗███████╗██║ ╚████║███████║╚██████╔╝ \n"
                + "╚══════╝╚══════╝    ╚═════╝ ╚══════╝╚══════╝ ╚═════╝╚══════╝╚═╝  ╚═══╝╚══════╝ ╚═════╝  \n"
                + "                                                                                        \n"
                + " █████╗     ██╗      █████╗ ███████╗    ██████╗ ██╗   ██╗██╗███╗   ██╗ █████╗ ███████╗  \n"
                + "██╔══██╗    ██║     ██╔══██╗██╔════╝    ██╔══██╗██║   ██║██║████╗  ██║██╔══██╗██╔════╝  \n"
                + "███████║    ██║     ███████║███████╗    ██████╔╝██║   ██║██║██╔██╗ ██║███████║███████╗  \n"
                + "██╔══██║    ██║     ██╔══██║╚════██║    ██╔══██╗██║   ██║██║██║╚██╗██║██╔══██║╚════██║  \n"
                + "██║  ██║    ███████╗██║  ██║███████║    ██║  ██║╚██████╔╝██║██║ ╚████║██║  ██║███████║  \n"
                + "╚═╝  ╚═╝    ╚══════╝╚═╝  ╚═╝╚══════╝    ╚═╝  ╚═╝ ╚═════╝ ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚══════╝  \n"
                + "                                                                                        \n"
                + "█▀█░█▀█░█▀▄░░░░░░░█▀▀░█▀█░█▀▄░▀█▀░█▀█░░░█░█░░░█▀▄░▀█▀░█▀▀░█▀█░█▀▄░█▀▄░█▀█               \n"
                + "█▀▀░█░█░█▀▄░░▀░░░░█▀▀░█▀█░█▀▄░░█░░█░█░░░░█░░░░█▀▄░░█░░█░░░█▀█░█▀▄░█░█░█░█               \n"
                + "▀░░░▀▀▀░▀░▀░░▀░░░░▀░░░▀░▀░▀▀░░▀▀▀░▀▀▀░░░░▀░░░░▀░▀░▀▀▀░▀▀▀░▀░▀░▀░▀░▀▀░░▀▀▀               \n");
        System.out.println("========================================================================================"
                + MotorCombate.ANSI_RESET + "\n");
    }
}