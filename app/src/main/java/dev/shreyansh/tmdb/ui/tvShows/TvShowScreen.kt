package dev.shreyansh.tmdb.ui.tvShows

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.ajalt.timberkt.e
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.shreyansh.tmdb.data.model.TvShow
import dev.shreyansh.tmdb.ui.*
import dev.shreyansh.tmdb.ui.home.ErrorUi
import dev.shreyansh.tmdb.ui.home.LoadingUi
import dev.shreyansh.tmdb.utils.Constants
import dev.shreyansh.tmdb.utils.DominantColors
import dev.shreyansh.tmdb.utils.NetworkImage
import dev.shreyansh.tmdb.utils.getColorsFromImageOrTheme

@Composable
fun TvShowScreen(viewModel: TmdbViewModel, tvShowId: Int, navigateBack: () -> Unit) {
    val tvShow by viewModel.getTvShowById(tvShowId).observeAsState(initial = Loading())
    val modifier = Modifier.fillMaxSize()
    val systemUiController = rememberSystemUiController()

    Surface(modifier = modifier) {
        when (tvShow) {
            is Loading -> LoadingUi(modifier = modifier)
            is Error -> ErrorUi(
                modifier = modifier,
                errorMessage = (tvShow as Error).errorMessage
            )
            is Success -> {
                val tvShowData = (tvShow as Success<TvShow>).data

                val dominantColor = getColorsFromImageOrTheme(
                    backdropUrl = "${Constants.URL.BACKDROP_URL}${tvShowData.backdrop}",
                    posterUrl = "${Constants.URL.POSTER_URL}${tvShowData.poster}"
                )

                systemUiController.setSystemBarsColor(
                    Color.Transparent,
                    dominantColor.validLuminance > 0.4
                )
                TvShowUi(
                    tvShow = tvShowData,
                    pop = navigateBack,
                    colors = dominantColor
                )
            }
        }

    }
}

@Composable
fun TvShowUi(
    tvShow: TvShow,
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
                url = "${Constants.URL.POSTER_URL}${tvShow.backdrop}",
                blur = true,
                successContent = { painter, imageLoadState ->
                    Image(
                        painter = painter,
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = "",
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
                TmdbAppBar(showBack = true, backAction = pop, darkIcon = colors.validLuminance > 0.4)
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
                        NetworkImage(url = "${Constants.URL.POSTER_URL}${tvShow.poster}") { painter, imageLoadState ->
                            Image(
                                painter = painter,
                                contentDescription = "",
                                contentScale = ContentScale.FillBounds
                            )
                        }

                    }
                }
            }
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = tvShow.name,
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.h5,
                color = colors.mainColor
            )
            Text(
                text = tvShow.genres.map { it.name }.toString().removePrefix("[")
                    .removeSuffix("]"),
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = colors.mainColor
            )
            Text(
                text = "Original Language: ${tvShow.language}",
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.body1,
                color = colors.firstColor
            )
            Text(
                text = "First aired on ${tvShow.firstAirDate}",
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.body1,
                color = colors.firstColor
            )
            Text(
                text = "Rating : ${tvShow.rating}",
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.body1,
                color = colors.firstColor
            )
            Text(
                text = tvShow.overview,
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