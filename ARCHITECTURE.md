# Arquitectura de la Aplicación

## Componentes Principales

### Backend
- **Servidor de Aplicación**: Spring Boot (Java 17)
- **Base de Datos**: PostgreSQL
- **ORM**: Hibernate/JPA
- **Autenticación**: Spring Security
- **API REST**: Spring MVC

### Frontend
- **Templates**: Thymeleaf
- **Estilos**: CSS
- **JavaScript**: Vanilla JS

## Estructura del Proyecto

```
gestion-viajes/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/transportes/urgentes/gestion_viajes/
│   │   │       ├── auth/           # Autenticación y autorización
│   │   │       ├── controllers/    # Controladores MVC
│   │   │       ├── models/         # Entidades JPA
│   │   │       ├── repositories/   # Repositorios JPA
│   │   │       ├── services/       # Lógica de negocio
│   │   │       └── config/         # Configuraciones
│   │   └── resources/
│   │       ├── static/            # Archivos estáticos
│   │       └── templates/         # Plantillas Thymeleaf
│   └── test/                      # Pruebas unitarias
├── pom.xml                        # Configuración Maven
└── render.yaml                    # Configuración Render
```

## Infraestructura de Despliegue

### Producción
- **Plataforma**: Render
- **Base de Datos**: PostgreSQL (Render)
- **Servidor Web**: Spring Boot Embedded Tomcat
- **CI/CD**: GitHub Actions

### Desarrollo
- **IDE**: VS Code
- **Base de Datos Local**: PostgreSQL
- **Herramientas de Desarrollo**: Maven, Git

## Seguridad
- Autenticación basada en roles (ADMIN, DRIVER, CLIENT)
- HTTPS obligatorio
- Protección contra CSRF
- Validación de entrada de datos
- Sanitización de salida

## Escalabilidad
- Arquitectura monolítica con separación clara de responsabilidades
- Posibilidad de migrar a microservicios en el futuro
- Base de datos relacional para consistencia de datos

## Monitoreo
- Spring Boot Actuator para métricas
- Logging estructurado
- Alertas de errores

## Limitaciones Actuales
- No incluye sistema de pagos
- No incluye notificaciones en tiempo real
- No incluye geolocalización en tiempo real
- No incluye sistema de calificaciones 