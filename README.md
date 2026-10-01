# CS-002: Registro y Listado de Estudiantes

API REST con Spring Boot, Java 21 y PostgreSQL. Usa arquitectura N-Capas: controlador, servicio y repositorio, con entidad y DTO. Permite registrar estudiantes y consultar el listado desde Postman.

## Requisitos

- JDK 21 o posterior instalado y `JAVA_HOME` configurado.
- PostgreSQL instalado y en ejecución en `localhost:5432`, con el comando `psql` disponible en PowerShell.
- Postman para probar los servicios.

No necesitas instalar Maven ni una interfaz gráfica para PostgreSQL. El proyecto incluye Maven Wrapper y usa `psql`, el cliente de consola que viene con PostgreSQL.

## Preparar la base de datos

En este equipo, la base `estudiantes` y la tabla `students` ya están creadas. Puedes pasar directamente a **Iniciar la aplicación**.

Para preparar la base en otra instalación, abre PowerShell en esta carpeta (`estudiantes`). Si aún no existe la base, créala una sola vez:

```powershell
psql -h localhost -p 5432 -U postgres -W -d postgres -v ON_ERROR_STOP=1 -c "CREATE DATABASE estudiantes;"
```

Después ejecuta el archivo SQL para crear la tabla:

```powershell
psql -h localhost -p 5432 -U postgres -W -d estudiantes -v ON_ERROR_STOP=1 -f .\database\estudiantes.sql
```

`psql` te pedirá la contraseña del usuario `postgres`. El SQL usa `CREATE TABLE IF NOT EXISTS`, así que repetirlo no borra estudiantes registrados. Si `psql` no se reconoce, en una instalación estándar de PostgreSQL 18 en Windows puedes ejecutarlo como `& 'C:\Program Files\PostgreSQL\18\bin\psql.exe'` seguido de los mismos argumentos.

## Iniciar la aplicación

Desde PowerShell, en esta carpeta, ejecuta:

```powershell
.\mvnw.cmd spring-boot:run
```

La API estará en `http://localhost:8080/api/v1/students`. Por defecto se conecta a la base `estudiantes` en `localhost:5432`, con usuario `postgres` y contraseña `root`, como está configurado en este equipo. Mantén la consola abierta mientras haces las pruebas. Si la contraseña de PostgreSQL es distinta, establece `$env:DB_PASSWORD = "tu_contraseña"` antes de iniciar la aplicación.

## Probar con Postman

Importa [postman/estudiantes.postman_collection.json](postman/estudiantes.postman_collection.json). La variable `baseUrl` vale `http://localhost:8080` y puede editarse en la colección.

1. **Listar estudiantes**: `GET {{baseUrl}}/api/v1/students` devuelve `200 OK` y un arreglo; si aún no hay registros, será `[]`.
2. **Registrar estudiante**: `POST {{baseUrl}}/api/v1/students` devuelve `201 Created` con el estudiante y su `id` generado.
3. Ejecuta de nuevo **Listar estudiantes** para consultar todos los registros guardados.

El POST usa `Content-Type: application/json` y este cuerpo:

```json
{
  "full_name": "Ana López",
  "email": "ana.lopez@example.com",
  "enrollment_date": "2026-09-25"
}
```

El `id` se genera en PostgreSQL y no hace falta enviarlo. Para cambiar el nombre en Postman, edita el **Body → raw → JSON de la solicitud** antes de pulsar **Send**. La respuesta de la parte inferior es de solo lectura. Cada ejecución del POST crea un registro nuevo; la colección comprueba el estado HTTP y los campos devueltos.

Los tres campos del POST son obligatorios. Usa un correo válido y una fecha en formato `AAAA-MM-DD`; los datos inválidos devuelven `400 Bad Request`.

Puedes ejecutar las pruebas automáticas del controlador sin conectar la base de datos:

```powershell
.\mvnw.cmd test
```

## Configuración opcional

La aplicación admite las variables `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD` y `SERVER_PORT`. Si necesitas cambiarlas, defínelas en la misma consola de PowerShell antes de iniciar Spring Boot. Por ejemplo, si el puerto `8080` está ocupado:

```powershell
$env:SERVER_PORT = "8081"
.\mvnw.cmd spring-boot:run
```

En ese caso cambia `baseUrl` a `http://localhost:8081` en Postman. El archivo `.env.example` solo muestra valores de ejemplo; Spring Boot lee las variables de entorno de la consola.

## Detener

Presiona `Ctrl+C` en la consola donde ejecutaste Spring Boot. Los estudiantes permanecen guardados en PostgreSQL al iniciar la aplicación de nuevo.
