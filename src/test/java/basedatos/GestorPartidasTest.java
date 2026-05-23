package basedatos;

import basedatos.gestores.GestorPartidas;
import basedatos.gestores.GestorUsuarios;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorPartidasTest {

    private GestorPartidas gestorPartidas;
    private GestorUsuarios gestorUsuarios;
    private int idUsuarioFalso = -1;
    private String nombrePartidaUnico;

    @BeforeEach
    public void setUp() {
        gestorPartidas = new GestorPartidas();
        gestorUsuarios = new GestorUsuarios();

        // Genera partidas unicas usando la hora
        String nombreUser = "TestUserPartida_" + System.currentTimeMillis();
        nombrePartidaUnico = "PartidaTest_" + System.currentTimeMillis();

        // Genera usuario ficticio obligatorio para asociarle la partida
        idUsuarioFalso = gestorUsuarios.registrarUsuario(nombreUser, "1234", nombreUser + "@test.com");
    }

    @Test
    public void crearPartidaDevuelveIdValido() {
        if (idUsuarioFalso == -1) {
            fail("No se pudo crear usuario previo. Comprueba si MySQL / XAMPP está encendido.");
            return;
        }

        // Asigna Dificultad Normal (ID 1)
        int idPartida = gestorPartidas.crearNuevaPartida(nombrePartidaUnico, idUsuarioFalso, 1);

        assertTrue(idPartida > 0, "El ID de la nueva partida debería ser un número positivo válido.");
    }

    @Test
    public void guardarPartidaActualizaSalaActual() {
        if (idUsuarioFalso == -1) {
            fail("No se pudo crear usuario previo. Comprueba si MySQL / XAMPP está encendido.");
            return;
        }

        // Crea la partida
        int idPartida = gestorPartidas.crearNuevaPartida(nombrePartidaUnico, idUsuarioFalso, 1);
        assertTrue(idPartida > 0, "La partida debe crearse correctamente.");

        // Simula guardar la partida tras avanzar hasta la sala 5 con 500 puntos
        int salaAvanzada = 5;
        int puntos = 500;
        int idHeroeDummy = 1; // Id del primer héroe (Guerrero)

        // Ejecuta query multiple de guardado (Partida + Situacion Heroe)
        boolean exitoGuardado = gestorPartidas.guardarPartidaCompleta(idPartida, salaAvanzada, puntos, idHeroeDummy, 50,
                20, 20);

        assertTrue(exitoGuardado,
                "El método guardarPartidaCompleta debería devolver true si ha actualizado los datos.");
    }
}
