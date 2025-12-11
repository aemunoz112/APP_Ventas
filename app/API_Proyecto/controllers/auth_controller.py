from fastapi import HTTPException
import mysql.connector
import json
import requests

from app.config.db_config import get_db_connection
from app.controllers.menu_controller import MenuController
from app.controllers.favorito_controller import FavoritoController

# URL de la API de Node.js para departamentos y ciudades
NODE_API_BASE_URL = "http://localhost:3000"


class AuthController:
    
    def _obtener_nombre_departamento(self, departamento_id: int) -> str:
        """Obtiene el nombre del departamento desde la API de Node.js"""
        if not departamento_id:
            return None
        try:
            response = requests.get(f"{NODE_API_BASE_URL}/getDepartamentoById/{departamento_id}", timeout=5)
            if response.status_code == 200:
                data = response.json()
                if data.get("data") and len(data["data"]) > 0:
                    return data["data"][0].get("nombre")
        except Exception as e:
            print(f"Error obteniendo departamento desde Node API: {e}")
        return None
    
    def _obtener_nombre_ciudad(self, ciudad_id: int) -> str:
        """Obtiene el nombre de la ciudad desde la API de Node.js"""
        if not ciudad_id:
            return None
        try:
            response = requests.get(f"{NODE_API_BASE_URL}/getCiudadById/{ciudad_id}", timeout=5)
            if response.status_code == 200:
                data = response.json()
                if data.get("data") and len(data["data"]) > 0:
                    return data["data"][0].get("nombre")
        except Exception as e:
            print(f"Error obteniendo ciudad desde Node API: {e}")
        return None

    def login(self, credenciales):
        try:
            conn = get_db_connection()
            cursor = conn.cursor()

            # Buscar usuario activo con email y contraseña
            cursor.execute(
                """
                SELECT id, nombres, apellidos, email, telefono, cedula, 
                       rol_id, estado, departamento_id, ciudad_id
                FROM usuarios
                WHERE email = %s AND contrasena = %s AND estado = 'Activo'
                """,
                (credenciales.email, credenciales.password)
            )
            usuario = cursor.fetchone()

            if not usuario:
                raise HTTPException(status_code=401, detail="Credenciales inválidas")

            departamento_id = int(usuario[8]) if usuario[8] is not None else None
            ciudad_id = int(usuario[9]) if usuario[9] is not None else None
            
            # Obtener nombres desde la API de Node.js
            departamento_nombre = self._obtener_nombre_departamento(departamento_id) if departamento_id else None
            ciudad_nombre = self._obtener_nombre_ciudad(ciudad_id) if ciudad_id else None

            # Asegurar que los IDs se incluyan incluso si son None
            usuario_data = {
                "id": int(usuario[0]),
                "nombres": usuario[1],
                "apellidos": usuario[2],
                "email": usuario[3],
                "telefono": usuario[4] if usuario[4] else None,
                "cedula": usuario[5] if usuario[5] else None,
                "rol_id": int(usuario[6]),
                "estado": usuario[7],
                "departamento_id": departamento_id,  # Incluir incluso si es None
                "ciudad_id": ciudad_id,  # Incluir incluso si es None
                "departamento_nombre": departamento_nombre,  # Incluir incluso si es None
                "ciudad_nombre": ciudad_nombre  # Incluir incluso si es None
            }
            
            # Debug: imprimir los valores obtenidos
            print(f"DEBUG Login - Usuario ID: {usuario_data['id']}")
            print(f"DEBUG Login - Departamento ID: {departamento_id}, Nombre: {departamento_nombre}")
            print(f"DEBUG Login - Ciudad ID: {ciudad_id}, Nombre: {ciudad_nombre}")
            print(f"DEBUG Login - usuario_data completo: {usuario_data}")

            # Información del rol
            cursor.execute(
                """
                SELECT id, nombre, descripcion, estado
                FROM roles
                WHERE id = %s
                """,
                (usuario_data["rol_id"],)
            )
            rol = cursor.fetchone()

            if not rol:
                raise HTTPException(status_code=500, detail="El rol asignado no existe")

            rol_data = {
                "id": int(rol[0]),
                "nombre": rol[1],
                "descripcion": rol[2],
                "estado": rol[3]
            }

            menu_controller = MenuController()
            favorito_controller = FavoritoController()
            try:
                menu_tree = menu_controller.get_menu_tree_by_role(rol_data["id"])
            except HTTPException as menu_error:
                # Si la tabla aún no existe o no hay configuración, permitimos el ingreso sin menú
                print(f"Advertencia al obtener menú para el rol {rol_data['id']}: {menu_error.detail}")
                menu_tree = []

            favoritos = favorito_controller.listar_favoritos(usuario_data["id"], rol_data["id"])

            # Asegurar que los campos None se incluyan en la respuesta JSON
            response_data = {
                "usuario": usuario_data,
                "rol": rol_data,
                "menu": menu_tree,
                "modulos": menu_tree,
                "favoritos": [favorito.dict() for favorito in favoritos]
            }
            
            # Debug: imprimir la respuesta completa
            import json
            print(f"DEBUG Login - Respuesta completa: {json.dumps(response_data, default=str, indent=2)}")
            
            return response_data

        except mysql.connector.Error as err:
            print(f"Error en autenticación (MySQL): {err}")
            raise HTTPException(status_code=500, detail="Error al autenticar usuario")
        except Exception as err:
            print(f"Error inesperado en autenticación: {err}")
            raise HTTPException(status_code=500, detail="Error inesperado al autenticar")

        finally:
            conn.close()

