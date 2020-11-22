package dev.shreyansh.tmdb.data.repository

import com.github.ajalt.timberkt.Timber.e
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import dev.shreyansh.tmdb.data.api.TmdbService
import dev.shreyansh.tmdb.data.db.dao.GenreDao
import javax.inject.Inject

class TmdbRepository @Inject constructor(
    private val genreDao: GenreDao,
    private val service: TmdbService
) {
    suspend fun getAllGenreAndSave() =
        service.getListOfGenre().suspendOnSuccess {
            data?.let { response ->
                response.genres.forEach { e { "$it" } }
                genreDao.insertAll(response.genres)
            }
        }.suspendOnError {
            e { message() }
        }.suspendOnException {
            e { exception.localizedMessage }
        }


    fun getAllGenre() = genreDao.getAll()
}