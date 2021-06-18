package dev.shreyansh.tmdb.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState

@Composable
fun NetworkImage(
    context: Context = LocalContext.current,
    url: String,
    blur: Boolean = false,
    loadingContent: @Composable() () -> Unit = {},
    successContent: @Composable() (Painter, ImageLoadState) -> Unit
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
            successContent(painter, painter.loadState)
        }
    }
}