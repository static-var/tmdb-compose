package dev.shreyansh.tmdb.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.sp
import dev.shreyansh.tmdb.R

// Set of Material typography styles to start with
val regular = Font(R.font.firesans_light)
val medium = Font(R.font.firesans_regular, FontWeight.W500)
val bold = Font(R.font.firesans_bold, FontWeight.W600)

val appFontFamily = FontFamily(
        fonts = listOf(
                regular,
                medium,
                bold
        )
)

val typography = Typography(
        h4 = TextStyle(
                fontFamily = appFontFamily,
                fontWeight = FontWeight.W600,
                fontSize = 30.sp
        ),
        h5 = TextStyle(
                fontFamily = appFontFamily,
                fontWeight = FontWeight.W600,
                fontSize = 24.sp
        ),
        h6 = TextStyle(
                fontFamily = appFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp
        ),
        subtitle1 = TextStyle(
                fontFamily = appFontFamily,
                fontWeight = FontWeight.W600,
                fontSize = 16.sp
        ),
        subtitle2 = TextStyle(
                fontFamily = appFontFamily,
                fontWeight = FontWeight.W500,
                fontSize = 14.sp
        ),
        body1 = TextStyle(
                fontFamily = appFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
        ),
        body2 = TextStyle(
                fontFamily = appFontFamily,
                fontSize = 14.sp
        ),
        button = TextStyle(
                fontFamily = appFontFamily,
                fontWeight = FontWeight.W500,
                fontSize = 14.sp
        ),
        caption = TextStyle(
                fontFamily = appFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp
        ),
        overline = TextStyle(
                fontFamily = appFontFamily,
                fontWeight = FontWeight.W500,
                fontSize = 12.sp
        )
)