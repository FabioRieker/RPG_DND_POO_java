package habilidad;

import personajes.Personaje;

/**
 * Hechizo defensivo que obliga a los enemigos a golpear un muro resistente.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class MuroPiedra extends HechizoMagico {

	/**
	 * Construye los costes y características del Muro de Piedra.
	 */
	public MuroPiedra() {
		super("Muro de Piedra", 15, 0, 0, null);
	}

	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		System.out.println(usuario.getNombre() + " lanza " + nombre + "...");
		System.out.println("¡IMPACTO! " + objetivo.getNombre()
				+ " bloquea ataques masivos y provoca a sus atacantes por 1 turno!");
		objetivo.aplicarMuroPiedra();
	}
}