package dev.shreyansh.tmdb.data.db.dao

import androidx.room.*
import dev.shreyansh.tmdb.data.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(genres: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(genre: Movie)

    @Transaction
    @Query("SELECT * FROM movie")
    fun getAll(): Flow<List<Movie>>

    @Query("SELECT * FROM movie WHERE movieId = :id")
    fun getMovie(id: Int): Flow<Movie>
}