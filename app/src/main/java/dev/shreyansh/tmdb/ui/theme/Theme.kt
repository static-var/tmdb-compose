package dev.shreyansh.tmdb.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = tmdbBGColor,
    primaryVariant = tmdbBGColor,
    secondary = tmdbBGColor
)

private val LightColorPalette = lightColors(
    primary = tmdbBGColor,
    primaryVariant = tmdbBGColor,
    secondary = tmdbBGColor

    /* Other default colors to override
background = Color.White,
surface = Color.White,
onPrimary = Color.White,
onSecondary = Color.Black,
onBackground = Color.Black,
onSurface = Color.Black,
*/
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