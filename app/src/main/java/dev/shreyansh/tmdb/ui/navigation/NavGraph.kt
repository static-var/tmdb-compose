package dev.shreyansh.tmdb.ui.navigation

import androidx.navigation.NavController
import dev.shreyansh.tmdb.ui.navigation.Destinations.About
import dev.shreyansh.tmdb.ui.navigation.Destinations.Actors
import dev.shreyansh.tmdb.ui.navigation.Destinations.Movie
import dev.shreyansh.tmdb.ui.navigation.Destinations.TvShow

object Destinations {
    const val Home = "home"
    const val Movie = "movie"
    const val TvShow = "tvShow"
    const val Actors = "actors"
    const val About = "about"

    object NavArgs {
        const val MovieId = "movieID"
        const val TvShowId = "tvShowID"
        const val ActorId = "actorID"
    }
}

class Actions(navController: NavController) {
    val openMovie: (Int) -> Unit = { movieId ->
        navController.navigate("$Movie/$movieId")
    }
    val openTvShow: (Int) -> Unit = { tvShowId ->
        navController.navigate("$TvShow/$tvShowId")
    }
    val openActor: (Int) -> Unit = { actorId ->
        navController.navigate("$Actors/$actorId")
    }
    val openAbout: () -> Unit = {
        navController.navigate(About)
    }
    val pop: () -> Unit = {
        navController.popBackStack()
    }
}