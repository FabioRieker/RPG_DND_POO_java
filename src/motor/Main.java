package motor;

import personajes.*;
import java.util.List;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {

		System.out.println("===========================================");
		System.out.println("    AVENTURA: EL DESCENSO A LAS RUINAS");
		System.out.println("===========================================\n");

		// llamar a heroes
		List<Personaje> listaHeroes = FabricaHeroes.crearEquipoInicial();
		List<Personaje> reserva = new ArrayList<>();
		Personaje[] heroes = listaHeroes.toArray(new Personaje[0]);

		// bucle de 1 a 20 salas
		for (int i = 1; i <= 20; i++) {
			if (!MotorCombate.hayVivos(heroes)) {
				System.out.println("EL EQUIPO HA CAÍDO EN LA SALA " + i + ". FIN DE LA AVENTURA.");
				break;
			}

			System.out.println("\n>>> ENTRANDO EN LA SALA " + i + "...");

			// salas no de combate
			if (i == 2) {
				System.out.println("Encontráis suministros en una caravana saqueada.");
				for (Personaje h : heroes) {
					if (h.estaVivo())
						h.recuperarRecursos(30);
				}
			} else if (i == 5) {
				System.out.println("¡Rescatáis a Kallista! Se une a vuestra reserva.");
				reserva.add(FabricaHeroes.crearKallista());
			} else if (i == 7) {
				System.out.println("¡BOOM! Una trampa de fuego estalla.");
				for (Personaje h : heroes) {
					if (h.estaVivo())
						h.recibirDaño(12, true);
				}
			} else if (i == 9) {
				System.out.println("Llegáis a una fuente curativa. El grupo descansa.");
				for (Personaje h : heroes) {
					if (h.estaVivo())
						h.curar(50);
				}
			} else if (i == 12) {
				System.out.println("Un monje llamado Kwai Chang se une a vuestra reserva.");
				reserva.add(FabricaHeroes.crearMonjeKwai());
			} else if (i == 14) {
				System.out.println("Sala vacía... un silencio sepulcral inunda el lugar.");
			} else if (i == 17) {
				System.out.println("Lulu Nightingale, la barda, se une a vuestra reserva.");
				reserva.add(FabricaHeroes.crearBardoLulu());
			} else if (i == 19) {
				System.out.println("Último descanso antes del gran final. Salud y recursos al máximo.");
				for (Personaje h : heroes) {
					if (h.estaVivo()) {
						h.curar(100);
						h.recuperarRecursos(100);
					}
				}
			} else {
				// salas de combate
				Sala salaActual = FabricaSalas.generarSala(i);
				List<Personaje> listaEnemigos = salaActual.getEnemigos();
				Personaje[] enemigos = listaEnemigos.toArray(new Personaje[0]);

				// ejecuto combate
				MotorCombate.iniciarCombate(heroes, enemigos);
			}

			for (int j = 0; j < heroes.length; j++) {
				if (!heroes[j].estaVivo() && !reserva.isEmpty()) {
					Personaje caido = heroes[j];
					for (int r = 0; r < reserva.size(); r++) {
						if (reserva.get(r).estaVivo()) {
							Personaje sustituto = reserva.remove(r);
							System.out.println("\n[SISTEMA] " + caido.getNombre() + " ha muerto en acto de servicio.");
							System.out.println("[RESERVA] " + sustituto.getNombre()
									+ " entra al equipo principal para ocupar su lugar.");
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

			if (MotorCombate.hayVivos(heroes)) {
				System.out.println("\n===========================================");
				System.out.println("    ¡AVENTURA COMPLETADA CON ÉXITO!");
				System.out.println("===========================================");
			}
		}

	}
}