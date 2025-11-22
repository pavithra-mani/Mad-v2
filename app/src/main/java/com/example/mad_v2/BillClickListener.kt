package com.example.mad_v2.adapters

import com.example.mad_v2.data.Bill // UPDATED IMPORT

interface BillClickListener {
    fun onModifyBill(bill: Bill)
    fun onDeleteBill(bill: Bill)
}