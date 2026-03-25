package armaduras;

public enum CategoriaArmadura {
  NADA("Sin armadura", 0, 2),
  LIGERA("Armadura Ligera", 3, 1),
  MEDIA("Armadura Media", 6, 0),
  PESADA("Armadura Pesada", 10, -5);

  public final String nombre;
  public final int bonoDefensa;
  public final int modificadorDestreza;

  CategoriaArmadura(String nombre, int defensa, int destreza) {
    this.nombre = nombre;
    this.bonoDefensa = defensa;
    this.modificadorDestreza = destreza;
  }
}
