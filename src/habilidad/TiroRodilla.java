package habilidad;

// Tiro en las rodillas
// Daño: 1d6, Coste: 20 PE, Escalado: DES
// Ledia al objetivo
public class TiroRodilla extends HabilidadFisica {
	public TiroRodilla() {
		super("Tiro a la Rodilla", 20, 1, 6, Estadistica.DESTREZA, "LISIADO");
	}
}