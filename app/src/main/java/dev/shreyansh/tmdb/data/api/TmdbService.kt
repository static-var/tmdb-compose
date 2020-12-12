package dev.shreyansh.tmdb.data.api

import dev.shreyansh.tmdb.data.model.responses.GenreListResponse
import dev.shreyansh.tmdb.data.model.responses.MovieListResponse
import dev.shreyansh.tmdb.data.model.responses.TvShowListResponse
import retrofit2.http.GET

interface TmdbService {

    @GET("/3/genre/movie/list")
    suspend fun getListOfMovieGenre(): GenreListResponse

    @GET("/3/genre/tv/list")
    suspend fun getListOfTvShowGenre(): GenreListResponse

    @GET("/3/trending/movie/week")
    suspend fun getTrendingMovies(): MovieListResponse

    @GET("/3/trending/tv/week")
    suspend fun getTrendingShows(): TvShowListResponse
}