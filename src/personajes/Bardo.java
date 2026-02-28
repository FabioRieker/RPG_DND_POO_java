package personajes;

public class Bardo extends Personaje {

  public Bardo(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {
    super(nombre, raza, TipoClase.BARDO, fue, des, con, intel, defBase);
  }

  public void lanzarHechizo() {
    System.out.println(nombre + " está cargado su fuerza...");
  }
}
