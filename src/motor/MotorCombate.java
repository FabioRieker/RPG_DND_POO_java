package motor;

import personajes.*;
import consumibles.*;
import armas.Arma;
import armas.Armeria;
import java.util.ArrayList;

public class MotorCombate {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_ROJO = "\u001B[31m";
	public static final String ANSI_VERDE_OSCURO = "\u001B[32m"; // Verde estándar (no fosforito)
	public static final String ANSI_AMARILLO = "\u001B[33m";
	public static final String ANSI_AZUL = "\u001B[34m"; 
	public static final String ANSI_AZUL_MARINO = "\u001B[38;5;18m"; // Azul marino 256
	public static final String ANSI_MORADO = "\u001B[35m";
	public static final String ANSI_ROSA = "\u001B[38;5;206m";
	public static final String ANSI_CIAN = "\u001B[36m";

	// variable para rastrear quién fue el último en golpear a un jefe
	private static Personaje ultimoAtacanteJefe = null;

	// mochula común a todo el equipo
	public static ArrayList<Arma> mochilaComun = new ArrayList<>();

	public static void iniciarCombate(Personaje[] heroes, Personaje[] enemigos) {
		ultimoAtacanteJefe = null;

		// COMBATE POR TURNOS
		System.out.println("\n" + ANSI_AZUL_MARINO + "===========================================");
		System.out.println("          [SISTEMA] ¡COMBATE COMIENZA!");
		System.out.println("===========================================" + ANSI_RESET);

		int turno = 1;
		while (hayVivos(heroes) && hayVivos(enemigos) && turno <= 10) {
			System.out.println("\n" + ANSI_CIAN + "======= [TURNO " + turno + "] =======" + ANSI_RESET);

			// proceso de estados de heroes
			for (int i = 0; i < heroes.length; i++) {
				Personaje h = heroes[i];
				if (h.estaVivo()) {
					h.pasarTurnoDeEstados();
				}
			}

			// proceso de estados de enemigos
			for (int i = 0; i < enemigos.length; i++) {
				Personaje e = enemigos[i];
				if (e.estaVivo()) {
					e.pasarTurnoDeEstados();
				}
			}

			// creo una lista con todos los combatientes para calcular la iniciativa
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

			// ordeno por destreza total de mayor a menor
			todos.sort((p1, p2) -> Integer.compare(p2.getDestrezaTotal(), p1.getDestrezaTotal()));

			// genero la lista de orden de ataque para mostrar por pantalla
			String listaOrdenada = "";
			for (int i = 0; i < todos.size(); i++) {
				Personaje pActual = todos.get(i);

				// compruebo si es heroe o enemigo para poner una marca indicativa
				String marca = " [E]";
				for (int j = 0; j < heroes.length; j++) {
					if (heroes[j] == pActual) {
						marca = " [H]";
					}
				}

				listaOrdenada = listaOrdenada + pActual.getNombre() + marca;

				// añado coma si no es el último de la lista
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
					System.out.println(ANSI_AMARILLO + "\n[ESTADO] " + p.getNombre() + " no puede actuar este turno debido a su estado." + ANSI_RESET);
					continue;
				}

				// identifio si es héroe para aplicar su lógica
				boolean esHeroe = false;
				for (int j = 0; j < heroes.length; j++) {
					if (heroes[j] == p) {
						esHeroe = true;
					}
				}

				if (esHeroe == true) {
					turnoHeroe(p, enemigos);
				} else {
					turnoEnemigo(p, heroes);
				}

				// prueba del sleep, se implementará mejor después
				try {
					Thread.sleep(800);
				} catch (InterruptedException ex) {
				}
			}

			// mostrar estado
			System.out.println("\n" + ANSI_ROSA + "--- [SISTEMA] Estado tras turno " + turno + " ---" + ANSI_RESET);
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

		// RESULTADO
		System.out.println("\n" + ANSI_AZUL_MARINO + "===========================================");
		System.out.println("            [RESULTADO]");
		System.out.println("===========================================" + ANSI_RESET);

		if (hayVivos(heroes) == true) {
			System.out.println(ANSI_VERDE_OSCURO + "[SISTEMA] ¡VICTORIA! Los heroes han ganado." + ANSI_RESET);
			// producir botín automático de los enemigos derrotados
			for (Personaje e : enemigos) {
				procesarAutoLoot(heroes, e);
			}
		} else if (hayVivos(enemigos) == true) {
			System.out.println(ANSI_ROJO + "¡DERROTA! Los enemigos han ganado." + ANSI_RESET);
		} else {
			System.out.println(ANSI_AMARILLO + "¡EMPATE!" + ANSI_RESET);
		}

		System.out.println("\n--- Estado Final ---");
		for (int i = 0; i < heroes.length; i++) {
			heroes[i].mostrarInfo();
		}
		for (int i = 0; i < enemigos.length; i++) {
			enemigos[i].mostrarInfo();
		}

		System.out.println("\n" + ANSI_AZUL_MARINO + "===========================================");
		System.out.println("         [SISTEMA] COMBATE FINALIZADO");
		System.out.println("===========================================" + ANSI_RESET + "\n");
	}

	private static void turnoHeroe(Personaje p, Personaje[] enemigos) {
		// Turno de Heroes
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

			// si el objetivo es un jefe, guarda quién le ataca para su venganza
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
							
							// Evitar lanzar curas o bufos a los monstruos
							if (nHab.equals("Purificación") || nHab.equals("Muro de Piedra")) {
								objHab = p;
							} 
							// El bot no está preparado para castear áreas complejas
							else if (nHab.equals("Nube Tóxica") || nHab.equals("Ventisca") || nHab.equals("Lluvia de Flechas") || nHab.equals("Rayo Encadenado") || nHab.equals("Grito de Guerra")) {
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

	private static void turnoEnemigo(Personaje p, Personaje[] heroes) {
		// Turno de Enemigos
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
				// el ataque normal ya comprueba estados de incapacidad internamente
				// si es jefe, usar habilidad especial
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
								} else if (nHab.equals("Nube Tóxica") || nHab.equals("Ventisca") || nHab.equals("Lluvia de Flechas") || nHab.equals("Rayo Encadenado") || nHab.equals("Grito de Guerra")) {
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

	private static void procesarAutoLoot(Personaje[] heroes, Personaje enemigo) {
		double chance = Math.random();
		double limite = (enemigo.getTipoClase() == TipoClase.JEFE) ? 1.0 : 0.70;

		if (chance <= limite && enemigo.getArma() != null) {
			Arma loot = enemigo.getArma();

			if (enemigo instanceof JefeDragon)
				loot = new Armeria().get("Hoja Fénix");
			if (enemigo instanceof JefeLich)
				loot = new Armeria().get("Colmillo de Araña");

			if (loot != null) {
				System.out.println(ANSI_AMARILLO + "\n[BOTÍN] " + enemigo.getNombre() + " ha soltado: " + loot.getNombre() + ANSI_RESET);

				boolean equipado = false;
				for (Personaje h : heroes) {
					// solo equipa automáticamente si no tiene arma equipada
					if (h.estaVivo() && h.getArma() == null && h.getArmasPermitidas().contains(loot.getCategoria())) {
						h.equiparArma(loot);
						equipado = true;
						break;
					}
				}

				if (!equipado) {
					mochilaComun.add(loot);
					System.out.println(ANSI_CIAN + "[SISTEMA] " + loot.getNombre() + " se ha guardado en la mochila común." + ANSI_RESET);
				}
			}
		}
	}

	public static boolean hayVivos(Personaje[] grupo) {
		for (int i = 0; i < grupo.length; i++) {
			if (grupo[i].estaVivo() == true) {
				return true;
			}
		}
		return false;
	}

	public static Personaje seleccionarObjetivoInteligente(Personaje atacante, Personaje[] posiblesObjetivos) {
		// primero ver quienes están vivos para no atacar cadáveres
		java.util.ArrayList<Personaje> vivos = new java.util.ArrayList<Personaje>();
		for (int i = 0; i < posiblesObjetivos.length; i++) {
			if (posiblesObjetivos[i].estaVivo()) {
				vivos.add(posiblesObjetivos[i]);
			}
		}

		if (vivos.isEmpty())
			return null;

		// provocación del Muro de Piedra
		for (Personaje vivo : vivos) {
			if (vivo.isMuroActivo()) {
				System.out.println(ANSI_AMARILLO + "\n[IA] " + atacante.getNombre() + " se ve forzado a atacar a " + vivo.getNombre()
						+ " por el muro de piedra!" + ANSI_RESET);
				return vivo;
			}
		}

		// sistema especial de venganza en jefes (70/30)
		if (atacante.getTipoClase() == TipoClase.JEFE && ultimoAtacanteJefe != null && ultimoAtacanteJefe.estaVivo()) {
			if (Math.random() < 0.7) {
				System.out.println(ANSI_ROJO + "\n[IA ATAQUE] ¡" + atacante.getNombre() + " ruge de furia contra "
						+ ultimoAtacanteJefe.getNombre() + " por el último golpe!" + ANSI_RESET);
				return ultimoAtacanteJefe;
			}
		}

		// probabilidades generales (25/25/50)
		double azar = Math.random() * 100;

		// 25% ataque al tanque (Más defensa)
		if (azar <= 25) {
			Personaje tanque = vivos.get(0);
			for (int i = 1; i < vivos.size(); i++) {
				if (vivos.get(i).getDefensaTotal() > tanque.getDefensaTotal()) {
					tanque = vivos.get(i);
				}
			}
			System.out.println(ANSI_ROJO + "\n[IA ATAQUE] ¡" + atacante.getNombre() + " se choca contra el escudo de " + tanque.getNombre() + "!" + ANSI_RESET);
			return tanque;
		}
		// 25% ataque normal (aleatorio)
		else if (azar <= 50) {
			System.out.println(ANSI_ROJO + "\n[IA ATAQUE] ¡" + atacante.getNombre() + " lanza un ataque frenético al azar!" + ANSI_RESET);
			int index = (int) (Math.random() * vivos.size());
			return vivos.get(index);
		}
		// 50% ataque al más débil (menos vida actual)
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

	// mantengo esta clase de momento y la arreglo para que no genere excepciones en
	// caso de usarla
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
}