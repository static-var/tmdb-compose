package dev.shreyansh.tmdb.data.repository

import com.github.ajalt.timberkt.Timber.e
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import dev.shreyansh.tmdb.data.api.TmdbService
import dev.shreyansh.tmdb.data.db.dao.GenreDao
import dev.shreyansh.tmdb.data.db.dao.MovieDao
import dev.shreyansh.tmdb.data.db.dao.TvShowDao
import dev.shreyansh.tmdb.data.model.Genre
import dev.shreyansh.tmdb.data.model.Movie
import dev.shreyansh.tmdb.data.model.TvShow
import dev.shreyansh.tmdb.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TmdbRepository @Inject constructor(
    private val genreDao: GenreDao,
    private val movieDao: MovieDao,
    private val tvShowDao: TvShowDao,
    private val service: TmdbService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    lateinit var genreList: List<Genre>

    suspend fun getAllGenreAndSave() {
        service.getListOfMovieGenre().suspendOnSuccess {
            data?.let { response ->
                genreList = response.genres
                genreDao.insertAll(response.genres)
            }
        }.suspendOnError {
            e { message() }
        }.suspendOnException {
            e { exception.localizedMessage }
        }

        service.getListOfTvShowGenre().suspendOnSuccess {
            data?.let { response ->
                genreDao.insertAll(response.genres)
            }
        }.suspendOnError {
            e { message() }
        }.suspendOnException {
            e { exception.localizedMessage }
        }
    }

    suspend fun getAllGenre() = flow {
        genreDao.getAll().collect {
            if (it.isEmpty()) {
                getAllGenreAndSave()
            } else {
                emit(it)
            }
        }
    }.flowOn(ioDispatcher)

    suspend fun getMoviesAndSave() {
        service.getTrendingMovies().suspendOnSuccess {
            data?.let { response ->
                e { " inserting value from api call " }
                movieDao.insertAll(response.results)
            }
        }.suspendOnError {
            e { message() }
        }.suspendOnException {
            e { exception.localizedMessage }
        }
    }

    suspend fun getTvShowAndSave() {
        service.getTrendingShows().suspendOnSuccess {
            data?.let { response ->
                tvShowDao.insertAll(response.results)
            }
        }.suspendOnError {
            e { message() }
        }.suspendOnException {
            e { exception.localizedMessage }
        }
    }

    suspend fun getMovies() = flow {
        movieDao.getAll().collect { movies ->
            if (movies.isEmpty())
                getMoviesAndSave()
            else {
                if (::genreList.isInitialized.not()) {
                    genreDao.getAll().collect {
                        genreList = it
                        emit(mapGenreToMovie(movies, genreList))
                    }
                } else
                    emit(mapGenreToMovie(movies, genreList))
            }
        }
    }.flowOn(ioDispatcher)

    suspend fun getTvShows() = flow {
        tvShowDao.getAll().collect { shows ->
            if (shows.isEmpty()) {
                getTvShowAndSave()
            } else {
                if (::genreList.isInitialized.not())
                    genreDao.getAll().collect {
                        genreList = it
                        emit(mapGenreToTvShows(shows, genreList))
                    }
                else
                    emit(mapGenreToTvShows(shows, genreList))
            }
        }
    }.flowOn(ioDispatcher)

    private fun mapGenreToTvShows(tvShow: List<TvShow>, genre: List<Genre>): List<TvShow> {
        tvShow.forEach { show ->
            show.genres = genre.filter { it.genreId in show.genreIds }
        }
        return tvShow
    }

    private fun mapGenreToMovie(movies: List<Movie>, genre: List<Genre>): List<Movie> {
        movies.forEach { movie ->
            movie.genres = genre.filter { it.genreId in movie.genreIds }
        }
        return movies
    }
}