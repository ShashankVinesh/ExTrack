package com.shashank.extrack.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shashank.extrack.data.local.entity.TransactionEntity

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)

    // HOME: only active (not deleted)
    @Query("SELECT * FROM transactions WHERE isDeleted = 0 ORDER BY date DESC")
    fun getActiveTransactions(): LiveData<List<TransactionEntity>>

    // HISTORY: only deleted (sort by deletedAt newest first)
    @Query("SELECT * FROM transactions WHERE isDeleted = 1 ORDER BY deletedAt DESC")
    fun getDeletedTransactions(): LiveData<List<TransactionEntity>>

    // Totals should count only active
    @Query("SELECT SUM(amount) FROM transactions WHERE isDeleted = 0 AND isIncome = 1")
    fun getTotalIncome(): LiveData<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE isDeleted = 0 AND isIncome = 0")
    fun getTotalExpense(): LiveData<Double?>

    // HISTORY filter by deletedAt time range
    @Query("""
        SELECT * FROM transactions
        WHERE isDeleted = 1 AND deletedAt BETWEEN :start AND :end
        ORDER BY deletedAt DESC
    """)
    fun getDeletedBetween(start: Long, end: Long): LiveData<List<TransactionEntity>>

    // Soft delete ONE: set isDeleted + deletedAt
    @Query("""
        UPDATE transactions
        SET isDeleted = 1, deletedAt = :deletedAt
        WHERE id = :id AND isDeleted = 0
    """)
    suspend fun softDelete(id: Int, deletedAt: Long)

    // Bulk soft-delete for auto delete: delete old active items
    @Query("""
        UPDATE transactions
        SET isDeleted = 1, deletedAt = :deletedAt
        WHERE isDeleted = 0 AND date < :cutoff
    """)
    suspend fun softDeleteOlderThan(cutoff: Long, deletedAt: Long): Int

    // Clear only deleted (History "Clear All")
    @Query("DELETE FROM transactions WHERE isDeleted = 1")
    suspend fun clearDeleted()

    // (Optional) if you ever need it
    @Query("DELETE FROM transactions")
    suspend fun clearAll()
}