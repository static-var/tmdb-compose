package dev.shreyansh.tmdb.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dev.shreyansh.tmdb.data.model.Genre
import dev.shreyansh.tmdb.data.repository.TmdbRepository
import dev.shreyansh.tmdb.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class TmdbViewModel @ViewModelInject constructor(
    private val repository: TmdbRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    fun getListOfGenre(): LiveData<List<Genre>> =
        liveData {
            emitSource(
                repository.getAllGenre()
            )
        }

    fun getGenreFromNetwork() {
        viewModelScope.launch(ioDispatcher) {
            viewModelScope.launch(ioDispatcher) {
                repository.getAllGenreAndSave()
            }
        }
    }
}