# Core Package

Este paquete contiene los componentes fundamentales y reutilizables de la aplicación.

## Estructura

### `constants/`
Contiene todas las constantes de la aplicación:
- `AppConstants.kt`: Constantes generales de la app (formato de fechas, límites, configuración UI, etc.)

### `util/`
Contiene utilidades y extensiones:
- `Extensions.kt`: Extensiones de Kotlin para tipos comunes (String, Double, Int)
- `Formatters.kt`: Utilidades para formatear datos (moneda, fechas, etc.)

## Uso

### Constantes
```kotlin
import co.edu.anders.proyectoventas.core.constants.AppConstants

val padding = AppConstants.DEFAULT_PADDING.dp
```

### Extensiones
```kotlin
import co.edu.anders.proyectoventas.core.util.toCurrency
import co.edu.anders.proyectoventas.core.util.getInitials

val price = 45000.0.toCurrency() // "$45.000,00"
val initials = "Juan Pérez".getInitials() // "JP"
```

### Formatters
```kotlin
import co.edu.anders.proyectoventas.core.util.Formatters

val formatted = Formatters.formatCurrency(45000.0)
```

