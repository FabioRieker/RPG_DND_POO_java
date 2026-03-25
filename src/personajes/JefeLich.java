package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

public class JefeLich extends Jefe {

  public JefeLich(String nombre, int fue, int des, int con, int intel, int defBase) {
    super(nombre, Raza.NO_MUERTO, TipoJefe.LICH, fue, des, con, intel, defBase);

    this.armasPermitidas.add(CategoriaArma.distancia);
    this.armadurasPermitidas.add(CategoriaArmadura.LIGERA);
  }

  @Override
  public void habilidadEspecial() {
    System.out.println(nombre + " invoca a los muertos!");
  }

  public void lanzarHechizo() {
    System.out.println(nombre + " canaliza energía necromántica!");
  }
}
