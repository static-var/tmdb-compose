package dev.shreyansh.tmdb.data.repository

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.fresh
import dev.shreyansh.tmdb.data.api.TmdbService
import dev.shreyansh.tmdb.data.db.dao.GenreDao
import dev.shreyansh.tmdb.data.db.dao.MovieDao
import dev.shreyansh.tmdb.data.db.dao.TvShowDao
import dev.shreyansh.tmdb.data.model.Genre
import dev.shreyansh.tmdb.data.model.Movie
import dev.shreyansh.tmdb.data.model.TvShow
import dev.shreyansh.tmdb.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TmdbRepository @Inject constructor(
    private val genreDao: GenreDao,
    private val movieDao: MovieDao,
    private val tvShowDao: TvShowDao,
    private val service: TmdbService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    private lateinit var genreList: List<Genre>

    fun tvShowStore() = StoreBuilder.from(
        fetcher = Fetcher.of { _ ->
            val show = service.getTrendingShows().results
            if (::genreList.isInitialized.not())
                genreList = genreStore().fresh("")
            mapGenreToTvShows(show, genreList)
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { tvShowDao.getAll() },
            writer = { _, list -> tvShowDao.insertAll(list) }
        )
    ).build()

    fun movieStore() = StoreBuilder.from(
        fetcher = Fetcher.of { _ ->
            val movies = service.getTrendingMovies().results
            if (::genreList.isInitialized.not())
                genreList = genreStore().fresh("")
            mapGenreToMovie(movies, genreList)
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { movieDao.getAll() },
            writer = { _, list ->
                movieDao.insertAll(list)
            }
        )
    ).build()

    fun genreStore() = StoreBuilder.from(
        fetcher = Fetcher.of { _ ->
            service.getListOfMovieGenre().genres
                .plus(service.getListOfTvShowGenre().genres)
                .distinctBy { it.genreId }
                .also { genreList = it }
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { genreDao.getAll() },
            writer = { _, list -> genreDao.insertAll(list) }
        )
    ).build()

    private fun mapGenreToTvShows(tvShow: List<TvShow>, genre: List<Genre>) = tvShow.map { show ->
        show.genres = genre.filter { it.genreId in show.genreIds }
        show
    }

    private fun mapGenreToMovie(movies: List<Movie>, genre: List<Genre>) = movies.map { movie ->
        movie.genres = genre.filter { it.genreId in movie.genreIds }
        movie
    }

    fun getMovieById(movieId: Int) = movieDao.getMovie(movieId).flowOn(ioDispatcher)

    fun getTvShowById(showId: Int) = tvShowDao.getShow(showId).flowOn(ioDispatcher)
}