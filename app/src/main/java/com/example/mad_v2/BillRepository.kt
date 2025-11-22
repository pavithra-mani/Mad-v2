package com.example.mad_v2.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class BillRepository(private val billDao: BillDao) {
    val allBills: LiveData<List<Bill>> = billDao.getAllBills()

    suspend fun insert(bill: Bill) {
        billDao.insert(bill)
    }

    suspend fun update(bill: Bill) {
        billDao.update(bill)
    }

    suspend fun delete(bill: Bill) {
        billDao.delete(bill)
    }
}