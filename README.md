# 🚗 Sistema de Gestión de Viajes

## 📋 Tabla de Contenidos
- [Descripción](#-descripción)
- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Estructura](#-estructura)
- [Roles](#-roles)
- [Requisitos](#-requisitos)
- [Configuración](#-configuración)
- [Despliegue](#-despliegue)
- [Roadmap](#-roadmap)
- [Contribución](#-contribución)
- [Licencia](#-licencia)
- [Contacto](#-contacto)

## 📝 Descripción
Sistema de gestión de viajes desarrollado con Spring Boot que permite la administración integral de conductores, clientes y órdenes de viaje. El sistema está diseñado para optimizar la gestión de flotas de vehículos y mejorar la experiencia tanto de conductores como de clientes.

## ✨ Características
- 🚗 **Gestión de Conductores**
  - Panel de control personalizado
  - Gestión de disponibilidad en tiempo real
  - Historial de viajes
  - Sistema de calificaciones

- 👥 **Panel de Administración**
  - Gestión completa de usuarios
  - Control de flota de vehículos
  - Reportes y estadísticas
  - Monitoreo en tiempo real

- 📱 **Interfaz de Usuario**
  - Diseño responsive con Bootstrap
  - Navegación intuitiva
  - Notificaciones en tiempo real
  - Dashboard personalizado por rol

- 🔒 **Seguridad**
  - Autenticación multi-rol
  - Autorización basada en permisos
  - Protección contra ataques comunes
  - Encriptación de datos sensibles

- 📊 **Reportes y Análisis**
  - Estadísticas de viajes
  - Rendimiento de conductores
  - Satisfacción de clientes
  - Reportes financieros

## 🛠 Tecnologías
### Backend
- **Spring Boot 3.2.3**: Framework principal
- **Spring Security**: Autenticación y autorización
- **Spring Data JPA**: Persistencia de datos
- **Spring MVC**: Controladores y vistas

### Frontend
- **Thymeleaf**: Motor de plantillas
- **Bootstrap 5**: Framework CSS
- **JavaScript**: Interactividad
- **jQuery**: Utilidades y AJAX

### Base de Datos
- **PostgreSQL 15**: Sistema de gestión de base de datos
- **JPA/Hibernate**: ORM
- **Flyway**: Migraciones de base de datos

### Infraestructura
- **Docker**: Contenedorización
- **Docker Compose**: Orquestación
- **Git**: Control de versiones

## 📁 Estructura
```
src/
├── main/
│   ├── java/
│   │   └── com/transportes/urgentes/gestion_viajes/
│   │       ├── config/         # Configuraciones
│   │       ├── dashboards/     # Controladores de paneles
│   │       ├── drivers/        # Lógica de conductores
│   │       ├── orders/         # Gestión de órdenes
│   │       └── users/          # Gestión de usuarios
│   └── resources/
│       ├── static/            # Recursos estáticos
│       ├── templates/         # Vistas Thymeleaf
│       └── application*.properties # Configuraciones
└── test/                      # Pruebas unitarias
```

## 👥 Roles
### Administrador
- Gestión completa del sistema
- Administración de usuarios
- Generación de reportes
- Configuración del sistema

### Conductor
- Panel de control personal
- Gestión de disponibilidad
- Visualización de órdenes
- Historial de viajes

### Cliente
- Creación de órdenes
- Seguimiento de viajes
- Historial de viajes
- Calificación de servicios

## 📋 Requisitos
- Java 17 o superior
- PostgreSQL 15
- Maven 3.8+
- Docker y Docker Compose (opcional)

## ⚙ Configuración
1. Clonar el repositorio
   ```bash
   git clone https://github.com/tu-usuario/gestion-viajes.git
   ```

2. Configurar la base de datos
   ```bash
   # Crear base de datos
   createdb gestion_viajes
   
   # Configurar variables de entorno
   export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/gestion_viajes
   export SPRING_DATASOURCE_USERNAME=postgres
   export SPRING_DATASOURCE_PASSWORD=postgres
   ```

3. Ejecutar la aplicación
   ```bash
   mvn spring-boot:run
   ```

## 🚀 Despliegue
### Local
```bash
mvn clean package
java -jar target/gestion-viajes.jar
```

### Docker
```bash
# Construir imagen
docker build -t gestion-viajes .

# Ejecutar con Docker Compose
docker-compose up -d
```

## 🗺 Roadmap
### Fase 1 - MVP (Actual)
- [x] Sistema de autenticación
- [x] Gestión básica de usuarios
- [x] Panel de administración
- [x] Gestión de conductores
- [x] Sistema de órdenes básico

### Fase 2 - Mejoras (En Desarrollo)
- [ ] Sistema de notificaciones
- [ ] Integración con mapas
- [ ] Sistema de pagos
- [ ] App móvil para conductores
- [ ] API REST para integraciones

### Fase 3 - Optimización
- [ ] Análisis predictivo
- [ ] Optimización de rutas
- [ ] Sistema de lealtad
- [ ] Integración con otros servicios
- [ ] Dashboard avanzado

### Fase 4 - Escalabilidad
- [ ] Microservicios
- [ ] Caché distribuido
- [ ] Balanceo de carga
- [ ] Monitoreo avanzado
- [ ] Sistema de backup automático

## 🤝 Contribución
1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request
