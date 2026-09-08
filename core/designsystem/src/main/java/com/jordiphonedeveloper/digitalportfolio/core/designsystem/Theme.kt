package com.jordiphonedeveloper.digitalportfolio.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = DeepBlue,
    onPrimary = White,
    primaryContainer = Ice,
    onPrimaryContainer = Navy,
    secondary = Emerald,
    onSecondary = White,
    secondaryContainer = ColorTokens.MintContainer,
    onSecondaryContainer = Navy,
    tertiary = Cyan,
    background = Cloud,
    onBackground = Ink,
    surface = White,
    onSurface = Ink,
    surfaceVariant = Ice,
    onSurfaceVariant = Slate,
    outline = ColorTokens.Outline,
)

private val DarkColors = darkColorScheme(
    primary = Mint,
    onPrimary = Ink,
    primaryContainer = DeepBlue,
    onPrimaryContainer = DarkText,
    secondary = ColorTokens.SoftCyan,
    onSecondary = Ink,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = DarkText,
    tertiary = ColorTokens.Sky,
    background = Ink,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = ColorTokens.DarkMuted,
    outline = ColorTokens.DarkOutline,
)

object ColorTokens {
    val GradientStart = Navy
    val GradientEnd = DeepBlue
    val Accent = Mint
    val MintContainer = androidx.compose.ui.graphics.Color(0xFFD9F8F1)
    val SoftCyan = androidx.compose.ui.graphics.Color(0xFF73D9E8)
    val Sky = androidx.compose.ui.graphics.Color(0xFF72C7F1)
    val Outline = androidx.compose.ui.graphics.Color(0xFFB8C9D0)
    val DarkMuted = androidx.compose.ui.graphics.Color(0xFFB7CDD6)
    val DarkOutline = androidx.compose.ui.graphics.Color(0xFF456273)
}

@Composable
fun PortfolioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = PortfolioTypography,
        content = content,
    )
}
