package motor;

import personajes.*;
import armas.*;
import habilidad.*;
import armaduras.CategoriaArmadura;
import java.util.ArrayList;
import java.util.List;

/**
 * Sirve para crear directamente a los héroes iniciales sin picarlos a mano en
 * el test. Les pone las estadísticas, armas, armaduras y habilidades a cada
 * uno.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class FabricaHeroes {

	private static Armeria armeria = new Armeria();

	public static List<Personaje> crearEquipoInicial() {
		List<Personaje> equipo = new ArrayList<>();

		// --- THORIN (GUERRERO) ---
		Guerrero thorin = new Guerrero("Thorin", Raza.ENANO, 16, 10, 16, 8, 12);
		thorin.equiparArmadura(CategoriaArmadura.PESADA);
		thorin.equiparArma(armeria.get("Hacha"));
		thorin.agregarHabilidad(new GolpeSanguinario());
		thorin.agregarHabilidad(new Rompecraneos());
		thorin.agregarHabilidad(new TajoSismico());
		thorin.agregarHabilidad(new Ejecucion());
		equipo.add(thorin);

		// --- ELARA (MAGO) ---
		Mago elara = new Mago("Elara", Raza.ELFO, 6, 14, 8, 18, 10);
		elara.equiparArmadura(CategoriaArmadura.LIGERA);
		elara.equiparArma(armeria.get("Ballesta"));
		elara.agregarHabilidad(new BolaFuego());
		elara.agregarHabilidad(new RafagaGlacial());
		elara.agregarHabilidad(new Ventisca());
		elara.agregarHabilidad(new ToqueVampirico());
		elara.agregarHabilidad(new Purificacion());
		elara.agregarHabilidad(new RayoEncadenado());
		equipo.add(elara);

		// --- VEX (PICARO) ---
		Picaro vex = new Picaro("Vex", Raza.HUMANO, 10, 16, 10, 12, 14);
		vex.equiparArmadura(CategoriaArmadura.LIGERA);
		vex.equiparArma(armeria.get("Daga"));
		vex.agregarHabilidad(new HojaPonzoñosa());
		vex.agregarHabilidad(new AtaqueFurtivo());
		vex.agregarHabilidad(new TiroRodilla());
		vex.agregarHabilidad(new LluviaFlechas());
		equipo.add(vex);

		// --- MARCUS (PALADIN) ---
		Paladin marcus = new Paladin("Marcus", Raza.HUMANO, 14, 10, 14, 12, 14);
		marcus.equiparArmadura(CategoriaArmadura.PESADA);
		marcus.equiparArma(armeria.get("Espada"));
		marcus.agregarHabilidad(new LuzSagrada());
		marcus.agregarHabilidad(new Purificacion());
		marcus.agregarHabilidad(new GritoGuerra());
		marcus.agregarHabilidad(new MuroPiedra());
		equipo.add(marcus);

		return equipo;
	}

	public static Personaje crearKallista() {
		Brujo kallista = new Brujo("Kallista", Raza.TIEFLING, 8, 14, 14, 18, 14);
		kallista.equiparArmadura(CategoriaArmadura.LIGERA);
		kallista.equiparArma(armeria.get("Ballesta"));
		kallista.agregarHabilidad(new ToqueVampirico());
		kallista.agregarHabilidad(new NubeToxica());
		kallista.agregarHabilidad(new ExplosionArcana());
		return kallista;
	}

	public static Personaje crearMonjeKwai() {
		Monje kwai = new Monje("Kwai Chang", Raza.HUMANO, 14, 16, 12, 10, 12);
		// Requisito de la propia clase Monje: No portan armaduras pesadas ni ligeras
		kwai.agregarHabilidad(new FintaRapida());
		kwai.agregarHabilidad(new GolpeSanguinario());
		return kwai;
	}

	public static Personaje crearBardoLulu() {
		Bardo lulu = new Bardo("Lulu Nightingale", Raza.MEDIANO, 10, 14, 12, 16, 10);
		lulu.equiparArmadura(CategoriaArmadura.MEDIA);
		lulu.equiparArma(armeria.get("Jabalina"));
		lulu.agregarHabilidad(new GritoGuerra());
		lulu.agregarHabilidad(new Purificacion());
		return lulu;
	}
}