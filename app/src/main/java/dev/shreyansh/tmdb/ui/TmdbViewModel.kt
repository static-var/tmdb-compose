package dev.shreyansh.tmdb.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import dev.shreyansh.tmdb.data.model.MediaContentType
import dev.shreyansh.tmdb.data.repository.TmdbRepository
import dev.shreyansh.tmdb.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TmdbViewModel @ViewModelInject constructor(
    private val repository: TmdbRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    fun getListOfGenre() =
        liveData {
            emit(Loading())
            repository.getAllGenre().collect {
                if (it.isEmpty())
                    getGenreFromNetwork()
                else
                    emit(Success(it))
            }
        }

    fun getGenreFromNetwork() {
        viewModelScope.launch(ioDispatcher) {
            repository.getAllGenreAndSave()
        }
    }

    fun getTrendingMoviesFromNetwork() {
        viewModelScope.launch(ioDispatcher) {
            repository.getMoviesAndSave()
        }
    }

    fun getTrendingTvShowFromNetwork() {
        viewModelScope.launch(ioDispatcher) {
            repository.getTvShowAndSave()
        }
    }

    fun getListTrendingMovies() =
        liveData {
            emitSource(repository.getMovies().map {
                if (it.isEmpty())
                    repository.getMoviesAndSave()
                Success(it)
            }.asLiveData(ioDispatcher))
        }

    fun getListTrendingTvShows() =
        liveData {
            emitSource(repository.getTvShows().map {
                if (it.isEmpty())
                    repository.getTvShowAndSave()
                Success(it)
            }.asLiveData(ioDispatcher))
        }

    fun getMovieById(movieId: Int) =
        liveData {
            emitSource(repository.getMovieById(movieId).map { Success(it) }
                .asLiveData(ioDispatcher))
        }

    fun getTvShowById(showId: Int) =
        liveData {
            emitSource(repository.getTvShowById(showId).map { Success(it) }
                .asLiveData(ioDispatcher))
        }

    private val mode = MutableLiveData<MediaContentType>()

    fun setUiMode(type: MediaContentType) {
        mode.value = type
    }

    fun getUiMode() = liveData<MediaContentType> { emitSource(mode) }

}