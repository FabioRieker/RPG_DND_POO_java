package habilidad;

/**
 * Enumerado que almacena los datos de las habilidades hibridas simples.
 */
public enum TipoHabilidadHibridaSimple {
	EXPLOSION_ARCANA("Explosión Arcana", 15, 15, 3, 8, AccionCombate.Estadistica.INTELIGENCIA, Efecto.NINGUNO),
	GOLPE_SANGUINARIO("Golpe Sanguinario", 15, 10, 1, 8, AccionCombate.Estadistica.FUERZA, Efecto.CURAR_VIDA),
	TOQUE_VAMPIRICO("Toque Vampírico", 10, 15, 1, 4, AccionCombate.Estadistica.INTELIGENCIA, Efecto.ROBO_VIDA);

	private final String nombre;
	private final int costeEnergia;
	private final int costeMana;
	private final int cantidadDados;
	private final int carasDado;
	private final AccionCombate.Estadistica estadistica;
	private final Efecto efectoExtra;

	TipoHabilidadHibridaSimple(String nombre, int costeEnergia, int costeMana, int cantidadDados, int carasDado, AccionCombate.Estadistica estadistica, Efecto efectoExtra) {
		this.nombre = nombre;
		this.costeEnergia = costeEnergia;
		this.costeMana = costeMana;
		this.cantidadDados = cantidadDados;
		this.carasDado = carasDado;
		this.estadistica = estadistica;
		this.efectoExtra = efectoExtra;
	}

	public String getNombre() { return nombre; }
	public int getCosteEnergia() { return costeEnergia; }
	public int getCosteMana() { return costeMana; }
	public int getCantidadDados() { return cantidadDados; }
	public int getCarasDado() { return carasDado; }
	public AccionCombate.Estadistica getEstadistica() { return estadistica; }
	public Efecto getEfectoExtra() { return efectoExtra; }
}
