package com.example.cst438_project_1.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Custom "Gaming" Color Palette
private val MidnightBlue = Color(0xFF121212) // Deep background
private val ElectricBlue = Color(0xFF00E5FF) // Neon primary
private val CyberPurple = Color(0xFFBB86FC) // Accent
private val ErrorRed = Color(0xFFCF6679)    // Errors

private val GameColorScheme = darkColorScheme(
    primary = ElectricBlue,
    secondary = CyberPurple,
    background = MidnightBlue,
    surface = Color(0xFF1E1E1E), // Slightly lighter than background for cards
    error = ErrorRed,
    onPrimary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun gametheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GameColorScheme,
        typography = Typography, // Uses your existing Typography.kt
        content = content
    )
}