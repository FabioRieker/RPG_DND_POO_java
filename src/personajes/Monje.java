package personajes;

public class Monje extends Personaje {

  public Monje(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {

    super(nombre, raza, TipoClase.MONJE, fue, des, con, intel, defBase);
  }

  public void lanzarHechizo() {
    System.out.println(nombre + "Esta haciendo taichi a la fresca");
  }

}
