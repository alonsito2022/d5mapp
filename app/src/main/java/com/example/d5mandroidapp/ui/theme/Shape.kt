package com.example.d5mandroidapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    // Para elementos pequeños como chips, badges
    extraSmall = RoundedCornerShape(4.dp),
    
    // Para botones, campos de texto
    small = RoundedCornerShape(8.dp),
    
    // Para cards, diálogos pequeños
    medium = RoundedCornerShape(12.dp),
    
    // Para cards grandes, sheets
    large = RoundedCornerShape(16.dp),
    
    // Para bottom sheets, modales grandes
    extraLarge = RoundedCornerShape(24.dp)
)
