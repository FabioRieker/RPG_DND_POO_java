package habilidad;

import personajes.Personaje;

/**
 * Habilidad que representa un golpe sorpresa buscando el punto ciego.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class AtaqueFurtivo extends HabilidadFisica {
	/**
	 * Construye los costes y características del Ataque Furtivo.
	 */
	public AtaqueFurtivo() {
		super("Ataque Furtivo", 20, 1, 6, Estadistica.DESTREZA, null);
	}

	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		int daño = tirarDados() + bono;

		// Bonificación táctica: causa el doble de daño a objetivos aturdidos
		if (objetivo.tieneEstado("Aturdimiento")) {
			daño *= 2;
			System.out.println("¡GOLPE CRÍTICO! Objetivo aturdido - DAÑO x2!");
		}

		System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
		objetivo.recibirDaño(daño, false);
	}
}