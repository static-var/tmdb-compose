package dev.shreyansh.tmdb.data.repository

import com.dropbox.android.external.store4.*
import dev.shreyansh.tmdb.data.api.TmdbService
import dev.shreyansh.tmdb.data.db.dao.GenreDao
import dev.shreyansh.tmdb.data.db.dao.MovieDao
import dev.shreyansh.tmdb.data.db.dao.TvShowDao
import dev.shreyansh.tmdb.data.model.Genre
import dev.shreyansh.tmdb.data.model.Movie
import dev.shreyansh.tmdb.data.model.TvShow
import dev.shreyansh.tmdb.di.IoDispatcher
import dev.shreyansh.tmdb.utils.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.time.Duration

class TmdbRepository @Inject constructor(
    private val genreDao: GenreDao,
    private val movieDao: MovieDao,
    private val tvShowDao: TvShowDao,
    private val service: TmdbService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    private lateinit var genreList: List<Genre>

    fun tvShowStore() = StoreBuilder.from(
        fetcher = Fetcher.of<String, List<TvShow>> { _ ->
            val show = service.getTrendingShows().results
            if (::genreList.isInitialized.not())
                genreList = genreStore().get("")
            mapGenreToTvShows(show, genreList)
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { tvShowDao.getAll() },
            writer = { _, list -> tvShowDao.insertAll(list) }
        )
    ).cachePolicy(
        MemoryPolicy
            .MemoryPolicyBuilder<String, List<TvShow>>()
            .setExpireAfterAccess(Duration.minutes(5))
            .build()
    ).build()

    fun movieStore() =
        StoreBuilder.from(
            fetcher = Fetcher.of<String, List<Movie>> { _ ->
                val movies = service.getTrendingMovies().results
                if (::genreList.isInitialized.not())
                    genreList = genreStore().get("")
                mapGenreToMovie(movies, genreList)
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { movieDao.getAll() },
                writer = { _, list ->
                    movieDao.insertAll(list)
                }
            )
        ).cachePolicy(
            MemoryPolicy
                .MemoryPolicyBuilder<String, List<Movie>>()
                .setExpireAfterAccess(Duration.minutes(5))
                .build()
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