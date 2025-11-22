package com.example.mad_v2

import android.app.Application
import com.example.mad_v2.data.BillDatabase // <--- CHANGED: Import BillDatabase
import com.example.mad_v2.data.BillRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Custom Application class used to initialize the Room database and Repository.
 * This class is referenced in the AndroidManifest.xml.
 */
class BillReminderApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using lazy so the database and repository are only created when they're needed
    // rather than when the app starts.
    val database by lazy { BillDatabase.getDatabase(this) } // <--- CHANGED: Use BillDatabase
    val repository by lazy { BillRepository(database.billDao()) }
}