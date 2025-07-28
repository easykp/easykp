package com.mebill.app

import android.app.Application
import com.mebill.app.data.database.BillDatabase
import com.mebill.app.data.repository.BillRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BillApplication : Application() {
    
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val applicationScope = CoroutineScope(SupervisorJob())
    
    val database by lazy { BillDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { BillRepository(database.billDao(), database.categoryDao()) }
}