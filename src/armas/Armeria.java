package armas;

import java.util.HashMap;
import java.util.Map;

public class Armeria {
	private Map<String, Arma> listaArmas = new HashMap<>();

	public Armeria() {
		listaArmas.put("Espada", new ArmaCuerpoACuerpo("Espada Larga", 1, 8));
		listaArmas.put("Hacha", new ArmaCuerpoACuerpo("Hacha de Batalla", 1, 12));
		listaArmas.put("Daga", new ArmaCuerpoACuerpo("Daga de Asesino", 1, 4));
		listaArmas.put("Maza", new ArmaCuerpoACuerpo("Maza Potententosa", 8, 12));

		listaArmas.put("Arco", new ArmaADistancia("Arco Corto", 1, 8));
		listaArmas.put("Ballesta", new ArmaADistancia("Ballesta Pesada", 1, 10));
		listaArmas.put("Jabalina", new ArmaADistancia("Jabalina de Caza", 1, 6));
		listaArmas.put("Cetro", new ArmaADistancia("Cetro de Almas", 2, 10));

		listaArmas.put("Hoja Fénix", new HojaFenix());
		listaArmas.put("Colmillo", new ColmilloArana());
	}

	public Arma get(String nombre) {
		return listaArmas.get(nombre);
	}
}