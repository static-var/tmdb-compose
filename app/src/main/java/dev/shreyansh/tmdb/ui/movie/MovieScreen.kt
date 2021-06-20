package dev.shreyansh.tmdb.ui.movie

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.shreyansh.tmdb.data.model.Movie
import dev.shreyansh.tmdb.ui.*
import dev.shreyansh.tmdb.ui.home.ErrorUi
import dev.shreyansh.tmdb.ui.home.LoadingUi
import dev.shreyansh.tmdb.ui.theme.TmDBTheme
import dev.shreyansh.tmdb.utils.Constants
import dev.shreyansh.tmdb.utils.DominantColors
import dev.shreyansh.tmdb.utils.NetworkImage
import dev.shreyansh.tmdb.utils.getColorsFromImageOrTheme

@Composable
fun MovieScreen(viewModel: TmdbViewModel, movieId: Int, navigateBack: () -> Unit) {
    val movie by viewModel.getMovieById(movieId).observeAsState(initial = Loading())
    val modifier = Modifier.fillMaxSize()
    val systemUiController = rememberSystemUiController()

    Surface(modifier = modifier) {
        when (movie) {
            is Loading -> LoadingUi(modifier = modifier)
            is Error -> ErrorUi(
                modifier = modifier,
                errorMessage = (movie as Error).errorMessage
            )
            is Success -> {
                val movieData = (movie as Success<Movie>).data

                val dominantColor = getColorsFromImageOrTheme(
                    backdropUrl = "${Constants.URL.BACKDROP_URL}${movieData.backdrop}",
                    posterUrl = "${Constants.URL.POSTER_URL}${movieData.poster}"
                )
                systemUiController.setSystemBarsColor(
                    Color.Transparent,
                    dominantColor.validLuminance > 0.4
                )
                MovieUi(
                    movie = movieData,
                    pop = navigateBack,
                    colors = dominantColor
                )
            }
        }

    }
}

@Composable
fun MovieUi(
    movie: Movie,
    pop: () -> Unit,
    colors: DominantColors
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
        ) {

            NetworkImage(
                url = "${Constants.URL.BACKDROP_URL}${movie.backdrop}",
                blur = true,
                successContent = { painter, loadState ->
                    Image(
                        painter = painter,
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize().placeholder(
                            visible = loadState is ImageLoadState.Empty || loadState is ImageLoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = TmDBTheme.shapes.small,
                            color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                        ),
                        contentScale = ContentScale.Crop
                    )
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsHeight()
                        .background(
                            Color.Transparent
                        )
                )
                TmdbAppBar(
                    showBack = true,
                    backAction = pop,
                    darkIcon = colors.validLuminance > 0.4
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(0.66f)
                            .padding(16.dp),
                        elevation = 16.dp,
                        border = BorderStroke(1.dp, colors.mainColor),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        NetworkImage(url = "${Constants.URL.POSTER_URL}${movie.poster}") { painter, imageLoadState ->
                            Image(
                                painter = painter,
                                contentDescription = "",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.placeholder(
                                    visible = imageLoadState is ImageLoadState.Empty || imageLoadState is ImageLoadState.Loading,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    shape = TmDBTheme.shapes.small,
                                    color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                                )
                            )
                        }

                    }
                }
            }
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = movie.title,
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.h5,
                color = colors.mainColor
            )
            Text(
                text = movie.genres.map { it.name }.toString().removePrefix("[")
                    .removeSuffix("]"),
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = colors.mainColor
            )
            if (movie.adult)
                Text(
                    text = "Adult",
                    modifier = Modifier.padding(vertical = 4.dp),
                    style = MaterialTheme.typography.body1,
                    color = colors.firstColor
                )
            Text(
                text = "Original Language: ${movie.language}",
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.body1,
                color = colors.firstColor
            )
            Text(
                text = movie.releaseDate,
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.body1,
                color = colors.firstColor
            )
            Text(
                text = "Rating : ${movie.rating}",
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.body1,
                color = colors.firstColor
            )
            Text(
                text = movie.overview,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.body2,
                color = colors.secondColor
            )
            Spacer(
                modifier = Modifier
                    .navigationBarsHeight()
                    .fillMaxWidth()
            )
        }
    }
}