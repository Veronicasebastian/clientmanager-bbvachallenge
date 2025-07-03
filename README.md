# ClientManager

## Descripción

Este proyecto es una API REST desarrollada con Java + Spring Boot + Maven + JPA
para la gestión de clientes y sus productos bancarios, solicitad a modo de challenge para proceso de seleccion BBVA.

Incluye:

- Arquitectura limpia con capas Controller, Service, Repository, Config, Exception y Security. Uso de Entitys y DTOs.
- Persistencia implementada con Spring Data JPA.
- Seguridad JWT.
- Base de datos en memoria H2.
- Documentación de endpoints con Swagger.
- Inicialización de datos: carga la base de productos bancarios con el contenido del enum de productos bancarios para el challenge.
- Control de errores y manejo de excepciones centralizado.
- Documentación JavaDoc agregada para la mayoría de las clases y métodos, facilitando la comprensión y mantenimiento del código.

---

## Requisitos

- Java 17   
- Maven 3.x 
- IDE (IntelliJ, Eclipse, VSCode) opcional

---

## Ejecución local

1. Descomprimir el proyecto (en este challenge se entrega en un .zip).
2. Navegar hasta la carpeta raíz del proyecto.
3. Ejecutar: mvn clean install
4. Para levantar el proyecto: mvn spring-boot:run (o run ClientManagerApplication)

---

## Base de datos H2

- La consola H2 estará disponible en: http://localhost:8080/h2-console
- Usar las credenciales configuradas en application.yml

---

## Inicialización de datos
-  Al iniciar la aplicación, la base de datos se inicializa con los productos bancarios definidos en el enum TipoProductoBancario, 
asegurando datos disponibles para prueba de funcionalidades en este challenge.

---

## Documentación Swagger
- Acceder a Swagger UI en: http://localhost:8080/swagger-ui/index.html
- Desde allí se pueden probar todos los endpoints, incluyendo AuthController para generar el token JWT 
y luego invocar métodos protegidos agregando el token en Authorize.

---

## Endpoints implementados

- POST /clients
Crea un nuevo cliente.

- GET /clients
Obtiene el listado de todos los clientes.

- GET /clients/{id}
Busca un cliente por su ID.

- GET /clients/producto/{tipoProductoBancario}
Busca clientes que posean un producto bancario determinado.

- DELETE /clients/{id}
Elimina un cliente por su ID.

- PUT /clients/{id}
Actualiza completamente un cliente.

- PATCH /clients/{id}
Realiza actualización parcial de un cliente (nombre, apellido, dirección, contacto, tipo de documento, productos).

- PATCH /clients/{id}/telefono
Actualiza solo el teléfono de un cliente.

- POST /auth/login
Genera el token JWT para autenticarse en la API.

---

## Persistencia con JPA

- Se utilizó Spring Data JPA para la gestión de entidades, consultas y operaciones sobre la base de datos.

---

## Tests Unitarios
- Ejecutar con: mvn test
- Faltan algunos tests unitarios por implementar. Se priorizó la cobertura de métodos de negocio principales.

---

## SonarQube
- No se pudo ejecutar SonarQube debido a problemas de performance en mi máquina local. Estaba planificado como extra.

---

## Manejo de errores
- Se implementó un manejo centralizado de errores y excepciones, con @ControllerAdvice, devolviendo respuestas controladas ante 
errores de negocio, validaciones y parámetros inválidos.

---

## Posibles mejoras futuras

- Agregar seguridad a nivel de método usando anotaciones como @PreAuthorize.
- Completar cobertura de tests unitarios e integración.
- Configurar SonarQube para mejorar calidad de código.

---

## Estructura entregada
- En el zip se incluye:
  src/
  pom.xml
  README.md

Se excluyen carpetas de build automáticas (target/, .idea/, .mvn/) y archivos de entorno.