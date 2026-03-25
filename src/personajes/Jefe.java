package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

public abstract class Jefe extends Personaje {

  protected TipoJefe tipo;

  public Jefe(String nombre, Raza raza, TipoJefe tipo, int fue, int des, int con, int intel, int defBase) {
    super(nombre, raza, TipoClase.JEFE, fue, des, con, intel, defBase);
    this.tipo = tipo;
  }

  public TipoJefe getTipo() {
    return tipo;
  }

  public abstract void habilidadEspecial();
}
