package habilidad;

// Golpe en la cabeza
// Daño: 1d10, Coste: 35 PE, Escalado: FUE
// Puede aturdir al objetivo
public class Rompecraneos extends HabilidadFisica {
	public Rompecraneos() {
		super("Rompecráneos", 35, 1, 10, Estadistica.FUERZA, "ATURDIR");
	}
}