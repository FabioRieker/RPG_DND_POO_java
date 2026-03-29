package estado;

import personajes.Personaje;

public class EstadoSangrado extends Estado {

    public EstadoSangrado(int turnos, int potencia) {
        super("Sangrado", turnos, potencia);
    }

    @Override
    public void alPasarTurnoEstado(Personaje obj) {
        System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] " + obj.getNombre() + " pierde sangre." + motor.MotorCombate.ANSI_RESET);
        obj.recibirDaño(potencia, true);
    }

    @Override
    public void alTerminarEstado(Personaje obj) {
        System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] El sangrado de " + obj.getNombre() + " se ha detenido." + motor.MotorCombate.ANSI_RESET);
    }
}
