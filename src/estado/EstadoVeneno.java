package estado;

import personajes.Personaje;

public class EstadoVeneno extends Estado {

	public EstadoVeneno(int turnos, int potencia) {
		super("Veneno", turnos, potencia);
	}

	@Override
	public void alPasarTurnoEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] El veneno afecta a " + obj.getNombre() + motor.MotorCombate.ANSI_RESET);
		obj.recibirDaño(potencia, true);
	}

	@Override
	public void alTerminarEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] El veneno se ha disipado de " + obj.getNombre() + motor.MotorCombate.ANSI_RESET);
	}
}