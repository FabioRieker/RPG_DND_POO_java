package habilidad;

import personajes.Personaje;
import estado.EstadoQuemadura;

// Lanza una bola de fuego
// Daño: 3d6, Coste: 25 PM
// Aplica Quemadura y sinergiza con Veneno
public class BolaFuego extends HechizoMagico {

	public BolaFuego() {
		super("Bola de Fuego", 25, 3, 6, "QUEMA");
	}

	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		int daño = tirarDados() + bono;

		// Sinergia fuego + veneno
		if (objetivo.tieneEstado("Veneno")) {
			daño *= 2;
			System.out.println("¡SINERGIA! Fuego contra Veneno - DAÑO x2!");
		}

		System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
		objetivo.recibirDaño(daño, false);

		// Aplica quemadura
		if (!objetivo.tieneEstado("Quemadura")) {
			objetivo.aplicarEstado(new EstadoQuemadura(3, 5));
			System.out.println("-- ¡" + objetivo.getNombre() + " está en llamas!");
		}
	}
}