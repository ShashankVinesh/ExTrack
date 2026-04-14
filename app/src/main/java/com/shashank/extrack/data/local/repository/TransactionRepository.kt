package com.shashank.extrack.data.local.repository

import androidx.lifecycle.LiveData
import com.shashank.extrack.data.local.dao.TransactionDao
import com.shashank.extrack.data.local.entity.TransactionEntity

class TransactionRepository(private val dao: TransactionDao) {

    suspend fun insert(transaction: TransactionEntity) = dao.insert(transaction)

    val activeTransactions: LiveData<List<TransactionEntity>> = dao.getActiveTransactions()
    val deletedTransactions: LiveData<List<TransactionEntity>> = dao.getDeletedTransactions()

    val totalIncome: LiveData<Double?> = dao.getTotalIncome()
    val totalExpense: LiveData<Double?> = dao.getTotalExpense()

    fun getDeletedBetween(start: Long, end: Long): LiveData<List<TransactionEntity>> =
        dao.getDeletedBetween(start, end)

    suspend fun softDelete(id: Int, deletedAt: Long) = dao.softDelete(id, deletedAt)

    suspend fun softDeleteOlderThan(cutoff: Long, deletedAt: Long): Int =
        dao.softDeleteOlderThan(cutoff, deletedAt)

    suspend fun clearDeleted() = dao.clearDeleted()
}