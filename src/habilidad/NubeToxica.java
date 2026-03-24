package habilidad;

import personajes.Personaje;
import estado.EstadoVeneno;
import java.util.ArrayList;

// Nube venenosa que afecta a todos los enemigos
// Daño: 1d6, Coste: 35 PM
// Aplica Veneno a todos los objetivos
public class NubeToxica extends HechizoMagico {

  public NubeToxica() {
    super("Nube Tóxica", 35, 1, 6, null);
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
    System.out.println("--- NUBE TÓXICA ---");

    for (Personaje obj : objetivos) {
      int tirada = dado.nextInt(20) + 1;
      int totalImpacto = tirada + bono;

      System.out.println("-> " + obj.getNombre() + " - Tirada: " + tirada + " + " + bono + " = " + totalImpacto);

      if (totalImpacto >= obj.getDefensaTotal()) {
        int daño = tirarDados() + bono;
        System.out.println("  ¡IMPACTO! " + obj.getNombre() + " recibe " + daño + " de daño.");
        obj.recibirDaño(daño, false);

        // Solo aplica veneno si impactó
        if (!obj.tieneEstado("Veneno")) {
          obj.aplicarEstado(new EstadoVeneno(3, 5));
          System.out.println("  -- ¡" + obj.getNombre() + " está envenenado!");
        }
      } else {
        System.out.println("  ¡FALLO! El hechizo no atraviesa la defensa.");
      }
    }
  }
}
