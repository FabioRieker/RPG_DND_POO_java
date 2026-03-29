package habilidad;

import java.util.Random;
import personajes.Personaje;

/**
 * Clase base para todas las habilidades del juego. Controla si cuestan energía
 * o maná, cuántos dados hay que tirar para ver el daño y con qué atributo
 * escalan.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public abstract class AccionCombate {

	protected String nombre;
	protected int costeEnergia; // Puntos de Energía base gastados al activarse (Valor 0 si es gratuita)
	protected int costeMana; // Puntos de Maná base consumidos al lanzarse (Valor 0 si no requiere)
	protected int dadosCantidad; // Cantidad total de dados rodados en la tirada base
	protected int dadosCaras; // Número de caras del tipo de dado usado para el daño
	protected Estadistica estadisticaClave; // Estadística principal que escala el potencial de la habilidad
	protected Random dado = new Random();

	/**
	 * Estadísticas del personaje que pueden potenciar el efecto de la habilidad.
	 */
	public enum Estadistica {
		FUERZA, DESTREZA, INTELIGENCIA, CONSTITUCION
	}

	/**
	 * Prepara la acción de combate con sus costes y estadísticas base.
	 * 
	 * @param nombre           Nombre de la habilidad.
	 * @param costeEnergia     Puntos de energía (SP) que consume. 0 si no usa.
	 * @param costeMana        Puntos de maná (MP) que consume. 0 si no usa.
	 * @param dadosCantidad    Número de dados a tirar para calcular el daño base.
	 * @param dadosCaras       Caras de cada dado (ej. 6 para un dado normal).
	 * @param estadisticaClave Estadística del personaje que mejora el ataque.
	 */
	public AccionCombate(String nombre, int costeEnergia, int costeMana, int dadosCantidad, int dadosCaras,
			Estadistica estadisticaClave) {
		this.nombre = nombre;
		this.costeEnergia = costeEnergia;
		this.costeMana = costeMana;
		this.dadosCantidad = dadosCantidad;
		this.dadosCaras = dadosCaras;
		this.estadisticaClave = estadisticaClave;
	}

	/**
	 * Tira los dados virtuales correspondientes y suma el resultado para obtener el
	 * daño base.
	 * 
	 * @return La suma del daño tras tirar todos los dados.
	 */
	protected int tirarDados() {
		int total = 0;
		for (int i = 0; i < dadosCantidad; i++) {
			total += dado.nextInt(dadosCaras) + 1;
		}
		return total;
	}

	/**
	 * Consigue el valor correspondiente de fuerza, destreza, etc. del personaje
	 * para potenciar la habilidad.
	 * 
	 * @param p Personaje que usa la habilidad.
	 * @return El valor de su estadística clave.
	 */
	protected int getModificador(Personaje p) {
		switch (estadisticaClave) {
		case FUERZA:
			return p.getFuerza();
		case DESTREZA:
			return p.getDestreza();
		case INTELIGENCIA:
			return p.getInteligencia();
		case CONSTITUCION:
			return p.getConstitucion();
		default:
			return 0;
		}
	}

	/**
	 * Realiza todo el proceso de usar la habilidad: comprueba si hay recursos,
	 * calcula si se acierta al objetivo, y si impacta, se llama al efecto propio de
	 * la habilidad y resta los recursos gastados.
	 * 
	 * @param usuario  Personaje que ejecuta la habilidad.
	 * @param objetivo Enemigo o aliado al que se le lanza la habilidad.
	 */
	public void ejecutar(Personaje usuario, Personaje objetivo) {
		if (!usuario.tieneRecursos(costeEnergia, costeMana)) {
			System.out.println(usuario.getNombre() + " no tiene suficientes recursos.");
			return;
		}

		int tirada = dado.nextInt(20) + 1;
		int bono = getModificador(usuario) / 2;
		int totalImpacto = tirada + bono;

		System.out.println("\n[HABILIDAD] " + usuario.getNombre() + " usa " + nombre + "...");
		System.out.println(motor.MotorCombate.ANSI_CIAN + "[SISTEMA] Tirada: " + tirada + " + " + bono + " = "
				+ totalImpacto + motor.MotorCombate.ANSI_RESET);

		if (totalImpacto >= objetivo.getDefensaTotal()) {
			usuario.consumirRecursos(costeEnergia, costeMana);
			aplicarEfectoImpacto(usuario, objetivo, bono);
		} else {
			System.out
					.println(motor.MotorCombate.ANSI_AMARILLO + "[ALERTA] ¡FALLO! El ataque no atraviesa la defensa de "
							+ objetivo.getNombre() + "." + motor.MotorCombate.ANSI_RESET);
		}
	}

	/**
	 * Cada habilidad concreta define aquí qué hace exactamente cuando acierta al
	 * rival.
	 * 
	 * @param usuario  Personaje que ha lanzado el ataque.
	 * @param objetivo Personaje que sufre o recibe la habilidad.
	 * @param bono     Puntos extra de daño calculados al ejecutar el ataque.
	 */
	protected abstract void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono);

	public String getNombre() {
		return nombre;
	}

	public int getCosteEnergia() {
		return costeEnergia;
	}

	public int getCosteMana() {
		return costeMana;
	}
}