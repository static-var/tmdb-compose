package dev.shreyansh.tmdb.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shreyansh.tmdb.data.db.TmdbDatabase
import dev.shreyansh.tmdb.data.db.dao.GenreDao
import dev.shreyansh.tmdb.utils.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    @Singleton
    fun provideTmdbDatabase(application: Application): TmdbDatabase {
        return Room.databaseBuilder(
            application,
            TmdbDatabase::class.java,
            Constants.Database.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideGenreDao(db: TmdbDatabase): GenreDao {
        return db.genreDao()
    }
}