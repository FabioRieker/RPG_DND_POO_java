package habilidad;

/**
 * Habilidad contundente que busca aturdir al objetivo de un golpe.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Rompecraneos extends HabilidadFisica {
	/**
	 * Construye los costes y características del ataque Rompecráneos.
	 */
	public Rompecraneos() {
		super("Rompecráneos", 35, 1, 10, Estadistica.FUERZA, "ATURDIR");
	}
}