package com.example.mad_v2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Transaction::class, Budget::class], version = 2)
abstract class TransactionDb : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: TransactionDb? = null

        fun getDatabase(context: Context): TransactionDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDb::class.java,
                    "expense_db"
                ).build().also { INSTANCE = it }
            }
    }
}
