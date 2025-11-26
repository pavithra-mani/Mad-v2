package com.example.mad_v2.data

import com.example.mad_v2.data.TransactionDao

class TransactionRepository(private val transactionDao: TransactionDao, private val budgetDao: BudgetDao) {

    val allTransactions = transactionDao.getAllTransactions()

    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction)
    }

    suspend fun addTransaction(transaction: Transaction) {
        transactionDao.insert(transaction)
        budgetDao.reduceBudget(transaction.amount, transaction.category)
    }
}
