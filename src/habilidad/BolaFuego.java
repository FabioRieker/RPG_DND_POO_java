package habilidad;

import personajes.Personaje;
import estado.EstadoQuemadura;

/**
 * Hechizo ofensivo de fuego que inflige daño e induce quemaduras.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class BolaFuego extends HechizoMagico {

	/**
	 * Construye los costes y características de Bola de Fuego.
	 */
	public BolaFuego() {
		super("Bola de Fuego", 25, 3, 6, "QUEMA");
	}

	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		int daño = tirarDados() + bono;

		// Sinergia entre Daño de Fuego y Veneno
		if (objetivo.tieneEstado("Veneno")) {
			daño *= 2;
			System.out.println("¡SINERGIA! Fuego contra Veneno - DAÑO x2!");
		}

		System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
		objetivo.recibirDaño(daño, false);

		// Aplica el estado alterado de Quemadura
		if (!objetivo.tieneEstado("Quemadura")) {
			objetivo.aplicarEstado(new EstadoQuemadura(3, 5));
			System.out.println("-- ¡" + objetivo.getNombre() + " está en llamas!");
		}
	}
}