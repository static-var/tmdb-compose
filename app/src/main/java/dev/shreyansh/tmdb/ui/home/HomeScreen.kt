package dev.shreyansh.tmdb.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.transform.RoundedCornersTransformation
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.shreyansh.tmdb.data.model.MediaContentType
import dev.shreyansh.tmdb.data.model.Movie
import dev.shreyansh.tmdb.data.model.TvShow
import dev.shreyansh.tmdb.ui.*
import dev.shreyansh.tmdb.ui.theme.TmDBTheme
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
    val systemUiController = rememberSystemUiController()

    systemUiController.setSystemBarsColor(Color.Transparent, TmDBTheme.colors.isLight)
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
                content = {
                    HomeScreenContent(viewModel, openMovie, openTvShow)
                }

            )
        }
    }
}

@Composable
fun NoNetworkCard(network: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "No Network connection.",
                modifier = Modifier.weight(4f)
            )
            Button(
                onClick = network,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onPrimary)
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
    val context = LocalContext.current
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
                viewModel.refreshGenre()
                viewModel.refreshTvShows()
                viewModel.refreshMovies()
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

    val movieUIState: UiState<List<Movie>> by viewModel.getMovies()
        .observeAsState(initial = Loading())

    when (movieUIState) {
        is Error -> {
            val error = (movieUIState as Error).errorMessage
            ErrorUi(modifier = modifier, errorMessage = error)
        }
        is Loading,
        is Success -> {
            val data = if (movieUIState is Success)
                (movieUIState as Success).data
            else generateSequence { Movie() }.take(10).toList()
            val loadingData = movieUIState is Loading
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = loadingData),
                onRefresh = { viewModel.refreshMovies() },
                modifier = modifier,
                indicator = { state, trigger ->
                    SwipeRefreshIndicator(
                        // Pass the SwipeRefreshState + trigger through
                        state = state,
                        refreshTriggerDistance = trigger,
                        // Enable the scale animation
                        scale = true,
                        // Change the color and shape
                        backgroundColor = TmDBTheme.colors.primary,
                        shape = TmDBTheme.shapes.small,
                    )
                }
            ) {
                LazyColumn(
                    contentPadding = rememberInsetsPaddingValues(
                        insets = LocalWindowInsets.current.navigationBars,
                        applyBottom = true,
                    )
                ) {
                    items(data) { item ->
                        MovieItem(movie = item, action, loadingData)
                    }
                }
            }
        }
    }
}

@Composable
fun TmdbTvShowList(modifier: Modifier, viewModel: TmdbViewModel, action: (Int) -> Unit) {

    val showUIState: UiState<List<TvShow>> by viewModel.getTvShows()
        .observeAsState(initial = Loading())

    when (showUIState) {
        is Error -> {
            val error = (showUIState as Error).errorMessage
            ErrorUi(modifier = modifier, errorMessage = error)
        }
        is Loading,
        is Success -> {
            val data = if (showUIState is Success)
                (showUIState as Success).data
            else generateSequence { TvShow() }.take(10).toList()
            val loadingData = showUIState is Loading
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = loadingData),
                onRefresh = { viewModel.refreshTvShows() },
                modifier = modifier,
                indicator = { state, trigger ->
                    SwipeRefreshIndicator(
                        // Pass the SwipeRefreshState + trigger through
                        state = state,
                        refreshTriggerDistance = trigger,
                        // Enable the scale animation
                        scale = true,
                        // Change the color and shape
                        backgroundColor = TmDBTheme.colors.primary,
                        shape = TmDBTheme.shapes.small,
                    )
                }
            ) {
                LazyColumn(
                    contentPadding = rememberInsetsPaddingValues(
                        insets = LocalWindowInsets.current.navigationBars,
                        applyBottom = true,
                    )
                ) {
                    items(data) { item ->
                        TvShowItem(tvShow = item, action, loadingData)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, action: (Int) -> Unit, loadingData: Boolean) {

    val painter = rememberCoilPainter(
        request = "${Constants.URL.POSTER_URL}${movie.poster}",
        fadeIn = true,
        fadeInDurationMs = 800,
        requestBuilder = {
            transformations(
                listOf(
                    RoundedCornersTransformation(0f),
                )
            )
        },
    )
    Card(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 8.dp,
        shape = TmDBTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.clickable { action(movie.movieId) }
        ) {
            Card(
                modifier = Modifier
                    .size(80.dp, 120.dp)
                    .padding(8.dp)
                    .placeholder(
                        visible = painter.loadState is ImageLoadState.Empty || painter.loadState is ImageLoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        shape = TmDBTheme.shapes.small,
                        color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                    ),
                shape = TmDBTheme.shapes.medium
            ) {
                Image(
                    painter = painter,
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
            }

            Column(modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = movie.title,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                        .placeholder(
                            visible = loadingData,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = TmDBTheme.shapes.small,
                            color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                        ),
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = movie.genres.map { it.name }.toString().removePrefix("[")
                        .removeSuffix("]"),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .placeholder(
                            visible = loadingData,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = TmDBTheme.shapes.small,
                            color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                        ),
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Release Date : ${movie.releaseDate}",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .placeholder(
                            visible = loadingData,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = TmDBTheme.shapes.small,
                            color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                        ),
                    style = MaterialTheme.typography.body2,
                )
                Text(
                    text = "Rating : ${movie.rating}",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .placeholder(
                            visible = loadingData,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = TmDBTheme.shapes.small,
                            color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                        ),
                    style = MaterialTheme.typography.body2,
                )
            }
        }
    }
}

@Composable
fun TvShowItem(tvShow: TvShow, action: (Int) -> Unit, loadingData: Boolean) {
    val painter = rememberCoilPainter(
        request = "${Constants.URL.POSTER_URL}${tvShow.poster}",
        fadeIn = true,
        fadeInDurationMs = 800,
        requestBuilder = {
            transformations(
                listOf(
                    RoundedCornersTransformation(0f),
                )
            )
        },
    )

    Card(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 8.dp,
        shape = TmDBTheme.shapes.medium
    ) {
        Row(modifier = Modifier.clickable {
            action(tvShow.tvShowId)
        }) {
            Card(
                modifier = Modifier
                    .size(80.dp, 120.dp)
                    .padding(8.dp)
                    .placeholder(
                        visible = painter.loadState is ImageLoadState.Empty || painter.loadState is ImageLoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        shape = TmDBTheme.shapes.small,
                        color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                    ),
                shape = TmDBTheme.shapes.medium
            ) {
                Image(
                    painter = painter,
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
            }
            Column(modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = tvShow.name,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 4.dp, end = 8.dp)
                        .placeholder(
                            visible = loadingData,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = TmDBTheme.shapes.small,
                            color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                        ),
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = tvShow.genres.map { it.name }.toString().removePrefix("[")
                        .removeSuffix("]"),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .placeholder(
                            visible = loadingData,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = TmDBTheme.shapes.small,
                            color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                        ),
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "First Air date : ${tvShow.firstAirDate}",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .placeholder(
                            visible = loadingData,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = TmDBTheme.shapes.small,
                            color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                        ),
                    style = MaterialTheme.typography.body2,
                )
                Text(
                    text = "Rating : ${tvShow.rating}",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .placeholder(
                            visible = loadingData,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = TmDBTheme.shapes.small,
                            color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                        ),
                    style = MaterialTheme.typography.body2,
                )
            }
        }
    }
}