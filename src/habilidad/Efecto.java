package habilidad;

// Estados adicionales aplicables por habilidades híbridas
public enum Efecto {
	NINGUNO, // Ningún efecto secundario
	CURAR_VIDA, // Restaura puntos de vida basándose en el daño infligido
	ROBO_VIDA, // Funcionalmente idéntico a Curar Vida, pero temáticamente oscuro
	BUFF_ALIADOS // Otorga beneficios temporales a objetivos aliados
}
