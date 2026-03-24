package habilidad;

import personajes.Personaje;
import estado.EstadoCongelado;
import java.util.ArrayList;

// Tormenta de nieve
// Daño: 2d6, Coste: 40 PM
// Puede congelar a los objetivos (50% probabilidad)
public class Ventisca extends HechizoMagico {

  public Ventisca() {
    super("Ventisca", 40, 2, 6, null);
  }

  @Override
  public void ejecutar(Personaje usuario, Personaje objetivo) {
    System.out.println(usuario.getNombre() + " quiere usar " + nombre + "...");
    System.out.println("Este hechizo afecta a múltiples objetivos. Usa la versión con lista de enemigos.");
  }

  @Override
  public void ejecutar(Personaje usuario, ArrayList<Personaje> objetivos) {
    if (!usuario.tieneRecursos(costeEnergia, costeMana)) {
      System.out.println(usuario.getNombre() + " no tiene suficientes recursos.");
      return;
    }

    usuario.consumirRecursos(costeEnergia, costeMana);

    int bono = usuario.getInteligencia() / 2;

    System.out.println(usuario.getNombre() + " lanza " + nombre + "...");
    System.out.println("--- VENTISCA ---");

    for (Personaje obj : objetivos) {
      int tirada = dado.nextInt(20) + 1;
      int totalImpacto = tirada + bono;

      System.out.println("-> " + obj.getNombre() + " - Tirada: " + tirada + " + " + bono + " = " + totalImpacto);

      if (totalImpacto >= obj.getDefensaTotal()) {
        int daño = tirarDados() + bono;
        System.out.println("  ¡IMPACTO! " + obj.getNombre() + " recibe " + daño + " de daño.");
        obj.recibirDaño(daño, false);

        // 50% de probabilidad de congelar
        int probabilidadCongelar = dado.nextInt(100) + 1;
        if (probabilidadCongelar <= 50 && !obj.tieneEstado("Congelado")) {
          obj.aplicarEstado(new EstadoCongelado(2, 3));
          System.out.println("  -- ¡" + obj.getNombre() + " está congelado!");
        }
      } else {
        System.out.println("  ¡FALLO! El hechizo no atraviesa la defensa.");
      }
    }
  }
}
