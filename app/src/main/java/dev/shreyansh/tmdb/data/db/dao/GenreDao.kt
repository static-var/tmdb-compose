package dev.shreyansh.tmdb.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.shreyansh.tmdb.data.model.Genre

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(genres: List<Genre>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(genre: Genre)

    @Query("SELECT * FROM genre")
    fun getAll(): LiveData<List<Genre>>

//    @Query("SELECT * FROM genre WHERE id IN (:ids)")
//    suspend fun getGenres(ids: List<Int>): Flow<List<Genre>>
}