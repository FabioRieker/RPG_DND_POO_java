package armas;

import personajes.Personaje;

public class ArmaCuerpoACuerpo extends Arma {

  public ArmaCuerpoACuerpo(String nombre, int cantidadDados, int carasDado) {
    super(nombre, cantidadDados, carasDado);
  }

  @Override
  public int calcularDaño(Personaje atacante, Personaje defensor) {
    // Tirada de arma + Modificador de Fuerza
    int dañoBase = tirarDados();
    int bonoFuerza = atacante.getFuerza() / 2; // Dividimos para balancear y que no este to chetado xd

    System.out.println(atacante.getNombre() + " ataca cuerpo a cuerpo con " + this.nombre);
    return dañoBase + bonoFuerza;
  }
}
