package habilidad;

import personajes.Personaje;

// Explosión de energía arcana
// Daño: 3d8, Coste: 15 PE + 15 PM
public class ExplosionArcana extends HabilidadHibrida {

	public ExplosionArcana() {
		super("Explosión Arcana", 15, 15, 3, 8, Estadistica.INTELIGENCIA, Efecto.NINGUNO);
	}
}