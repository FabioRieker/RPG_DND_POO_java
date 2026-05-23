package consumibles;

import personajes.Personaje;
import personajes.Raza;
import personajes.TipoClase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConsumibleTest {

    private Personaje heroeDummy;
    private PocionCuracion pocionVida;
    private PocionRecurso pocionRecurso;

    @BeforeEach
    public void setUp() {
        // Instancia un personaje base para evaluar efectos de objetos
        heroeDummy = new Personaje("Explorador", Raza.ELFO, TipoClase.PICARO, 10, 10, 10, 10, 10) {
        };

        // Inicializa pociones con un uso de cada
        pocionVida = new PocionCuracion(1);
        pocionRecurso = new PocionRecurso(1);
    }

    @Test
    public void pocionCuracionCuraAlHeroe() {
        // Disminuye la salud actual para habilitar ventana de curacion
        heroeDummy.recibirDaño(40, true);
        int vidaHerido = heroeDummy.getVidaActual();

        // Consume la pocion para curar al heroe
        pocionVida.usar(heroeDummy, heroeDummy);

        assertTrue(heroeDummy.getVidaActual() > vidaHerido,
                "La poción debería haber aumentado la vida actual del héroe.");
    }

    @Test
    public void pocionCuracionNoSuperaVidaMaxima() {
        int vidaMax = heroeDummy.getVidaMax();

        // Aplica daño minimo (5 puntos)
        heroeDummy.recibirDaño(5, true);

        // Consume objeto de curacion alta (50 puntos) para intentar sobrepasar el
        // maximo
        pocionVida.usar(heroeDummy, heroeDummy);

        assertEquals(vidaMax, heroeDummy.getVidaActual(), "La poción no puede curar por encima de la vida máxima.");
    }

    @Test
    public void pocionRecursoNoSuperaRecursoMaximo() {
        int manaMax = heroeDummy.getManaMax();

        // Incrementa los recursos artificialmente
        heroeDummy.recuperarRecursos(1000);

        // Ejecuta curacion extra (40 puntos)
        pocionRecurso.usar(heroeDummy, heroeDummy);

        assertEquals(manaMax, heroeDummy.getManaActual(), "La poción de recurso no debe rebasar el maná máximo.");
    }
}
