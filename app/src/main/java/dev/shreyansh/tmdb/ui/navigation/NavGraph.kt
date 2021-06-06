package dev.shreyansh.tmdb.ui.navigation

import androidx.navigation.NavController


sealed class Destination(val route: String) {
    object Home : Destination(DestinationConstants.HOME)

    object About : Destination(DestinationConstants.ABOUT)

    object Movie : Destination("${DestinationConstants.MOVIE}/{${Args.MovieId}}") {
        object Args {
            const val MovieId = "movieID"
        }
    }

    object TvShow : Destination("${DestinationConstants.TV_SHOW}/{${Args.TvShowId}}") {
        object Args {
            const val TvShowId = "tvShowID"
        }
    }
}


private object DestinationConstants {
    const val HOME = "home"
    const val MOVIE = "movie"
    const val TV_SHOW = "tvShow"
    const val ABOUT = "about"
}


class Actions(navController: NavController) {
    val openMovie: (Int) -> Unit = { movieId ->
        navController.navigate("${DestinationConstants.MOVIE}/$movieId")
    }
    val openTvShow: (Int) -> Unit = { tvShowId ->
        navController.navigate("${DestinationConstants.TV_SHOW}/$tvShowId")
    }
    val openAbout: () -> Unit = {
        navController.navigate(DestinationConstants.ABOUT)
    }
    val pop: () -> Unit = {
        navController.popBackStack()
    }
}