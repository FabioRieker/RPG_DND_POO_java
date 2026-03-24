package habilidad;

import personajes.Personaje;

// Ataque rápido de distracción
// Daño: 1d4, Coste: 10 PE, Escalado: DES
// Daño bajo pero puede mejorar la evasión
public class FintaRapida extends HabilidadFisica {
  public FintaRapida() {
    super("Finta Rápida", 10, 1, 4, Estadistica.DESTREZA, null);
  }

  @Override
  public void ejecutar(Personaje usuario, Personaje objetivo) {
    if (!usuario.tieneRecursos(costeEnergia, costeMana)) {
      System.out.println(usuario.getNombre() + " no tiene suficientes recursos.");
      return;
    }

    usuario.consumirRecursos(costeEnergia, costeMana);

    int tirada = dado.nextInt(20) + 1;
    int bono = getModificador(usuario) / 2;
    int totalImpacto = tirada + bono;

    System.out.println(usuario.getNombre() + " usa " + nombre + "...");
    System.out.println("Tirada de impacto: " + tirada + " + " + bono + " = " + totalImpacto);

    if (totalImpacto >= objetivo.getDefensaTotal()) {
      int daño = tirarDados() + bono;
      System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
      objetivo.recibirDaño(daño, false);
      System.out.println("-- Daño leve completado.");
    } else {
      System.out.println("¡FALLO! El ataque no atraviesa la defensa del objetivo.");
    }
  }
}
