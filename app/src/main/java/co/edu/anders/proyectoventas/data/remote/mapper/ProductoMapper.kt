package co.edu.anders.proyectoventas.data.remote.mapper

import co.edu.anders.proyectoventas.data.models.*
import co.edu.anders.proyectoventas.data.remote.dto.*

/**
 * Mappers para convertir DTOs de la API a modelos de dominio
 */

fun ProductoDto.toDomain(): Producto {
    return Producto(
        id = this.id,
        codigoProducto = this.codigoProducto,
        nombreProducto = this.nombreProducto,
        descripcion = this.descripcion,
        categoria = this.categoria,
        unidadMedida = this.unidadMedida,
        estado = this.estado,
        dimensiones = this.dimensiones?.firstOrNull()?.toDomain(), // Tomar la primera dimensión si existe
        inventario = this.inventario?.toDomain()
    )
}

fun DimensionesDto.toDomain(): DimensionesProducto {
    return DimensionesProducto(
        id = this.id,
        idProducto = this.idProducto,
        ancho = this.ancho,
        espesor = this.espesor,
        diametroInterno = this.diametroInterno,
        diametroExterno = this.diametroExterno
    )
}

fun InventarioDto.toDomain(): Inventario {
    return Inventario(
        id = this.id,
        idProducto = this.idProducto,
        lote = this.lote,
        cantidadDisponible = this.cantidadDisponible
    )
}

fun PedidoDto.toDomain(): Pedido {
    return Pedido(
        id = this.id,
        tipoPedido = this.tipoPedido,
        idCliente = this.idCliente,
        idVendedor = this.idVendedor,
        moneda = this.moneda,
        trm = this.trm,
        ocCliente = this.ocCliente,
        condicionPago = this.condicionPago,
        direccion = this.direccion,
        departamentoId = this.departamentoId,
        ciudadId = this.ciudadId,
        detalles = emptyList(), // Se cargan por separado
        fechaCreacion = this.fechaCreacion
    )
}

fun DetallePedidoDto.toDomain(): DetallePedido {
    return DetallePedido(
        id = this.id,
        idPedido = this.idPedido,
        idProducto = this.idProducto,
        numeroLinea = this.numeroLinea,
        cantidadSolicitada = this.cantidadSolicitada,
        cantidadConfirmada = this.cantidadConfirmada,
        precioUnitario = this.precioUnitario,
        precioTotal = this.precioTotal,
        producto = this.producto?.toDomain()
    )
}

fun UsuarioDto.toDomain(): Usuario {
    return Usuario(
        id = this.id,
        nombres = this.nombres,
        apellidos = this.apellidos,
        email = this.email,
        telefono = this.telefono,
        cedula = this.cedula,
        rolId = this.rolId,
        estado = this.estado,
        departamentoNombre = this.departamentoNombre,
        ciudadNombre = this.ciudadNombre,
        rol = this.rol?.toDomain()
    )
}

fun RolDto.toDomain(): Rol {
    return Rol(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        estado = this.estado
    )
}

