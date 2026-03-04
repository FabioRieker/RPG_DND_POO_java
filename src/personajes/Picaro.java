package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

public class Picaro extends Personaje {

  public Picaro(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {

    super(nombre, raza, TipoClase.PICARO, fue, des, con, intel, defBase);

    // Armas a melee y a distancia
    this.armasPermitidas.add(CategoriaArma.melee);
    this.armasPermitidas.add(CategoriaArma.distancia);
    // armaduras ligeras
    this.armadurasPermitidas.add(CategoriaArmadura.LIGERA);

  }

  public void lanzarHechizo() {
    System.out.println(nombre + " está canalizando energía arcana...");
  }

}
