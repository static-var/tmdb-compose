package dev.shreyansh.tmdb.ui.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.insets.AmbientWindowInsets
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import dev.chrisbanes.accompanist.insets.toPaddingValues
import dev.shreyansh.tmdb.data.model.Movie
import dev.shreyansh.tmdb.ui.*
import dev.shreyansh.tmdb.utils.NetworkUtil

@Composable
fun HomeScreen(
    viewModel: TmdbViewModel,
    openAbout: () -> Unit,
    modifier: Modifier = Modifier.fillMaxSize()
) {
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
                    TmdbAppBar(showBack = false)
                },
                bodyContent = {
                    HomeScreenContent(viewModel)
                }

            )
        }
    }
}

@Composable
fun NoNetworkCard(viewModel: TmdbViewModel, context: Context, isConnected: MutableState<Boolean>) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "No Network connection.",
                modifier = Modifier.weight(4f)
            )
            Button(
                onClick = {
                    if (NetworkUtil.isConnected(context)) {
                        isConnected.value = true
                    }
                },
                colors = ButtonConstants.defaultButtonColors(backgroundColor = MaterialTheme.colors.onPrimary)
            ) {
                Text(text = "Retry")
            }
        }
    }
}

@Composable
fun HomeScreenContent(viewModel: TmdbViewModel) {
    val isConnected = mutableStateOf(NetworkUtil.isConnected(ContextAmbient.current))
    val modifier = Modifier.fillMaxSize()
    Column(
        modifier = modifier
    ) {
        if (!isConnected.value) {
            NoNetworkCard(
                viewModel = viewModel,
                ContextAmbient.current,
                isConnected
            )
        } else {
            viewModel.getGenreFromNetwork()
            viewModel.getTrendingMoviesFromNetwork()
            viewModel.getTrendingTvShowFromNetwork()
        }

        val movieUIState: UiState<List<Movie>> by viewModel.getListTrendingMovies()
            .observeAsState(initial = Loading())

        when (movieUIState) {
            is Loading -> {
                Column(
                    modifier = modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Error -> {
                val error = (movieUIState as Error).errorMessage
                Column(
                    modifier = modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = error)
                }
            }
            is Success -> {
                val data = (movieUIState as Success).data
                Column(modifier = modifier) {
                    LazyColumnFor(
                        items = data,
                        contentPadding = AmbientWindowInsets.current.systemBars
                            .toPaddingValues(top = false)
                    ) { item ->
                        Text(text = item.title, modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }
}