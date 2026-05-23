package motor;

import personajes.Personaje;
import personajes.Raza;
import personajes.TipoClase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MotorCombateTest {

    private Personaje heroe1;
    private Personaje heroe2;
    private Personaje[] grupo;

    @BeforeEach
    public void setUp() {
        // Inicializa un grupo de dos heroes para las pruebas de combate
        heroe1 = new Personaje("Heroe1", Raza.HUMANO, TipoClase.GUERRERO, 10, 10, 10, 10, 10) {};
        heroe2 = new Personaje("Heroe2", Raza.ELFO, TipoClase.MAGO, 10, 10, 10, 10, 10) {};
        
        grupo = new Personaje[]{heroe1, heroe2};
    }

    @Test
    public void hayVivosDevuelveTrueSiAlMenosUnHeroeEstaVivo() {
        // Elimina al primer heroe
        heroe1.setVidaActual(0);
        // heroe2 mantiene su salud inicial
        
        assertTrue(MotorCombate.hayVivos(grupo), "Debería devolver true porque heroe2 sigue vivo.");
    }

    @Test
    public void hayVivosDevuelveFalseSiTodosLosHeroesEstanMuertos() {
        // Reduce la salud de todo el grupo a cero
        heroe1.setVidaActual(0);
        heroe2.setVidaActual(0);
        
        assertFalse(MotorCombate.hayVivos(grupo), "Debería devolver false porque todos están muertos.");
    }

    @Test
    public void hayVivosIgnoraPersonajesNullSiElArrayTieneHuecos() {
        // Crea un arreglo con espacios vacios
        Personaje[] grupoConHueco = new Personaje[]{heroe1, null};
        
        // Verifica que la evaluacion omita el indice nulo sin lanzar NullPointerException
        assertTrue(MotorCombate.hayVivos(grupoConHueco), "Debería devolver true ignorando el hueco null.");
        
        // Elimina al unico heroe valido para corroborar condicion de derrota
        heroe1.setVidaActual(0);
        assertFalse(MotorCombate.hayVivos(grupoConHueco), "Debería devolver false ignorando el null si el único héroe ha muerto.");
    }

    @Test
    public void combateDetectaDerrotaSiTodosMuerenEnElMismoTurno() {
        // Aplica daño letal simultaneo a todos los integrantes
        int danoAreaMasivo = 1000;
        
        heroe1.recibirDaño(danoAreaMasivo, true);
        heroe2.recibirDaño(danoAreaMasivo, true);
        
        assertFalse(MotorCombate.hayVivos(grupo), "El motor debe detectar derrota si mueren todos simultáneamente.");
    }
}
