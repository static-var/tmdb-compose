package dev.shreyansh.tmdb.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.insets.AmbientWindowInsets
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import dev.chrisbanes.accompanist.insets.toPaddingValues
import dev.shreyansh.tmdb.data.model.MediaContentType
import dev.shreyansh.tmdb.data.model.Movie
import dev.shreyansh.tmdb.data.model.TvShow
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
fun NoNetworkCard(viewModel: TmdbViewModel, network: () -> Unit) {
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
                onClick = network,
                colors = ButtonConstants.defaultButtonColors(backgroundColor = MaterialTheme.colors.onPrimary)
            ) {
                Text(text = "Retry")
            }
        }
    }
}

@Composable
fun HomeScreenContent(viewModel: TmdbViewModel) {
    val context = ContextAmbient.current
    var isConnected by remember { mutableStateOf(NetworkUtil.isConnected(context)) }
    val modifier = Modifier.fillMaxSize()
    Column(
        modifier = modifier
    ) {
        if (!isConnected) {
            NoNetworkCard(viewModel = viewModel) {
                isConnected = NetworkUtil.isConnected(context)
            }
        } else {
            viewModel.getGenreFromNetwork()
            viewModel.getTrendingMoviesFromNetwork()
            viewModel.getTrendingTvShowFromNetwork()
        }

        TmdbTabBar(modifier = modifier, viewModel = viewModel)
    }
}

@Composable
fun TmdbTabBar(
    modifier: Modifier, viewModel: TmdbViewModel
) {
    var tabState by remember { mutableStateOf(MediaContentType.MOVIE) }

    TabRow(
        selectedTabIndex = tabState.ordinal,
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.background.copy(0.7f)
    ) {
        Tab(
            selected = tabState == MediaContentType.MOVIE,
            onClick = { tabState = MediaContentType.MOVIE }) {
            Text(
                text = "Movies",
                modifier = Modifier.padding(8.dp, 16.dp),
                textAlign = TextAlign.Center
            )
        }
        Tab(
            selected = tabState == MediaContentType.TVSHOW,
            onClick = { tabState = MediaContentType.TVSHOW }) {
            Text(
                text = "TV Shows",
                modifier = Modifier.padding(8.dp, 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    when (tabState) {
        MediaContentType.MOVIE -> {
            TmdbMovieList(modifier = modifier, viewModel = viewModel)
        }
        MediaContentType.TVSHOW -> {
            TmdbTvShowList(modifier = modifier, viewModel = viewModel)
        }
    }
}

@Composable
fun TmdbMovieList(modifier: Modifier, viewModel: TmdbViewModel) {

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

@Composable
fun TmdbTvShowList(modifier: Modifier, viewModel: TmdbViewModel) {

    val movieUIState: UiState<List<TvShow>> by viewModel.getListTrendingTvShows()
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
            Box(modifier = modifier) {
                LazyColumnFor(
                    items = data,
                    contentPadding = AmbientWindowInsets.current.systemBars
                        .toPaddingValues(top = false)
                ) { item ->
                    Text(text = item.name, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}