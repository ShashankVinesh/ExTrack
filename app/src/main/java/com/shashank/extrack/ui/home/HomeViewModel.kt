package com.shashank.extrack.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashank.extrack.data.local.entity.TransactionEntity
import com.shashank.extrack.data.local.repository.TransactionRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.map

class HomeViewModel(private val repository: TransactionRepository) : ViewModel() {

    val transactions: LiveData<List<TransactionEntity>> = repository.activeTransactions

    val totalIncome: LiveData<Double> = repository.totalIncome.map { it ?: 0.0 }
    val totalExpense: LiveData<Double> = repository.totalExpense.map { it ?: 0.0 }

    val totalProfit: LiveData<Double> = MediatorLiveData<Double>().apply {
        var income = 0.0
        var expense = 0.0
        fun update() { value = income - expense }

        addSource(totalIncome) { income = it; update() }
        addSource(totalExpense) { expense = it; update() }
    }

    fun insert(transaction: TransactionEntity) = viewModelScope.launch {
        repository.insert(transaction)
    }

    fun softDelete(id: Int) = viewModelScope.launch {
        repository.softDelete(id, System.currentTimeMillis())
    }
}