package habilidad;

/**
 * Técnica física que impregna un arma con toxinas peligrosas.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class HojaPonzoñosa extends HabilidadFisica {
	/**
	 * Construye los costes y características del tajo ponzoñoso.
	 */
	public HojaPonzoñosa() {
		super("Hoja Ponzoñosa", 15, 1, 6, Estadistica.DESTREZA, "VENENO");
	}
}