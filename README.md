# RPG_DND_POO_java

Proyecto RPG por turnos desarrollado en Java con persistencia en MySQL.

## Requisitos

- Java 17 o superior.
- Eclipse IDE.
- MySQL mediante XAMPP o servidor local.
- Base de datos importada desde `database/rpg_dnd.sql`.

## Configuración de base de datos

El proyecto se conecta desde:

`src/main/java/basedatos/conexion/ConexionBD.java`

Antes de ejecutar el juego, importa el archivo:

`database/rpg_dnd.sql`

desde phpMyAdmin.

Configuración usada por el proyecto:

- Base de datos: `rpg_dnd`
- Host: revisar `ConexionBD.java`
- Puerto: revisar `ConexionBD.java`
- Usuario: revisar `ConexionBD.java`
- Contraseña: revisar `ConexionBD.java`

### Nota sobre el puerto de MySQL/XAMPP

En algunos equipos XAMPP usa el puerto por defecto `3306`, mientras que el proyecto está configurado para el puerto 3307 en `ConexionBD.java`. Si aparece un error de conexión como `Communications link failure`, comprueba que el puerto de MySQL/XAMPP coincide con el puerto indicado en `ConexionBD.java`.

Se recomienda adaptar la configuración local de MySQL/XAMPP al puerto del proyecto, o cambiar el puerto en `ConexionBD.java` solo de forma local y con cuidado de no subir ese cambio si el equipo está usando otra configuración.

## Ejecución

Ejecutar la clase:

`motor.Main`

desde Eclipse.

## Tests

Los tests JUnit se encuentran en:

`src/test/java`

Se ejecutan desde Eclipse con:

`Run As -> JUnit Test`

## Funcionalidades principales

- Login y registro de usuarios.
- Guardado y carga de partidas.
- Guardado automático/manual.
- Dificultad configurable.
- Ranking global por puntuación.
- Sistema de logros.
- Visualización de datos con XChart.
- Tests JUnit.