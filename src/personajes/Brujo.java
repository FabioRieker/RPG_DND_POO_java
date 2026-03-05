package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

public class Brujo extends Personaje {

  public Brujo(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {

    super(nombre, raza, TipoClase.BRUJO, fue, des, con, intel, defBase);

    // Armas a distanciaa
    this.armasPermitidas.add(CategoriaArma.distancia);
    // armadura ligera
    this.armadurasPermitidas.add(CategoriaArmadura.LIGERA);
  }

  public void lanzarHechizo() {
    System.out.println(nombre + " está canalizando energía demoniaca...");
  }

}
