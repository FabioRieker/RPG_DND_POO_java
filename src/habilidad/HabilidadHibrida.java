package habilidad;

import personajes.Personaje;
import java.util.ArrayList;

// Habilidades que usan tanto energia como mana
// Útiles para clases híbridas como el Paladín
public class HabilidadHibrida extends AccionCombate {

    private Efecto efecto;

    public HabilidadHibrida(String nombre, int costeEnergia, int costeMana, int dadosCantidad, int dadosCaras,
        Estadistica estadistica, Efecto efecto) {
        super(nombre, costeEnergia, costeMana, dadosCantidad, dadosCaras, estadistica);
        this.efecto = efecto;
    }

    @Override
    public void ejecutar(Personaje usuario, Personaje objetivo) {
        if (!usuario.tieneRecursos(costeEnergia, costeMana)) {
            System.out.println(usuario.getNombre() + " no tiene suficientes recursos.");
            return;
        }

        usuario.consumirRecursos(costeEnergia, costeMana);

        int tirada = dado.nextInt(20) + 1;
        int bono = getModificador(usuario) / 2;
        int totalImpacto = tirada + bono;

        System.out.println(usuario.getNombre() + " usa " + nombre + "...");
        System.out.println("Coste: " + costeEnergia + " SP + " + costeMana + " MP");
        System.out.println("Tirada: " + tirada + " + " + bono + " = " + totalImpacto);

        if (totalImpacto >= objetivo.getDefensaTotal()) {
            int daño = tirarDados() + bono;
            System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
            objetivo.recibirDaño(daño, false);
            aplicarEfecto(usuario, objetivo, daño);
        } else {
            System.out.println("¡FALLO! El ataque no atraviesa la defensa.");
        }
    }

    // Aplica efecto especial después de impactar
    private void aplicarEfecto(Personaje usuario, Personaje objetivo, int daño) {
        switch (efecto) {
            case CURAR_VIDA:
                usuario.curar(daño);
                System.out.println("-- " + usuario.getNombre() + " se cura " + daño + " HP!");
                break;
            case ROBO_VIDA:
                usuario.curar(daño);
                System.out.println("-- " + usuario.getNombre() + " roba " + daño + " HP!");
                break;
            case NINGUNO:
            default:
                break;
        }
    }

    public void ejecutar(Personaje usuario, ArrayList<Personaje> objetivos) {
        for (Personaje objetivo : objetivos) {
            ejecutar(usuario, objetivo);
        }
    }
}
