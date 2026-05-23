package habilidad;

import personajes.Personaje;
import estado.EstadoFuria;
import java.util.ArrayList;

/**
 * Habilidades que usan tanto energia como mana al mismo tiempo. Utiles para
 * clases hibridas como el Paladin o el Bardo.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class HabilidadHibrida extends AccionCombate {

	private Efecto efecto;

	/**
	 * Configura el ataque mezclado con magia y fuerza fisica.
	 * 
	 * @param nombre        Nombre del conjuro marcial.
	 * @param costeEnergia  Resistencia aerobica gastada.
	 * @param costeMana     Puntos de energia espiritual gastados.
	 * @param dadosCantidad Multiplicador de la tirada.
	 * @param dadosCaras    Limite maximo del daño.
	 * @param estadistica   Puntos de atributo con los que escala.
	 * @param efecto        Si este ataque cura, bufon o roba vida.
	 */
	public HabilidadHibrida(String nombre, int costeEnergia, int costeMana, int dadosCantidad, int dadosCaras,
			Estadistica estadistica, Efecto efecto) {
		super(nombre, costeEnergia, costeMana, dadosCantidad, dadosCaras, estadistica);
		this.efecto = efecto;
	}

	/**
	 * Se ejecuta al aceptar que el proyectil hibrido ha acertado a su blanco.
	 * 
	 * @param usuario  Tirador del hechizo.
	 * @param objetivo Recepcionista del daño.
	 * @param bono     Adicional por estadisticas del lanzador.
	 */
	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		int daño = tirarDados() + bono;
		String colorDaño = objetivo.esEnemigo() ? motor.MotorCombate.ANSI_VERDE_OSCURO : motor.MotorCombate.ANSI_ROJO;
		System.out.println(colorDaño + "[DAÑO HIBRIDO] ¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño
				+ " de daño." + motor.MotorCombate.ANSI_RESET);

		// Explosion Arcana hace daño puro
		boolean esPuro = this.nombre.equals("Explosión Arcana");
		objetivo.recibirDaño(daño, esPuro);

		aplicarEfectoEspecial(usuario, objetivo, daño);
	}

	private void aplicarEfectoEspecial(Personaje usuario, Personaje objetivo, int daño) {
		switch (efecto) {
		case CURAR_VIDA:
			System.out.println(motor.MotorCombate.ANSI_MORADO + "[CURA] " + usuario.getNombre() + " se cura " + daño
					+ " HP!" + motor.MotorCombate.ANSI_RESET);
			usuario.curar(daño);
			break;
		case ROBO_VIDA:
			System.out.println(motor.MotorCombate.ANSI_MORADO + "[DRENAJE] " + usuario.getNombre() + " roba " + daño
					+ " HP!" + motor.MotorCombate.ANSI_RESET);
			usuario.curar(daño);
			break;
		case BUFF_ALIADOS:
			String colorBuff = objetivo.esEnemigo() ? motor.MotorCombate.ANSI_ROJO
					: motor.MotorCombate.ANSI_VERDE_OSCURO;
			System.out.println(colorBuff + "[BUFF] " + objetivo.getNombre() + " recibe +" + daño + " de daño temporal!"
					+ motor.MotorCombate.ANSI_RESET);
			objetivo.aplicarEstado(new EstadoFuria(3, daño));
			break;
		case NINGUNO:
		default:
			break;
		}
	}

	/**
	 * Ejecuta la habilidad híbrida contra cada personaje de la lista de objetivos.
	 *
	 * @param usuario   Personaje que usa la habilidad.
	 * @param objetivos Lista de objetivos a los que se aplica.
	 */
	public void ejecutar(Personaje usuario, ArrayList<Personaje> objetivos) {
		for (Personaje objetivo : objetivos) {
			ejecutar(usuario, objetivo);
		}
	}
}