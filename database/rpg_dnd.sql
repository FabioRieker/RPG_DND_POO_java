-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 21-05-2026 a las 19:05:20
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `rpg_dnd`
--
CREATE DATABASE IF NOT EXISTS `rpg_dnd` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `rpg_dnd`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Armas`
--

DROP TABLE IF EXISTS `Armas`;
CREATE TABLE `Armas` (
  `ID_arma` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `cantidad_dados` int(11) NOT NULL,
  `caras_dado` int(11) NOT NULL,
  `categoria` enum('cuerpo_a_cuerpo','distancia') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Armas`
--

INSERT INTO `Armas` (`ID_arma`, `nombre`, `cantidad_dados`, `caras_dado`, `categoria`) VALUES
(1, 'Daga de Asesino', 1, 10, 'cuerpo_a_cuerpo'),
(2, 'Colmillo de Araña', 2, 6, 'cuerpo_a_cuerpo'),
(3, 'Maza Pesada', 3, 5, 'cuerpo_a_cuerpo'),
(4, 'Arco Corto', 1, 8, 'distancia'),
(5, 'Hacha de Batalla', 2, 8, 'cuerpo_a_cuerpo'),
(6, 'Espada Larga', 2, 7, 'cuerpo_a_cuerpo'),
(7, 'Ballesta Pesada', 3, 6, 'distancia'),
(8, 'Hoja Fénix', 5, 5, 'cuerpo_a_cuerpo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Consumibles`
--

DROP TABLE IF EXISTS `Consumibles`;
CREATE TABLE `Consumibles` (
  `ID_consumible` int(11) NOT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `efecto` varchar(100) NOT NULL,
  `cantidad` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Consumibles`
--

INSERT INTO `Consumibles` (`ID_consumible`, `nombre`, `efecto`, `cantidad`) VALUES
(1, 'Poción de Curación', 'Restaura vitalidad moderada', 1),
(2, 'Poción de Maná', 'Restaura energía mágica', 1),
(3, 'Poción de Energía', 'Restaura aguante físico', 1),
(4, 'Elixir de los Dioses', 'Restaura todos los atributos', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Dificultades`
--

DROP TABLE IF EXISTS `Dificultades`;
CREATE TABLE `Dificultades` (
  `ID_dificultad` int(11) NOT NULL,
  `nivel` enum('facil','normal','dificil') NOT NULL,
  `mult_vida` float NOT NULL DEFAULT 1,
  `mult_dano` float NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Dificultades`
--

INSERT INTO `Dificultades` (`ID_dificultad`, `nivel`, `mult_vida`, `mult_dano`) VALUES
(1, 'facil', 0.6, 0.6),
(2, 'normal', 1, 1),
(3, 'dificil', 1.5, 1.5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Estados`
--

DROP TABLE IF EXISTS `Estados`;
CREATE TABLE `Estados` (
  `ID_estado` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `potencia_base` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Gestor_estados`
--

DROP TABLE IF EXISTS `Gestor_estados`;
CREATE TABLE `Gestor_estados` (
  `ID_estado_gestor` int(11) NOT NULL,
  `id_personaje` int(11) NOT NULL,
  `id_estado` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Gestor_Personajes`
--

DROP TABLE IF EXISTS `Gestor_Personajes`;
CREATE TABLE `Gestor_Personajes` (
  `ID_gestor_personajes` int(11) NOT NULL,
  `id_personaje` int(11) NOT NULL,
  `id_sala` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Historial_Acciones`
--

DROP TABLE IF EXISTS `Historial_Acciones`;
CREATE TABLE `Historial_Acciones` (
  `ID_historial` int(11) NOT NULL,
  `id_partida` int(11) NOT NULL,
  `turno` int(11) NOT NULL DEFAULT 1,
  `descripcion` text NOT NULL,
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Historial_Acciones`
--

INSERT INTO `Historial_Acciones` (`ID_historial`, `id_partida`, `turno`, `descripcion`, `fecha_registro`) VALUES
(1, 9, 4, 'Victoria en combate.', '2026-05-21 16:15:50'),
(2, 9, 2, 'Victoria en combate.', '2026-05-21 16:15:53'),
(3, 9, 5, 'Victoria en combate.', '2026-05-21 16:16:07'),
(4, 9, 6, 'Victoria en combate.', '2026-05-21 16:16:27'),
(5, 9, 6, 'Victoria en combate.', '2026-05-21 16:16:49'),
(6, 9, 4, 'Victoria en combate.', '2026-05-21 16:18:03'),
(7, 9, 7, 'Victoria en combate.', '2026-05-21 16:18:28'),
(8, 9, 6, 'Victoria en combate.', '2026-05-21 16:18:50'),
(9, 9, 4, 'Victoria en combate.', '2026-05-21 16:19:00'),
(10, 9, 4, 'Victoria en combate.', '2026-05-21 16:19:11'),
(11, 9, 6, 'Victoria en combate.', '2026-05-21 16:19:33'),
(12, 9, 7, 'Victoria en combate.', '2026-05-21 16:21:02'),
(13, 10, 4, 'Victoria en combate.', '2026-05-21 16:35:09'),
(14, 10, 3, 'Victoria en combate.', '2026-05-21 16:35:17'),
(15, 10, 4, 'Victoria en combate.', '2026-05-21 16:35:25'),
(16, 10, 6, 'Victoria en combate.', '2026-05-21 16:35:45'),
(17, 10, 8, 'Victoria en combate.', '2026-05-21 16:36:19');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Logros`
--

DROP TABLE IF EXISTS `Logros`;
CREATE TABLE `Logros` (
  `ID_logro` int(11) NOT NULL,
  `codigo` varchar(50) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `requisito` text DEFAULT NULL,
  `puntos` int(11) NOT NULL DEFAULT 0,
  `tipo` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Logros`
--

INSERT INTO `Logros` (`ID_logro`, `codigo`, `nombre`, `descripcion`, `requisito`, `puntos`, `tipo`) VALUES
(1, 'BOSS_KILL', 'Asesino de Gigantes', 'Mata a tu primer Jefe en la mazmorra.', 'Matar un jefe', 500, ''),
(2, 'PRIMER_PASO', 'Primeros Pasos', 'Supera con éxito la Sala 1.', 'Completar sala 1', 50, 'PROGRESO'),
(3, 'CAMPISTA', 'Campista Novato', 'Alcanza la fuente curativa de la Sala 9.', 'Llegar a sala 9', 100, 'EXPLORACION'),
(4, 'NUEVO_ALIADO', 'Un Nuevo Aliado', 'Rescata a Kallista en la Sala 5 e incorpórala a la reserva.', 'Llegar a sala 5', 150, 'HISTORIA'),
(5, 'BIEN_EQUIPADO', 'Bien Equipado', 'Guarda tu primera arma en la mochila común.', 'Añadir arma a la mochila', 200, 'INVENTARIO'),
(6, 'VERDUGO', 'Verdugo de Monstruos', 'Acumula una puntuación superior a 1,000 puntos en una sola partida.', 'Más de 1000 puntos', 300, 'PROGRESO'),
(7, 'MOCHILA_PESADA', 'Mochila Pesada', 'Acumula 5 o más consumibles (pociones) en el inventario del grupo a la vez.', 'Tener 5 pociones', 250, 'INVENTARIO'),
(8, 'BORDE_ABISMO', 'Al Borde del Abismo', 'Sobrevive a un combate con al menos un héroe con 5 puntos de vida o menos.', 'Vida <= 5 al acabar combate', 400, 'COMBATE'),
(9, 'ULTIMO_PIE', 'El Último en Pie', 'Gana un combate con 3 héroes muertos y solo 1 vivo.', '3 muertos y 1 vivo al final', 600, 'COMBATE'),
(10, 'MATADRAGONES', 'Matadragones', 'Derrota al jefe final en la Sala 20 y completa el juego.', 'Ganar sala 20', 1000, 'PROGRESO'),
(11, 'LOCURA', 'Locura Absoluta', 'Completa la mazmorra entera en dificultad \"Difícil\".', 'Ganar en difícil', 1500, 'PROGRESO'),
(12, 'INTOCABLE', 'Intocable', 'Termina un combate sin que ningún héroe haya recibido ni un solo punto de daño.', 'Vidas intactas', 800, 'COMBATE'),
(13, 'MAESTRO_ARMAS', 'Maestro de Armas', 'Cambia el arma de un héroe usando la opción \"Rearmarse\" en un campamento.', 'Equipar arma de mochila', 750, 'INVENTARIO'),
(14, 'COLECCIONISTA', 'Coleccionista de Arsenal', 'Reúne 4 armas distintas en la mochila común en la misma partida.', '4 armas distintas en DB', 500, 'INVENTARIO'),
(15, 'PLATINADO', 'Leyenda del Gremio', 'Consigue todos los demás logros del juego.', 'Obtener los 14 logros', 2000, 'META');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Mochila_Armas`
--

DROP TABLE IF EXISTS `Mochila_Armas`;
CREATE TABLE `Mochila_Armas` (
  `ID_mochila_armas` int(11) NOT NULL,
  `id_partida` int(11) NOT NULL,
  `id_arma` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Mochila_Armas`
--

INSERT INTO `Mochila_Armas` (`ID_mochila_armas`, `id_partida`, `id_arma`, `cantidad`) VALUES
(1, 9, 1, 2),
(3, 9, 2, 2),
(4, 9, 3, 2),
(5, 9, 4, 3),
(7, 9, 6, 1),
(8, 9, 5, 2),
(13, 10, 1, 2),
(15, 10, 3, 1),
(16, 10, 2, 1),
(17, 10, 4, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Mochila_Consumibles`
--

DROP TABLE IF EXISTS `Mochila_Consumibles`;
CREATE TABLE `Mochila_Consumibles` (
  `ID_mochila_consumibles` int(11) NOT NULL,
  `id_partida` int(11) NOT NULL,
  `id_consumible` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Partidas`
--

DROP TABLE IF EXISTS `Partidas`;
CREATE TABLE `Partidas` (
  `ID_partida` int(11) NOT NULL,
  `nombre_partida` varchar(100) NOT NULL,
  `fecha_inicio` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_ultimo_turno` timestamp NULL DEFAULT NULL,
  `estado` varchar(50) NOT NULL DEFAULT 'activa',
  `usuario_id` int(11) NOT NULL,
  `sala_actual` int(11) DEFAULT NULL,
  `puntuacion` int(11) NOT NULL DEFAULT 0,
  `dificultad_id` int(11) NOT NULL DEFAULT 2
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Partidas`
--

INSERT INTO `Partidas` (`ID_partida`, `nombre_partida`, `fecha_inicio`, `fecha_ultimo_turno`, `estado`, `usuario_id`, `sala_actual`, `puntuacion`, `dificultad_id`) VALUES
(1, 'prueba', '2026-05-20 17:09:16', NULL, 'activa', 1, 1, 0, 3),
(2, 'prueba2', '2026-05-20 17:28:43', '2026-05-20 17:33:50', 'activa', 1, 13, 500, 1),
(3, 'TestAutoGame', '2026-05-20 17:57:35', NULL, 'activa', 1, 1, 0, 1),
(4, 'pruebiña', '2026-05-20 18:49:56', '2026-05-20 18:51:28', 'activa', 4, 17, 500, 1),
(5, 'pruebaPruebosa', '2026-05-21 06:53:37', '2026-05-21 06:54:54', 'activa', 5, 12, 500, 1),
(6, 'jiji', '2026-05-21 08:59:29', NULL, 'activa', 7, 16, 0, 1),
(7, 'jij', '2026-05-21 09:06:38', NULL, 'activa', 8, 1, 0, 1),
(8, '1', '2026-05-21 09:24:10', '2026-05-21 09:27:31', 'completada', 10, 20, 1500, 1),
(9, '1', '2026-05-21 16:15:37', '2026-05-21 16:20:40', 'completada', 12, 20, 1500, 1),
(10, '2', '2026-05-21 16:34:56', '2026-05-21 16:36:19', 'activa', 12, 9, 500, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Partida_Logros`
--

DROP TABLE IF EXISTS `Partida_Logros`;
CREATE TABLE `Partida_Logros` (
  `ID_partida_logro` int(11) NOT NULL,
  `partida_id` int(11) NOT NULL,
  `logro_id` int(11) NOT NULL,
  `fecha_desbloqueo` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Partida_Logros`
--

INSERT INTO `Partida_Logros` (`ID_partida_logro`, `partida_id`, `logro_id`, `fecha_desbloqueo`) VALUES
(2, 2, 1, '2026-05-20 17:29:15'),
(4, 4, 1, '2026-05-20 18:50:28'),
(6, 5, 1, '2026-05-21 06:54:09'),
(8, 6, 1, '2026-05-21 08:59:56'),
(10, 8, 1, '2026-05-21 09:24:42'),
(14, 9, 5, '2026-05-21 16:15:50'),
(16, 9, 7, '2026-05-21 16:15:50'),
(17, 9, 2, '2026-05-21 16:15:50'),
(19, 9, 12, '2026-05-21 16:15:53'),
(22, 9, 1, '2026-05-21 16:16:07'),
(24, 9, 4, '2026-05-21 16:16:07'),
(26, 9, 14, '2026-05-21 16:16:49'),
(30, 9, 3, '2026-05-21 16:16:49'),
(43, 9, 6, '2026-05-21 16:19:11'),
(47, 9, 8, '2026-05-21 16:19:33'),
(51, 9, 10, '2026-05-21 16:21:02'),
(53, 10, 5, '2026-05-21 16:35:09'),
(55, 10, 7, '2026-05-21 16:35:09'),
(56, 10, 2, '2026-05-21 16:35:09'),
(59, 10, 1, '2026-05-21 16:35:25'),
(61, 10, 4, '2026-05-21 16:35:25'),
(64, 10, 14, '2026-05-21 16:36:19'),
(69, 10, 3, '2026-05-21 16:36:19');

--
-- Disparadores `Partida_Logros`
--
DROP TRIGGER IF EXISTS `trigger_platinado`;
DELIMITER $$
CREATE TRIGGER `trigger_platinado` AFTER INSERT ON `Partida_Logros` FOR EACH ROW BEGIN
    DECLARE num_logros INT;
    
    
    SELECT COUNT(DISTINCT pl.logro_id) INTO num_logros
    FROM Partida_Logros pl
    JOIN Partidas p ON pl.partida_id = p.ID_partida
    WHERE p.usuario_id = (SELECT usuario_id FROM Partidas WHERE ID_partida = NEW.partida_id)
      AND pl.logro_id != 15;
      
    IF num_logros >= 14 THEN
        
        INSERT IGNORE INTO Partida_Logros (partida_id, logro_id) VALUES (NEW.partida_id, 15);
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Personajes`
--

DROP TABLE IF EXISTS `Personajes`;
CREATE TABLE `Personajes` (
  `ID_personaje` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `tipo_clase` varchar(50) NOT NULL,
  `raza` varchar(50) NOT NULL,
  `fuerza` int(11) NOT NULL DEFAULT 0,
  `destreza` int(11) NOT NULL DEFAULT 0,
  `constitucion` int(11) NOT NULL DEFAULT 0,
  `inteligencia` int(11) NOT NULL DEFAULT 0,
  `vida_max` int(11) NOT NULL DEFAULT 0,
  `mana_max` int(11) NOT NULL DEFAULT 0,
  `energia_max` int(11) NOT NULL DEFAULT 0,
  `defensa_base` int(11) NOT NULL DEFAULT 0,
  `es_monstruo` tinyint(1) DEFAULT 0,
  `ID_arma` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Personajes`
--

INSERT INTO `Personajes` (`ID_personaje`, `nombre`, `tipo_clase`, `raza`, `fuerza`, `destreza`, `constitucion`, `inteligencia`, `vida_max`, `mana_max`, `energia_max`, `defensa_base`, `es_monstruo`, `ID_arma`) VALUES
(1, 'Thorin', 'GUERRERO', '', 16, 10, 16, 8, 73, 39, 63, 12, 0, NULL),
(2, 'Elara', 'MAGO', '', 6, 14, 8, 18, 49, 69, 33, 8, 0, NULL),
(3, 'Vex', 'PICARO', '', 10, 16, 10, 12, 55, 51, 45, 10, 0, NULL),
(4, 'Marcus', 'PALADIN', '', 14, 10, 14, 12, 67, 51, 57, 14, 0, NULL),
(5, 'Kallista', 'BRUJO', '', 8, 14, 14, 18, 67, 69, 39, 10, 0, NULL),
(6, 'Kwai Chang', 'MONJE', '', 14, 16, 12, 10, 60, 30, 50, 12, 0, NULL),
(7, 'Lulu Nightingale', 'BARDO', '', 8, 14, 10, 16, 55, 60, 40, 10, 0, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Salas`
--

DROP TABLE IF EXISTS `Salas`;
CREATE TABLE `Salas` (
  `ID_sala` int(11) NOT NULL,
  `numero` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `tipo` enum('requisito','narrativa','pelea') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Salas`
--

INSERT INTO `Salas` (`ID_sala`, `numero`, `nombre`, `tipo`) VALUES
(1, 1, 'Entrada de la Mazmorra', 'pelea'),
(2, 2, 'Caravana Saqueada', 'narrativa'),
(3, 3, 'Pasillo Oscuro', 'pelea'),
(4, 4, 'Guarida del Gigante', 'pelea'),
(5, 5, 'Celda de Kallista', 'narrativa'),
(6, 6, 'Nido de Arañas', 'pelea'),
(7, 7, 'Pasillo Trampa', 'narrativa'),
(8, 8, 'Campamento Goblin', 'pelea'),
(9, 9, 'Fuente Curativa', 'narrativa'),
(10, 10, 'Cámara del Traidor', 'pelea'),
(11, 11, 'Cuartel Orco', 'pelea'),
(12, 12, 'Santuario del Monje', 'narrativa'),
(13, 13, 'Cripta Olvidada', 'pelea'),
(14, 14, 'Sala Vacía', 'narrativa'),
(15, 15, 'Trono del Rey Obould', 'pelea'),
(16, 16, 'Armería Corrompida', 'pelea'),
(17, 17, 'Prisión del Bardo', 'narrativa'),
(18, 18, 'Antesala del Dragón', 'pelea'),
(19, 19, 'Último Descanso', 'narrativa'),
(20, 20, 'Guarida de Furia', 'pelea');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Situacion_heroe`
--

DROP TABLE IF EXISTS `Situacion_heroe`;
CREATE TABLE `Situacion_heroe` (
  `ID_situacion_heroe` int(11) NOT NULL,
  `id_partida` int(11) NOT NULL,
  `id_personaje` int(11) NOT NULL,
  `id_arma` int(11) DEFAULT NULL,
  `vida_actual` int(11) NOT NULL,
  `mana_actual` int(11) NOT NULL,
  `energia_actual` int(11) NOT NULL,
  `armadura_equipada` varchar(50) DEFAULT 'NADA',
  `en_reserva` tinyint(1) DEFAULT 0,
  `vivo` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Situacion_heroe`
--

INSERT INTO `Situacion_heroe` (`ID_situacion_heroe`, `id_partida`, `id_personaje`, `id_arma`, `vida_actual`, `mana_actual`, `energia_actual`, `armadura_equipada`, `en_reserva`, `vivo`) VALUES
(1, 2, 1, NULL, 73, 29, 3, 'NADA', 0, 1),
(2, 2, 4, NULL, 67, 11, 27, 'NADA', 0, 1),
(3, 4, 1, NULL, 55, 29, 8, 'NADA', 0, 1),
(4, 4, 2, NULL, 67, 39, 9, 'NADA', 0, 1),
(5, 4, 4, NULL, 67, 16, 42, 'NADA', 0, 1),
(6, 5, 1, NULL, 73, 29, 13, 'NADA', 0, 1),
(7, 5, 2, NULL, 67, 39, 14, 'NADA', 0, 1),
(8, 8, 1, NULL, 73, 39, 63, 'NADA', 0, 1),
(9, 8, 2, NULL, 61, 63, 45, 'NADA', 0, 1),
(10, 8, 3, NULL, 61, 45, 57, 'NADA', 0, 1),
(11, 8, 4, NULL, 67, 51, 57, 'NADA', 0, 1),
(12, 9, 1, NULL, 73, 39, 63, 'NADA', 0, 1),
(13, 9, 2, NULL, 67, 69, 39, 'NADA', 0, 1),
(14, 9, 3, NULL, 55, 51, 45, 'NADA', 0, 1),
(15, 9, 4, NULL, 61, 45, 57, 'NADA', 0, 1),
(156, 10, 1, NULL, 60, 29, 3, 'NADA', 0, 1),
(157, 10, 2, NULL, 38, 14, 33, 'NADA', 0, 1),
(158, 10, 3, NULL, 43, 51, 5, 'NADA', 0, 1),
(159, 10, 4, NULL, 23, 1, 42, 'NADA', 0, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Usuarios`
--

DROP TABLE IF EXISTS `Usuarios`;
CREATE TABLE `Usuarios` (
  `ID_usuario` int(11) NOT NULL,
  `nombre_usuario` varchar(50) NOT NULL,
  `contraseña` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `Usuarios`
--

INSERT INTO `Usuarios` (`ID_usuario`, `nombre_usuario`, `contraseña`, `email`) VALUES
(1, 'PruebaFabio', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'prueba'),
(2, 'TestE2EUser', 'd3dec3f35387156495cbc21471313f87155f878f3435b693f50077c2be479033', 'teste2e@test.com'),
(3, 'E2E_TestUser', 'd3dec3f35387156495cbc21471313f87155f878f3435b693f50077c2be479033', 'e2e_test@test.com'),
(4, 'prueba67', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'hola'),
(5, 'pepe ', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', '222'),
(6, 'ffs<f', '1159e52d83bf6469d530c8d407366da107384076effce9811d60068438698361', 'se<f'),
(7, 'jaustur', '5fa99ab263c6a027dc029325b2a4447ae440ac40507039a66fb1841c22dc6265', 'aghfusjgfyvfaejuy@adbvjcyansg.com'),
(8, 'pipu', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', '123'),
(9, 'GERBASIO', 'ESPAÑÑÑÑÑÑÑÑÑÑÑÑÑÑÑA', '22222'),
(10, 'hola', 'b221d9dbb083a7f33428d7c2a3c3198ae925614d70210e28716ccaa7cd4ddb79', 'tuvieja'),
(12, 'prueba78', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'ha');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `Armas`
--
ALTER TABLE `Armas`
  ADD PRIMARY KEY (`ID_arma`);

--
-- Indices de la tabla `Consumibles`
--
ALTER TABLE `Consumibles`
  ADD PRIMARY KEY (`ID_consumible`);

--
-- Indices de la tabla `Dificultades`
--
ALTER TABLE `Dificultades`
  ADD PRIMARY KEY (`ID_dificultad`),
  ADD UNIQUE KEY `nivel` (`nivel`);

--
-- Indices de la tabla `Estados`
--
ALTER TABLE `Estados`
  ADD PRIMARY KEY (`ID_estado`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `Gestor_estados`
--
ALTER TABLE `Gestor_estados`
  ADD PRIMARY KEY (`ID_estado_gestor`),
  ADD UNIQUE KEY `unique_personaje_estado` (`id_personaje`,`id_estado`),
  ADD KEY `id_estado` (`id_estado`);

--
-- Indices de la tabla `Gestor_Personajes`
--
ALTER TABLE `Gestor_Personajes`
  ADD PRIMARY KEY (`ID_gestor_personajes`),
  ADD UNIQUE KEY `unique_sala_personaje` (`id_sala`,`id_personaje`),
  ADD KEY `id_personaje` (`id_personaje`);

--
-- Indices de la tabla `Historial_Acciones`
--
ALTER TABLE `Historial_Acciones`
  ADD PRIMARY KEY (`ID_historial`),
  ADD KEY `id_partida` (`id_partida`);

--
-- Indices de la tabla `Logros`
--
ALTER TABLE `Logros`
  ADD PRIMARY KEY (`ID_logro`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `Mochila_Armas`
--
ALTER TABLE `Mochila_Armas`
  ADD PRIMARY KEY (`ID_mochila_armas`),
  ADD UNIQUE KEY `unique_partida_arma` (`id_partida`,`id_arma`),
  ADD KEY `id_arma` (`id_arma`);

--
-- Indices de la tabla `Mochila_Consumibles`
--
ALTER TABLE `Mochila_Consumibles`
  ADD PRIMARY KEY (`ID_mochila_consumibles`),
  ADD UNIQUE KEY `unique_partida_consumible` (`id_partida`,`id_consumible`),
  ADD KEY `id_consumible` (`id_consumible`);

--
-- Indices de la tabla `Partidas`
--
ALTER TABLE `Partidas`
  ADD PRIMARY KEY (`ID_partida`),
  ADD KEY `sala_actual` (`sala_actual`),
  ADD KEY `dificultad_id` (`dificultad_id`),
  ADD KEY `idx_partidas_usuario` (`usuario_id`),
  ADD KEY `idx_partidas_puntuacion` (`puntuacion`);

--
-- Indices de la tabla `Partida_Logros`
--
ALTER TABLE `Partida_Logros`
  ADD PRIMARY KEY (`ID_partida_logro`),
  ADD UNIQUE KEY `unique_partida_logro` (`partida_id`,`logro_id`),
  ADD KEY `logro_id` (`logro_id`);

--
-- Indices de la tabla `Personajes`
--
ALTER TABLE `Personajes`
  ADD PRIMARY KEY (`ID_personaje`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD KEY `ID_arma` (`ID_arma`);

--
-- Indices de la tabla `Salas`
--
ALTER TABLE `Salas`
  ADD PRIMARY KEY (`ID_sala`);

--
-- Indices de la tabla `Situacion_heroe`
--
ALTER TABLE `Situacion_heroe`
  ADD PRIMARY KEY (`ID_situacion_heroe`),
  ADD UNIQUE KEY `unique_partida_personaje` (`id_partida`,`id_personaje`),
  ADD KEY `id_personaje` (`id_personaje`),
  ADD KEY `id_arma` (`id_arma`);

--
-- Indices de la tabla `Usuarios`
--
ALTER TABLE `Usuarios`
  ADD PRIMARY KEY (`ID_usuario`),
  ADD UNIQUE KEY `nombre_usuario` (`nombre_usuario`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `Armas`
--
ALTER TABLE `Armas`
  MODIFY `ID_arma` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `Consumibles`
--
ALTER TABLE `Consumibles`
  MODIFY `ID_consumible` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `Dificultades`
--
ALTER TABLE `Dificultades`
  MODIFY `ID_dificultad` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `Estados`
--
ALTER TABLE `Estados`
  MODIFY `ID_estado` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `Gestor_estados`
--
ALTER TABLE `Gestor_estados`
  MODIFY `ID_estado_gestor` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `Gestor_Personajes`
--
ALTER TABLE `Gestor_Personajes`
  MODIFY `ID_gestor_personajes` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `Historial_Acciones`
--
ALTER TABLE `Historial_Acciones`
  MODIFY `ID_historial` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `Logros`
--
ALTER TABLE `Logros`
  MODIFY `ID_logro` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `Mochila_Armas`
--
ALTER TABLE `Mochila_Armas`
  MODIFY `ID_mochila_armas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT de la tabla `Mochila_Consumibles`
--
ALTER TABLE `Mochila_Consumibles`
  MODIFY `ID_mochila_consumibles` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `Partidas`
--
ALTER TABLE `Partidas`
  MODIFY `ID_partida` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `Partida_Logros`
--
ALTER TABLE `Partida_Logros`
  MODIFY `ID_partida_logro` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=70;

--
-- AUTO_INCREMENT de la tabla `Personajes`
--
ALTER TABLE `Personajes`
  MODIFY `ID_personaje` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `Salas`
--
ALTER TABLE `Salas`
  MODIFY `ID_sala` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `Situacion_heroe`
--
ALTER TABLE `Situacion_heroe`
  MODIFY `ID_situacion_heroe` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=188;

--
-- AUTO_INCREMENT de la tabla `Usuarios`
--
ALTER TABLE `Usuarios`
  MODIFY `ID_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `Gestor_estados`
--
ALTER TABLE `Gestor_estados`
  ADD CONSTRAINT `Gestor_estados_ibfk_1` FOREIGN KEY (`id_personaje`) REFERENCES `Personajes` (`ID_personaje`) ON DELETE CASCADE,
  ADD CONSTRAINT `Gestor_estados_ibfk_2` FOREIGN KEY (`id_estado`) REFERENCES `Estados` (`ID_estado`) ON DELETE CASCADE;

--
-- Filtros para la tabla `Gestor_Personajes`
--
ALTER TABLE `Gestor_Personajes`
  ADD CONSTRAINT `Gestor_Personajes_ibfk_1` FOREIGN KEY (`id_personaje`) REFERENCES `Personajes` (`ID_personaje`) ON DELETE CASCADE,
  ADD CONSTRAINT `Gestor_Personajes_ibfk_2` FOREIGN KEY (`id_sala`) REFERENCES `Salas` (`ID_sala`) ON DELETE CASCADE;

--
-- Filtros para la tabla `Historial_Acciones`
--
ALTER TABLE `Historial_Acciones`
  ADD CONSTRAINT `Historial_Acciones_ibfk_1` FOREIGN KEY (`id_partida`) REFERENCES `Partidas` (`ID_partida`) ON DELETE CASCADE;

--
-- Filtros para la tabla `Mochila_Armas`
--
ALTER TABLE `Mochila_Armas`
  ADD CONSTRAINT `Mochila_Armas_ibfk_1` FOREIGN KEY (`id_partida`) REFERENCES `Partidas` (`ID_partida`) ON DELETE CASCADE,
  ADD CONSTRAINT `Mochila_Armas_ibfk_2` FOREIGN KEY (`id_arma`) REFERENCES `Armas` (`ID_arma`) ON DELETE CASCADE;

--
-- Filtros para la tabla `Mochila_Consumibles`
--
ALTER TABLE `Mochila_Consumibles`
  ADD CONSTRAINT `Mochila_Consumibles_ibfk_1` FOREIGN KEY (`id_partida`) REFERENCES `Partidas` (`ID_partida`) ON DELETE CASCADE,
  ADD CONSTRAINT `Mochila_Consumibles_ibfk_2` FOREIGN KEY (`id_consumible`) REFERENCES `Consumibles` (`ID_consumible`) ON DELETE CASCADE;

--
-- Filtros para la tabla `Partidas`
--
ALTER TABLE `Partidas`
  ADD CONSTRAINT `Partidas_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `Usuarios` (`ID_usuario`) ON DELETE CASCADE,
  ADD CONSTRAINT `Partidas_ibfk_2` FOREIGN KEY (`sala_actual`) REFERENCES `Salas` (`ID_sala`) ON DELETE SET NULL,
  ADD CONSTRAINT `Partidas_ibfk_3` FOREIGN KEY (`dificultad_id`) REFERENCES `Dificultades` (`ID_dificultad`);

--
-- Filtros para la tabla `Partida_Logros`
--
ALTER TABLE `Partida_Logros`
  ADD CONSTRAINT `Partida_Logros_ibfk_1` FOREIGN KEY (`partida_id`) REFERENCES `Partidas` (`ID_partida`) ON DELETE CASCADE,
  ADD CONSTRAINT `Partida_Logros_ibfk_2` FOREIGN KEY (`logro_id`) REFERENCES `Logros` (`ID_logro`) ON DELETE CASCADE;

--
-- Filtros para la tabla `Personajes`
--
ALTER TABLE `Personajes`
  ADD CONSTRAINT `Personajes_ibfk_1` FOREIGN KEY (`ID_arma`) REFERENCES `Armas` (`ID_arma`) ON DELETE SET NULL;

--
-- Filtros para la tabla `Situacion_heroe`
--
ALTER TABLE `Situacion_heroe`
  ADD CONSTRAINT `Situacion_heroe_ibfk_1` FOREIGN KEY (`id_partida`) REFERENCES `Partidas` (`ID_partida`) ON DELETE CASCADE,
  ADD CONSTRAINT `Situacion_heroe_ibfk_2` FOREIGN KEY (`id_personaje`) REFERENCES `Personajes` (`ID_personaje`) ON DELETE CASCADE,
  ADD CONSTRAINT `Situacion_heroe_ibfk_3` FOREIGN KEY (`id_arma`) REFERENCES `Armas` (`ID_arma`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
