package habilidad;

import personajes.Personaje;
import estado.EstadoVeneno;
import estado.EstadoAturdimiento;
import estado.EstadoSangrado;
import estado.EstadoLisiado;

// Habilidades físicas: consumen energia, escalan con FUE o DES
// Incluyen efectos como Veneno, Aturdir, Sangrado, Lisiado
public abstract class HabilidadFisica extends AccionCombate {

    protected String efectoEspecial;  // Tipo de efecto secundario

    public HabilidadFisica(String nombre, int costeEnergia, int dadosCantidad, int dadosCaras,
        Estadistica estadistica, String efecto) {
        super(nombre, costeEnergia, 0, dadosCantidad, dadosCaras, estadistica);
        this.efectoEspecial = efecto;
    }

    @Override
    public void ejecutar(Personaje usuario, Personaje objetivo) {
        // Comprueba que tenga recursos suficientes
        if (!usuario.tieneRecursos(costeEnergia, costeMana)) {
            System.out.println(usuario.getNombre() + " no tiene suficientes recursos.");
            return;
        }

        usuario.consumirRecursos(costeEnergia, costeMana);

        // Tirada de impacto: 1d20 + modificador contra la defensa del objetivo
        int tirada = dado.nextInt(20) + 1;
        int bono = getModificador(usuario) / 2;
        int totalImpacto = tirada + bono;

        System.out.println(usuario.getNombre() + " usa " + nombre + "...");
        System.out.println("Tirada de impacto: " + tirada + " + " + bono + " = " + totalImpacto);

        if (totalImpacto >= objetivo.getDefensaTotal()) {
            // Ha golpeado, tira los dados de daño
            int daño = tirarDados() + bono;
            System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño");
            objetivo.recibirDaño(daño, false);

            // Aplica el efecto secundario si tiene
            aplicarEfecto(objetivo);
        } else {
            System.out.println("¡FALLO! El ataque no acierta");
        }
    }

    // Aplica el efecto secundario según el tipo
    protected void aplicarEfecto(Personaje objetivo) {
        if (efectoEspecial == null)
            return;

        switch (efectoEspecial) {
            case "VENENO":
                if (!objetivo.tieneEstado("Veneno")) {
                    objetivo.aplicarEstado(new EstadoVeneno(3, 5));
                    System.out.println("-- ¡" + objetivo.getNombre() + " está envenenado!");
                }
                break;
            case "ATURDIR":
                if (!objetivo.tieneEstado("Aturdimiento")) {
                    objetivo.aplicarEstado(new EstadoAturdimiento(2));
                    System.out.println("-- ¡" + objetivo.getNombre() + " está aturdido!");
                }
                break;
            case "SANGRADO":
                if (!objetivo.tieneEstado("Sangrado")) {
                    objetivo.aplicarEstado(new EstadoSangrado(3, 4));
                    System.out.println("-- ¡" + objetivo.getNombre() + " está sangrando!");
                }
                break;
            case "LISIADO":
                if (!objetivo.tieneEstado("Lisiado")) {
                    objetivo.aplicarEstado(new EstadoLisiado(2, 5));
                    System.out.println("-- ¡" + objetivo.getNombre() + " está lisiado!");
                }
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
