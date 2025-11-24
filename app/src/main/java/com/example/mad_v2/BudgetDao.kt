package com.example.mad_v2.data

import androidx.room.*

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Budget)

    @Update
    suspend fun updateItem(item: Budget)

    @Delete
    suspend fun deleteItem(item: Budget)

    @Query("SELECT * FROM budget_items ORDER BY id DESC")
    suspend fun getAllItems(): List<Budget>

    @Query("SELECT SUM(amount) FROM budget_items")
    suspend fun getTotalAllocated(): Double?
}
