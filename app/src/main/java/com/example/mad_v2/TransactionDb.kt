package com.example.mad_v2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Database version is set to 3 to incorporate Transaction, Budget, and Bill entities
@Database(entities = [Transaction::class, Budget::class, Bill::class], version = 3, exportSchema = false)
abstract class TransactionDb : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun billDao(): BillDao

    companion object {
        @Volatile
        private var INSTANCE: TransactionDb? = null

        fun getDatabase(context: Context): TransactionDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDb::class.java,
                    "expense_db"
                )
                    // FIX: Allows the database to be recreated when the version changes (e.g., from 2 to 3)
                    // This resolves the "migration not found" IllegalStateException.
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}