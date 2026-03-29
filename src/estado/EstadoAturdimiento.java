package estado;

import personajes.Personaje;

public class EstadoAturdimiento extends Estado {

    public EstadoAturdimiento(int turnos) {
        super("Aturdimiento", turnos, 0);
    }

    @Override
    public void alPasarTurnoEstado(Personaje obj) {
        System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] " + obj.getNombre() + " esta aturdido y no puede actuar!" + motor.MotorCombate.ANSI_RESET);
    }

    @Override
    public void alTerminarEstado(Personaje obj) {
        System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] " + obj.getNombre() + " ya no esta aturdido." + motor.MotorCombate.ANSI_RESET);
    }
}
