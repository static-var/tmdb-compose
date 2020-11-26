package dev.shreyansh.tmdb.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.shreyansh.tmdb.data.model.Genre
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(genres: List<Genre>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(genre: Genre)

    @Query("SELECT * FROM genre")
    fun getAll(): Flow<List<Genre>>

    @Query("SELECT * FROM genre WHERE genreId IN (:ids)")
    fun getGenres(ids: List<Int>): List<Genre>
}