package habilidad;

// Ráfaga de hielo
// Daño: 2d6, Coste: 20 PM
// Aplica Congelar al objetivo
public class RafagaGlacial extends HechizoMagico {

	public RafagaGlacial() {
		super("Ráfaga Glacial", 20, 2, 6, "CONGELA");
	}
}