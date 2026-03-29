package estado;

import personajes.Personaje;

public class EstadoQuemadura extends Estado {

    public EstadoQuemadura(int turnos, int potencia) {
        super("Quemadura", turnos, potencia);
    }

    @Override
    public void alPasarTurnoEstado(Personaje obj) {
        System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] El fuego arde en " + obj.getNombre() + motor.MotorCombate.ANSI_RESET);
        obj.recibirDaño(potencia, true);
    }

    @Override
    public void alTerminarEstado(Personaje obj) {
        System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] Las llamas se han extinguido en " + obj.getNombre() + motor.MotorCombate.ANSI_RESET);
    }
}
