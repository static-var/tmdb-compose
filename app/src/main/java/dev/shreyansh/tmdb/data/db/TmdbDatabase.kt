package dev.shreyansh.tmdb.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.shreyansh.tmdb.data.db.dao.GenreDao
import dev.shreyansh.tmdb.data.model.Genre

@Database(
    entities = [
        Genre::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TmdbDatabase: RoomDatabase() {

    abstract fun genreDao(): GenreDao

}