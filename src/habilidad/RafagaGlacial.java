package habilidad;

import personajes.Personaje;
import estado.EstadoCongelado;

// Ráfaga de hielo
// Daño: 2d6, Coste: 20 PM
// Aplica Congelar al objetivo
public class RafagaGlacial extends HechizoMagico {

  public RafagaGlacial() {
    super("Ráfaga Glacial", 20, 2, 6, null);
  }

  @Override
  public void ejecutar(Personaje usuario, Personaje objetivo) {
    if (!usuario.tieneRecursos(costeEnergia, costeMana)) {
      System.out.println(usuario.getNombre() + " no tiene suficientes recursos.");
      return;
    }

    usuario.consumirRecursos(costeEnergia, costeMana);

    int tirada = dado.nextInt(20) + 1;
    int bono = usuario.getInteligencia() / 2;
    int totalImpacto = tirada + bono;

    System.out.println(usuario.getNombre() + " lanza " + nombre + "...");
    System.out.println("Tirada de impacto: " + tirada + " + " + bono + " = " + totalImpacto);

    if (totalImpacto >= objetivo.getDefensaTotal()) {
      int daño = tirarDados() + bono;
      System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
      objetivo.recibirDaño(daño, false);

      if (!objetivo.tieneEstado("Congelado")) {
        objetivo.aplicarEstado(new EstadoCongelado(2, 3));
        System.out.println("-- ¡" + objetivo.getNombre() + " está congelado!");
      }
    } else {
      System.out.println("¡FALLO! El hechizo no atraviesa la defensa.");
    }
  }
}
