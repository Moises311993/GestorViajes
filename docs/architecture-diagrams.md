# Diagramas de Arquitectura

## Diagrama de Capas

```mermaid
graph TD
    subgraph "Capa de Presentación"
        A1[AuthController]
        A2[AdminControllers]
        A3[ClientControllers]
        A4[DriverControllers]
    end

    subgraph "Capa de Servicios"
        B1[UserService]
        B2[ConductorService]
        B3[OrderService]
    end

    subgraph "Capa de Persistencia"
        C1[UserRepository]
        C2[ConductorRepository]
        C3[OrderRepository]
    end

    subgraph "Modelo de Datos"
        D1[User]
        D2[Conductor]
        D3[Order]
    end

    A1 --> B1
    A1 --> B2
    A2 --> B3
    A2 --> B2
    A2 --> B1
    A3 --> B3
    A4 --> B3
    A4 --> B2

    B1 --> C1
    B2 --> C2
    B2 --> C3
    B3 --> C3
    B3 --> C1
    B3 --> C2

    C1 --> D1
    C1 --> D2
    C2 --> D2
    C3 --> D3
    C3 --> D1
    C3 --> D2

    D1 --> D2
    D1 --> D3
    D2 --> D3
```

## Diagrama de Controladores

```mermaid
graph TD
    A[AuthController] --> B[UserService]
    A --> C[ConductorService]
    
    D[AdminDashboardController] --> E[OrderService]
    D --> F[ConductorService]
    D --> G[UserService]
    
    H[AdminOrderController] --> E
    H --> F
    
    I[AdminUserController] --> G
    
    J[AdminDriverController] --> F
    
    K[ClientDashboardController] --> E
    K --> L[ClientOrderController]
    
    M[DriverDashboardController] --> E
    M --> F
```

## Diagrama de Servicios

```mermaid
graph TD
    A[UserService] --> B[UserRepository]
    
    C[ConductorService] --> D[ConductorRepository]
    C --> E[OrderRepository]
    
    F[OrderService] --> E
    F --> G[UserRepository]
    F --> D
```

## Diagrama de Repositorios

```mermaid
graph TD
    A[UserRepository] --> B[User]
    A --> C[Conductor]
    
    D[ConductorRepository] --> C
    
    E[OrderRepository] --> F[Order]
    E --> B
    E --> C
```

## Diagrama de Modelos

```mermaid
graph TD
    A[User] --> B[Conductor]
    A --> C[Order]
    
    B --> C
```

## Flujo de una Operación Típica

```mermaid
sequenceDiagram
    participant User
    participant Controller
    participant Service
    participant Repository
    participant Database

    User->>Controller: HTTP Request
    Controller->>Service: Llamada a método
    Service->>Repository: Operación CRUD
    Repository->>Database: Query SQL
    Database-->>Repository: Resultados
    Repository-->>Service: Objetos de dominio
    Service-->>Controller: Resultado procesado
    Controller-->>User: HTTP Response
```

## Herramientas para Visualizar los Diagramas

1. **GitHub**: Los diagramas se renderizarán automáticamente en GitHub cuando veas el archivo Markdown.

2. **Mermaid Live Editor**: 
   - Ve a [Mermaid Live Editor](https://mermaid.live)
   - Copia y pega el código de cualquier diagrama
   - Verás el diagrama renderizado en tiempo real

3. **VS Code**:
   - Instala la extensión "Markdown Preview Mermaid Support"
   - Abre el archivo en VS Code
   - Usa la vista previa de Markdown (Ctrl+Shift+V)

4. **Documentación Local**:
   - Los diagramas se pueden incluir en tu documentación técnica
   - Útiles para explicar la arquitectura a nuevos desarrolladores
   - Ayudan a mantener la consistencia del código 