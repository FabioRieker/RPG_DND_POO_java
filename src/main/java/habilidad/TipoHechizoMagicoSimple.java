package habilidad;

/**
 * Enumerado que almacena los datos de los hechizos magicos simples.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public enum TipoHechizoMagicoSimple {
	RAFAGA_GLACIAL("Ráfaga Glacial", 20, 2, 6, "CONGELA");

	private final String nombre;
	private final int costeMana;
	private final int cantidadDados;
	private final int carasDado;
	private final String estadoAsociado;

	TipoHechizoMagicoSimple(String nombre, int costeMana, int cantidadDados, int carasDado, String estadoAsociado) {
		this.nombre = nombre;
		this.costeMana = costeMana;
		this.cantidadDados = cantidadDados;
		this.carasDado = carasDado;
		this.estadoAsociado = estadoAsociado;
	}

	public String getNombre() { return nombre; }
	public int getCosteMana() { return costeMana; }
	public int getCantidadDados() { return cantidadDados; }
	public int getCarasDado() { return carasDado; }
	public String getEstadoAsociado() { return estadoAsociado; }
}
