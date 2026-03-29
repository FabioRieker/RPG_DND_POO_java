package consumibles;

import personajes.Personaje;

public class PocionCuracion extends Consumible {

	public PocionCuracion(int cantidad) {
		super("Poción de Curación", cantidad);
	}

	@Override
	public void usar(Personaje usuario, Personaje objetivo) {
		if (cantidad <= 0) {
			System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[SISTEMA] No quedan " + nombre + "." + motor.MotorCombate.ANSI_RESET);
			return;
		}

		cantidad--;
		System.out.println(motor.MotorCombate.ANSI_MORADO + "\n[OBJETO] " + usuario.getNombre() + " usa " + nombre + "." + motor.MotorCombate.ANSI_RESET);
		usuario.curar(50); 
	}
}