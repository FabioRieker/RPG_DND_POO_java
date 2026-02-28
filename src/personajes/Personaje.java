package personajes;

import armas.Arma;
import java.util.ArrayList;

public abstract class Personaje {
  protected String nombre;
  protected Raza raza;
  protected TipoClase tipoClase;

  // Stats y Recursos
  protected int fuerza, destreza, constitucion, inteligencia;
  protected int vidaMax, vidaActual, manaMax, manaActual, energiaMax, energiaActual;
  protected int defensaBase;
  protected boolean vivo;

  protected Arma armaEquipada;

  public Personaje(String nombre, Raza raza, TipoClase tipoClase, int fue, int des, int con, int intel, int defBase) {
    this.nombre = nombre;
    this.raza = raza;
    this.tipoClase = tipoClase;
    this.fuerza = fue;
    this.destreza = des;
    this.constitucion = con;
    this.inteligencia = intel;
    this.defensaBase = defBase;
    this.vivo = true;

    // formulas de recursos
    this.vidaMax = (this.constitucion * 5) + 50;
    this.vidaActual = this.vidaMax;
    this.manaMax = (this.inteligencia * 5) + 20;
    this.manaActual = this.manaMax;
    this.energiaMax = (this.fuerza * 5) + 20;
    this.energiaActual = this.energiaMax;

  }

  public void mostrarInfo() {
    System.out.println("------------------------------------------");
    System.out.println("PERSONAJE: " + nombre + " [" + tipoClase + " " + raza + "]");
    System.out.println("--- Estadísticas ---");
    System.out.println("FUE: " + fuerza + " | DES: " + destreza +
        " | CON: " + constitucion + " | INT: " + inteligencia);
    System.out.println("--- Barras de Recursos ---");
    System.out.println("Vida: " + vidaActual + "/" + vidaMax);
    System.out.println("Maná: " + manaActual + "/" + manaMax);
    System.out.println("Energía: " + energiaActual + "/" + energiaMax);
    System.out.println("--- Estado de Combate ---");
    System.out.println("Defensa Base: " + defensaBase);
    System.out.println("Arma: " + (armaEquipada != null ? armaEquipada.getNombre() : "Desarmado"));
    System.out.println(" Vivo: " + (vivo ? "SÍ" : "NO"));
    System.out.println("------------------------------------------");
  }
}
