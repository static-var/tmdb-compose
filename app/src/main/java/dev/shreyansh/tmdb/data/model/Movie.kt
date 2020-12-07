package dev.shreyansh.tmdb.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.shreyansh.tmdb.utils.Constants
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


@Entity(tableName = Constants.Database.MOVIE_TABLE)
@Parcelize
@Keep
@JsonClass(generateAdapter = true)
data class Movie(
    @PrimaryKey
    @Json(name = "id") val movieId: Int = -1,
    val title: String = "",
    val overview: String = "",
    val adult: Boolean = false,
    @Json(name = "release_date") val releaseDate: String = "",
    @Json(name = "original_language") val language: String = "",
    @Json(name = "vote_average") val rating: Double = Double.NaN,
    @Json(name = "vote_count") val votes: Long = 0L,
    @Json(name = "poster_path") val poster: String = "",
    @Json(name = "backdrop_path") val backdrop: String = "",
    @Json(name = "genre_ids") val genreIds: List<Int> = listOf()
) : Parcelable {
    @IgnoredOnParcel
    @Ignore
    var genres: List<Genre> = listOf()
}