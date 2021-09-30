package com.example.pichurchyk_p3.ui.feedFragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.pichurchyk_p3.model.UnsplashRepository
import com.example.pichurchyk_p3.room.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class FeedViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }


    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    fun addQuery() {
        viewModelScope.launch(Dispatchers.Default) {
            delay(2000)
            val total = repository.getTotalSearchResults(getCurrentQuery())
            repository.addQuery(Query(getCurrentQuery(), false, total, getCurrentQueryDate()))
        }
    }

    private fun getCurrentQueryDate(): String {
        return DateTime.now().toString()
    }

    fun getCurrentQuery(): String {
        return currentQuery.value!!
    }

    companion object {
        private const val DEFAULT_QUERY = "San Francisco"
    }
}