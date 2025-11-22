package com.example.mad_v2.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BillViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BillRepository
    val allBills: LiveData<List<Bill>>

    init {
        val billDao = BillDatabase.getDatabase(application).billDao()
        repository = BillRepository(billDao)
        allBills = repository.allBills
    }


    fun insert(bill: Bill) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(bill)
    }

    fun update(bill: Bill) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(bill)
    }

    fun delete(bill: Bill) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(bill)
    }
}