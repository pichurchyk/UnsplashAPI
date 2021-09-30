package com.example.pichurchyk_p3.ui.historyFragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pichurchyk_p3.model.UnsplashRepository
import com.example.pichurchyk_p3.room.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Minutes
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class HistoryViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository,
) : ViewModel() {

    val allQueries: Flow<List<Query>> = repository.allQueries

    fun changeQueryLikeState(query: Query) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.changeQueryLikeState(query)
        }
    }

    fun deleteQueries() {
        viewModelScope.launch(Dispatchers.Default) {
            repository.deleteQueries()
        }
    }

    fun compareDates(date: String): String {
        val currentDate = DateTime.now()
        val queryDate = DateTime.parse(date)

        val daysBetween = Days.daysBetween(queryDate, currentDate)
        val hoursBetween = Hours.hoursBetween(queryDate, currentDate)
        val minutesBetween = Minutes.minutesBetween(queryDate, currentDate)

        val formatter: DateTimeFormatter = DateTimeFormat.forPattern("MMMM")
        val month = formatter.print(queryDate)

        when (daysBetween.days) {
            0 -> when (hoursBetween.hours) {
                0 -> return if (minutesBetween.minutes <= 5) "just now" else "${minutesBetween.minutes} min ago"
                in 1..24 -> return "${hoursBetween.hours} hours ago"
            }
            1 -> return "yesterday"
            in 1..9999 -> "${queryDate.dayOfMonth().asString} $month"
        }
        return ""
    }
}