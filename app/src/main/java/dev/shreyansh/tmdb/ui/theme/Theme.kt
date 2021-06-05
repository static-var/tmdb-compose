package dev.shreyansh.tmdb.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = tmdbGradientStartColor,
    primaryVariant = tmdbGradientEndColor,
    secondary = tmdbGradientStartColor
)

private val LightColorPalette = lightColors(
    primary = tmdbGradientStartColor,
    primaryVariant = tmdbGradientStartColor,
    secondary = tmdbGradientStartColor
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


val TmDBTheme
    @Composable get() = MaterialTheme