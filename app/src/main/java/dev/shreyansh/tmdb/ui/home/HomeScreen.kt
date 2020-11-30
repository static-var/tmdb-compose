package dev.shreyansh.tmdb.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.transform.RoundedCornersTransformation
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.insets.AmbientWindowInsets
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import dev.chrisbanes.accompanist.insets.toPaddingValues
import dev.shreyansh.tmdb.data.model.MediaContentType
import dev.shreyansh.tmdb.data.model.Movie
import dev.shreyansh.tmdb.data.model.TvShow
import dev.shreyansh.tmdb.ui.*
import dev.shreyansh.tmdb.utils.Constants
import dev.shreyansh.tmdb.utils.NetworkUtil
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: TmdbViewModel,
    openAbout: () -> Unit,
    openMovie: (Int) -> Unit,
    openTvShow: (Int) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
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
                    HomeScreenContent(viewModel, openMovie, openTvShow)
                }

            )
        }
    }
}

@Composable
fun NoNetworkCard(network: () -> Unit) {
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
fun HomeScreenContent(
    viewModel: TmdbViewModel,
    openMovie: (Int) -> Unit,
    openTvShow: (Int) -> Unit
) {
    val context = ContextAmbient.current
    var isConnected by remember { mutableStateOf(NetworkUtil.isConnected(context)) }
    val modifier = Modifier.fillMaxSize()
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier
    ) {
        if (!isConnected) {
            NoNetworkCard {
                isConnected = NetworkUtil.isConnected(context)
            }
        } else {
            coroutineScope.launch {
                viewModel.getGenreFromNetwork()
                viewModel.getTrendingMoviesFromNetwork()
                viewModel.getTrendingTvShowFromNetwork()
            }
        }

        TmdbTabBarAndBody(modifier = modifier, viewModel = viewModel, openMovie, openTvShow)
    }
}

@Composable
fun TmdbTabBarAndBody(
    modifier: Modifier,
    viewModel: TmdbViewModel,
    openMovie: (Int) -> Unit,
    openTvShow: (Int) -> Unit
) {
    val tabState by viewModel.getUiMode().observeAsState(MediaContentType.MOVIE)

    TabRow(
        selectedTabIndex = tabState.ordinal,
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.background.copy(0.7f)
    ) {
        Tab(
            selected = tabState == MediaContentType.MOVIE,
            onClick = { viewModel.setUiMode(MediaContentType.MOVIE) }) {
            Text(
                text = "Movies",
                modifier = Modifier.padding(8.dp, 16.dp),
                textAlign = TextAlign.Center
            )
        }
        Tab(
            selected = tabState == MediaContentType.TVSHOW,
            onClick = { viewModel.setUiMode(MediaContentType.TVSHOW) }) {
            Text(
                text = "TV Shows",
                modifier = Modifier.padding(8.dp, 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    when (tabState) {
        MediaContentType.MOVIE -> {
            TmdbMovieList(modifier = modifier, viewModel = viewModel, openMovie)
        }
        MediaContentType.TVSHOW -> {
            TmdbTvShowList(modifier = modifier, viewModel = viewModel, openTvShow)
        }
    }
}

@Composable
fun TmdbMovieList(modifier: Modifier, viewModel: TmdbViewModel, action: (Int) -> Unit) {

    val movieUIState: UiState<List<Movie>> by viewModel.getListTrendingMovies()
        .observeAsState(initial = Loading())

    when (movieUIState) {
        is Loading -> {
            LoadingUi(modifier = modifier)
        }
        is Error -> {
            val error = (movieUIState as Error).errorMessage
            ErrorUi(modifier = modifier, errorMessage = error)
        }
        is Success -> {
            val data = (movieUIState as Success).data
            Column(modifier = modifier) {
                LazyColumnFor(
                    items = data,
                    contentPadding = AmbientWindowInsets.current.systemBars
                        .toPaddingValues(top = false)
                ) { item ->
                    MovieItem(movie = item, action)
                }
            }
        }
    }
}

@Composable
fun TmdbTvShowList(modifier: Modifier, viewModel: TmdbViewModel, action: (Int) -> Unit) {

    val movieUIState: UiState<List<TvShow>> by viewModel.getListTrendingTvShows()
        .observeAsState(initial = Loading())

    when (movieUIState) {
        is Loading -> {
            LoadingUi(modifier = modifier)
        }
        is Error -> {
            val error = (movieUIState as Error).errorMessage
            ErrorUi(modifier = modifier, errorMessage = error)
        }
        is Success -> {
            val data = (movieUIState as Success).data
            Column(modifier = modifier) {
                LazyColumnFor(
                    items = data,
                    contentPadding = AmbientWindowInsets.current.systemBars
                        .toPaddingValues(top = false)
                ) { item ->
                    TvShowItem(tvShow = item, action)
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, action: (Int) -> Unit) {
    Card(
        Modifier.fillMaxWidth().clickable(onClick = {
            action(movie.movieId)
        }).padding(8.dp),
        elevation = 8.dp
    ) {
        Row {
            CoilImage(
                data = "${Constants.URL.POSTER_URL}${movie.poster}",
                modifier = Modifier.size(80.dp, 108.dp).padding(8.dp),
                fadeIn = true,
                requestBuilder = {
                    transformations(RoundedCornersTransformation(16f))
                },
                loading = {
                    Box {
                        CircularProgressIndicator(
                            Modifier.align(Alignment.Center),
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            )
            Column(modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = movie.title,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp),
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = movie.genres.map { it.name }.toString().removePrefix("[")
                        .removeSuffix("]"),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Release Date : ${movie.releaseDate}",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.body2,
                )
                Text(
                    text = "Rating : ${movie.rating}",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.body2,
                )
            }
        }
    }
}

@Composable
fun TvShowItem(tvShow: TvShow, action: (Int) -> Unit) {
    Card(
        Modifier.fillMaxWidth().clickable(onClick = { action.invoke(tvShow.tvShowId) })
            .padding(8.dp), elevation = 8.dp
    ) {
        Row {
            CoilImage(
                data = "${Constants.URL.POSTER_URL}${tvShow.poster}",
                modifier =  Modifier.size(80.dp, 108.dp).padding(8.dp),
                fadeIn = true,
                requestBuilder = {
                    transformations(RoundedCornersTransformation(16f))
                },
                loading = {
                    Box {
                        CircularProgressIndicator(
                            Modifier.align(Alignment.Center),
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            )
            Column(modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = tvShow.name,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp),
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = tvShow.genres.map { it.name }.toString().removePrefix("[")
                        .removeSuffix("]"),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "First Air date : ${tvShow.firstAirDate}",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.body2,
                )
                Text(
                    text = "Rating : ${tvShow.rating}",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.body2,
                )
            }
        }
    }
}