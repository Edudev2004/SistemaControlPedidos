# Sistema de Control de Pedidos y Repartidores

Este es un sistema de escritorio sencillo desarrollado en Java para gestionar pedidos y asignarlos a repartidores. Utiliza una arquitectura MVC básica y se conecta a una base de datos en Supabase.

## Requisitos
- **Java 23** o superior.
- **Maven** (para gestionar las dependencias).
- Una cuenta en **Supabase** (PostgreSQL).

## Instalación y Configuración

### 1. Base de Datos
Antes de correr el programa, debes crear las tablas en tu base de datos de Supabase:
1. Entra a tu proyecto en Supabase.
2. Ve al **SQL Editor**.
3. Copia el contenido del archivo `database.sql` (ubicado en la raíz de este proyecto) y dale a **Run**.

### 2. Configuración del entorno
Crea un archivo llamado `.env` en la raíz del proyecto (puedes guiarte del archivo `.env.template`) y coloca tus credenciales de Supabase:

```env
DB_HOST=tu_host_de_supabase
DB_PORT=5432
DB_NAME=postgres
DB_USER=postgres
DB_PASSWORD=tu_contraseña_segura
```

### 3. Compilar y Ejecutar
Una vez configurado el `.env` y las tablas creadas, puedes ejecutar el proyecto:

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar la clase principal
# (O simplemente dale a Run en tu IDE sobre Main.java)
```

## Credenciales de Acceso
Por defecto, el script de la base de datos crea un usuario administrador:
- **Usuario:** `admin`
- **Contraseña:** `admin123`

## Funcionalidades
- Login con validación en base de datos.
- Registro de nuevos pedidos con descripción y dirección.
- Visualización de pedidos en tiempo real con colores según el estado (Pendiente, En camino, Entregado).
- Asignación de repartidores y actualización de estados.
- Buscador integrado y barra de estado con contadores.
