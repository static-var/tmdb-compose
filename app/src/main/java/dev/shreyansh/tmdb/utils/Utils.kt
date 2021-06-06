package dev.shreyansh.tmdb.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.ColorUtils
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import com.github.ajalt.timberkt.e
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState

@Composable
fun NetworkImage(
    context: Context = LocalContext.current,
    url: String,
    blur: Boolean = false,
    loadingContent: @Composable() () -> Unit = {},
    successContent: @Composable() (Painter) -> Unit
) {
    val painter = rememberCoilPainter(
        request = url,
        fadeIn = true,
        fadeInDurationMs = 800,
        requestBuilder = {
            if (blur)
                transformations(
                    listOf(
                        RoundedCornersTransformation(0f),
                        BlurTransformation(context, 8f, 2f)
                    )
                )
            else this
        },
    )

    when (painter.loadState) {
        ImageLoadState.Empty,
        is ImageLoadState.Loading -> {
            loadingContent()
        }
        is ImageLoadState.Success -> {
            successContent(painter)
        }
    }
}