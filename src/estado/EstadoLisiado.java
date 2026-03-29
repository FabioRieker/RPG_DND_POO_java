package estado;

import personajes.Personaje;

public class EstadoLisiado extends Estado {

    public EstadoLisiado(int turnos, int potencia) {
        super("Lisiado", turnos, potencia);
    }

    @Override
    public void alPasarTurnoEstado(Personaje obj) {
        System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] " + obj.getNombre() + " esta lisiado y se mueve con dificultad." + motor.MotorCombate.ANSI_RESET);
    }

    @Override
    public void alTerminarEstado(Personaje obj) {
        System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] " + obj.getNombre() + " ya no esta lisiado." + motor.MotorCombate.ANSI_RESET);
    }
}
