package motor;

import personajes.*;
import armas.*;
import habilidad.*;
import armaduras.CategoriaArmadura;

public class MainCombate {

	public static void main(String[] args) {

		System.out.println("===========================================");
		System.out.println("    COMBATE: HEROES vs ENEMIGOS + JEFE");
		System.out.println("===========================================\n");

		// CREAR HEROES
		Guerrero heroe1 = new Guerrero("Thorin", Raza.ENANO, 16, 10, 16, 8, 12);
		Mago heroe2 = new Mago("Elara", Raza.ELFO, 6, 14, 8, 18, 10);
		Picaro heroe3 = new Picaro("Vex", Raza.HUMANO, 10, 16, 10, 12, 14);
		Paladin heroe4 = new Paladin("Marcus", Raza.HUMANO, 14, 10, 14, 12, 14);

		// EQUIPAR ARMADURAS A HEROES
		heroe1.equiparArmadura(CategoriaArmadura.PESADA);
		heroe2.equiparArmadura(CategoriaArmadura.LIGERA);
		heroe3.equiparArmadura(CategoriaArmadura.LIGERA);
		heroe4.equiparArmadura(CategoriaArmadura.PESADA);

		// EQUIPAR ARMAS A HEROES
		Armeria armeria = new Armeria();
		heroe1.equiparArma(armeria.get("Hacha"));
		heroe2.equiparArma(armeria.get("Ballesta"));
		heroe3.equiparArma(armeria.get("Daga"));
		heroe4.equiparArma(armeria.get("Espada"));

		// AÑADIR HABILIDADES A HEROES
		heroe1.agregarHabilidad(new GolpeSanguinario());
		heroe1.agregarHabilidad(new Rompecraneos());
		heroe1.agregarHabilidad(new TajoSismico());

		heroe2.agregarHabilidad(new BolaFuego());
		heroe2.agregarHabilidad(new RafagaGlacial());
		heroe2.agregarHabilidad(new Ventisca());

		heroe3.agregarHabilidad(new HojaPonzoñosa());
		heroe3.agregarHabilidad(new AtaqueFurtivo());
		heroe3.agregarHabilidad(new TiroRodilla());

		heroe4.agregarHabilidad(new LuzSagrada());
		heroe4.agregarHabilidad(new Purificacion());
		heroe4.agregarHabilidad(new GritoGuerra());

		// CREAR ENEMIGOS
		Monstruo orco = new Monstruo("Grumsh", Raza.ORCO, 14, 8, 14, 6, 10);
		Monstruo goblin = new Monstruo("Snitch", Raza.GOBLIN, 8, 14, 8, 6, 8);
		Monstruo bestia = new Monstruo("Garra", Raza.BESTIA, 12, 12, 12, 4, 10);

		// EQUIPAR ARMAS A MONSTRUOS
		orco.equiparArma(armeria.get("Maza")); // Cambiado a Armeria para mayor consistencia
		goblin.equiparArma(armeria.get("Arco"));
		bestia.equiparArma(new ArmaCuerpoACuerpo("Garras", 1, 6));

		// CREAR JEFE
		JefeDragon dragon = new JefeDragon("Valdrax el Devorador", 18, 10, 20, 12, 14);
		dragon.equiparArma(new ArmaCuerpoACuerpo("Colmillos de Dragón", 2, 8));

		// LISTAS DE COMBATE
		Personaje[] heroes = { heroe1, heroe2, heroe3, heroe4 };
		Personaje[] enemigos = { orco, goblin, bestia, dragon };

		// MOSTRAR INFO
		System.out.println("=== HEROES ===");
		for (Personaje h : heroes) {
			h.mostrarInfo();
		}

		System.out.println("\n=== ENEMIGOS ===");
		for (Personaje e : enemigos) {
			e.mostrarInfo();
		}

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
					Personaje objetivo = obtenerObjetivoAleatorio(enemigos);
					heroe.atacar(objetivo);
				}
			}

			// Turno de enemigos
			System.out.println("\n--- Turno de Enemigos ---");
			for (int i = 0; i < enemigos.length; i++) {
				Personaje enemigo = enemigos[i];
				if (enemigo.estaVivo() && hayVivos(heroes)) {
					Personaje objetivo = obtenerObjetivoAleatorio(heroes);

					// El ataque normal ya comprueba estados de incapacidad internamente
					enemigo.atacar(objetivo);

					// Si es jefe, usar habilidad especial (Corregido con objetivo y chequeo de
					// estado)
					if (enemigo instanceof Jefe && Math.random() < 0.3) {
						// Comprobamos si el Jefe puede actuar (Problema #1)
						if (!enemigo.tieneEstado("Aturdimiento") && !enemigo.tieneEstado("Congelado")) {
							((Jefe) enemigo).habilidadEspecial(objetivo);
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

	static boolean hayVivos(Personaje[] grupo) {
		for (Personaje p : grupo) {
			if (p.estaVivo()) {
				return true;
			}
		}
		return false;
	}

	static Personaje obtenerObjetivoAleatorio(Personaje[] grupo) {
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