package com.shashank.extrack.ui.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.shashank.extrack.data.local.repository.TransactionRepository
import kotlinx.coroutines.launch
import java.util.Calendar


class HistoryViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    val filterType = MutableLiveData("ALL")

    val transactions = filterType.switchMap { type ->
        val now = System.currentTimeMillis()

        when (type) {
            "ALL" -> repository.deletedTransactions

            "DAY" -> repository.getDeletedBetween(startOfTodayMillis(), now)

            "WEEK" -> repository.getDeletedBetween(startOfWeekMillis(), now)

            "MONTH" -> repository.getDeletedBetween(startOfMonthMillis(), now)

            else -> repository.deletedTransactions
        }
    }

    fun setFilter(type: String) {
        filterType.value = type
    }

    fun clearDeleted() = viewModelScope.launch {
        repository.clearDeleted()
    }

    private fun startOfTodayMillis(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun startOfWeekMillis(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // Go to first day of this week (device locale)
            set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        }.timeInMillis
    }

    private fun startOfMonthMillis(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            set(Calendar.DAY_OF_MONTH, 1)
        }.timeInMillis
    }
}
