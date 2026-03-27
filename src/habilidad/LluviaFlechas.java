package habilidad;

// Lluvia de flechas contra múltiples enemigos
// Daño: 1d8, Coste: 40 PE, Escalado: DES
// Golpea a todos los enemigos
public class LluviaFlechas extends HabilidadFisica {
	public LluviaFlechas() {
		super("Lluvia de Flechas", 40, 1, 8, Estadistica.DESTREZA, null);
	}
}