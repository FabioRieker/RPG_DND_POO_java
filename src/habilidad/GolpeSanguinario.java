package habilidad;

// Ataque que cura al usuario
// Daño: 1d8, Coste: 15 PE + 10 PM, Escalado: FUE
// Cura al usuario según el daño causado
public class GolpeSanguinario extends HabilidadHibrida {

	public GolpeSanguinario() {
		super("Golpe Sanguinario", 15, 10, 1, 8, Estadistica.FUERZA, Efecto.CURAR_VIDA);
	}
}