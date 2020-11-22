package dev.shreyansh.tmdb.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import dev.shreyansh.tmdb.ui.TmDBAppBar
import dev.shreyansh.tmdb.ui.TmdbViewModel

@Composable
fun HomeScreen(viewModel: TmdbViewModel, openAbout: () -> Unit, modifier: Modifier = Modifier.fillMaxSize()) {
    Surface(modifier = modifier) {
        Column {
            Spacer(
                Modifier
                    .background(MaterialTheme.colors.background)
                    .statusBarsHeight() // Match the height of the status bar
                    .fillMaxWidth()
            )
            Scaffold(
                topBar = {
                    TmDBAppBar(false)
                },
                bodyContent = {
                    viewModel.getGenreFromNetwork()
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = openAbout) {
                            Text(text = "About")
                        }
                    }
                }
            )
        }
    }
}