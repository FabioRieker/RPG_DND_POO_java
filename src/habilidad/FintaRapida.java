package habilidad;

// Ataque rápido de distracción
// Daño: 1d4, Coste: 10 PE, Escalado: DES
// Daño bajo pero puede mejorar la evasión
public class FintaRapida extends HabilidadFisica {
	public FintaRapida() {
		super("Finta Rápida", 10, 1, 4, Estadistica.DESTREZA, null);
	}
}