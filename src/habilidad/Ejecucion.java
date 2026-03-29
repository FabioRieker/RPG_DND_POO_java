package habilidad;

import personajes.Personaje;

// Golpe devastador contra enemigos debilitados
// Daño: 1d12, Coste: 40 PE, Escalado: FUE
// Si el objetivo tiene menos del 20% de vida, hace daño x3
public class Ejecucion extends HabilidadFisica {
	public Ejecucion() {
		super("Ejecución", 40, 1, 12, Estadistica.FUERZA, null);
	}

	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		int daño = tirarDados() + bono;

		// Calcula si es ejecución
		if (objetivo.getVidaMax() <= 0) return;
		int porcentajeVida = (objetivo.getVidaActual() * 100) / objetivo.getVidaMax();

		if (porcentajeVida <= 20) {
			daño *= 3;
			System.out.println("¡EJECUCIÓN! Objetivo con poca vida - DAÑO x3!");
		}

		System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
		objetivo.recibirDaño(daño, false);
	}
}