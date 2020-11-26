package dev.shreyansh.tmdb.data.model.responses

import dev.shreyansh.tmdb.data.model.Movie

data class MovieListResponse(
    val results: List<Movie>
)