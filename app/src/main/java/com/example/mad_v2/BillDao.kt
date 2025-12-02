package com.example.mad_v2.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bill: Bill)

    @Update
    suspend fun update(bill: Bill)

    @Delete
    suspend fun delete(bill: Bill)

    @Query("SELECT * FROM bill_table ORDER BY dueDate ASC")
    fun getAllBills(): LiveData<List<Bill>>

    @Query("SELECT * FROM bill_table WHERE id = :billId")
    suspend fun getBillById(billId: Int): Bill?

    @Query("SELECT * FROM bill_table ORDER BY dueDate ASC")
    suspend fun getAllBillsForDashboard(): List<Bill>
}