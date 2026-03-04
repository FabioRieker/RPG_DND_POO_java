package armaduras;

public class Armadura {
  private String nombre;
  private CategoriaArmadura categoria;
  private int bonoDefensa;
  private int penalizacionDestreza; // Cuánta agilidad penaliza, si es una armadura pesada
                                    // pues tendra mucha defensa pero a costa de la destreza

  public Armadura(String nombre, CategoriaArmadura categoria, int bonoDefensa, int penalizacionDestreza) {
    this.nombre = nombre;
    this.categoria = categoria;
    this.bonoDefensa = bonoDefensa;
    this.penalizacionDestreza = penalizacionDestreza;
  }

  public String getNombre() {
    return nombre;
  }

  public CategoriaArmadura getCategoria() {
    return categoria;
  }

  public int getBonoDefensa() {
    return bonoDefensa;
  }

  public int getPenalizacionDestreza() {
    return penalizacionDestreza;
  }
}
