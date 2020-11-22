package dev.shreyansh.tmdb.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import dev.shreyansh.tmdb.ui.TmDBAppBar

@Composable
fun AboutScreen(pop: () -> Unit, modifier: Modifier = Modifier.fillMaxWidth()) {
    Surface(modifier = modifier) {
        Column {
            Spacer(
                Modifier
                    .background(Color.Transparent)
                    .statusBarsHeight() // Match the height of the status bar
                    .fillMaxWidth()
            )
            Scaffold(
                topBar = {
                    TmDBAppBar(true, pop)
                },
                bodyContent = {

                }
            )
        }
    }
}