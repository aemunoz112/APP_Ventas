package co.edu.anders.proyectoventas.data.sample

import co.edu.anders.proyectoventas.data.models.DetallePedido
import co.edu.anders.proyectoventas.data.models.DimensionesProducto
import co.edu.anders.proyectoventas.data.models.Inventario
import co.edu.anders.proyectoventas.data.models.Pedido
import co.edu.anders.proyectoventas.data.models.Producto
import co.edu.anders.proyectoventas.data.models.Rol
import co.edu.anders.proyectoventas.data.models.Usuario

object SampleData {
    val productos = listOf(
        Producto(
            id = 1,
            codigoProducto = "BAC-1045-20",
            nombreProducto = "Barra de Acero AISI 1045 Ø20mm",
            descripcion = "Barra redonda de acero al carbono AISI 1045",
            categoria = "Barra Redonda",
            unidadMedida = "m",
            estado = "Activo",
            dimensiones = DimensionesProducto(1, 1, null, null, null, 20.0),
            inventario = Inventario(1, 1, "LOTE-2025-001", 250.0)
        ),
        Producto(
            id = 2,
            codigoProducto = "BAC-304-25",
            nombreProducto = "Barra de Acero Inoxidable 304 Ø25mm",
            descripcion = "Barra redonda inoxidable AISI 304",
            categoria = "Barra Redonda",
            unidadMedida = "m",
            estado = "Activo",
            dimensiones = DimensionesProducto(2, 2, null, null, null, 25.0),
            inventario = Inventario(2, 2, "LOTE-2025-002", 180.0)
        ),
        Producto(
            id = 8,
            codigoProducto = "TUB-304-50x2",
            nombreProducto = "Tubo Inoxidable 304 Ø50mm x 2mm",
            descripcion = "Tubo redondo inoxidable AISI 304",
            categoria = "Tubería",
            unidadMedida = "m",
            estado = "Activo",
            dimensiones = DimensionesProducto(8, 8, null, 2.0, 46.0, 50.0),
            inventario = Inventario(8, 8, "LOTE-2025-008", 400.0)
        ),
        Producto(
            id = 13,
            codigoProducto = "LAM-HR-3mm",
            nombreProducto = "Lámina Acero HR 3mm",
            descripcion = "Lámina laminada en caliente 3mm",
            categoria = "Lámina",
            unidadMedida = "m²",
            estado = "Activo",
            dimensiones = DimensionesProducto(13, 13, 1220.0, 3.0, null, null),
            inventario = Inventario(13, 13, "LOTE-2025-013", 500.0)
        ),
        Producto(
            id = 19,
            codigoProducto = "ANG-50x50x5",
            nombreProducto = "Ángulo Estructural 50x50x5mm",
            descripcion = "Ángulo de acero estructural A36",
            categoria = "Perfil Angular",
            unidadMedida = "m",
            estado = "Activo",
            dimensiones = DimensionesProducto(19, 19, 50.0, 5.0, null, null),
            inventario = Inventario(19, 19, "LOTE-2025-019", 350.0)
        ),
        Producto(
            id = 20,
            codigoProducto = "IPN-200",
            nombreProducto = "Viga IPN 200",
            descripcion = "Perfil I normal europeo 200mm",
            categoria = "Viga I",
            unidadMedida = "m",
            estado = "Activo",
            dimensiones = DimensionesProducto(20, 20, 200.0, 8.10, null, null),
            inventario = Inventario(20, 20, "LOTE-2025-020", 180.0)
        )
    )

    val pedidos = listOf(
        Pedido(
            id = 1,
            tipoPedido = "Nacional",
            idCliente = 26,
            idVendedor = 20,
            moneda = "COP",
            trm = 4050.0,
            ocCliente = "OC-2024-001",
            condicionPago = "Crédito 30 días",
            fechaCreacion = "2024-01-15"
        ),
        Pedido(
            id = 2,
            tipoPedido = "Nacional",
            idCliente = 27,
            idVendedor = 21,
            moneda = "COP",
            trm = 4050.0,
            ocCliente = "OC-2024-002",
            condicionPago = "Contado",
            fechaCreacion = "2024-01-22"
        ),
        Pedido(
            id = 3,
            tipoPedido = "Exportación",
            idCliente = 28,
            idVendedor = 22,
            moneda = "USD",
            trm = 4050.0,
            ocCliente = "OC-2024-003",
            condicionPago = "Crédito 60 días",
            fechaCreacion = "2024-01-29"
        )
    )

    val usuarioActual = Usuario(
        id = 68,
        nombres = "Anders Enrique",
        apellidos = "Muñoz Pua",
        email = "aemunoz@unibarranquilla.edu.co",
        telefono = "3045390596",
        cedula = "1043129348",
        rolId = 5,
        estado = "Activo",
        rol = Rol(5, "Comercial", "", "Activo")
    )
}

