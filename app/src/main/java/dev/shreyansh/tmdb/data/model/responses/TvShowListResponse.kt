package dev.shreyansh.tmdb.data.model.responses

import dev.shreyansh.tmdb.data.model.TvShow

data class TvShowListResponse(
    val results: List<TvShow>
)