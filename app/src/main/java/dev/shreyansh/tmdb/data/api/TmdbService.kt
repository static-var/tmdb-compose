package dev.shreyansh.tmdb.data.api

import com.skydoves.sandwich.ApiResponse
import dev.shreyansh.tmdb.data.model.responses.GenreListResponse
import dev.shreyansh.tmdb.data.model.responses.MovieListResponse
import dev.shreyansh.tmdb.data.model.responses.TvShowListResponse
import retrofit2.http.GET

interface TmdbService {

    @GET("/3/genre/movie/list")
    suspend fun getListOfMovieGenre(): ApiResponse<GenreListResponse>

    @GET("/3/genre/tv/list")
    suspend fun getListOfTvShowGenre(): ApiResponse<GenreListResponse>

    @GET("/3/trending/movie/week")
    suspend fun getTrendingMovies(): ApiResponse<MovieListResponse>

    @GET("/3/trending/tv/week")
    suspend fun getTrendingShows(): ApiResponse<TvShowListResponse>
}