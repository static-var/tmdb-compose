package dev.shreyansh.tmdb.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.shreyansh.tmdb.R

@Composable
fun TmDBAppBar(showBack: Boolean, backAction: () -> Unit = {}) {
    TopAppBar(
        title = {
            Text(text = "tmDB", modifier = if (showBack) Modifier else Modifier.fillMaxWidth(), textAlign = if (showBack) null else TextAlign.Center)
        },
        backgroundColor = MaterialTheme.colors.background,
        navigationIcon = if (showBack) {
            {
                IconButton(onClick = backAction) {
                    Icon(vectorResource(R.drawable.ic_baseline_arrow_back))
                }
            }
        } else null,
        elevation = 0.dp
    )
}