package dev.shreyansh.tmdb.data.model.responses

import dev.shreyansh.tmdb.data.model.Genre

data class GenreListResponse(
    val genres: List<Genre>
)