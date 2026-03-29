package consumibles;

import personajes.Personaje;

public class PocionRecurso extends Consumible {

	public PocionRecurso(int cantidad) {
		super("Poción de Recurso", cantidad);
	}

	@Override
	public void usar(Personaje usuario, Personaje objetivo) {
		if (cantidad <= 0) {
			System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[SISTEMA] No quedan " + nombre + "." + motor.MotorCombate.ANSI_RESET);
			return;
		}

		cantidad--;
		System.out.println(motor.MotorCombate.ANSI_AZUL + "\n[OBJETO] " + usuario.getNombre() + " usa " + nombre + "." + motor.MotorCombate.ANSI_RESET);
		usuario.recuperarRecursos(40);
	}
}