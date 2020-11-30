package dev.shreyansh.tmdb.ui.tvShows

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.insets.navigationBarsHeight
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import dev.shreyansh.tmdb.data.model.TvShow
import dev.shreyansh.tmdb.ui.*
import dev.shreyansh.tmdb.ui.home.ErrorUi
import dev.shreyansh.tmdb.ui.home.LoadingUi
import dev.shreyansh.tmdb.utils.Constants
import dev.shreyansh.tmdb.utils.DominantColors
import dev.shreyansh.tmdb.utils.getColorsFromImageOrTheme

@Composable
fun TvShowScreen(viewModel: TmdbViewModel, tvShowId: Int, navigateBack: () -> Unit) {
    val tvShow by viewModel.getTvShowById(tvShowId).observeAsState(initial = Loading())
    val modifier = Modifier.fillMaxSize()
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
    context: Context = ContextAmbient.current,
    tvShow: TvShow,
    pop: () -> Unit,
    colors: DominantColors
) {
    ScrollableColumn(Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.aspectRatio(1f).fillMaxWidth()) {
                CoilImage(
                    data = "${Constants.URL.BACKDROP_URL}${tvShow.backdrop}",
                    modifier = Modifier.fillMaxSize(),
                    fadeIn = true,
                    requestBuilder = {
                        transformations(
                            listOf(
                                RoundedCornersTransformation(0f),
                                BlurTransformation(context, 10f, 4.5f)
                            )
                        )
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
                Column(
                    modifier = Modifier.fillMaxSize().background(Color.Transparent)
                ) {
                    Spacer(
                        modifier = Modifier.fillMaxWidth().statusBarsHeight()
                            .background(
                                Color.Transparent
                            )
                    )
                    TmdbAppBar(showBack = true, backAction = pop)
                    Column(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Card(
                            modifier = Modifier.fillMaxHeight().aspectRatio(0.7f)
                                .padding(16.dp),
                            elevation = 16.dp,
                            border = BorderStroke(1.dp, colors.mainColor),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            CoilImage(
                                data = "${Constants.URL.POSTER_URL}${tvShow.poster}",
                                fadeIn = true,
                                requestBuilder = {
                                    transformations(
                                        RoundedCornersTransformation(8f)
                                    )
                                }
                            )
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
                Spacer(modifier = Modifier.navigationBarsHeight().fillMaxWidth())
            }
        }
    }
}