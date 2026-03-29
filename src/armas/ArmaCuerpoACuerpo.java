package armas;

import personajes.Personaje;

public class ArmaCuerpoACuerpo extends Arma {

  public ArmaCuerpoACuerpo(String nombre, int cantidadDados, int carasDado) {
    // La categoria de arma hace que se diferencie el tipo de arma
    super(nombre, cantidadDados, carasDado, CategoriaArma.melee);
  }

  @Override
  public int calcularDaño(Personaje atacante, Personaje defensor) {
    // Tirada de arma + Modificador de Fuerza
    int dañoBase = tirarDados();
    // Dividimos para balancear y que no este to chetado xd
    int bonoFuerza = atacante.getFuerza() / 2;
    return dañoBase + bonoFuerza;
  }
}
