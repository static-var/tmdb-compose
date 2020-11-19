package dev.shreyansh.tmdb.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = tmdbBGColor,
    primaryVariant = tmdbBGColor,
    secondary = tmdbBGColor
)

private val LightColorPalette = lightColors(
    primary = tmdbBGColor,
    primaryVariant = tmdbBGColor,
    secondary = tmdbBGColor
)

@Composable
fun TmDBTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        },
        typography = typography,
        shapes = shapes,
        content = content
    )
}