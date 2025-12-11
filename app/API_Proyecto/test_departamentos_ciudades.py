"""
Script de prueba para verificar que las tablas departamentos y ciudades existen
y tienen datos relacionados con los usuarios
"""
from app.config.db_config import get_db_connection

def test_tablas():
    try:
        conn = get_db_connection()
        cursor = conn.cursor(dictionary=True)
        
        # Verificar si las tablas existen
        print("=== VERIFICACIÓN DE TABLAS ===")
        cursor.execute("SHOW TABLES LIKE 'departamentos'")
        tabla_dep = cursor.fetchone()
        print(f"Tabla 'departamentos' existe: {tabla_dep is not None}")
        
        cursor.execute("SHOW TABLES LIKE 'ciudades'")
        tabla_ciu = cursor.fetchone()
        print(f"Tabla 'ciudades' existe: {tabla_ciu is not None}")
        
        cursor.execute("SHOW TABLES LIKE 'usuarios'")
        tabla_usu = cursor.fetchone()
        print(f"Tabla 'usuarios' existe: {tabla_usu is not None}")
        
        # Verificar estructura de usuarios
        print("\n=== ESTRUCTURA DE TABLA usuarios ===")
        cursor.execute("DESCRIBE usuarios")
        columnas = cursor.fetchall()
        print("Columnas en usuarios:")
        for col in columnas:
            print(f"  - {col['Field']} ({col['Type']})")
        
        # Verificar si hay usuarios con departamento_id y ciudad_id
        print("\n=== USUARIOS CON DEPARTAMENTO Y CIUDAD ===")
        cursor.execute("""
            SELECT id, nombres, email, departamento_id, ciudad_id 
            FROM usuarios 
            WHERE departamento_id IS NOT NULL OR ciudad_id IS NOT NULL
            LIMIT 5
        """)
        usuarios_con_ubicacion = cursor.fetchall()
        print(f"Usuarios con departamento_id o ciudad_id: {len(usuarios_con_ubicacion)}")
        for u in usuarios_con_ubicacion:
            print(f"  Usuario {u['id']} ({u['email']}): dept_id={u['departamento_id']}, ciudad_id={u['ciudad_id']}")
        
        # Verificar datos en departamentos
        print("\n=== DATOS EN DEPARTAMENTOS ===")
        cursor.execute("SELECT COUNT(*) as total FROM departamentos")
        total_dep = cursor.fetchone()
        print(f"Total departamentos: {total_dep['total']}")
        
        cursor.execute("SELECT id, nombre FROM departamentos LIMIT 5")
        deps = cursor.fetchall()
        print("Primeros 5 departamentos:")
        for d in deps:
            print(f"  ID {d['id']}: {d['nombre']}")
        
        # Verificar datos en ciudades
        print("\n=== DATOS EN CIUDADES ===")
        cursor.execute("SELECT COUNT(*) as total FROM ciudades")
        total_ciu = cursor.fetchone()
        print(f"Total ciudades: {total_ciu['total']}")
        
        cursor.execute("SELECT id, nombre, departamento_id FROM ciudades LIMIT 5")
        ciuds = cursor.fetchall()
        print("Primeras 5 ciudades:")
        for c in ciuds:
            print(f"  ID {c['id']}: {c['nombre']} (dept_id: {c['departamento_id']})")
        
        # Probar JOIN con un usuario específico
        print("\n=== PRUEBA DE JOIN ===")
        cursor.execute("""
            SELECT 
                u.id, u.email, u.departamento_id, u.ciudad_id,
                d.nombre as departamento_nombre,
                c.nombre as ciudad_nombre
            FROM usuarios u
            LEFT JOIN departamentos d ON u.departamento_id = d.id
            LEFT JOIN ciudades c ON u.ciudad_id = c.id
            WHERE u.departamento_id IS NOT NULL OR u.ciudad_id IS NOT NULL
            LIMIT 3
        """)
        usuarios_join = cursor.fetchall()
        print("Usuarios con JOIN:")
        for u in usuarios_join:
            print(f"  Usuario {u['id']} ({u['email']}):")
            print(f"    Departamento ID: {u['departamento_id']}, Nombre: {u['departamento_nombre']}")
            print(f"    Ciudad ID: {u['ciudad_id']}, Nombre: {u['ciudad_nombre']}")
        
        conn.close()
        print("\n=== PRUEBA COMPLETADA ===")
        
    except Exception as e:
        print(f"ERROR: {e}")
        import traceback
        traceback.print_exc()

if __name__ == "__main__":
    test_tablas()

