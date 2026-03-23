package habilidad;

import personajes.Personaje;

public abstract class HechizoMagico extends AccionCombate {

  protected String efectoEspecial;

  public HechizoMagico(String nombre, int costeMana, int dadosCantidad, int dadosCaras, String efecto) {
    super(nombre, 0, costeMana, dadosCantidad, dadosCaras, Estadistica.INTELIGENCIA);
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
    int bono = usuario.getInteligencia() / 2;
    int totalImpacto = tirada + bono;

    System.out.println(usuario.getNombre() + " lanza " + nombre + "...");
    System.out.println("Tirada de impacto: " + tirada + " + " + bono + " = " + totalImpacto);

    if (totalImpacto >= objetivo.getDefensaTotal()) {
      int daño = tirarDados() + bono;
      System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + "de daño magico");
      objetivo.recibirDaño(daño, false);

      aplicarEfecto(objetivo);
    } else {
      System.out.println("¡FALLO! El hechizo no atravesó la defensa");
    }
  }

  protected void aplicarEfecto(Personaje objetivo) {
    System.out.println("-- Efecto: " + efectoEspecial);
  }
}
