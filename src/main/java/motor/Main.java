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

/**
 * Clase principal que arranca el juego. Gestiona el Login, el Menu Principal,
 * la persistencia en base de datos y el bucle de las 20 salas.
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Main {

    // --- VARIABLES GLOBALES DE PERSISTENCIA ---
    public static int idUsuarioLogueado = -1;
    public static String nombreUsuarioLogueado = "";
    public static int idPartidaActual = -1;
    public static int salaActual = 1;
    public static int puntuacionPartida = 0;

    public static void main(String[] args) {

        imprimirAsciiArt();

        GestorUsuarios gestorUsuarios = new GestorUsuarios();

        // 1. BUCLE DE AUTENTICACION (LOGIN / REGISTRO)
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

                idUsuarioLogueado = gestorUsuarios.validarLogin(nombre, pass);
                if (idUsuarioLogueado == -1) {
                    System.out.println(MotorCombate.ANSI_ROJO + "Credenciales incorrectas." + MotorCombate.ANSI_RESET);
                } else {
                    nombreUsuarioLogueado = nombre;
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
                    } else if (gestorUsuarios.existeUsuario(nombre)) {
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
                    } else if (gestorUsuarios.existeEmail(email)) {
                        System.out.println(MotorCombate.ANSI_ROJO
                                + "[SISTEMA] Ese email ya está registrado." + MotorCombate.ANSI_RESET);
                    } else {
                        break;
                    }
                }

                int nuevoId = gestorUsuarios.registrarUsuario(nombre, pass, email);
                if (nuevoId != -1) {
                    System.out.println(MotorCombate.ANSI_VERDE_OSCURO
                            + "Cuenta creada con éxito. Ya puedes iniciar sesión." + MotorCombate.ANSI_RESET);
                }
            }
        }

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
            System.out.println(MotorCombate.ANSI_AZUL_MARINO + "\n=== [SELECCIÓN DE DIFICULTAD] ===" + MotorCombate.ANSI_RESET);
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
            System.out.println(MotorCombate.ANSI_VERDE_OSCURO + "\n¡Partida '" + nombrePartida + "' creada con éxito!"
                    + MotorCombate.ANSI_RESET);
            iniciarAventura();
        }
    }

    /**
     * Consulta las partidas activas del usuario logueado, permite seleccionar una 
     * mediante su ID y restaura la sala, puntuación y dificultad antes de saltar a la aventura.
     */
    private static void cargarPartidaGuardada() {
        System.out.println(MotorCombate.ANSI_AZUL_MARINO + "\n=== [CARGAR PARTIDA] ===" + MotorCombate.ANSI_RESET);
        System.out.println("--- TUS PARTIDAS GUARDADAS ---");
        String sql = "SELECT ID_partida, nombre_partida, sala_actual, puntuacion FROM Partidas WHERE usuario_id = ? AND estado = 'activa'";

        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuarioLogueado);
            try (ResultSet rs = ps.executeQuery()) {
                boolean hayPartidas = false;
                while (rs.next()) {
                    hayPartidas = true;
                    System.out.println("ID: " + rs.getInt("ID_partida") +
                            " | Nombre: " + rs.getString("nombre_partida") +
                            " | Sala: " + rs.getInt("sala_actual") +
                            " | Puntos: " + rs.getInt("puntuacion"));
                }
                if (!hayPartidas) {
                    System.out.println(
                            MotorCombate.ANSI_ROJO + "No tienes partidas guardadas." + MotorCombate.ANSI_RESET);
                    return;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar partidas: " + e.getMessage());
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
            String sqlCarga = "SELECT sala_actual, puntuacion, dificultad_id FROM Partidas WHERE ID_partida = ? AND usuario_id = ?";
            try (PreparedStatement ps2 = con.prepareStatement(sqlCarga)) {
                ps2.setInt(1, idElegido);
                ps2.setInt(2, idUsuarioLogueado);
                try (ResultSet rs2 = ps2.executeQuery()) {
                    if (rs2.next()) {
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
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void mostrarMisLogros() {
        System.out.println(
                "\n" + MotorCombate.ANSI_MORADO + "=== MIS LOGROS DESBLOQUEADOS ===" + MotorCombate.ANSI_RESET);
        String sql = "SELECT DISTINCT l.nombre, l.puntos FROM Partida_Logros pl " +
                "JOIN Logros l ON pl.logro_id = l.ID_logro " +
                "JOIN Partidas p ON pl.partida_id = p.ID_partida " +
                "WHERE p.usuario_id = ?";
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuarioLogueado);
            try (ResultSet rs = ps.executeQuery()) {
                boolean tieneLogros = false;
                while (rs.next()) {
                    tieneLogros = true;
                    System.out.println("- " + rs.getString("nombre") + " (+" + rs.getInt("puntos") + " pts)");
                }
                if (!tieneLogros) {
                    System.out.println("Aún no has desbloqueado ningún logro. ¡Sigue jugando!");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar logros: " + e.getMessage());
        }
        System.out.println("Presiona ENTER para volver...");
        MotorCombate.sc.nextLine();
    }

    // --- BUCLE PRINCIPAL DEL JUEGO ---

    /*
     * Bucle principal del juego. Gestiona eventos de historia estáticos (salas 2, 5, 7, etc.), 
     * eventos de curación, reclutamientos y genera combates en el resto de salas.
     * También controla los puntos de guardado y los reemplazos del equipo de reserva.
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
                System.out.println(
                        MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Un Nuevo Aliado." + MotorCombate.ANSI_RESET);
            } else if (i == 7) {
                System.out.println("¡BOOM! Una trampa de fuego estalla.");
                for (Personaje h : heroes) {
                    if (h.estaVivo())
                        h.recibirDaño(12, true);
                }
            } else if (i == 9) {
                System.out.println(MotorCombate.ANSI_MORADO
                        + "[EVENTO] Llegáis a una fuente curativa. El grupo descansa." + MotorCombate.ANSI_RESET);
                for (Personaje h : heroes) {
                    if (h.estaVivo())
                        h.curar(50);
                }
                new basedatos.gestores.GestorRecompensas().desbloquearLogro(idPartidaActual, 3);
                System.out.println(
                        MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Campista Novato." + MotorCombate.ANSI_RESET);
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
                System.out.println(
                        MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Matadragones." + MotorCombate.ANSI_RESET);

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

                if (diffId == 3) {
                    gr.desbloquearLogro(idPartidaActual, 11);
                    System.out.println(MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Locura Absoluta."
                            + MotorCombate.ANSI_RESET);
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
                System.out.println(
                        MotorCombate.ANSI_MORADO + "[LOGRO DESBLOQUEADO] Primeros Pasos." + MotorCombate.ANSI_RESET);
            }
            if (puntuacionPartida > 1000) {
                new basedatos.gestores.GestorRecompensas().desbloquearLogro(idPartidaActual, 6);
                // Ocultar mensaje repetitivo para evitar saturar la consola.
            }
            // Pausa para leer eventos que no son de combate (los combates ya pausan por sí solos).
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