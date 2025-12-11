# 👥 Usuarios de Prueba - Asistente Virtual de Ventas

## 🔑 Credenciales de Acceso

### 1. 👨‍💼 Administrador - Ciro Durán
```
📧 Email: caduran@unibarranquilla.edu.co
🔐 Contraseña: Ciro1234
🏷️ Rol: Administrador (ID: 1)
```

**Permisos:**
- ✅ Acceso completo a todos los módulos
- ✅ Dashboard
- ✅ Ventas (crear, editar, eliminar)
- ✅ Reportes
- ✅ Productos (gestión completa)
- ✅ Usuarios (gestión completa)
- ✅ Roles y permisos
- ✅ Configuración del sistema
- ✅ Módulos

---

### 2. 💼 Comercial (Vendedor) - Anders Muñoz
```
📧 Email: aemunoz@unibarranquilla.edu.co
🔐 Contraseña: anders.2025*
🏷️ Rol: Comercial (ID: 5)
```

**Permisos:**
- ✅ Dashboard (solo lectura)
- ✅ Ventas (crear, editar, eliminar)
- ✅ Productos (ver)
- ✅ Asistente virtual
- ✅ Mis pedidos
- ✅ Mi perfil
- ❌ Sin acceso a usuarios, roles ni configuración

**Casos de uso:**
- Crear pedidos para clientes
- Consultar inventario
- Ver historial de ventas
- Usar asistente virtual para consultas

---

### 3. 📊 Auditor - Esteban Jinete
```
📧 Email: edjinete@unibarranquilla.edu.co
🔐 Contraseña: Esteban.2025*
🏷️ Rol: Auditor (ID: 3)
```

**Permisos:**
- ✅ Dashboard (solo lectura)
- ✅ Ventas (solo lectura)
- ✅ Reportes (solo lectura)
- ✅ Productos (solo lectura)
- ✅ Usuarios (solo lectura)
- ✅ Roles (solo lectura)
- ✅ Configuración (solo lectura)
- ✅ Módulos (solo lectura)
- ❌ No puede crear, editar ni eliminar nada

**Casos de uso:**
- Revisar reportes de ventas
- Auditar transacciones
- Verificar inventario
- Consultar información del sistema

---

### 4. 🛍️ Cliente - Karla De la hoz
```
📧 Email: kdelahoz@unibarranquilla.edu.co
🔐 Contraseña: Karla.2025*
🏷️ Rol: Cliente (ID: 4)
```

**Permisos:**
- ❌ Sin acceso a la aplicación móvil/web
- ℹ️ Solo para identificación en pedidos

**Nota:** Este usuario está registrado en la base de datos para asociar pedidos, pero no tiene permisos para acceder a la aplicación.

---

## 🧪 Casos de Prueba

### ✅ Login Exitoso
1. Abrir la app
2. Ingresar email y contraseña de cualquier usuario válido
3. Presionar "Iniciar Sesión"
4. **Resultado esperado:** Navegar al Dashboard con mensaje "¡Bienvenido!"

### ❌ Login Fallido
1. Abrir la app
2. Ingresar email válido con contraseña incorrecta
3. Presionar "Iniciar Sesión"
4. **Resultado esperado:** Mensaje de error "Email o contraseña incorrectos"

### 🔒 Sesión Persistente
1. Hacer login exitoso
2. Cerrar completamente la app
3. Volver a abrir la app
4. **Resultado esperado:** Ir directamente al Dashboard (sin pantalla de login)

### 🚪 Cerrar Sesión
1. Estando en el Dashboard
2. Ir a "Mi Perfil"
3. Presionar "Cerrar Sesión"
4. **Resultado esperado:** Volver a la pantalla de Login

---

## 📊 Resumen de Roles

| Rol | Acceso Dashboard | Crear Ventas | Ver Reportes | Gestión Usuarios | Configuración |
|-----|-----------------|--------------|--------------|------------------|---------------|
| **Administrador** | ✅ Total | ✅ Sí | ✅ Sí | ✅ Sí | ✅ Sí |
| **Comercial** | ✅ Lectura | ✅ Sí | ❌ No | ❌ No | ❌ No |
| **Auditor** | ✅ Lectura | ❌ No | ✅ Sí | ✅ Lectura | ✅ Lectura |
| **Cliente** | ❌ No | ❌ No | ❌ No | ❌ No | ❌ No |

---

## 🗄️ Datos en Base de Datos

```sql
-- Tabla usuarios
INSERT INTO usuarios VALUES 
(15, 'Ciro Andres', 'Durán Morelo', 'caduran@unibarranquilla.edu.co', '3176911418', '123456789', 'Ciro1234', 1, 'Activo'),
(69, 'Anders Enrique', 'Muñoz Pua', 'aemunoz@unibarranquilla.edu.co', '3176911418', '1043129348', 'anders.2025*', 5, 'Activo'),
(81, 'Esteban David', 'Jinete Castro', 'edjinete@unibarranquilla.edu.co', '3025479632', '1043257415', 'Esteban.2025*', 3, 'Activo'),
(80, 'Karla Maria', 'De la hoz Cera', 'kdelahoz@unibarranquilla.edu.co', '3014588907', '1378901235', 'Karla.2025*', 4, 'Activo');

-- Tabla roles
INSERT INTO roles VALUES 
(1, 'Administrador', 'Admin del sistema', 'Activo'),
(3, 'Auditor', 'Auditor con permisos de solo lectura en todos los módulos', 'Activo'),
(4, 'Cliente', 'Cliente sin acceso a la plataforma, solo para identificación', 'Activo'),
(5, 'Comercial', 'Vendedor con permisos completos en el módulo de ventas', 'Activo');
```

---

## 🔧 Configuración del Backend

Asegúrate de que tu backend FastAPI esté corriendo en:

```bash
# Desarrollo local
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

**URLs de conexión:**
- Emulador Android: `http://10.0.2.2:8000`
- Dispositivo físico: `http://TU_IP_LOCAL:8000`

Verifica la URL en `RetrofitClient.kt`:

```kotlin
private const val BASE_URL = "http://10.0.2.2:8000/"  // Para emulador
// private const val BASE_URL = "http://192.168.1.100:8000/"  // Para dispositivo
```

---

## 📱 Próximos Pasos

Una vez que los usuarios inicien sesión, puedes:

1. **Visualizar el rol del usuario** en ProfileScreen
2. **Mostrar/ocultar opciones** según permisos
3. **Gestionar pedidos** según el rol
4. **Consultar el asistente virtual**
5. **Ver reportes** (solo Administrador y Auditor)

---

¿Necesitas más usuarios de prueba o modificar permisos? ¡Házmelo saber! 🚀

