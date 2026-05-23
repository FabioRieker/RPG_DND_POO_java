package motor;

import personajes.*;
import basedatos.gestores.*;

import java.util.List;
import java.util.ArrayList;

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
                    idPartidaActual = new GestorPartidas().menuNuevaPartida(idUsuarioLogueado, nombreUsuarioLogueado);
                    if (idPartidaActual != -1) {
                        iniciarAventura();
                    }
                    break;
                case 2:
                    idPartidaActual = new GestorPartidas().menuCargarPartida(idUsuarioLogueado);
                    if (idPartidaActual != -1) {
                        iniciarAventura();
                    }
                    break;
                case 3:
                    gestorUsuarios.mostrarRankingGlobal();
                    break;
                case 4:
                    new GestorRecompensas().mostrarMenuLogros(idUsuarioLogueado);
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

    // --- BUCLE PRINCIPAL DEL JUEGO ---

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