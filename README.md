# Gas El Volcán · Servicio de autenticación

Servicio Spring Boot que valida credenciales con BCrypt y emite JWT firmados para el resto de los servicios de Gas El Volcán. Se ejecuta por separado del backend de negocio y mantiene su propia base de datos.

## Requisitos

- Java 17.
- MySQL activo en `127.0.0.1:3306`.
- Una base `gas_volcan_auth` y una cuenta con permisos sobre esa base.

```sql
CREATE DATABASE gas_volcan_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'gas_volcan_auth_app'@'localhost' IDENTIFIED BY 'CAMBIAR_CLAVE';
GRANT ALL PRIVILEGES ON gas_volcan_auth.* TO 'gas_volcan_auth_app'@'localhost';
FLUSH PRIVILEGES;
```

## Variables de entorno

| Variable | Obligatoria | Uso |
|---|---|---|
| `JWT_SECRET` | Sí | Clave Base64 compartida con el backend de negocio. Nunca se versiona. |
| `AUTH_DB_PASSWORD` | Sí | Contraseña de `gas_volcan_auth_app`. |

El puerto (`8081`), la URL de la base, el usuario, el emisor (`gasvolcan-auth`) y la expiración (1 hora) están fijos en `application.properties`, igual que en la guía.

Genera la clave una vez y usa el mismo valor en Auth y en Negocio:

```powershell
$bytes = New-Object byte[] 32
[Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
```

## Arrancar en Windows

```powershell
$env:JWT_SECRET = "LA_CLAVE_BASE64"
$env:AUTH_DB_PASSWORD = "CAMBIAR_CLAVE"
.\mvnw.cmd spring-boot:run
```

El servicio queda en `http://localhost:8081`. Swagger: `http://localhost:8081/swagger-ui/index.html`.

## Endpoint

| Método | Ruta | Autenticación | Respuesta |
|---|---|---|---|
| POST | `/api/auth/login` | Pública | `200` con `{ token, correo, rol }` o `401` |

Ejemplo:

```json
{
  "correo": "admin@gaselvolcan.cl",
  "password": "Admin123!"
}
```

## Cuentas demo

Se crean al arrancar si no existen. Son cuentas temporales de desarrollo: cámbialas o elimínalas antes de desplegar.

| Rol | Correo | Contraseña por defecto |
|---|---|---|
| ADMIN | `admin@gaselvolcan.cl` | `Admin123!` |
| VENDEDOR | `vendedor@gaselvolcan.cl` | `Vendedor123!` |
| CLIENTE | `cliente@gaselvolcan.cl` | `Cliente123!` |

Las contraseñas se guardan como hash BCrypt. Las claves se pueden cambiar con las propiedades `app.seed.admin-password`, `app.seed.vendedor-password` y `app.seed.cliente-password`.

## Pruebas

```powershell
.\mvnw.cmd test
```

Las pruebas unitarias cubren la emisión del token y el login válido, la contraseña incorrecta, la cuenta deshabilitada y el correo inexistente.

## Integración

El backend de negocio valida el mismo token con el mismo `JWT_SECRET` y el emisor `gasvolcan-auth`. Auth no comparte clases ni base de datos con Negocio: la única relación es el JWT.
