package dev.shreyansh.tmdb.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.shreyansh.tmdb.ui.about.AboutScreen
import dev.shreyansh.tmdb.ui.home.HomeScreen
import dev.shreyansh.tmdb.ui.navigation.Actions
import dev.shreyansh.tmdb.ui.navigation.Destinations
import dev.shreyansh.tmdb.ui.theme.TmDBTheme
import dev.shreyansh.tmdb.utils.composableWithCrossfade

@Composable
fun TmDBApp(viewModel: TmdbViewModel) {
    val navController = rememberNavController()
    val action = remember(navController) { Actions(navController) }
    TmDBTheme {
        ProvideWindowInsets {
            // A surface container using the 'background' color from the theme
            NavHost(navController = navController, startDestination = Destinations.Home) {
                composableWithCrossfade(Destinations.Home) {
                    HomeScreen(
                        viewModel,
                        action.openAbout
                    )
                }
                composableWithCrossfade(Destinations.About) {
                    AboutScreen(
                        action.pop
                    )
                }
                composableWithCrossfade(
                    "${Destinations.Actors}/${Destinations.NavArgs.ActorId}",
                    arguments = listOf(navArgument(Destinations.NavArgs.ActorId) {
                        type = NavType.StringType
                    })
                ) {

                }
                composableWithCrossfade(
                    "${Destinations.Movie}/${Destinations.NavArgs.MovieId}",
                    arguments = listOf(navArgument(Destinations.NavArgs.ActorId) {
                        type = NavType.StringType
                    })
                ) {

                }
            }

        }
    }
}