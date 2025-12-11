package co.edu.anders.proyectoventas.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Sistema de colores empresarial
 * Paleta profesional para aplicación B2B de productos de acero
 */

// Colores Primarios - Azul Corporativo Moderno
val PrimaryBlue = Color(0xFF1E88E5)  // Azul corporativo vibrante y moderno
val PrimaryBlueLight = Color(0xFF42A5F5)  // Más claro para hover/estados activos
val PrimaryBlueDark = Color(0xFF1565C0)   // Más oscuro para profundidad
val PrimaryBlueVariant = Color(0xFF0D47A1) // Variante más oscura

val SecondaryBlue = Color(0xFF0277BD)  // Azul secundario más sobrio
val SecondaryBlueLight = Color(0xFF039BE5)
val SecondaryBlueDark = Color(0xFF01579B)

// Colores de acento modernos
val AccentBlue = Color(0xFF2196F3)      // Azul de acento brillante
val AccentBlueLight = Color(0xFF64B5F6)  // Versión clara
val AccentBlueDark = Color(0xFF1976D2)   // Versión oscura

// Colores Legacy (mantener compatibilidad)
val SteelBlue = PrimaryBlueDark
val SteelBlueLight = PrimaryBlue
val SteelBlueDark = PrimaryBlueDark

val IndustrialOrange = PrimaryBlue
val IndustrialOrangeLight = PrimaryBlueLight
val IndustrialOrangeDark = PrimaryBlueDark

// Colores Secundarios
val Silver = Color(0xFFBDC3C7)
val SilverLight = Color(0xFFECF0F1)
val SilverDark = Color(0xFF95A5A6)

val Charcoal = Color(0xFF34495E)
val CharcoalLight = Color(0xFF5D6D7E)
val CharcoalDark = Color(0xFF1B2631)

// Colores de Estado
val Success = Color(0xFF27AE60)
val SuccessLight = Color(0xFF2ECC71)
val SuccessDark = Color(0xFF1E8449)

val Error = Color(0xFFE74C3C)
val ErrorLight = Color(0xFFEC7063)
val ErrorDark = Color(0xFFC0392B)

val Warning = Color(0xFFF39C12)
val WarningLight = Color(0xFFF7DC6F)
val WarningDark = Color(0xFFD68910)

val Info = Color(0xFF3498DB)
val InfoLight = Color(0xFF5DADE2)
val InfoDark = Color(0xFF2874A6)

// Colores de Fondo - Mejorados para mejor contraste
val BackgroundLight = Color(0xFFFAFBFC)  // Fondo principal más suave
val BackgroundDark = Color(0xFF1C1C1E)

val SurfaceLight = Color(0xFFFFFFFF)      // Superficie principal
val SurfaceDark = Color(0xFF2C2C2E)

val SurfaceVariantLight = Color(0xFFF8F9FA)  // Variante más suave
val SurfaceVariantDark = Color(0xFF3A3A3C)

// Nuevos colores de superficie con mejor jerarquía visual
val SurfaceElevated = Color(0xFFFFFFFF)     // Superficie elevada (cards)
val SurfaceElevatedDark = Color(0xFF2C2C2E)
val SurfaceSubtle = Color(0xFFF5F7FA)        // Superficie sutil para secciones
val SurfaceSubtleDark = Color(0xFF252528)

// Colores de Texto
val TextPrimaryLight = Color(0xFF212121)
val TextPrimaryDark = Color(0xFFE5E5E7)

val TextSecondaryLight = Color(0xFF757575)
val TextSecondaryDark = Color(0xFF98989D)

val TextDisabledLight = Color(0xFFBDBDBD)
val TextDisabledDark = Color(0xFF636366)

// Colores de Borde - Mejorados para mejor definición
val BorderLight = Color(0xFFE8EAED)  // Bordes más suaves y modernos
val BorderDark = Color(0xFF38383A)
val BorderHover = Color(0xFFDADCE0)   // Borde en hover
val BorderFocus = PrimaryBlue.copy(alpha = 0.5f)  // Borde cuando está enfocado

val DividerLight = Color(0xFFE8EAED)  // Divisores más sutiles
val DividerDark = Color(0xFF38383A)
val DividerSubtle = Color(0xFFF1F3F4)  // Divisor muy sutil

// Colores de Chat/Asistente
val ChatUserBubble = PrimaryBlue
val ChatAssistantBubble = Color(0xFFECF0F1)
val ChatUserText = Color(0xFFFFFFFF)
val ChatAssistantText = Color(0xFF212121)

// Colores de Producto
val ProductCardBackground = Color(0xFFFFFFFF)
val ProductCardShadow = Color(0x1A000000)

// Colores de Acción
val PrimaryAction = PrimaryBlue
val PrimaryActionLight = PrimaryBlueLight
val PrimaryActionDark = PrimaryBlueDark

val SecondaryAction = SecondaryBlue
val SecondaryActionLight = SecondaryBlueLight
val SecondaryActionDark = SecondaryBlueDark

// Colores de Overlay
val OverlayLight = Color(0x80000000)
val OverlayDark = Color(0x80000000)

// Colores de Gradiente Corporativo
val GradientStart = Color(0xFF1565C0)  // Azul corporativo
val GradientEnd = Color(0xFF0D47A1)     // Azul más oscuro

// Colores Empresariales Adicionales
val CorporateGray = Color(0xFF424242)
val CorporateGrayLight = Color(0xFF616161)
val CorporateGrayDark = Color(0xFF212121)

val AccentGold = Color(0xFFFFB300)      // Dorado para destacados premium
val AccentGoldLight = Color(0xFFFFCA28)
val AccentGoldDark = Color(0xFFF57F17)

// Colores de Dashboard - Mejorados
val DashboardCard = Color(0xFFFFFFFF)
val DashboardBackground = Color(0xFFF5F7FA)
val DashboardBorder = Color(0xFFE1E8ED)
val DashboardCardHover = Color(0xFFFAFBFC)  // Estado hover para cards
val DashboardAccent = PrimaryBlue.copy(alpha = 0.1f)  // Acento sutil

// Colores Legacy (para compatibilidad)
val Purple80 = SteelBlueLight
val PurpleGrey80 = Silver
val Pink80 = IndustrialOrangeLight

val Purple40 = SteelBlue
val PurpleGrey40 = Charcoal
val Pink40 = IndustrialOrange