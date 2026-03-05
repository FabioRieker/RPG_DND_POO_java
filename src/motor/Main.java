package motor;

import armas.Arma;
import armas.ArmaCuerpoACuerpo;
import armas.ArmaADistancia;
import java.util.HashMap;
import java.util.Map;
import personajes.*;

public class Main {
	public static void main(String[] args) {

		Map<String, Arma> armeria = new HashMap<>();

		// TODO: sacar armas del main
		// ARMAS CUERPO A CUERPO
		armeria.put("Espada", new ArmaCuerpoACuerpo("Espada Larga", 1, 8));
		armeria.put("Hacha", new ArmaCuerpoACuerpo("Hacha de Batalla", 1, 12));
		armeria.put("Daga", new ArmaCuerpoACuerpo("Daga de Asesino", 1, 4));
		armeria.put("Maza", new ArmaCuerpoACuerpo("Maza Potententosa", 8, 12));

		// ARMAS A DISTANCIA
		armeria.put("Arco", new ArmaADistancia("Arco Corto", 1, 8));
		armeria.put("Ballesta", new ArmaADistancia("Ballesta Pesada", 1, 10));
		armeria.put("Jabalina", new ArmaADistancia("Jabalina de Caza", 1, 6));

		// ARMAS ESPECIALES (Por ahora hacen daño normal, luego le metemos estados)
		armeria.put("Hoja Fénix", new ArmaCuerpoACuerpo("Hoja Fénix", 1, 8));
		armeria.put("Colmillo", new ArmaCuerpoACuerpo("Colmillo de Araña", 1, 4));

		System.out.println("Iniciando Test del Motor RPG... \n");

		Mago elara = new Mago("Elara", Raza.ELFO, 6, 16, 8, 20, 16);
		Guerrero kaelen = new Guerrero("Kaelen", Raza.ENANO, 18, 10, 18, 6, 16);
		Picaro vex = new Picaro("Vex", Raza.HUMANO, 10, 18, 12, 12, 18);
		Brujo kallista = new Brujo("Kallista", Raza.TIEFLING, 8, 14, 14, 18, 14);
		Paladin cirric = new Paladin("Cirric", Raza.HUMANO, 14, 10, 16, 14, 18);

		kaelen.mostrarInfo();
		elara.mostrarInfo();

		kaelen.equiparArma(armeria.get("Maza"));
		elara.equiparArma(armeria.get("Ballesta"));

//He cambiado bastante el combate de prueba, hay varias cosas q no actuan correctamente, las corrijo mañana
		System.out.println("\n--- COMIENZA EL DUELO DE PRUEBA ---");

		for (int i = 1; i <= 3; i++) {
		    System.out.println("\n======= TURNO " + i + " =======");
		    
		    kaelen.mostrarInfoBreve();
		    elara.mostrarInfoBreve();

		    kaelen.pasarTurnoDeEstados();
		    elara.pasarTurnoDeEstados();

		    if (kaelen.estaVivo() == true && elara.estaVivo() == true) {
		        kaelen.atacar(elara);
		    }

		    if (elara.estaVivo() == true && kaelen.estaVivo() == true) {
		        elara.atacar(kaelen);
		    }

		    System.out.println("\n--- Resumen Turno " + i + " ---");
		    kaelen.mostrarInfoBreve();
		    elara.mostrarInfoBreve();

		    if (kaelen.estaVivo() == false || elara.estaVivo() == false) {
		        break;
		    }
		}
	}
}
