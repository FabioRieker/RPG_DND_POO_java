package habilidad;

/**
 * Magia siniestra que drena salud del rival y cura al usuario.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class ToqueVampirico extends HabilidadHibrida {

	/**
	 * Construye los costes y características del Toque Vampírico.
	 */
	public ToqueVampirico() {
		super("Toque Vampírico", 10, 15, 1, 4, Estadistica.INTELIGENCIA, Efecto.ROBO_VIDA);
	}
}