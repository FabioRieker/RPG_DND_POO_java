package habilidad;

/**
 * Habilidad física que busca incapacitar o lisiar al oponente.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class TiroRodilla extends HabilidadFisica {
	/**
	 * Construye los costes y características del Tiro a la Rodilla.
	 */
	public TiroRodilla() {
		super("Tiro a la Rodilla", 20, 1, 6, Estadistica.DESTREZA, "LISIADO");
	}
}