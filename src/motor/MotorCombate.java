package motor;

import personajes.*;
import consumibles.*;
import armas.Arma;
import armas.Armeria;
import java.util.ArrayList;

/**
 * Clase principal que maneja los turnos e interacciones de la batalla. Controla
 * si actúan los monstruos o los aliados, los bufos de estado y el inventario
 * del grupo.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class MotorCombate {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_ROJO = "\u001B[31m";
	public static final String ANSI_VERDE_OSCURO = "\u001B[32m"; // Verde estándar
	public static final String ANSI_AMARILLO = "\u001B[33m";
	public static final String ANSI_AZUL = "\u001B[34m";
	public static final String ANSI_AZUL_MARINO = "\u001B[38;5;39m"; // Azul Claro/Cian oscuro
	public static final String ANSI_BEIGE = "\u001B[38;5;223m";
	public static final String ANSI_MORADO = "\u001B[35m";
	public static final String ANSI_CIAN = "\u001B[36m";

	// Guardar quién ha sido el último en pegarle al jefe para la venganza
	private static Personaje ultimoAtacanteJefe = null;

	public static boolean modoManual = false;
	public static java.util.Scanner sc = new java.util.Scanner(System.in);
	public static ArrayList<Consumible> inventarioGrupo = new ArrayList<>();

	// Mochila común con armas para todo el grupo
	public static ArrayList<Arma> mochilaComun = new ArrayList<>();

	/**
	 * Método principal que inicia el combate contra una lista de enemigos. Gestiona
	 * el bucle de turnos hasta que uno de los dos bandos es derrotado.
	 * 
	 * @param heroes   Array que representa al equipo controlado por el jugador.
	 * @param enemigos Array que representa a los oponentes.
	 */
	public static void iniciarCombate(Personaje[] heroes, Personaje[] enemigos) {
		ultimoAtacanteJefe = null;

		// Arranca el bucle visual y lógico del combate
		System.out.println("\n" + ANSI_AZUL_MARINO + "===========================================");
		System.out.println("          [SISTEMA] ¡COMBATE COMIENZA!");
		System.out.println("===========================================" + ANSI_RESET);

		int turno = 1;
		while (hayVivos(heroes) && hayVivos(enemigos) && turno <= 10) {
			System.out.println("\n" + ANSI_CIAN + "======= [TURNO " + turno + "] =======" + ANSI_RESET);

			// Restar turnos de estados alterados a los héroes
			for (int i = 0; i < heroes.length; i++) {
				Personaje h = heroes[i];
				if (h.estaVivo()) {
					h.pasarTurnoDeEstados();
				}
			}

			// Restar turnos de estados alterados a los enemigos
			for (int i = 0; i < enemigos.length; i++) {
				Personaje e = enemigos[i];
				if (e.estaVivo()) {
					e.pasarTurnoDeEstados();
				}
			}

			// Juntar héroes y enemigos en una lista para ver quién ataca primero
			java.util.ArrayList<Personaje> todos = new java.util.ArrayList<Personaje>();

			for (int i = 0; i < heroes.length; i++) {
				Personaje h = heroes[i];
				if (h.estaVivo()) {
					todos.add(h);
				}
			}

			for (int i = 0; i < enemigos.length; i++) {
				Personaje e = enemigos[i];
				if (e.estaVivo()) {
					todos.add(e);
				}
			}

			// Ordenar a todos según su destreza de mayor a menor
			todos.sort((p1, p2) -> Integer.compare(p2.getDestrezaTotal(), p1.getDestrezaTotal()));

			// Crear el formato de texto para imprimir el orden de ataque
			String listaOrdenada = "";
			for (int i = 0; i < todos.size(); i++) {
				Personaje pActual = todos.get(i);

				// Añadir marca [H] para héroes o [E] para enemigos
				String marca = " [E]";
				for (int j = 0; j < heroes.length; j++) {
					if (heroes[j] == pActual) {
						marca = " [H]";
					}
				}

				listaOrdenada = listaOrdenada + pActual.getNombre() + marca;

				// Poner coma de separación si quedan más luchadores
				if (i < todos.size() - 1) {
					listaOrdenada = listaOrdenada + ", ";
				}
			}

			System.out.println(ANSI_CIAN + "[INICIATIVA] Orden: " + listaOrdenada + ANSI_RESET);

			for (int i = 0; i < todos.size(); i++) {
				Personaje p = todos.get(i);

				if (p.estaVivo() == false || hayVivos(heroes) == false || hayVivos(enemigos) == false) {
					continue;
				}

				p.reiniciarDefensa();

				if (p.tieneEstado("Aturdimiento") || p.tieneEstado("Congelado") || p.tieneEstado("Lisiado")) {
					System.out.println(ANSI_AMARILLO + "\n[ESTADO] " + p.getNombre()
							+ " no puede actuar este turno debido a su estado." + ANSI_RESET);
					continue;
				}

				// Comprobar si le toca a un héroe o a un enemigo
				boolean esHeroe = false;
				for (int j = 0; j < heroes.length; j++) {
					if (heroes[j] == p) {
						esHeroe = true;
					}
				}

				if (esHeroe == true) {
					if (modoManual) {
						turnoHeroeManual(p, enemigos, heroes);
					} else {
						turnoHeroe(p, enemigos);
					}
				} else {
					turnoEnemigo(p, heroes);
				}

				// Pausa para que dé tiempo a leer la consola
				try {
					Thread.sleep(800);
				} catch (InterruptedException ex) {
				}
			}

			// Mostrar resumen de vidas al acabar la ronda actual
			System.out.println("\n" + ANSI_BEIGE + "--- [SISTEMA] Estado tras turno " + turno + " ---" + ANSI_RESET);
			System.out.println("Heroes:");
			for (int i = 0; i < heroes.length; i++) {
				heroes[i].mostrarInfoBreve();
			}
			System.out.println("Enemigos:");
			for (int i = 0; i < enemigos.length; i++) {
				enemigos[i].mostrarInfoBreve();
			}

			System.out.println("");
			turno++;
		}

		// Imprimir ganador y recoger recompensas al acabar la pelea
		System.out.println("\n" + ANSI_AZUL_MARINO + "===========================================");
		System.out.println("            [RESULTADO]");
		System.out.println("===========================================" + ANSI_RESET);

		if (hayVivos(heroes) == true) {
			System.out.println(ANSI_VERDE_OSCURO + "[SISTEMA] ¡VICTORIA! Los heroes han ganado." + ANSI_RESET);
			// Dar botín de los monstruos que han muerto
			for (Personaje e : enemigos) {
				procesarAutoLoot(heroes, e);
			}
		} else if (hayVivos(enemigos) == true) {
			System.out.println(ANSI_ROJO + "¡DERROTA! Los enemigos han ganado." + ANSI_RESET);
		} else {
			System.out.println(ANSI_AMARILLO + "¡EMPATE!" + ANSI_RESET);
		}

		System.out.println("\n" + ANSI_AZUL_MARINO + "===========================================");
		System.out.println("         [SISTEMA] COMBATE FINALIZADO");
		System.out.println("===========================================" + ANSI_RESET + "\n");
	}

	/**
	 * Turno automático de un héroe cuando no está en modo manual. Decide si usa
	 * pociones, habilidades o ataca directamente.
	 * 
	 * @param p        El personaje que ataca.
	 * @param enemigos Los enemigos a los que puede atacar.
	 */
	private static void turnoHeroe(Personaje p, Personaje[] enemigos) {
		// Lógica matemática para cuando un héroe es controlado por la CPU
		boolean haUsadoItem = false;
		if (p.getVidaActual() < (p.getVidaMax() * 0.3)) {
			for (int j = 0; j < p.getInventario().size(); j++) {
				Consumible c = p.getInventario().get(j);
				if (c.getNombre().equals("Poción de Curación") && c.getCantidad() > 0) {
					c.usar(p, p);
					if (c.getCantidad() <= 0) {
						p.getInventario().remove(j);
						j--;
					}
					haUsadoItem = true;
					break;
				}
			}
			if (!haUsadoItem) {
				if (Math.random() < 0.5) {
					p.defenderse();
					haUsadoItem = true;
				}
			}
		}

		if (haUsadoItem == false) {
			Personaje objetivo = seleccionarObjetivoInteligente(p, enemigos);

			// Guardar referencia si el bot ataca a un jefe para el sistema de represalias
			if (objetivo != null && objetivo.getTipoClase() == TipoClase.JEFE) {
				ultimoAtacanteJefe = p;
			}

			if (objetivo != null) {
				boolean usoHabilidad = false;
				java.util.ArrayList<habilidad.AccionCombate> habs = p.getHabilidades();
				if (habs != null && habs.size() > 0) {
					double probMagia = 0.5;
					if (p.getVidaActual() < (p.getVidaMax() * 0.5)) {
						probMagia = 0.85;
					}
					if (Math.random() < probMagia) {
						java.util.ArrayList<habilidad.AccionCombate> clon = new java.util.ArrayList<>(habs);
						java.util.Collections.shuffle(clon);
						for (int j = 0; j < clon.size(); j++) {
							habilidad.AccionCombate h = clon.get(j);

							String nHab = h.getNombre();
							Personaje objHab = objetivo;

							// Prohibir que la IA asigne curaciones al bando rival
							if (nHab.equals("Purificación") || nHab.equals("Muro de Piedra")) {
								objHab = p;
							}
							// Bloquear ataques de área porque causan problemas con los bucles actuales
							else if (nHab.equals("Nube Tóxica") || nHab.equals("Ventisca")
									|| nHab.equals("Lluvia de Flechas") || nHab.equals("Rayo Encadenado")
									|| nHab.equals("Grito de Guerra")) {
								continue;
							}

							if (p.tieneRecursos(h.getCosteEnergia(), h.getCosteMana())) {
								h.ejecutar(p, objHab);
								usoHabilidad = true;
								break;
							}
						}
					}
				}
				if (usoHabilidad == false) {
					p.atacar(objetivo);
				}
			}
		}
	}

	/**
	 * Controla el turno de los enemigos. Decide si deciden curarse, usar
	 * habilidades o atacar a un héroe.
	 * 
	 * @param p      El enemigo que tiene el turno.
	 * @param heroes Los héroes aliados disponibles para atacar.
	 */
	private static void turnoEnemigo(Personaje p, Personaje[] heroes) {
		// Lógica y variables del turno de la CPU enemiga
		boolean haUsadoItem = false;
		if (p.getVidaActual() < (p.getVidaMax() * 0.3)) {
			for (int j = 0; j < p.getInventario().size(); j++) {
				Consumible c = p.getInventario().get(j);
				if (c.getNombre().equals("Poción de Curación") && c.getCantidad() > 0) {
					c.usar(p, p);
					if (c.getCantidad() <= 0) {
						p.getInventario().remove(j);
						j--;
					}
					haUsadoItem = true;
					break;
				}
			}
			if (!haUsadoItem) {
				if (Math.random() < 0.5) {
					p.defenderse();
					haUsadoItem = true;
				}
			}
		}

		if (haUsadoItem == false) {
			Personaje objetivo = seleccionarObjetivoInteligente(p, heroes);

			if (objetivo != null) {
				// Dar prioridad del 30% al hechizo letal nativo de los jefes
				boolean usoHabilidadEspecial = false;
				if (p instanceof Jefe && Math.random() < 0.3) {
					((Jefe) p).habilidadEspecial(objetivo);
					usoHabilidadEspecial = true;
				} else {
					boolean usoHabilidad = false;
					java.util.ArrayList<habilidad.AccionCombate> habs = p.getHabilidades();
					if (habs != null && habs.size() > 0) {
						if (Math.random() < 0.3) {
							java.util.ArrayList<habilidad.AccionCombate> clon = new java.util.ArrayList<>(habs);
							java.util.Collections.shuffle(clon);
							for (int j = 0; j < clon.size(); j++) {
								habilidad.AccionCombate h = clon.get(j);

								String nHab = h.getNombre();
								Personaje objHab = objetivo;

								if (nHab.equals("Purificación") || nHab.equals("Muro de Piedra")) {
									objHab = p;
								} else if (nHab.equals("Nube Tóxica") || nHab.equals("Ventisca")
										|| nHab.equals("Lluvia de Flechas") || nHab.equals("Rayo Encadenado")
										|| nHab.equals("Grito de Guerra")) {
									continue;
								}

								if (p.tieneRecursos(h.getCosteEnergia(), h.getCosteMana())) {
									h.ejecutar(p, objHab);
									usoHabilidad = true;
									break;
								}
							}
						}
					}
					if (usoHabilidad == false && usoHabilidadEspecial == false) {
						p.atacar(objetivo);
					}
				}
			}
		}
	}

	// Calcular si los enemigos sueltan su arma al morir
	/**
	 * Calcula si un enemigo suelta un arma u objeto después de morir. Si puede, se
	 * lo equipa automáticamente a un héroe o lo guarda en la mochila.
	 * 
	 * @param heroes  Lista de héroes actuales.
	 * @param enemigo Enemigo que acaba de ser derrotado.
	 */
	private static void procesarAutoLoot(Personaje[] heroes, Personaje enemigo) {
		double chance = Math.random();
		// Los monstruos normales sueltan algo al morir el 30% de veces, los jefes un
		// 100% de veces
		double limite = (enemigo.getTipoClase() == TipoClase.JEFE) ? 1.0 : 0.70;

		if (chance <= limite && enemigo.getArma() != null) {
			Arma loot = enemigo.getArma();

			if (enemigo instanceof JefeDragon)
				loot = new Armeria().get("Hoja Fénix");
			if (enemigo instanceof JefeLich)
				loot = new Armeria().get("Colmillo de Araña");

			if (loot != null) {
				System.out.println(ANSI_AMARILLO + "\n[BOTÍN] " + enemigo.getNombre() + " ha soltado: "
						+ loot.getNombre() + ANSI_RESET);

				boolean equipado = false;
				for (Personaje h : heroes) {
					// Conceder el arma inmediatamente si el héroe está desarmado y la domina
					if (h.estaVivo() && h.getArma() == null && h.getArmasPermitidas().contains(loot.getCategoria())) {
						h.equiparArma(loot);
						equipado = true;
						break;
					}
				}

				if (!equipado) {
					mochilaComun.add(loot);
					System.out.println(ANSI_CIAN + "[SISTEMA] " + loot.getNombre()
							+ " se ha guardado en la mochila común." + ANSI_RESET);
				}
			}
		}
	}

	/**
	 * Comprueba si todavía quedan personajes con vida en un grupo.
	 * 
	 * @param grupo Array de personajes a revisar.
	 * @return true si queda alguno vivo, false si todos están muertos.
	 */
	public static boolean hayVivos(Personaje[] grupo) {
		for (int i = 0; i < grupo.length; i++) {
			if (grupo[i].estaVivo() == true) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Lógica para elegir al mejor objetivo al que atacar en el turno automático.
	 * Valora estados de provocación, quién fue el último atacante o la vida máxima.
	 * 
	 * @param atacante          Personaje que va a realizar el ataque.
	 * @param posiblesObjetivos Enemigos a los que podría atacar.
	 * @return Personaje elegido para recibir el impacto.
	 */
	public static Personaje seleccionarObjetivoInteligente(Personaje atacante, Personaje[] posiblesObjetivos) {
		// Mirar quién está vivo para no atacar a muertos
		java.util.ArrayList<Personaje> vivos = new java.util.ArrayList<Personaje>();
		for (int i = 0; i < posiblesObjetivos.length; i++) {
			if (posiblesObjetivos[i].estaVivo()) {
				vivos.add(posiblesObjetivos[i]);
			}
		}

		if (vivos.isEmpty())
			return null;

		// Obligar a la IA a pegar a quien haya invocado defensa zonal
		for (Personaje vivo : vivos) {
			if (vivo.isMuroActivo()) {
				System.out.println(ANSI_AMARILLO + "\n[IA] " + atacante.getNombre() + " se ve forzado a atacar a "
						+ vivo.getNombre() + " por el muro de piedra!" + ANSI_RESET);
				return vivo;
			}
		}

		// Si el jefe recibe daño, tiene un 70% de probabilidades de vengarse
		if (atacante.getTipoClase() == TipoClase.JEFE && ultimoAtacanteJefe != null && ultimoAtacanteJefe.estaVivo()) {
			if (Math.random() < 0.7) {
				System.out.println(ANSI_ROJO + "\n[IA ATAQUE] ¡" + atacante.getNombre() + " ruge de furia contra "
						+ ultimoAtacanteJefe.getNombre() + " por el último golpe!" + ANSI_RESET);
				return ultimoAtacanteJefe;
			}
		}

		// Balance general de IA (25% fuerte / 25% aleatorio / 50% rematar débil)
		double azar = Math.random() * 100;

		// 25% ataque al tanque (Más defensa)
		if (azar <= 25) {
			Personaje tanque = vivos.get(0);
			for (int i = 1; i < vivos.size(); i++) {
				if (vivos.get(i).getDefensaTotal() > tanque.getDefensaTotal()) {
					tanque = vivos.get(i);
				}
			}
			return tanque;
		}
		// (25% de lanzar golpe rápido totalmente al azar)
		else if (azar <= 50) {
			int index = (int) (Math.random() * vivos.size());
			return vivos.get(index);
		}
		// (50% de seleccionar a la unidad más herida)
		else {
			Personaje debil = vivos.get(0);
			for (int i = 1; i < vivos.size(); i++) {
				if (vivos.get(i).getVidaActual() < debil.getVidaActual()) {
					debil = vivos.get(i);
				}
			}
			return debil;
		}
	}

	/**
	 * Método alternativo guardado por si en un futuro hacemos habilidades
	 * aleatorias. Solo coge una víctima viva al azar.
	 * 
	 * @param grupo Matriz de víctimas de la que queremos extraer una diana.
	 * @return Personaje elegido aleatoriamente listo para ser atacado.
	 */
	// Método mantenido por si acaso, aunque no se use
	public static Personaje obtenerObjetivoAleatorio(Personaje[] grupo) {
		ArrayList<Personaje> vivos = new ArrayList<>();
		for (int i = 0; i < grupo.length; i++) {
			if (grupo[i].estaVivo() == true) {
				vivos.add(grupo[i]);
			}
		}
		if (vivos.isEmpty()) {
			return null;
		}
		int indiceAleatorio = (int) (Math.random() * vivos.size());
		return vivos.get(indiceAleatorio);
	}

	/**
	 * Menú interactivo que aparece entre combates (y en ciertas paradas). Permite
	 * curarse, o cambiar titulares por reservas.
	 * 
	 * @param titulares Los héroes que están jugando ahora.
	 * @param reserva   Los héroes que están en el banquillo.
	 */
	public static void gestionarCampamento(Personaje[] titulares, java.util.List<Personaje> reserva) {
		boolean salir = false;
		do {
			System.out.println(ANSI_CIAN
					+ "\n[CAMPAMENTO] El fuego de campamento crepita. ¿Qué deseas hacer con el grupo?" + ANSI_RESET);
			System.out.println("1. Descansar y continuar la aventura");
			System.out.println("2. Relevar Héroes (Banquillo)");
			System.out.println("3. Rearmarse (Sacar arma de mochila comun)");
			System.out.print("> Elige: ");

			int opt = 1;
			if (sc.hasNextInt()) {
				opt = sc.nextInt();
			}
			sc.nextLine();

			if (opt == 1) {
				salir = true;
				System.out.println(ANSI_BEIGE + "[SISTEMA] El grupo recoge el campamento y avanza." + ANSI_RESET);
			} else if (opt == 2) {
				// Ejecutar entrada/salida de personajes de la partida
				if (reserva.isEmpty()) {
					System.out.println(ANSI_AMARILLO + "[SISTEMA] No hay héroes vivos en la reserva." + ANSI_RESET);
					continue;
				}
				System.out.println("--- Equipo Titular ---");
				for (int i = 0; i < titulares.length; i++) {
					System.out.println(
							(i + 1) + ". " + titulares[i].getNombre() + " (" + titulares[i].getVidaActual() + " HP)");
				}
				System.out.print("> Elige titular a retirar (1-" + titulares.length + ") o 0 para cancelar: ");
				int numeroTitular = 0;
				if (sc.hasNextInt()) {
					numeroTitular = sc.nextInt();
				}
				sc.nextLine();

				if (numeroTitular > 0 && numeroTitular <= titulares.length) {
					System.out.println("--- Equipo Reserva ---");
					for (int i = 0; i < reserva.size(); i++) {
						System.out.println((i + 1) + ". " + reserva.get(i).getNombre() + " ("
								+ reserva.get(i).getVidaActual() + " HP)");
					}
					System.out.print("> Elige reserva a introducir (1-" + reserva.size() + ") o 0 para cancelar: ");
					int numeroReserva = 0;
					if (sc.hasNextInt()) {
						numeroReserva = sc.nextInt();
					}
					sc.nextLine();

					if (numeroReserva > 0 && numeroReserva <= reserva.size()) {
						Personaje elQueSale = titulares[numeroTitular - 1];
						Personaje elQueEntra = reserva.remove(numeroReserva - 1);

						// Intercambiar jugador activo por el suplente
						titulares[numeroTitular - 1] = elQueEntra;
						reserva.add(elQueSale);
						System.out.println(
								ANSI_MORADO + "[EVENTO] " + elQueSale.getNombre() + " se retira a descansar, y "
										+ elQueEntra.getNombre() + " se une al grupo activo." + ANSI_RESET);
					}
				}
			} else if (opt == 3) {
				// Ejecutar préstamo de armas al equipo protagonista
				if (mochilaComun.isEmpty()) {
					System.out.println(ANSI_AMARILLO + "[SISTEMA] La armería del grupo está vacía." + ANSI_RESET);
					continue;
				}
				System.out.println("--- Elige Héroe a equipar ---");
				for (int i = 0; i < titulares.length; i++) {
					if (titulares[i].estaVivo())
						System.out.println((i + 1) + ". " + titulares[i].getNombre());
				}
				System.out.print("> Elige (1-" + titulares.length + "): ");
				int numeroTitular = 0;
				if (sc.hasNextInt()) {
					numeroTitular = sc.nextInt();
				}
				sc.nextLine();

				if (numeroTitular > 0 && numeroTitular <= titulares.length) {
					Personaje h = titulares[numeroTitular - 1];
					System.out.println("--- Mochila Comun ---");
					for (int i = 0; i < mochilaComun.size(); i++) {
						System.out.println((i + 1) + ". " + mochilaComun.get(i).getNombre());
					}
					System.out.print("> Elige arma para " + h.getNombre() + " (1-" + mochilaComun.size() + "): ");
					int numeroArma = 0;
					if (sc.hasNextInt()) {
						numeroArma = sc.nextInt();
					}
					sc.nextLine();

					if (numeroArma > 0 && numeroArma <= mochilaComun.size()) {
						Arma armaElegida = mochilaComun.get(numeroArma - 1);
						if (h.getArmasPermitidas().contains(armaElegida.getCategoria())) {
							Arma aVieja = h.getArma();
							mochilaComun.remove(numeroArma - 1);
							h.equiparArma(armaElegida);
							if (aVieja != null) {
								mochilaComun.add(aVieja);
								System.out.println(ANSI_CIAN + "[SISTEMA] El " + aVieja.getNombre()
										+ " se devolvió a la mochila." + ANSI_RESET);
							}
						} else {
							System.out.println(ANSI_ROJO + "[SISTEMA] " + h.getNombre() + " no sabe usar "
									+ armaElegida.getNombre() + ANSI_RESET);
						}
					}
				}
			}
		} while (!salir);
	}

	/**
	 * Menú de opciones para controlar manualmente a cada héroe en su turno. Muestra
	 * comandos para ataque, magia y objetos.
	 * 
	 * @param p        Héroe actual.
	 * @param enemigos Enemigos vivos.
	 * @param aliados  Compañeros de equipo vivos.
	 */
	private static void turnoHeroeManual(Personaje p, Personaje[] enemigos, Personaje[] aliados) {
		boolean turnoCompletado = false;
		do {
			System.out.println(ANSI_CIAN + "\n> Es el turno de " + p.getNombre() + " [" + p.getVidaActual() + "/"
					+ p.getVidaMax() + " HP]. ¿Qué deseas hacer?" + ANSI_RESET);
			System.out.println("1. Atacar");
			System.out.println("2. Habilidades / Magia");
			System.out.println("3. Objetos (Mochila Comun)");
			System.out.println("4. Defenderse (+Defensa temporal)");
			System.out.println("5. Inspeccionar y Estadísticas");
			System.out.print("> Elige opción: ");

			int opt = 0;
			if (sc.hasNextInt())
				opt = sc.nextInt();
			sc.nextLine();

			if (opt == 1) {
				java.util.ArrayList<Personaje> vivosObj = new java.util.ArrayList<>();
				System.out.println("--- Elige Objetivo ---");
				System.out.println("Enemigos:");
				for (int j = 0; j < enemigos.length; j++) {
					if (enemigos[j].estaVivo()) {
						vivosObj.add(enemigos[j]);
						System.out.println(vivosObj.size() + ". [E] " + enemigos[j].getNombre() + " ("
								+ enemigos[j].getVidaActual() + " HP)");
					}
				}

				System.out.print("> Numero (0 para cancelar): ");
				int target = 0;
				if (sc.hasNextInt()) {
					target = sc.nextInt();
				}
				sc.nextLine();
				if (target > 0 && target <= vivosObj.size()) {
					p.atacar(vivosObj.get(target - 1));
					turnoCompletado = true;
				}
			} else if (opt == 2) {
				java.util.ArrayList<habilidad.AccionCombate> habs = p.getHabilidades();
				if (habs.isEmpty()) {
					System.out.println(ANSI_AMARILLO + "[SISTEMA] No tienes habilidades aprendidas." + ANSI_RESET);
				} else {
					System.out.println("--- Tus Habilidades ---");
					for (int j = 0; j < habs.size(); j++) {
						System.out.println((j + 1) + ". " + habs.get(j).getNombre() + " (Cuesta "
								+ habs.get(j).getCosteMana() + " MP / " + habs.get(j).getCosteEnergia() + " SP)");
					}
					System.out.print("> Elige (0 cancelar): ");
					int numeroHabilidad = 0;
					if (sc.hasNextInt()) {
						numeroHabilidad = sc.nextInt();
					}
					sc.nextLine();

					if (numeroHabilidad > 0 && numeroHabilidad <= habs.size()) {
						habilidad.AccionCombate habilidadElegida = habs.get(numeroHabilidad - 1);
						if (p.tieneRecursos(habilidadElegida.getCosteEnergia(), habilidadElegida.getCosteMana())) {
							String nHab = habilidadElegida.getNombre();
							boolean esBeneficiosa = nHab.equals("Purificación") || nHab.equals("Muro de Piedra")
									|| nHab.equals("Luz Sagrada");

							java.util.ArrayList<Personaje> vivosObj = new java.util.ArrayList<>();
							System.out.println("--- Elige Objetivo ---");
							if (esBeneficiosa) {
								System.out.println("Aliados:");
								for (int j = 0; j < aliados.length; j++) {
									if (aliados[j].estaVivo()) {
										vivosObj.add(aliados[j]);
										System.out.println(vivosObj.size() + ". [H] " + aliados[j].getNombre() + " ("
												+ aliados[j].getVidaActual() + " HP)");
									}
								}
							} else {
								System.out.println("Enemigos:");
								for (int j = 0; j < enemigos.length; j++) {
									if (enemigos[j].estaVivo()) {
										vivosObj.add(enemigos[j]);
										System.out.println(vivosObj.size() + ". [E] " + enemigos[j].getNombre() + " ("
												+ enemigos[j].getVidaActual() + " HP)");
									}
								}
							}

							System.out.print("> Numero (0 para cancelar): ");
							int target = 0;
							if (sc.hasNextInt()) {
								target = sc.nextInt();
							}
							sc.nextLine();
							if (target > 0 && target <= vivosObj.size()) {
								habilidadElegida.ejecutar(p, vivosObj.get(target - 1));
								turnoCompletado = true;
							}
						} else {
							System.out.println(ANSI_ROJO + "[SISTEMA] No tienes suficientes recursos." + ANSI_RESET);
						}
					}
				}
			} else if (opt == 3) {
				if (inventarioGrupo.isEmpty()) {
					System.out.println(ANSI_AMARILLO + "[SISTEMA] La mochila de grupo está vacía." + ANSI_RESET);
				} else {
					System.out.println("--- Mochila de Grupo ---");
					for (int j = 0; j < inventarioGrupo.size(); j++) {
						System.out.println((j + 1) + ". " + inventarioGrupo.get(j).getNombre() + " (x"
								+ inventarioGrupo.get(j).getCantidad() + ")");
					}
					System.out.print("> Elige (0 cancelar): ");
					int numeroObjeto = 0;
					if (sc.hasNextInt()) {
						numeroObjeto = sc.nextInt();
					}
					sc.nextLine();

					if (numeroObjeto > 0 && numeroObjeto <= inventarioGrupo.size()) {
						Consumible objetoElegido = inventarioGrupo.get(numeroObjeto - 1);

						java.util.ArrayList<Personaje> vivosObj = new java.util.ArrayList<>();
						System.out.println("--- Elige Objetivo ---");
						System.out.println("Aliados:");
						for (int j = 0; j < aliados.length; j++) {
							if (aliados[j].estaVivo()) {
								vivosObj.add(aliados[j]);
								System.out.println(vivosObj.size() + ". [H] " + aliados[j].getNombre() + " ("
										+ aliados[j].getVidaActual() + " HP)");
							}
						}

						System.out.print("> Numero (0 para cancelar): ");
						int target = 0;
						if (sc.hasNextInt()) {
							target = sc.nextInt();
						}
						sc.nextLine();

						if (target > 0 && target <= vivosObj.size()) {
							objetoElegido.usar(p, vivosObj.get(target - 1));

							// Borrar siempre objeto de la mochila común si cae a cero
							if (objetoElegido.getCantidad() <= 0)
								inventarioGrupo.remove(numeroObjeto - 1);
							turnoCompletado = true;
						}
					}
				}
			} else if (opt == 4) {
				p.defenderse();
				turnoCompletado = true;
			} else if (opt == 5) {
				java.util.ArrayList<Personaje> vivosObj = new java.util.ArrayList<>();
				System.out.println("--- Elige a quién Inspeccionar ---");
				for (int j = 0; j < aliados.length; j++) {
					if (aliados[j].estaVivo()) {
						vivosObj.add(aliados[j]);
						System.out.println(vivosObj.size() + ". [H] " + aliados[j].getNombre());
					}
				}
				for (int j = 0; j < enemigos.length; j++) {
					if (enemigos[j].estaVivo()) {
						vivosObj.add(enemigos[j]);
						System.out.println(vivosObj.size() + ". [E] " + enemigos[j].getNombre());
					}
				}

				System.out.print("> Numero (0 para cancelar): ");
				int target = 0;
				if (sc.hasNextInt()) {
					target = sc.nextInt();
				}
				sc.nextLine();

				if (target > 0 && target <= vivosObj.size()) {
					vivosObj.get(target - 1).mostrarInfo();
				}
			} else {
				System.out.println(ANSI_ROJO + "[SISTEMA] Opción no válida." + ANSI_RESET);
			}
		} while (!turnoCompletado);
	}
}