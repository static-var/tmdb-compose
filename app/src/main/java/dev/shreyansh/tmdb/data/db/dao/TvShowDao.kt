package dev.shreyansh.tmdb.data.db.dao

import androidx.room.*
import dev.shreyansh.tmdb.data.model.TvShow
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(genres: List<TvShow>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(genre: TvShow)

    @Transaction
    @Query("SELECT * FROM tv_show")
    fun getAll(): Flow<List<TvShow>>

    @Query("SELECT * FROM tv_show WHERE tvShowId = :id")
    fun getShow(id: Int): Flow<TvShow>
}