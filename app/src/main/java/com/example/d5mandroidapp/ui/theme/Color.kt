package com.example.d5mandroidapp.ui.theme

import androidx.compose.ui.graphics.Color

// ===========================================
// Paleta de colores principal - D5M Brand
// ===========================================

// Colores primarios (Verde D5M)
val GreenPrimary = Color(0xFF3FDC85)
val GreenPrimaryDark = Color(0xFF2DB86B)
val GreenPrimaryLight = Color(0xFF6FFFAA)
val GreenPrimaryContainer = Color(0xFFD0F8E0)
val GreenOnPrimaryContainer = Color(0xFF00210E)

// Colores secundarios (Índigo)
val IndigoPrimary = Color(0xFF1C5EB2)
val IndigoLight = Color(0xFF5A8CD4)
val IndigoDark = Color(0xFF003D82)
val IndigoContainer = Color(0xFFD6E3FF)
val OnIndigoContainer = Color(0xFF001B3E)

// Colores terciarios (Teal/Agua)
val TealPrimary = Color(0xFF018786)
val TealLight = Color(0xFF52B8B8)
val TealDark = Color(0xFF005958)
val TealContainer = Color(0xFFB0F1F0)
val OnTealContainer = Color(0xFF002020)

// ===========================================
// Tema Claro (Light Theme)
// ===========================================
val LightPrimary = GreenPrimary
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = GreenPrimaryContainer
val LightOnPrimaryContainer = GreenOnPrimaryContainer

val LightSecondary = IndigoPrimary
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = IndigoContainer
val LightOnSecondaryContainer = OnIndigoContainer

val LightTertiary = TealPrimary
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = TealContainer
val LightOnTertiaryContainer = OnTealContainer

val LightBackground = Color(0xFFF8FAF9)
val LightOnBackground = Color(0xFF191C1A)
val LightSurface = Color(0xFFFFFFFF)
val LightOnSurface = Color(0xFF191C1A)
val LightSurfaceVariant = Color(0xFFE7E9E6)
val LightOnSurfaceVariant = Color(0xFF414942)
val LightOutline = Color(0xFF717972)
val LightOutlineVariant = Color(0xFFC1C9C1)

val LightError = Color(0xFFBA1A1A)
val LightOnError = Color(0xFFFFFFFF)
val LightErrorContainer = Color(0xFFFFDAD6)
val LightOnErrorContainer = Color(0xFF410002)

val LightInverseSurface = Color(0xFF2E312E)
val LightInverseOnSurface = Color(0xFFEFF1EE)
val LightInversePrimary = Color(0xFF6FFFAA)

// ===========================================
// Tema Oscuro (Dark Theme)
// ===========================================
val DarkPrimary = GreenPrimaryLight
val DarkOnPrimary = Color(0xFF00391A)
val DarkPrimaryContainer = GreenPrimaryDark
val DarkOnPrimaryContainer = Color(0xFFD0F8E0)

val DarkSecondary = Color(0xFFAAC7FF)
val DarkOnSecondary = Color(0xFF002F64)
val DarkSecondaryContainer = Color(0xFF00468C)
val DarkOnSecondaryContainer = IndigoContainer

val DarkTertiary = Color(0xFF83D5D4)
val DarkOnTertiary = Color(0xFF003737)
val DarkTertiaryContainer = Color(0xFF004F4F)
val DarkOnTertiaryContainer = TealContainer

val DarkBackground = Color(0xFF191C1A)
val DarkOnBackground = Color(0xFFE1E3E0)
val DarkSurface = Color(0xFF121412)
val DarkOnSurface = Color(0xFFE1E3E0)
val DarkSurfaceVariant = Color(0xFF414942)
val DarkOnSurfaceVariant = Color(0xFFC1C9C1)
val DarkOutline = Color(0xFF8B938C)
val DarkOutlineVariant = Color(0xFF414942)

val DarkError = Color(0xFFFFB4AB)
val DarkOnError = Color(0xFF690005)
val DarkErrorContainer = Color(0xFF93000A)
val DarkOnErrorContainer = Color(0xFFFFDAD6)

val DarkInverseSurface = Color(0xFFE1E3E0)
val DarkInverseOnSurface = Color(0xFF2E312E)
val DarkInversePrimary = GreenPrimaryDark

// ===========================================
// Colores de utilidad (para uso directo)
// ===========================================
val GreenJC = GreenPrimary
val IndigoJC = IndigoPrimary
val Teal700 = TealDark
val Teal200 = TealLight
val PrimaryDarkJC = Color(0xFF444C4F)
val AccentJC = Color(0xFF278BE3)

// Estados y alertas
val SuccessColor = Color(0xFF4CAF50)
val SuccessContainerLight = Color(0xFFE8F5E9)
val SuccessContainerDark = Color(0xFF1B5E20)

val WarningColor = Color(0xFFFF9800)
val WarningContainerLight = Color(0xFFFFF3E0)
val WarningContainerDark = Color(0xFFE65100)

val DangerJC = Color(0xFFDE4F54)
val ErrorJC = Color(0xFFB00020)
val ErrorContainerLight = Color(0xFFFFEBEE)
val ErrorContainerDark = Color(0xFF93000A)

val InfoColor = Color(0xFF2196F3)
val InfoContainerLight = Color(0xFFE3F2FD)
val InfoContainerDark = Color(0xFF0D47A1)

// Divisores y bordes
val DividerLight = Color(0xFFE0E0E0)
val DividerDark = Color(0xFF424242)
val DividerJC = DividerLight

// Legacy colors (mantener por compatibilidad)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650A4)
val PurpleGrey40 = Color(0xFF625B71)
val Pink40 = Color(0xFF7D5260)
val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val PrimaryLightJC = Color(0xFFC5CAE9)
