package dev.shreyansh.tmdb.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.shreyansh.tmdb.utils.Constants
import kotlinx.parcelize.Parcelize

@Entity(tableName = Constants.Database.TV_SHOW_TABLE)
@Parcelize
@Keep
@JsonClass(generateAdapter = true)
data class TvShow(
    @PrimaryKey
    @Json(name = "id") val tvShowId: Int = -1,
    val name: String = "",
    val overview: String = "",
    @Json(name = "first_air_date") val firstAirDate: String = "",
    @Json(name = "original_language") val language: String = "",
    @Json(name = "vote_average") val rating: Double = Double.NaN,
    @Json(name = "vote_count") val votes: Long = 0L,
    @Json(name = "poster_path") val poster: String = "",
    @Json(name = "backdrop_path") val backdrop: String = "",
    @Json(name = "genre_ids") val genreIds: List<Int> = listOf(),
    var genres: List<Genre> = listOf()
) : Parcelable