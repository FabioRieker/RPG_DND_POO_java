package habilidad;

import personajes.Personaje;
import estado.EstadoRenovar;

/**
 * Magia sagrada híbrida que hace daño a monstruos pero cura a aliados.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class LuzSagrada extends HabilidadHibrida {

	/**
	 * Construye los costes y características de la Luz Sagrada.
	 */
	public LuzSagrada() {
		super("Luz Sagrada", 15, 20, 2, 8, Estadistica.INTELIGENCIA, Efecto.NINGUNO);
	}

	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		int daño = tirarDados() + bono;
		System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
		objetivo.recibirDaño(daño, false);

		// El efecto secundario solo se activa si el golpe inicial impactó
		usuario.curar(20);
		usuario.aplicarEstado(new EstadoRenovar(3, 10));
		System.out.println("-- " + usuario.getNombre() + " sanado + RENOVAR!");
	}
}