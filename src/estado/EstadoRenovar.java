package estado;

import personajes.Personaje;

public class EstadoRenovar extends Estado {

  public EstadoRenovar(int turnos, int potencia) {
    super("Renovar", turnos, potencia);
  }

  @Override
  public void alPasarTurnoEstado(Personaje obj) {
    System.out.println(motor.MotorCombate.ANSI_MORADO + "[ESTADO] La luz sana a " + obj.getNombre() + motor.MotorCombate.ANSI_RESET);
    obj.curar(potencia);
  }

  @Override
  public void alTerminarEstado(Personaje obj) {
    System.out.println(motor.MotorCombate.ANSI_MORADO + "[ESTADO] El efecto de renovación termina en " + obj.getNombre() + motor.MotorCombate.ANSI_RESET);
  }
}
