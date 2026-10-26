# Courier Service - LogiTrack 🚚

Microservicio encargado de la gestión de repartidores para el sistema LogiTrack, desarrollado para el Sprint 2 (Issue #1). Este servicio se encuentra desacoplado de la gestión de envíos para trabajar bajo una arquitectura verdaderamente distribuida.

## 🛠️ Tecnologías
* Java / Spring Boot
* MySQL (vía Docker)
* Spring Data JPA
* Springdoc OpenAPI (Swagger)

## 🚀 Ejecución Local

El proyecto está preparado para recibir configuraciones centralizadas en la nube, pero cuenta con un *fallback* automático. Si el Config Server no está disponible, utilizará las credenciales locales por defecto y su propio contenedor aislado.

**1. Levantar la base de datos (Puerto 3309)**
```bash
docker-compose up -d
```

## Iniciar el microservicio
Ejecutá la clase principal CourrierServiceApplication desde IntelliJ o utilizá Maven:

```bash
./mvnw spring-boot:run
```

## 📖 Pruebas Interactivas (Swagger)
👉 http://localhost:8080/swagger-ui.html
