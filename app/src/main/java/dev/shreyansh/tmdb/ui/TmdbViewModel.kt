package dev.shreyansh.tmdb.ui

import androidx.lifecycle.*
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.dropbox.android.external.store4.fresh
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shreyansh.tmdb.data.model.MediaContentType
import dev.shreyansh.tmdb.data.model.Movie
import dev.shreyansh.tmdb.data.model.TvShow
import dev.shreyansh.tmdb.data.repository.TmdbRepository
import dev.shreyansh.tmdb.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TmdbViewModel @Inject constructor(
    private val repository: TmdbRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

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

    fun getMovies() = liveData<UiState<List<Movie>>>(ioDispatcher) {
        repository.movieStore().stream(StoreRequest.cached("", false))
            .collect { storeResponse ->
                when (storeResponse) {
                    is StoreResponse.Loading -> emit(Loading())
                    is StoreResponse.Data -> {
                        val data = storeResponse.value
                        if (data.isNotEmpty())
                            emit(Success(data))
                        else
                            emit(Loading())
                    }
                    is StoreResponse.Error.Exception -> emit(
                        Error(
                            storeResponse.error.message ?: "Unable to fetch data!6"
                        )
                    )
                    is StoreResponse.Error.Message -> emit(Error(storeResponse.message))
                }
            }
    }

    fun getTvShows() = liveData<UiState<List<TvShow>>>(ioDispatcher) {
        repository.tvShowStore().stream(StoreRequest.cached("", false))
            .collect { storeResponse ->
                when (storeResponse) {
                    is StoreResponse.Loading -> emit(Loading())
                    is StoreResponse.Data -> {
                        val data = storeResponse.value
                        if (data.isNotEmpty())
                            emit(Success(data))
                        else
                            emit(Loading())
                    }
                    is StoreResponse.Error.Exception -> emit(
                        Error(
                            storeResponse.error.message ?: "Unable to fetch data!6"
                        )
                    )
                    is StoreResponse.Error.Message -> emit(Error(storeResponse.message))
                }
            }
    }

    fun refreshMovies() {
        viewModelScope.launch {
            repository.movieStore().fresh("")
        }
    }

    fun refreshTvShows() {
        viewModelScope.launch {
            repository.tvShowStore().fresh("")
        }
    }

    fun refreshGenre() {
        viewModelScope.launch {
            repository.genreStore().fresh("")
        }
    }

}