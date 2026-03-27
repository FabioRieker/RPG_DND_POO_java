package motor;

import personajes.*;
import java.util.List;

public class Main {

	public static void main(String[] args) {

		System.out.println("===========================================");
		System.out.println("    COMBATE: HEROES vs ENEMIGOS + JEFE");
		System.out.println("===========================================\n");

		// LLAMAR HEROES
		List<Personaje> listaHeroes = FabricaHeroes.crearEquipoInicial();
		Personaje[] heroes = listaHeroes.toArray(new Personaje[0]);

		int[] ordenSalas = {7, 6}; // TODO: añadir el resto de salas y un random que determine el orden

		for (int numSala : ordenSalas) {
			if (!MotorCombate.hayVivos(heroes)) {
				break;
			}

			// OBTENER SALAS DE LA FABRICA
			System.out.println("Entrando en la Sala " + numSala + "...");
			Sala salaActual = FabricaSalas.generarSala(numSala);
			List<Personaje> listaEnemigos = salaActual.getEnemigos();

			Personaje[] enemigos = listaEnemigos.toArray(new Personaje[0]);

			// MOSTRAR INFO
			System.out.println("=== HEROES ===");
			for (Personaje h : heroes) {
				h.mostrarInfo();
			}

			System.out.println("\n=== ENEMIGOS ===");
			for (Personaje e : enemigos) {
				e.mostrarInfo();
			}

			// EJECUTAR COMBATE
			MotorCombate.iniciarCombate(heroes, enemigos);
		}
	}
}