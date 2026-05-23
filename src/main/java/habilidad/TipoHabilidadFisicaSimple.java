package habilidad;

/**
 * Enumerado que almacena los datos de las habilidades fisicas simples
 * (aquellas que no requieren logica adicional o sobrescribir metodos).
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public enum TipoHabilidadFisicaSimple {
	FINTA_RAPIDA("Finta Rápida", 10, 1, 4, AccionCombate.Estadistica.DESTREZA, null),
	HOJA_PONZONOSA("Hoja Ponzoñosa", 15, 1, 6, AccionCombate.Estadistica.DESTREZA, "VENENO"),
	LLUVIA_FLECHAS("Lluvia de Flechas", 40, 1, 8, AccionCombate.Estadistica.DESTREZA, null),
	ROMPECRANEOS("Rompecráneos", 35, 1, 10, AccionCombate.Estadistica.FUERZA, "ATURDIR"),
	TAJO_SISMICO("Tajo Sísmico", 30, 2, 8, AccionCombate.Estadistica.FUERZA, "SANGRADO"),
	TIRO_RODILLA("Tiro a la Rodilla", 20, 1, 6, AccionCombate.Estadistica.DESTREZA, "LISIADO");

	private final String nombre;
	private final int costeEnergia;
	private final int cantidadDados;
	private final int carasDado;
	private final AccionCombate.Estadistica estadistica;
	private final String estadoAsociado;

	TipoHabilidadFisicaSimple(String nombre, int costeEnergia, int cantidadDados, int carasDado, AccionCombate.Estadistica estadistica, String estadoAsociado) {
		this.nombre = nombre;
		this.costeEnergia = costeEnergia;
		this.cantidadDados = cantidadDados;
		this.carasDado = carasDado;
		this.estadistica = estadistica;
		this.estadoAsociado = estadoAsociado;
	}

	public String getNombre() { return nombre; }
	public int getCosteEnergia() { return costeEnergia; }
	public int getCantidadDados() { return cantidadDados; }
	public int getCarasDado() { return carasDado; }
	public AccionCombate.Estadistica getEstadistica() { return estadistica; }
	public String getEstadoAsociado() { return estadoAsociado; }
}
