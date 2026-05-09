package habilidad;

import personajes.Personaje;

/**
 * Ataque letal que inflige más daño si el enemigo tiene poca vida.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Ejecucion extends HabilidadFisica {
	/**
	 * Construye los costes y características de la Ejecución.
	 */
	public Ejecucion() {
		super("Ejecución", 40, 1, 12, Estadistica.FUERZA, null);
	}

	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		int daño = tirarDados() + bono;

		// Comprueba si se cumplen los requisitos del umbral de vida para ejecutar
		if (objetivo.getVidaMax() <= 0)
			return;
		int porcentajeVida = (objetivo.getVidaActual() * 100) / objetivo.getVidaMax();

		if (porcentajeVida <= 20) {
			daño *= 3;
			System.out.println("¡EJECUCIÓN! Objetivo con poca vida - DAÑO x3!");
		}

		System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
		objetivo.recibirDaño(daño, false);
	}
}