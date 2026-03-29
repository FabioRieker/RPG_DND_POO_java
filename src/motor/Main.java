package motor;

import personajes.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Clase principal que arranca el juego. Se encarga de controlar el bucle de las
 * 20 salas, generar los monstruos y llamar al motor de combate.
 * 
 * @author Ricardo Crespo
 * @author Fabio Rieker
 */
public class Main {

	/**
	 * Metodo main de ejecucion principal. Se pregunta el modo de juego (Manual o
	 * Automatico) y se recorren las salas.
	 * 
	 * @param args Argumentos pasados por consola al compilar
	 */
	public static void main(String[] args) {

		System.out.println(motor.MotorCombate.ANSI_AZUL_MARINO + "\n===========================================");
		System.out.println("    [SISTEMA] Selecciona el modo de juego:");
		System.out.println("    1. Modo Automático (La IA controla todo)");
		System.out.println("    2. Modo Manual (Control total de Héroes)");
		System.out.println("===========================================" + motor.MotorCombate.ANSI_RESET);
		System.out.print("> Elige una opción: ");
		if (motor.MotorCombate.sc.hasNextInt()) {
			int opt = motor.MotorCombate.sc.nextInt();
			if (opt == 2) {
				motor.MotorCombate.modoManual = true;
				// Meter las 5 pociones de inicio en la mochila
				for (int j = 0; j < 5; j++) {
					motor.MotorCombate.inventarioGrupo.add(new consumibles.PocionCuracion(1));
				}
			}
		}
		motor.MotorCombate.sc.nextLine();

		System.out.println(motor.MotorCombate.ANSI_AZUL_MARINO + "\n===========================================");
		System.out.println("    AVENTURA: EL DESCENSO A LAS RUINAS");
		System.out.println("===========================================" + motor.MotorCombate.ANSI_RESET + "\n");

		// Crear el equipo que lucha y preparar la reserva
		List<Personaje> listaHeroes = FabricaHeroes.crearEquipoInicial();
		List<Personaje> reserva = new ArrayList<>();
		Personaje[] heroes = listaHeroes.toArray(new Personaje[0]);

		// Bucle que recorre las 20 salas de la mazmorra
		for (int i = 1; i <= 20; i++) {
			// Si mueren todos los heroes, termina la partida
			if (!MotorCombate.hayVivos(heroes)) {
				System.out.println(motor.MotorCombate.ANSI_ROJO + "[ALERTA FATAL] EL EQUIPO HA CAÍDO EN LA SALA " + i
						+ ". FIN DE LA AVENTURA." + motor.MotorCombate.ANSI_RESET);
				break;
			}

			System.out.println("\n>>> ENTRANDO EN LA SALA " + i + "...");

			// Eventos de historia (curas, trampas y reclutamientos)
			if (i == 2) {
				System.out.println("Encontráis suministros en una caravana saqueada.");
				for (Personaje h : heroes) {
					if (h.estaVivo())
						h.recuperarRecursos(30);
				}
			} else if (i == 5) {
				System.out.println(motor.MotorCombate.ANSI_MORADO
						+ "[EVENTO] ¡Rescatáis a Kallista! Se une a vuestra reserva." + motor.MotorCombate.ANSI_RESET);
				reserva.add(FabricaHeroes.crearKallista());
			} else if (i == 7) {
				System.out.println("¡BOOM! Una trampa de fuego estalla.");
				for (Personaje h : heroes) {
					if (h.estaVivo())
						h.recibirDaño(12, true);
				}
			} else if (i == 9) {
				System.out.println(motor.MotorCombate.ANSI_MORADO
						+ "[EVENTO] Llegáis a una fuente curativa. El grupo descansa." + motor.MotorCombate.ANSI_RESET);
				for (Personaje h : heroes) {
					if (h.estaVivo())
						h.curar(50);
				}
				if (motor.MotorCombate.modoManual)
					motor.MotorCombate.gestionarCampamento(heroes, reserva);
			} else if (i == 12) {
				System.out.println(motor.MotorCombate.ANSI_MORADO
						+ "[EVENTO] Un monje llamado Kwai Chang se une a vuestra reserva."
						+ motor.MotorCombate.ANSI_RESET);
				reserva.add(FabricaHeroes.crearMonjeKwai());
			} else if (i == 14) {
				System.out.println("Sala vacía... un silencio sepulcral inunda el lugar.");
			} else if (i == 17) {
				System.out.println(motor.MotorCombate.ANSI_MORADO
						+ "[EVENTO] Lulu Nightingale, la barda, se une a vuestra reserva."
						+ motor.MotorCombate.ANSI_RESET);
				reserva.add(FabricaHeroes.crearBardoLulu());
			} else if (i == 19) {
				System.out.println("Último descanso antes del gran final. Salud y recursos al máximo.");
				for (Personaje h : heroes) {
					if (h.estaVivo()) {
						h.curar(100);
						h.recuperarRecursos(100);
					}
				}
				if (motor.MotorCombate.modoManual)
					motor.MotorCombate.gestionarCampamento(heroes, reserva);
			} else {
				// Cargar los enemigos que toquen en esta sala
				Sala salaActual = FabricaSalas.generarSala(i);
				List<Personaje> listaEnemigos = salaActual.getEnemigos();
				Personaje[] enemigos = listaEnemigos.toArray(new Personaje[0]);

				// Empezar la pelea pasando los dos arrays
				MotorCombate.iniciarCombate(heroes, enemigos);
			}

			// Si un héroe principal muere y quedan reservas, se hace el cambio
			for (int j = 0; j < heroes.length; j++) {
				if (!heroes[j].estaVivo() && !reserva.isEmpty()) {
					Personaje caido = heroes[j];
					for (int r = 0; r < reserva.size(); r++) {
						if (reserva.get(r).estaVivo()) {
							Personaje sustituto = reserva.remove(r);
							System.out.println(motor.MotorCombate.ANSI_ROJO + "\n[SISTEMA] " + caido.getNombre()
									+ " ha muerto en acto de servicio." + motor.MotorCombate.ANSI_RESET);
							System.out.println(motor.MotorCombate.ANSI_MORADO + "[RESERVA] " + sustituto.getNombre()
									+ " entra al equipo principal para ocupar su lugar."
									+ motor.MotorCombate.ANSI_RESET);
							heroes[j] = sustituto;
							break;
						}
					}
				}

				// En las fuentes curativas, cambiar a los heridos por reservas sanos
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
				System.out
						.println(motor.MotorCombate.ANSI_AZUL_MARINO + "\n===========================================");
				System.out.println("    [SISTEMA] ¡AVENTURA COMPLETADA CON ÉXITO!");
				System.out.println("===========================================" + motor.MotorCombate.ANSI_RESET);
			}
		}

	}
}