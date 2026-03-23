package habilidad;

import personajes.Personaje;
import estado.EstadoVeneno;

public abstract class HabilidadFisica extends AccionCombate {

  protected String efectoEspecial;

  public HabilidadFisica(String nombre, int costeEnergia, int dadosCantidad, int dadosCaras,
      Estadistica estadistica, String efecto) {
    super(nombre, costeEnergia, 0, dadosCantidad, dadosCaras, estadistica);
    this.efectoEspecial = efecto;
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
      System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + "de daño");
      objetivo.recibirDaño(daño, false);

      aplicarEfecto(objetivo);
    } else {
      System.out.println("¡FALLO! El ataque no acierta");
    }
  }

  protected void aplicarEfecto(Personaje objetivo) {
    if (efectoEspecial == null)
      return;

    switch (efectoEspecial) {
      case "VENENO":
        if (!objetivo.tieneEstado("Veneno")) {
          objetivo.aplicarEstado(new EstadoVeneno(3, 5));
          System.out.println("-- ¡Se ha envenenado a " + objetivo.getNombre() + "!");
        }
        break;
      case "ATURDIR":
        System.out.println("-- ¡" + objetivo.getNombre() + " está aturdido!");
        break;
      case "SANGRADO":
        System.out.println("-- ¡" + objetivo.getNombre() + " está sangrando!");
        break;
      case "LISIADO":
        System.out.println("-- ¡" + objetivo.getNombre() + " está lisiado!");
        break;
    }
  }

  public void ejecutar(Personaje usuario, java.util.ArrayList<Personaje> objetivos) {
    for (Personaje objetivo : objetivos) {
      ejecutar(usuario, objetivo);
    }
  }
}
