package dev.shreyansh.tmdb.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.shreyansh.tmdb.data.db.dao.GenreDao
import dev.shreyansh.tmdb.data.db.dao.MovieDao
import dev.shreyansh.tmdb.data.db.dao.TvShowDao
import dev.shreyansh.tmdb.data.model.Genre
import dev.shreyansh.tmdb.data.model.Movie
import dev.shreyansh.tmdb.data.model.TvShow

@Database(
    entities = [
        Genre::class,
        Movie::class,
        TvShow::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(TmdbTypeConverters::class)
abstract class TmdbDatabase: RoomDatabase() {

    abstract fun genreDao(): GenreDao
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao

}