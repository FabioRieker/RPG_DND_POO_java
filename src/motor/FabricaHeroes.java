package motor;

import personajes.*;
import armas.*;
import habilidad.*;
import armaduras.CategoriaArmadura;
import java.util.ArrayList;
import java.util.List;

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
		equipo.add(thorin);

		// --- ELARA (MAGO) ---
		Mago elara = new Mago("Elara", Raza.ELFO, 6, 14, 8, 18, 10);
		elara.equiparArmadura(CategoriaArmadura.LIGERA);
		elara.equiparArma(armeria.get("Ballesta"));
		elara.agregarHabilidad(new BolaFuego());
		elara.agregarHabilidad(new RafagaGlacial());
		elara.agregarHabilidad(new Ventisca());
		equipo.add(elara);

		// --- VEX (PICARO) ---
		Picaro vex = new Picaro("Vex", Raza.HUMANO, 10, 16, 10, 12, 14);
		vex.equiparArmadura(CategoriaArmadura.LIGERA);
		vex.equiparArma(armeria.get("Daga"));
		vex.agregarHabilidad(new HojaPonzoñosa());
		vex.agregarHabilidad(new AtaqueFurtivo());
		vex.agregarHabilidad(new TiroRodilla());
		equipo.add(vex);

		// --- MARCUS (PALADIN) ---
		Paladin marcus = new Paladin("Marcus", Raza.HUMANO, 14, 10, 14, 12, 14);
		marcus.equiparArmadura(CategoriaArmadura.PESADA);
		marcus.equiparArma(armeria.get("Espada"));
		marcus.agregarHabilidad(new LuzSagrada());
		marcus.agregarHabilidad(new Purificacion());
		marcus.agregarHabilidad(new GritoGuerra());
		equipo.add(marcus);

		// escalable, se pueden añadir más que se unan durante la aventura
		return equipo;
	}
}