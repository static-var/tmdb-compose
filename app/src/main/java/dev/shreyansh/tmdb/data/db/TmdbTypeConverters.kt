package dev.shreyansh.tmdb.data.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dev.shreyansh.tmdb.data.model.Genre
import javax.inject.Inject
import javax.inject.Singleton

@ProvidedTypeConverter
@Singleton
class TmdbTypeConverters @Inject constructor(private val moshi: Moshi) {

    private val listOfGenreAdapter by lazy {
        moshi.adapter<List<Genre>>(
            Types.newParameterizedType(
                List::class.java,
                Genre::class.java
            )
        )
    }

    @TypeConverter
    fun intArrayToString(genreIds: List<Int>?): String? =
        genreIds?.map { it }?.joinToString(separator = " ")

    @TypeConverter
    fun stringToIntArray(genreIds: String?): List<Int>? =
        genreIds?.split(" ")?.map { it.toInt() }?.toList()

    @TypeConverter
    fun genreArrayToString(genre: List<Genre>): String? {
        return  listOfGenreAdapter.toJson(genre)
    }

    @TypeConverter
    fun stringToGenreArray(json: String): List<Genre>? {
        return listOfGenreAdapter.fromJson(json)
    }

}