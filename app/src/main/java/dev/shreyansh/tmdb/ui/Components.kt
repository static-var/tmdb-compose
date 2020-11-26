package dev.shreyansh.tmdb.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import dev.shreyansh.tmdb.R

@Composable
fun TmdbAppBar(showBack: Boolean, backAction: () -> Unit = {}) {
    Row(modifier = Modifier.height(56.dp).fillMaxWidth()) {
        Surface(color = Color.Transparent) {
            if (showBack) {
                IconButton(onClick = backAction) {
                    Icon(
                        asset = vectorResource(id = R.drawable.ic_baseline_arrow_back),
                        modifier = Modifier.padding(4.dp)
                    )
                }
            } else {
                Image(
                    asset = vectorResource(id = R.drawable.ic_tmdb_logo),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}