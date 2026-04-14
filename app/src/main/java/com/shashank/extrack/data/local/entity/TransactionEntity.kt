package com.shashank.extrack.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val date: Long,
    val isIncome: Boolean,
    val amount: Double,
    val isDeleted: Boolean = false,

    val deletedAt: Long? = null
) : Parcelable
