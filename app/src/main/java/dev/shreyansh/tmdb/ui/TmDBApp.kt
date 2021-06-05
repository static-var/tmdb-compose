package dev.shreyansh.tmdb.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import dev.shreyansh.tmdb.ui.about.AboutScreen
import dev.shreyansh.tmdb.ui.home.HomeScreen
import dev.shreyansh.tmdb.ui.movie.MovieScreen
import dev.shreyansh.tmdb.ui.navigation.Actions
import dev.shreyansh.tmdb.ui.navigation.Destinations
import dev.shreyansh.tmdb.ui.theme.TmDBTheme
import dev.shreyansh.tmdb.ui.tvShows.TvShowScreen

@Composable
fun TmDBApp() {
    val navController = rememberNavController()
    val action = remember(navController) { Actions(navController) }
    val viewModel: TmdbViewModel = hiltViewModel()
    TmDBTheme {
        ProvideWindowInsets {
            // A surface container using the 'background' color from the theme
            NavHost(navController = navController, startDestination = Destinations.Home) {
                composable(Destinations.Home) {
                    HomeScreen(
                        viewModel,
                        action.openAbout,
                        action.openMovie,
                        action.openTvShow,
                    )
                }
                composable(Destinations.About) {
                    AboutScreen(
                        action.pop
                    )
                }
                composable(
                    "${Destinations.Actors}/${Destinations.NavArgs.ActorId}",
                    arguments = listOf(navArgument(Destinations.NavArgs.ActorId) {
                        type = NavType.StringType
                    })
                ) {

                }
                composable(
                    "${Destinations.Movie}/{${Destinations.NavArgs.MovieId}}",
                    arguments = listOf(navArgument(Destinations.NavArgs.MovieId) {
                        type = NavType.IntType
                    })
                ) {
                    val id = it.arguments?.getInt(Destinations.NavArgs.MovieId) ?: -1
                    MovieScreen(viewModel = viewModel, movieId = id, navigateBack = action.pop)
                }
                composable(
                    "${Destinations.TvShow}/{${Destinations.NavArgs.TvShowId}}",
                    arguments = listOf(navArgument(Destinations.NavArgs.TvShowId) {
                        type = NavType.IntType
                    })
                ) {
                    val id = it.arguments?.getInt(Destinations.NavArgs.TvShowId) ?: -1
                    TvShowScreen(viewModel = viewModel, tvShowId = id, navigateBack = action.pop)
                }
            }
        }
    }
}