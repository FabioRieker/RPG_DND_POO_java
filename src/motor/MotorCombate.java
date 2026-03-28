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
			for (int i = 0; i < heroes.length; i++) {
				Personaje h = heroes[i];
				if (h.estaVivo()) {
					h.pasarTurnoDeEstados();
				}
			}

			// Proceso de estados de enemigos
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
			
			System.out.println("   [DESTREZA] El orden de batalla segun la agilidad es: " + listaOrdenada);

			for (int i = 0; i < todos.size(); i++) {
				Personaje p = todos.get(i);
				
				if (p.estaVivo() == false || hayVivos(heroes) == false || hayVivos(enemigos) == false) {
					continue;
				}

				// identificamos si es héroe para aplicar su lógica
				boolean esHeroe = false;
				for (int j = 0; j < heroes.length; j++) {
					if (heroes[j] == p) {
						esHeroe = true;
					}
				}

				if (esHeroe == true) {
					// Turno de Heroes
					boolean haUsadoItem = false;
					if (p.getVidaActual() < (p.getVidaMax() * 0.3)) {
						for (int j = 0; j < p.getInventario().size(); j++) {
							Consumible c = p.getInventario().get(j);
							if (c.getNombre().equals("Poción de Curación") && c.getCantidad() > 0) {
								c.usar(p, p);
								haUsadoItem = true;
								break;
							}
						}
					}

					if (haUsadoItem == false) {
						Personaje objetivo = obtenerObjetivoAleatorio(enemigos);
						p.atacar(objetivo);
					}
				} else {
					// Turno de Enemigos
					boolean haUsadoItem = false;
					if (p.getVidaActual() < (p.getVidaMax() * 0.3)) {
						for (int j = 0; j < p.getInventario().size(); j++) {
							Consumible c = p.getInventario().get(j);
							if (c.getNombre().equals("Poción de Curación") && c.getCantidad() > 0) {
								c.usar(p, p);
								haUsadoItem = true;
								break;
							}
						}
					}

					if (haUsadoItem == false) {
						Personaje objetivo = obtenerObjetivoAleatorio(heroes);

						// el ataque normal ya comprueba estados de incapacidad internamente
						p.atacar(objetivo);

						// si es jefe, usar habilidad especial
						if (p.getTipoClase() == TipoClase.JEFE && Math.random() < 0.3) {
							if (p.tieneEstado("Aturdimiento") == false && p.tieneEstado("Congelado") == false && p.tieneEstado("Lisiado") == false) {
								((Jefe) p).habilidadEspecial(objetivo);
							}
						}
					}
				}
				
				// Pausa para que el combate sea legible
				try { Thread.sleep(800); } catch (InterruptedException ex) {}
			}

			// Mostrar estado
			System.out.println("\n--- Estado tras turno " + turno + " ---");
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
		System.out.println("===========================================");
		System.out.println("            RESULTADO");
		System.out.println("===========================================");

		if (hayVivos(heroes) == true) {
			System.out.println("¡VICTORIA! Los heroes han ganado.");
		} else if (hayVivos(enemigos) == true) {
			System.out.println("¡DERROTA! Los enemigos han ganado.");
		} else {
			System.out.println("¡EMPATE!");
		}

		System.out.println("\n--- Estado Final ---");
		for (int i = 0; i < heroes.length; i++) {
			heroes[i].mostrarInfo();
		}
		for (int i = 0; i < enemigos.length; i++) {
			enemigos[i].mostrarInfo();
		}

		System.out.println("\n===========================================");
		System.out.println("         COMBATE FINALIZADO");
		System.out.println("===========================================\n");
	}

	public static boolean hayVivos(Personaje[] grupo) {
		for (int i = 0; i < grupo.length; i++) {
			if (grupo[i].estaVivo() == true) {
				return true;
			}
		}
		return false;
	}

	public static Personaje obtenerObjetivoAleatorio(Personaje[] grupo) {
		Personaje[] vivos = new Personaje[grupo.length];
		int count = 0;
		for (int i = 0; i < grupo.length; i++) {
			if (grupo[i].estaVivo() == true) {
				vivos[count] = grupo[i];
				count++;
			}
		}
		if (count == 0) {
			return null;
		}
		int indiceAleatorio = (int) (Math.random() * count);
		return vivos[indiceAleatorio];
	}
}