package com.mebill.app.data.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mebill.app.data.model.Bill
import com.mebill.app.data.model.Category
import com.mebill.app.data.model.DefaultCategories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Bill::class, Category::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BillDatabase : RoomDatabase() {
    
    abstract fun billDao(): BillDao
    abstract fun categoryDao(): CategoryDao
    
    private class BillDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.categoryDao())
                }
            }
        }
        
        suspend fun populateDatabase(categoryDao: CategoryDao) {
            // Delete all content here.
            // Add default categories
            categoryDao.insertCategories(DefaultCategories.categories)
        }
    }
    
    companion object {
        @Volatile
        private var INSTANCE: BillDatabase? = null
        
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): BillDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BillDatabase::class.java,
                    "bill_database"
                )
                .addCallback(BillDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}