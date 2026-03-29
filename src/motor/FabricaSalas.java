package motor;

import personajes.*;
import armas.Armeria;

public class FabricaSalas {
	private static Armeria armeria = new Armeria();

	public static Sala generarSala(int numero) {
		Sala sala = new Sala(numero);
		switch (numero) {
		case 1:
			Monstruo g1 = new Monstruo("Droop", Raza.GOBLIN, 8, 12, 8, 6, 8);
			g1.equiparArma(armeria.get("Daga"));
			sala.agregarEnemigo(g1);
			Monstruo g2 = new Monstruo("Snitch", Raza.GOBLIN, 8, 12, 8, 6, 8);
			g2.equiparArma(armeria.get("Daga"));
			sala.agregarEnemigo(g2);
			break;
		case 3:
			Monstruo b1 = new Monstruo("Garra", Raza.BESTIA, 12, 14, 12, 4, 10);
			b1.equiparArma(armeria.get("Colmillo de Araña"));
			sala.agregarEnemigo(b1);
			break;
		case 4:
			JefeGigante gigante = new JefeGigante("Grog el Aplastador", 18, 8, 20, 6, 14);
			gigante.equiparArma(armeria.get("Maza"));
			sala.agregarEnemigo(gigante);
			break;
		case 6:
			Monstruo m1 = new Monstruo("Slasher", Raza.BESTIA, 12, 14, 12, 4, 10);
			m1.equiparArma(armeria.get("Colmillo de Araña"));
			sala.agregarEnemigo(m1);
			Monstruo m2 = new Monstruo("Ripper", Raza.BESTIA, 12, 14, 12, 4, 10);
			m2.equiparArma(armeria.get("Colmillo de Araña"));
			sala.agregarEnemigo(m2);
			break;
		case 8:
			String[] nombresG = { "Splig", "Licktoad", "Vark" };
			for (String n : nombresG) {
				Monstruo g = new Monstruo(n, Raza.GOBLIN, 8, 14, 8, 6, 8);
				g.equiparArma(armeria.get("Arco"));
				sala.agregarEnemigo(g);
			}
			break;
		case 10:
			JefeDemonio demonio = new JefeDemonio("Malphas el Traidor", 16, 14, 16, 14, 14);
			demonio.equiparArma(armeria.get("Espada"));
			sala.agregarEnemigo(demonio);
			break;
		case 11:
			Monstruo o1 = new Monstruo("Brughor", Raza.ORCO, 16, 10, 16, 8, 12);
			o1.equiparArma(armeria.get("Hacha"));
			sala.agregarEnemigo(o1);
			Monstruo o2 = new Monstruo("Klarg", Raza.ORCO, 16, 10, 16, 8, 12);
			o2.equiparArma(armeria.get("Maza"));
			sala.agregarEnemigo(o2);
			break;
		case 13:
			Monstruo z1 = new Monstruo("Zombie Roedor", Raza.NO_MUERTO, 10, 8, 14, 4, 8);
			sala.agregarEnemigo(z1);
			Monstruo s1 = new Monstruo("Esqueleto Arquero", Raza.NO_MUERTO, 10, 16, 10, 6, 10);
			s1.equiparArma(armeria.get("Arco"));
			sala.agregarEnemigo(s1);
			break;
		case 15:
			Monstruo elite = new Monstruo("Rey Obould", Raza.ORCO, 18, 12, 18, 10, 14);
			elite.equiparArma(armeria.get("Hacha"));
			sala.agregarEnemigo(elite);
			break;
		case 16:
			JefeLich lich = new JefeLich("Malakor el Exánime", 10, 12, 16, 20, 12);
			lich.equiparArma(armeria.get("Cetro"));
			sala.agregarEnemigo(lich);
			break;
		case 18:
			Monstruo finalO = new Monstruo("Gromph", Raza.ORCO, 16, 10, 16, 8, 12);
			finalO.equiparArma(armeria.get("Hacha"));
			sala.agregarEnemigo(finalO);
			Monstruo finalB = new Monstruo("Quimera", Raza.BESTIA, 14, 14, 14, 6, 12);
			sala.agregarEnemigo(finalB);
			break;
		case 20:
			JefeDragon dragon = new JefeDragon("Valdrax el Devorador", 20, 10, 22, 15, 16);
			dragon.equiparArma(armeria.get("Garras Reales"));
			sala.agregarEnemigo(dragon);
			break;
		}
		return sala;
	}
}