package com.example.mad_v2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mad_v2.data.*
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TransactionRepository
    val transactionList: LiveData<List<Transaction>>

    init {
        val db = TransactionDb.getDatabase(application)
        val transactionDao = db.transactionDao()
        val budgetDao = db.budgetDao()
        repository = TransactionRepository(transactionDao, budgetDao)
        transactionList = repository.allTransactions
    }

    fun saveTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.addTransaction(transaction)
        }
    }
}
