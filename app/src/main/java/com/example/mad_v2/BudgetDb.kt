package com.example.mad_v2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Budget::class],
    version = 2,   // keep your new version here
    exportSchema = false
)
abstract class BudgetDb : RoomDatabase() {

    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: BudgetDb? = null

        fun getDatabase(context: Context): BudgetDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BudgetDb::class.java,
                    "budget_db"
                )
                    .fallbackToDestructiveMigration()   // ⬅️ ADD THIS
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
