package habilidad;

// Ataque con veneno
// Daño: 1d6, Coste: 15 PE, Escalado: DES
// Aplica Veneno al objetivo
public class HojaPonzoñosa extends HabilidadFisica {
  public HojaPonzoñosa() {
    super("Hoja Ponzoñosa", 15, 1, 6, Estadistica.DESTREZA, "VENENO");
  }
}
