package personajes;

public class Mago extends Personaje {

  public Mago(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {
    // El Mago siempre nace con TipoClase.MAGO
    super(nombre, raza, TipoClase.MAGO, fue, des, con, intel, defBase);
  }

  public void lanzarHechizo() {
    System.out.println(nombre + " está canalizando energía arcana...");
  }
}
