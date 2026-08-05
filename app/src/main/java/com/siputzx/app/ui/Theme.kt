package com.siputzx.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val Gray900 = Color(0xFF111111)
val Gray850 = Color(0xFF1A1A1A)
val Gray800 = Color(0xFF222222)
val Gray700 = Color(0xFF333333)
val Gray600 = Color(0xFF555555)
val Gray500 = Color(0xFF777777)
val Gray400 = Color(0xFF999999)
val Gray300 = Color(0xFFCCCCCC)
val Gray200 = Color(0xFFE5E5E5)
val Gray100 = Color(0xFFF5F5F5)

private val DarkScheme = darkColorScheme(
    primary = White,
    secondary = Gray400,
    tertiary = Gray500,
    background = Black,
    surface = Gray900,
    surfaceVariant = Gray800,
    onPrimary = Black,
    onSecondary = Black,
    onTertiary = Black,
    onBackground = White,
    onSurface = White,
    onSurfaceVariant = Gray400,
    outline = Gray700,
    outlineVariant = Gray800,
    inverseSurface = White,
    inverseOnSurface = Black,
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkScheme, content = content)
}
