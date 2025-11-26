package com.example.mad_v2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val amount: Double,
    val category: String,
    val date: String,
    val note: String = "",
    val paymentMode: String = ""
)
