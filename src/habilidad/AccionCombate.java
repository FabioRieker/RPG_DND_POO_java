package habilidad;

import java.util.Random;
import personajes.Personaje;

// Clase base abstracta para todas las habilidades de combate
// Gestiona: recursos (energia/mana), dados de daño y estadísticas que escalan el daño
// Subclases: HabilidadFisica, HechizoMagico, HabilidadHibrida
public abstract class AccionCombate {
    
    protected String nombre;
    protected int costeEnergia;      // SP consumidos (0 si no usa)
    protected int costeMana;         // MP consumidos (0 si no usa)
    protected int dadosCantidad;     // Cuántos dados tirar
    protected int dadosCaras;         // Caras de cada dado
    protected Estadistica estadisticaClave;  // FUE, DES, INT o CON
    protected Random dado = new Random();

    // Estadísticas disponibles para escalar el daño
    public enum Estadistica {
        FUERZA,       // Ataques cuerpo a cuerpo potentes
        DESTREZA,     // Ataques precisos y rápidos
        INTELIGENCIA, // Magia
        CONSTITUCION  // Ataques basados en vitalidad
    }

    public AccionCombate(String nombre, int costeEnergia, int costeMana, int dadosCantidad, int dadosCaras,
        Estadistica estadisticaClave) {
        this.nombre = nombre;
        this.costeEnergia = costeEnergia;
        this.costeMana = costeMana;
        this.dadosCantidad = dadosCantidad;
        this.dadosCaras = dadosCaras;
        this.estadisticaClave = estadisticaClave;
    }

    // Tira XdY y devuelve la suma total
    protected int tirarDados() {
        int total = 0;
        for (int i = 0; i < dadosCantidad; i++) {
            total += dado.nextInt(dadosCaras) + 1;
        }
        return total;
    }

    // Devuelve el valor de la estadística relevante del personaje
    protected int getModificador(Personaje p) {
        switch (estadisticaClave) {
            case FUERZA:
                return p.getFuerza();
            case DESTREZA:
                return p.getDestreza();
            case INTELIGENCIA:
                return p.getInteligencia();
            case CONSTITUCION:
                return p.getConstitucion();
            default:
                return 0;
        }
    }

    // Ejecuta la habilidad (cada subclase lo implementa)
    public abstract void ejecutar(Personaje usuario, Personaje objetivo);

    public String getNombre() {
        return nombre;
    }

    public int getCosteEnergia() {
        return costeEnergia;
    }

    public int getCosteMana() {
        return costeMana;
    }
}
