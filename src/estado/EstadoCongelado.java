package estado;

import personajes.Personaje;

public class EstadoCongelado extends Estado {

  public EstadoCongelado(int turnos, int potencia) {
    super("Congelado", turnos, potencia);
  }

  @Override
  public void alPasarTurnoEstado(Personaje obj) {
    System.out.println(motor.MotorCombate.ANSI_AZUL + "[ESTADO] El frío hiela a " + obj.getNombre() + motor.MotorCombate.ANSI_RESET);
    obj.recibirDaño(potencia, true);
  }

  @Override
  public void alTerminarEstado(Personaje obj) {
    System.out.println(motor.MotorCombate.ANSI_AZUL + "[ESTADO] " + obj.getNombre() + " se ha descongelado." + motor.MotorCombate.ANSI_RESET);
  }
}
