package habilidad;

import personajes.Personaje;

// Hechizo de purificación
// Daño: ninguno, Coste: 15 PM
// Elimina todos los estados negativos del aliado
public class Purificacion extends HechizoMagico {

	public Purificacion() {
		super("Purificación", 15, 0, 0, null);
	}

	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		System.out.println(usuario.getNombre() + " lanza " + nombre + "...");
		System.out.println("--- PURIFICACIÓN ---");
		System.out.println("Eliminando estados negativos de " + objetivo.getNombre() + "...");

		int estadosEliminados = 0;
		for (int i = objetivo.getEstadosActivos().size() - 1; i >= 0; i--) {
			estado.Estado e = objetivo.getEstadosActivos().get(i);
			String nombreEstado = e.getNombre();
			if (nombreEstado.equals("Veneno") || nombreEstado.equals("Quemadura") || nombreEstado.equals("Congelado")
					|| nombreEstado.equals("Aturdimiento") || nombreEstado.equals("Sangrado")
					|| nombreEstado.equals("Lisiado")) {
				objetivo.getEstadosActivos().remove(i);
				estadosEliminados++;
				System.out.println("-- Eliminado: " + nombreEstado);
			}
		}

		if (estadosEliminados == 0) {
			System.out.println("-- No se eliminó ningún estado negativo.");
		} else {
			System.out.println(
					"¡" + objetivo.getNombre() + " ha sido purificado! (" + estadosEliminados + " estados eliminados)");
		}
	}
}