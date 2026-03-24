package habilidad;

import personajes.Personaje;
import estado.EstadoQuemadura;
import estado.EstadoCongelado;
import estado.EstadoRenovar;

// Hechizos mágicos: consumen mana, escalan con INT
// Incluyen efectos como Quemadura, Congelar, Renovar
public abstract class HechizoMagico extends AccionCombate {

    protected String efectoEspecial;

    public HechizoMagico(String nombre, int costeMana, int dadosCantidad, int dadosCaras, String efecto) {
        super(nombre, 0, costeMana, dadosCantidad, dadosCaras, Estadistica.INTELIGENCIA);
        this.efectoEspecial = efecto;
    }

    @Override
    public void ejecutar(Personaje usuario, Personaje objetivo) {
        if (!usuario.tieneRecursos(costeEnergia, costeMana)) {
            System.out.println(usuario.getNombre() + " no tiene suficientes recursos.");
            return;
        }

        usuario.consumirRecursos(costeEnergia, costeMana);

        // Tirada de impacto mágico
        int tirada = dado.nextInt(20) + 1;
        int bono = usuario.getInteligencia() / 2;
        int totalImpacto = tirada + bono;

        System.out.println(usuario.getNombre() + " lanza " + nombre + "...");
        System.out.println("Tirada de impacto: " + tirada + " + " + bono + " = " + totalImpacto);

        if (totalImpacto >= objetivo.getDefensaTotal()) {
            int daño = tirarDados() + bono;

            // Sinergia: fuego contra veneno = daño doble y limpia el veneno
            if ("QUEMA".equals(efectoEspecial) && objetivo.tieneEstado("Veneno")) {
                daño *= 2;
                System.out.println("¡SINERGIA! Fuego contra Veneno - DAÑO x2");
                System.out.println("-- El veneno se ha limpiado con el fuego.");
            }

            System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
            objetivo.recibirDaño(daño, false);

            aplicarEfecto(objetivo);
        } else {
            System.out.println("¡FALLO! El hechizo no atraviesa la defensa.");
        }
    }

    // Aplica efecto secundario según el tipo
    protected void aplicarEfecto(Personaje objetivo) {
        if (efectoEspecial == null)
            return;

        switch (efectoEspecial) {
            case "QUEMA":
                if (!objetivo.tieneEstado("Quemadura")) {
                    objetivo.aplicarEstado(new EstadoQuemadura(3, 5));
                    System.out.println("-- ¡" + objetivo.getNombre() + " está en llamas!");
                }
                break;
            case "CONGELA":
                if (!objetivo.tieneEstado("Congelado")) {
                    objetivo.aplicarEstado(new EstadoCongelado(2, 3));
                    System.out.println("-- ¡" + objetivo.getNombre() + " está congelado!");
                }
                break;
            case "RENOVAR":
                objetivo.aplicarEstado(new EstadoRenovar(3, 8));
                System.out.println("-- ¡" + objetivo.getNombre() + " recibe efectos regenerativos!");
                break;
            case "MULTI":
                System.out.println("-- ¡El rayo salta a objetivos cercanos!");
                break;
            case "DEFENSA_MAS":
                System.out.println("-- ¡" + objetivo.getNombre() + " recibe +4 de defensa!");
                break;
        }
    }

    // Ejecuta contra varios objetivos a la vez
    public void ejecutar(Personaje usuario, java.util.ArrayList<Personaje> objetivos) {
        for (Personaje objetivo : objetivos) {
            ejecutar(usuario, objetivo);
        }
    }
}
