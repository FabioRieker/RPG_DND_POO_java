package motor;

import personajes.*;
import consumibles.*;

public class MotorCombate {

	public static void iniciarCombate(Personaje[] heroes, Personaje[] enemigos) {
		// COMBATE POR TURNOS
		System.out.println("\n===========================================");
		System.out.println("            ¡COMBATE COMIENZA!");
		System.out.println("===========================================\n");

		int turno = 1;
		while (hayVivos(heroes) && hayVivos(enemigos) && turno <= 10) {
			System.out.println("======= TURNO " + turno + " =======");

			// Proceso de estados de heroes
			for (Personaje h : heroes) {
				if (h.estaVivo()) {
					h.pasarTurnoDeEstados();
				}
			}

			// Proceso de estados de enemigos
			for (Personaje e : enemigos) {
				if (e.estaVivo()) {
					e.pasarTurnoDeEstados();
				}
			}

			// Turno de heroes
			System.out.println("\n--- Turno de Heroes ---");
			for (int i = 0; i < heroes.length; i++) {
				Personaje heroe = heroes[i];
				if (heroe.estaVivo() && hayVivos(enemigos)) {

					//introduzco una norma para que los héroes se curen cuando la vida es < a 30
					boolean haUsadoItem = false;
					if (heroe.getVidaActual() < (heroe.getVidaMax() * 0.3)) {
						for (Consumible c : heroe.getInventario()) {
							if (c.getNombre().equals("Poción de Curación") && c.getCantidad() > 0) {
							    c.usar(heroe, heroe); // 
							    haUsadoItem = true;
							    break;
							}
						}
					}

					if (!haUsadoItem) {
						Personaje objetivo = obtenerObjetivoAleatorio(enemigos);
						heroe.atacar(objetivo);
					}
				}
			}

			// Turno de enemigos
			System.out.println("\n--- Turno de Enemigos ---");
			for (int i = 0; i < enemigos.length; i++) {
				Personaje enemigo = enemigos[i];
				if (enemigo.estaVivo() && hayVivos(heroes)) {

					boolean haUsadoItem = false;
					if (enemigo.getVidaActual() < (enemigo.getVidaMax() * 0.3)) {
						for (Consumible c : enemigo.getInventario()) {
							if (c instanceof PocionCuracion && c.getCantidad() > 0) {
								c.usar(enemigo, enemigo);
								haUsadoItem = true;
								break;
							}
						}
					}

					if (!haUsadoItem) {
						Personaje objetivo = obtenerObjetivoAleatorio(heroes);

						// el ataque normal ya comprueba estados de incapacidad internamente
						enemigo.atacar(objetivo);

						// si es jefe, usar habilidad especial (Corregido con objetivo y chequeo de
						// estado)
						if (enemigo.getTipoClase() == TipoClase.JEFE && Math.random() < 0.3) {
						    if (!enemigo.tieneEstado("Aturdimiento") && !enemigo.tieneEstado("Congelado")) {
						        ((Jefe) enemigo).habilidadEspecial(objetivo);
						    }
						}
					}
				}
			}

			// Mostrar estado
			System.out.println("\n--- Estado tras turno " + turno + " ---");
			System.out.println("Heroes:");
			for (Personaje h : heroes) {
				h.mostrarInfoBreve();
			}
			System.out.println("Enemigos:");
			for (Personaje e : enemigos) {
				e.mostrarInfoBreve();
			}

			System.out.println("");
			turno++;
		}

		// RESULTADO
		System.out.println("===========================================");
		System.out.println("            RESULTADO");
		System.out.println("===========================================");

		if (hayVivos(heroes)) {
			System.out.println("¡VICTORIA! Los heroes han ganado.");
		} else if (hayVivos(enemigos)) {
			System.out.println("¡DERROTA! Los enemigos han ganado.");
		} else {
			System.out.println("¡EMPATE!");
		}

		System.out.println("\n--- Estado Final ---");
		for (Personaje h : heroes) {
			h.mostrarInfo();
		}
		for (Personaje e : enemigos) {
			e.mostrarInfo();
		}

		System.out.println("\n===========================================");
		System.out.println("         COMBATE FINALIZADO");
		System.out.println("===========================================\n");
	}

	public static boolean hayVivos(Personaje[] grupo) {
		for (Personaje p : grupo) {
			if (p.estaVivo()) {
				return true;
			}
		}
		return false;
	}

	public static Personaje obtenerObjetivoAleatorio(Personaje[] grupo) {
		Personaje[] vivos = new Personaje[grupo.length];
		int count = 0;
		for (Personaje p : grupo) {
			if (p.estaVivo()) {
				vivos[count++] = p;
			}
		}
		if (count == 0)
			return null;
		return vivos[(int) (Math.random() * count)];
	}
}