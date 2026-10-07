# Taller API

API REST para la gestión de un taller de vehículos: clientes, sus vehículos y las
órdenes de trabajo (reparaciones) asociadas a cada vehículo.

Proyecto pensado como pieza de portfolio, cubriendo el stack que más se pide
habitualmente en ofertas de backend Java: **Spring Boot, Spring Security + JWT,
Spring Data JPA / Hibernate, validación, DTOs, manejo global de errores,
documentación OpenAPI/Swagger, tests (JUnit + Mockito) y Docker.**

## Stack técnico

| Capa | Tecnología |
|---|---|
| Lenguaje / build | Java 17, Maven |
| Framework | Spring Boot 3.3 |
| Seguridad | Spring Security, JWT (jjwt) |
| Persistencia | Spring Data JPA, Hibernate, PostgreSQL |
| Validación | Bean Validation (jakarta.validation) |
| Documentación | springdoc-openapi (Swagger UI) |
| Tests | JUnit 5, Mockito, MockMvc, AssertJ |
| Despliegue | Docker, Docker Compose |

## Arquitectura

Arquitectura en capas clásica:

```
Controller  -->  Service (interfaz + impl)  -->  Repository  -->  Base de datos
    ^                    |
    |                    v
   DTO  <-----------  Mapper  <----------------  Entity
```

- **Controller**: recibe la petición HTTP, valida el DTO de entrada (`@Valid`) y
  delega en el service. No contiene lógica de negocio.
- **Service**: la lógica de negocio real (reglas, transiciones de estado,
  comprobaciones). Se define como interfaz + implementación para poder
  sustituir la implementación en los tests con mocks (inversión de dependencias).
- **Repository**: acceso a datos vía Spring Data JPA. Sin SQL manual salvo que
  haga falta una consulta muy específica.
- **Mapper**: convierte entidad <-> DTO. Las entidades JPA **nunca** salen del
  service hacia el controlador ni al JSON de respuesta — así se evita acoplar
  el modelo de base de datos al contrato público de la API, y se evitan bucles
  infinitos de serialización en las relaciones bidireccionales.
- **Exception handling centralizado**: un único `@RestControllerAdvice`
  (`GlobalExceptionHandler`) traduce cada excepción a un código HTTP y a un
  cuerpo de error consistente, en vez de repetir try/catch en cada endpoint.

## Modelo de dominio

- **Usuario**: quien usa el sistema (empleado/admin). Implementa `UserDetails`
  directamente, así se integra con Spring Security sin una clase adaptadora extra.
- **Cliente**: dueño de uno o varios vehículos.
- **Vehículo**: pertenece a un cliente, tiene varias órdenes de trabajo a lo largo del tiempo.
- **OrdenTrabajo**: una reparación/revisión concreta de un vehículo, con un
  estado que avanza por una máquina de estados simple:

```
RECIBIDA -> EN_DIAGNOSTICO -> EN_REPARACION -> FINALIZADA -> ENTREGADA
                    \-> ESPERANDO_PIEZAS -> EN_REPARACION
```

No se permite saltar estados (p. ej. de `RECIBIDA` a `ENTREGADA` directamente);
esto se valida en `OrdenTrabajoServiceImpl`.

## Seguridad

- Autenticación **stateless** con JWT: el servidor no guarda sesión, cada
  petición se autentica con el token que manda el cliente en el header
  `Authorization: Bearer <token>`.
- Contraseñas hasheadas con **BCrypt** (nunca en texto plano).
- `JwtAuthenticationFilter` intercepta cada petición, valida el token y
  registra al usuario autenticado en el `SecurityContext`.
- Autorización a nivel de método con `@PreAuthorize` (p. ej. solo `ADMIN`
  puede borrar clientes).
- Endpoints públicos: `/api/auth/**` (login/registro) y la documentación
  Swagger. El resto requiere JWT válido.

## Puesta en marcha

### Con Docker (recomendado, todo incluido)

```bash
docker compose up --build
```

Esto levanta PostgreSQL y la API. La API queda escuchando en `http://localhost:8080`.

### En local (sin Docker)

Requiere tener PostgreSQL corriendo y crear la base de datos:

```sql
CREATE DATABASE taller_db;
CREATE USER taller_user WITH PASSWORD 'taller_pass';
GRANT ALL PRIVILEGES ON DATABASE taller_db TO taller_user;
```

```bash
mvn spring-boot:run
```

### Usuario administrador por defecto

Al arrancar por primera vez (base de datos vacía), se crea automáticamente:

- **email**: `admin@taller.com`
- **password**: `Admin1234`

### Documentación interactiva (Swagger)

Con la app arrancada: `http://localhost:8080/docs`

Desde ahí puedes hacer login en `/api/auth/login`, copiar el `accessToken` de
la respuesta, pulsar el botón **Authorize** (arriba a la derecha) y pegarlo
para poder probar el resto de endpoints protegidos directamente desde el navegador.

## Endpoints principales

| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| POST | `/api/auth/registro` | Registrar un usuario | Pública |
| POST | `/api/auth/login` | Login, devuelve JWT | Pública |
| POST | `/api/clientes` | Crear cliente | JWT |
| GET | `/api/clientes?nombre=&page=&size=` | Listar/buscar clientes (paginado) | JWT |
| GET | `/api/clientes/{id}` | Obtener un cliente | JWT |
| PUT | `/api/clientes/{id}` | Editar cliente | JWT |
| DELETE | `/api/clientes/{id}` | Borrar cliente | JWT + rol ADMIN |
| POST | `/api/vehiculos` | Registrar vehículo | JWT |
| GET | `/api/vehiculos?clienteId=` | Listar vehículos (por cliente, paginado) | JWT |
| POST | `/api/ordenes` | Abrir una orden de trabajo | JWT |
| PATCH | `/api/ordenes/{id}/estado` | Cambiar el estado de una orden | JWT |

Todos los listados admiten paginación estándar de Spring (`?page=0&size=10&sort=nombre,asc`).

## Tests

```bash
mvn test
```

Incluye:
- Tests unitarios de servicio con **Mockito** (sin contexto de Spring, rápidos).
- Tests de integración de la capa web con **`@WebMvcTest` + MockMvc**, cubriendo
  también los casos de validación (400) y falta de autenticación (401).

## Posibles próximos pasos

Ideas para seguir ampliando el proyecto (no implementadas todavía):
- Migraciones versionadas con Flyway en vez de `ddl-auto: update`.
- Refresh token endpoint (`/api/auth/refresh`) para renovar el access token.
- Tests de integración con Testcontainers (PostgreSQL real en el test).
- Paginación con DTOs propios en vez de exponer directamente `Page` de Spring.
