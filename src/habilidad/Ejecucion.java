package habilidad;

import personajes.Personaje;

// Golpe devastador contra enemigos debilitados
// Daño: 1d12, Coste: 40 PE, Escalado: FUE
// Si el objetivo tiene menos del 20% de vida, hace daño x3
public class Ejecucion extends HabilidadFisica {
  public Ejecucion() {
    super("Ejecución", 40, 1, 12, Estadistica.FUERZA, null);
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

    // Calcula si es ejecución
    int vidaObjetivo = objetivo.getVidaActual();
    int vidaMaxObjetivo = objetivo.getVidaMax();
    int porcentajeVida = (vidaObjetivo * 100) / vidaMaxObjetivo;

    boolean esEjecucion = porcentajeVida <= 20;

    if (totalImpacto >= objetivo.getDefensaTotal()) {
      int daño = tirarDados() + bono;
      if (esEjecucion) {
        daño *= 3;
        System.out.println("¡EJECUCIÓN! Objetivo con poca vida - DAÑO x3!");
      }
      System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
      objetivo.recibirDaño(daño, false);
    } else {
      System.out.println("¡FALLO! El ataque no atraviesa la defensa del objetivo.");
    }
  }
}
