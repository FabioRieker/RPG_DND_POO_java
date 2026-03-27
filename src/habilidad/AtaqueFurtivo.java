package habilidad;

import personajes.Personaje;

// Ataque rápido de pícaro
// Daño: 1d6, Coste: 20 SP, Escalado: DES
public class AtaqueFurtivo extends HabilidadFisica {
	public AtaqueFurtivo() {
		super("Ataque Furtivo", 20, 1, 6, Estadistica.DESTREZA, null);
	}

	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		int daño = tirarDados() + bono;

		// Bonus: daño doble si el objetivo está aturdido
		if (objetivo.tieneEstado("Aturdimiento")) {
			daño *= 2;
			System.out.println("¡GOLPE CRÍTICO! Objetivo aturdido - DAÑO x2!");
		}

		System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
		objetivo.recibirDaño(daño, false);
	}
}