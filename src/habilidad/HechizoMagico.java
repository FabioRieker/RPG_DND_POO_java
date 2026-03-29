package habilidad;

import personajes.Personaje;
import estado.EstadoQuemadura;
import estado.EstadoCongelado;
import estado.EstadoRenovar;

// Hechizos mágicos: consumen mana, escalan con INT
// Incluyen efectos como Quemadura, Congelar, Renovar
public abstract class HechizoMagico extends AccionCombate {

	protected String efectoEspecial;

	public HechizoMagico(String nombre, int costeMana, int dadosCantidad, int dadosCaras, String efecto) {
		super(nombre, 0, costeMana, dadosCantidad, dadosCaras, Estadistica.INTELIGENCIA);
		this.efectoEspecial = efecto;
	}

	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		int daño = tirarDados() + bono;

		// Sinergia: fuego contra veneno = daño doble y limpia el veneno
		if ("QUEMA".equals(efectoEspecial) && objetivo.tieneEstado("Veneno")) {
			daño *= 2;
			System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[CRÍTICO MAGICO] ¡SINERGIA! Fuego contra Veneno - DAÑO x2" + motor.MotorCombate.ANSI_RESET);
			System.out.println(motor.MotorCombate.ANSI_MORADO + "[CURA] El veneno se ha sublimado con el fuego." + motor.MotorCombate.ANSI_RESET);
		}

		String colorDaño = objetivo.esEnemigo() ? motor.MotorCombate.ANSI_VERDE_OSCURO : motor.MotorCombate.ANSI_ROJO;
		System.out.println(colorDaño + "[DAÑO MAGICO] ¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño." + motor.MotorCombate.ANSI_RESET);
		objetivo.recibirDaño(daño, false);

		aplicarEfecto(objetivo);
	}

	// Aplica efecto secundario según el tipo
	protected void aplicarEfecto(Personaje objetivo) {
		if (efectoEspecial == null)
			return;

		switch (efectoEspecial) {
		case "QUEMA":
			if (!objetivo.tieneEstado("Quemadura")) {
				System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] ¡" + objetivo.getNombre() + " está en llamas!" + motor.MotorCombate.ANSI_RESET);
				objetivo.aplicarEstado(new EstadoQuemadura(3, 5));
			}
			break;
		case "CONGELA":
			if (!objetivo.tieneEstado("Congelado")) {
				System.out.println(motor.MotorCombate.ANSI_AZUL + "[ESTADO] ¡" + objetivo.getNombre() + " está congelado!" + motor.MotorCombate.ANSI_RESET);
				objetivo.aplicarEstado(new EstadoCongelado(2, 3));
			}
			break;
		case "RENOVAR":
			System.out.println(motor.MotorCombate.ANSI_MORADO + "[MEND] ¡" + objetivo.getNombre() + " recibe efectos regenerativos!" + motor.MotorCombate.ANSI_RESET);
			objetivo.aplicarEstado(new EstadoRenovar(3, 8));
			break;
		case "MULTI":
			System.out.println(motor.MotorCombate.ANSI_AZUL + "[MAGIA] ¡El rayo salta a objetivos cercanos!" + motor.MotorCombate.ANSI_RESET);
			break;
		case "DEFENSA_MAS":
			String colorBuff = objetivo.esEnemigo() ? motor.MotorCombate.ANSI_ROJO : motor.MotorCombate.ANSI_VERDE_OSCURO;
			System.out.println(colorBuff + "[DEFENSA] ¡" + objetivo.getNombre() + " recibe +4 de defensa temporal!" + motor.MotorCombate.ANSI_RESET);
			break;
		}
	}

	// Ejecuta contra varios objetivos a la vez
	public void ejecutar(Personaje usuario, java.util.ArrayList<Personaje> objetivos) {
		for (Personaje objetivo : objetivos) {
			ejecutar(usuario, objetivo);
		}
	}
}