package habilidad;

import personajes.Personaje;
import estado.EstadoRenovar;

// Hechizo de luz sagrada que también cura
// Daño: 2d8, Coste: 15 PE + 20 PM
// Sana al objetivo y aplica Renovar
public class LuzSagrada extends HabilidadHibrida {

  public LuzSagrada() {
    super("Luz Sagrada", 15, 20, 2, 8, Estadistica.INTELIGENCIA, Efecto.NINGUNO);
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
      objetivo.recibirDaño(daño, false);
    } else {
      System.out.println("¡FALLO! El hechizo no atraviesa la defensa.");
    }

    // Solo cura y aplica renovar si impactó
    if (totalImpacto >= objetivo.getDefensaTotal()) {
      objetivo.curar(20);
      objetivo.aplicarEstado(new EstadoRenovar(3, 10));
      System.out.println("-- " + objetivo.getNombre() + " sanado + RENOVAR!");
    }
  }
}
