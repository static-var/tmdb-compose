package dev.shreyansh.tmdb.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.shreyansh.tmdb.utils.Constants
import kotlinx.android.parcel.Parcelize

@Entity(tableName = Constants.Database.GENRE_TABLE)
@Parcelize
@Keep
@JsonClass(generateAdapter = true)
data class Genre(
    @PrimaryKey
    @Json(name = "id") val genreId: Int = -1,
    val name: String = ""
) : Parcelable