package com.example.mad_v2.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {

    // Original methods (keeping for compatibility)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>

    // Additional methods needed for StatsActivity
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    suspend fun getAllTransactionsForStats(): List<Transaction>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): Transaction?

    @Query("SELECT * FROM transactions WHERE category = :category ORDER BY date DESC")
    suspend fun getTransactionsByCategory(category: String): List<Transaction>

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    suspend fun getTransactionsBetweenDates(startDate: String, endDate: String): List<Transaction>

    @Query("SELECT SUM(amount) FROM transactions")
    suspend fun getTotalSpent(): Double?

    @Query("SELECT SUM(amount) FROM transactions WHERE category = :category")
    suspend fun getTotalSpentByCategory(category: String): Double?

    @Query("SELECT SUM(amount) FROM transactions WHERE date = :date")
    suspend fun getTotalSpentOnDate(date: String): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()

    @Query("SELECT DISTINCT category FROM transactions ORDER BY category ASC")
    suspend fun getAllCategories(): List<String>

    @Query("SELECT COUNT(*) FROM transactions")
    suspend fun getTransactionCount(): Int

    @Query("SELECT COUNT(*) FROM transactions WHERE category = :category")
    suspend fun getTransactionCountByCategory(category: String): Int
}