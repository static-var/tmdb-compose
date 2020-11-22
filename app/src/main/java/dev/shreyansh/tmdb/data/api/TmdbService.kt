package dev.shreyansh.tmdb.data.api

import com.skydoves.sandwich.ApiResponse
import dev.shreyansh.tmdb.data.model.responses.GenreListResponse
import retrofit2.http.GET

interface TmdbService {

    @GET("/3/genre/movie/list")
    suspend fun getListOfGenre(): ApiResponse<GenreListResponse>
}