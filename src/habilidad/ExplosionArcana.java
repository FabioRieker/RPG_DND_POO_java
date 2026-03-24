package habilidad;

import personajes.Personaje;

// Explosión de energía arcana
// Daño: 3d8, Coste: 15 PE + 15 PM
public class ExplosionArcana extends HabilidadHibrida {

  public ExplosionArcana() {
    super("Explosión Arcana", 15, 15, 3, 8, Estadistica.INTELIGENCIA, Efecto.NINGUNO);
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

    System.out.println(usuario.getNombre() + " lanza " + nombre + "...");
    System.out.println("Coste: " + costeEnergia + " PE + " + costeMana + " PM");
    System.out.println("Tirada: " + tirada + " + " + bono + " = " + totalImpacto);

    if (totalImpacto >= objetivo.getDefensaTotal()) {
      int daño = tirarDados() + bono;
      System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
      objetivo.recibirDaño(daño, true);
    } else {
      System.out.println("¡FALLO! El hechizo no atraviesa la defensa.");
    }
  }
}
