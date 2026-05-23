package basedatos.gestores;

import basedatos.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import motor.MotorCombate;
import motor.Main;

/**
 * Maneja el ciclo de vida de las partidas, desde su creación inicial hasta
 * el guardado de progreso y la restauración del estado de los héroes.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class GestorPartidas {

    /**
     * Flujo interactivo por consola para crear una nueva partida.
     * Pide nombre, dificultad y si es admin la sala de inicio.
     * Guarda el progreso inicial y configura el estado estático en Main.
     * 
     * @param idUsuarioLogueado ID del usuario actual.
     * @param nombreUsuarioLogueado Nombre del usuario (para comprobar si es Admin).
     * @return El ID de la partida generada o -1 en caso de fallo.
     */
    public int menuNuevaPartida(int idUsuarioLogueado, String nombreUsuarioLogueado) {
        System.out.println(MotorCombate.ANSI_AZUL_MARINO + "\n=== [NUEVA PARTIDA] ===" + MotorCombate.ANSI_RESET);
        String nombrePartida;
        while (true) {
            System.out.print(MotorCombate.ANSI_BEIGE + "Introduce un nombre para tu partida: " + MotorCombate.ANSI_RESET);
            nombrePartida = MotorCombate.sc.nextLine();
            if (nombrePartida.trim().isEmpty() || nombrePartida.length() > 100) {
                System.out.println(MotorCombate.ANSI_ROJO + "[SISTEMA] El nombre debe tener entre 1 y 100 caracteres."
                        + MotorCombate.ANSI_RESET);
            } else if (this.existeNombrePartida(nombrePartida, idUsuarioLogueado)) {
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

        int nuevaIdPartida = this.crearNuevaPartida(nombrePartida, idUsuarioLogueado, dif);

        if (nuevaIdPartida != -1) {
            double multiplicador = new GestorDificultad().obtenerMultiplicador(dif);
            MotorCombate.multiplicadorDificultad = multiplicador;

            Main.salaActual = salaInicio;
            Main.puntuacionPartida = 0;
            Main.bajasTotales = 0;
            System.out.println(MotorCombate.ANSI_VERDE_OSCURO + "\n¡Partida '" + nombrePartida + "' creada con éxito!"
                    + MotorCombate.ANSI_RESET);
        }
        
        return nuevaIdPartida;
    }

    /**
     * Consulta las partidas activas del usuario logueado, permite seleccionar una
     * mediante su ID y restaura la sala, puntuación y dificultad antes de saltar a
     * la aventura.
     * 
     * @param idUsuarioLogueado ID del usuario actual.
     * @return El ID de la partida cargada, o -1 si falla o se cancela.
     */
    public int menuCargarPartida(int idUsuarioLogueado) {
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
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar partidas: " + e.getMessage());
            return -1;
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
                            return -1;
                        }
                        
                        Main.salaActual = rs2.getInt("sala_actual");
                        Main.puntuacionPartida = rs2.getInt("puntuacion");
                        int diffId = rs2.getInt("dificultad_id");
                        double multiplicador = new GestorDificultad().obtenerMultiplicador(diffId);
                        MotorCombate.multiplicadorDificultad = multiplicador;

                        System.out.println(MotorCombate.ANSI_VERDE_OSCURO + "Partida cargada. Retomando desde la sala "
                                + Main.salaActual + "..." + MotorCombate.ANSI_RESET);
                        return idElegido;
                    } else {
                        System.out.println(MotorCombate.ANSI_ROJO + "[SISTEMA] ID de partida no válido o no te pertenece."
                                + MotorCombate.ANSI_RESET);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return -1;
    }

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
            System.out.println("Error al comprobar nombre de partida: " + e.getMessage());
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
            System.out.println("Error creando partida: " + e.getMessage());
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
            System.out.println("Error crítico al guardar. Aplicando Rollback: " + e.getMessage());
            try {
                con.rollback(); // Si algo falla, deshacemos todo para evitar datos corruptos
            } catch (SQLException ex) {
                System.out.println("Error en el rollback: " + ex.getMessage());
            }
        } finally {
            try {
                con.setAutoCommit(true); // Devolvemos la conexión a su comportamiento normal
            } catch (SQLException e) {
                System.out.println("Error de conexión: " + e.getMessage());
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
            System.out.println("Error guardando historial: " + e.getMessage());
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
            System.out.println("Error restaurando estado de los héroes: " + e.getMessage());
        }
    }
}