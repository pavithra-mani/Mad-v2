package com.example.mad_v2.data
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bill_table")
data class Bill(
    var name: String,
    var amount: Double,
    var dueDate: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)