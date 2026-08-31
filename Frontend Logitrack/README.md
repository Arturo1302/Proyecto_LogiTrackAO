
# LogiTrack — Sistema de Gestión y Auditoría de Bodegas

Backend REST desarrollado en Spring Boot para LogiTrack S.A., que centraliza el control de inventario entre bodegas: movimientos (entradas, salidas, transferencias), auditoría automática de cambios, autenticación JWT y reportes.

## Tecnologías

- **Java 17** · **Spring Boot 4.1.1** (Spring Framework 7)
- **Spring Data JPA** + **Hibernate**
- **MySQL 8**
- **Spring Security + JWT** (JJWT 0.12.6)
- **Bean Validation** (Jakarta Validation)
- **springdoc-openapi** (Swagger UI) 3.1.0
- **Lombok**
- **Maven**
- Frontend: **HTML/CSS/JS puro** (sin frameworks)

## Arquitectura

```
src/main/java/.../
 ├─ auth/           → AuthController, LoginRequest, RegisterRequest, LoginResponse
 ├─ config/         → JwtService, SecurityConfig, OpenApiConfig
 ├─ controller/     → Controladores REST (@RestController)
 ├─ dto/
 │   ├─ request/    → DTOs de entrada, con validaciones (@NotBlank, @Min, etc.)
 │   └─ response/   → DTOs de salida (nunca exponen entidades JPA directamente)
 ├─ exception/      → GlobalExceptionHandler, ResourceNotFoundException, BusinessRuleException, ErrorResponse
 ├─ mapper/         → Conversión Entidad ↔ DTO (dtoToEntity / entityToDto)
 ├─ model/          → Entidades JPA
 ├─ repository/     → Interfaces JpaRepository + Query Methods + @Query (JPQL)
 ├─ security/       → JwtAuthenticationFilter, UsuarioDetailsServiceImpl
 └─ service/        → Interfaz + ServiceImpl (lógica de negocio)
```

**Patrón por entidad:** `Request` → `Mapper.dtoToEntity` → `Service` (lógica + `Repository`) → `Mapper.entityToDto` → `Response` → `Controller` (arma el `ResponseEntity`).

## Modelo de datos

`roles`, `usuarios`, `categorias`, `bodegas`, `productos`, `bodega_producto` (stock, clave compuesta), `movimientos`, `detalle_movimiento`, `auditorias`.

- El **stock** se actualiza automáticamente vía trigger SQL (`trg_actualizar_stock_movimiento`) al insertar en `detalle_movimiento`.
- La **auditoría** (INSERT/UPDATE/DELETE) se registra automáticamente vía triggers SQL en `productos`, `bodegas`, `usuarios` y `movimientos` — no hay lógica de auditoría en Java.

## Configuración y ejecución

### 1. Base de datos

Ejecuta el script `schema.sql` (crea `logitrack_db`, tablas y triggers) y luego los `INSERT` de datos de prueba.

### 2. `application.properties`

```properties
spring.application.name=Proyecto_LogiTrackAO

spring.datasource.url=jdbc:mysql://localhost:3306/logitrack_db
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD:tu_password_local}

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true

jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B59
jwt.expiration=86400000
```

> En producción, `jwt.secret` y la contraseña de la BD deben ir por variable de entorno, nunca en el repositorio.

### 3. Ejecutar

Desde IntelliJ: ▶ sobre `ProyectoLogiTrackAoApplication`. Levanta en `http://localhost:8080` con Tomcat embebido.

## Autenticación

```
POST /auth/register   { "username", "email", "password", "rolNombre" }  → { "token" }
POST /auth/login       { "username", "password" }                        → { "token" }
```

Rutas protegidas requieren header `Authorization: Bearer <token>`. Roles: `ADMIN`, `EMPLEADO` (actualmente sin diferenciación de permisos entre ellos — cualquier autenticado puede hacer cualquier operación).

## Endpoints principales

| Recurso | Base | Notas |
|---|---|---|
| Roles | `/roles` | CRUD |
| Categorías | `/categorias` | CRUD |
| Usuarios | `/usuarios` | CRUD, password encriptado con BCrypt |
| Bodegas | `/bodegas` | CRUD |
| Productos | `/productos` | CRUD |
| Stock | `/bodega-producto` | CRUD + `GET /stock-bajo?umbral=10` |
| Movimientos | `/movimientos` | Solo crear/consultar (histórico inmutable) + `GET /por-fecha?inicio&fin` |
| Auditorías | `/auditorias` | Solo lectura + `/por-usuario`, `/por-operacion` |
| Reportes | `/reportes/general` | Stock total por bodega + top 5 productos más movidos |

Documentación interactiva: `http://localhost:8080/swagger-ui/index.html` (botón **Authorize** para probar con JWT).

## Manejo de errores

`GlobalExceptionHandler` centraliza las respuestas: `404` (`ResourceNotFoundException`), `400` (`BusinessRuleException`, validaciones `@Valid`, JSON mal formado), `401` (credenciales inválidas), `403` (sin permiso), `500` (no controlado). Formato uniforme:

```json
{ "timestamp": "...", "status": 404, "message": "...", "errorCode": "RESOURCE_NOT_FOUND" }
```

## Frontend

`src/main/resources/static/` — `index.html` (login) y `dashboard.html` (CRUD de todas las entidades + reporte), consumiendo la API vía `fetch` con el token guardado en `localStorage`. Se sirve directo desde Spring Boot en `http://localhost:8080/index.html`.

## Reglas de negocio destacadas

- Un movimiento `SALIDA`/`TRANSFERENCIA` se **rechaza** si no hay stock suficiente (no permite stock negativo).
- `ENTRADA` requiere bodega destino; `SALIDA` requiere bodega origen; `TRANSFERENCIA` requiere ambas.
- Los movimientos no se editan ni eliminan vía API (registro histórico).