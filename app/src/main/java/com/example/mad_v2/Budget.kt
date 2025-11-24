package com.example.mad_v2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_items")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "",
    val amount: Double,
    val category: String,
    val percentage: Int = 0
)
