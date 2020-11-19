package dev.shreyansh.tmdb.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.navigate
import androidx.navigation.NavHostController
import dev.shreyansh.tmdb.ui.navigation.Destinations.About
import dev.shreyansh.tmdb.ui.navigation.Destinations.Actors
import dev.shreyansh.tmdb.ui.navigation.Destinations.Movie
import dev.shreyansh.tmdb.ui.navigation.Destinations.NavArgs.ActorId
import dev.shreyansh.tmdb.ui.navigation.Destinations.NavArgs.MovieId

object Destinations {
    const val Home = "home"
    const val Movie = "movie"
    const val Actors = "actors"
    const val About = "about"

    object NavArgs {
        const val MovieId = "movieID"
        const val ActorId = "actorID"
    }
}

class Actions(navController: NavHostController) {
    val openMovie: (Int) -> Unit = {
        navController.navigate("$Movie/$MovieId")
    }
    val openActor: (Int) -> Unit = {
        navController.navigate("$Actors/$ActorId")
    }
    val openAbout: () -> Unit = {
        navController.navigate(About)
    }
    val pop: () -> Unit = {
        navController.popBackStack()
    }
}

@Composable
fun NavigationSetup(navController: NavHostController) {

}