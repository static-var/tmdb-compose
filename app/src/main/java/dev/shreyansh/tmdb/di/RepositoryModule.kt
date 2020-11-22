package dev.shreyansh.tmdb.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dev.shreyansh.tmdb.data.api.TmdbService
import dev.shreyansh.tmdb.data.db.dao.GenreDao
import dev.shreyansh.tmdb.data.repository.TmdbRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideTmdbRepository(genreDao: GenreDao, service: TmdbService): TmdbRepository {
        return TmdbRepository(genreDao, service)
    }
}