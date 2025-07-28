package com.mebill.app.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mebill.app.data.model.Bill
import com.mebill.app.data.model.Priority
import java.math.BigDecimal
import java.util.*

@Dao
interface BillDao {
    
    @Query("SELECT * FROM bills ORDER BY dueDate ASC")
    fun getAllBills(): LiveData<List<Bill>>
    
    @Query("SELECT * FROM bills WHERE id = :billId")
    suspend fun getBillById(billId: Long): Bill?
    
    @Query("SELECT * FROM bills WHERE isPaid = 0 ORDER BY dueDate ASC")
    fun getUnpaidBills(): LiveData<List<Bill>>
    
    @Query("SELECT * FROM bills WHERE isPaid = 1 ORDER BY paidDate DESC")
    fun getPaidBills(): LiveData<List<Bill>>
    
    @Query("SELECT * FROM bills WHERE dueDate < :today AND isPaid = 0 ORDER BY dueDate ASC")
    fun getOverdueBills(today: Date = Date()): LiveData<List<Bill>>
    
    @Query("SELECT * FROM bills WHERE dueDate BETWEEN :startDate AND :endDate ORDER BY dueDate ASC")
    fun getBillsBetweenDates(startDate: Date, endDate: Date): LiveData<List<Bill>>
    
    @Query("SELECT * FROM bills WHERE category = :category ORDER BY dueDate ASC")
    fun getBillsByCategory(category: String): LiveData<List<Bill>>
    
    @Query("SELECT * FROM bills WHERE isReceivable = :isReceivable ORDER BY dueDate ASC")
    fun getBillsByType(isReceivable: Boolean): LiveData<List<Bill>>
    
    @Query("SELECT * FROM bills WHERE priority = :priority ORDER BY dueDate ASC")
    fun getBillsByPriority(priority: Priority): LiveData<List<Bill>>
    
    @Query("SELECT * FROM bills WHERE name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' ORDER BY dueDate ASC")
    fun searchBills(searchQuery: String): LiveData<List<Bill>>
    
    @Query("SELECT * FROM bills WHERE dueDate BETWEEN :today AND :futureDate AND isPaid = 0 ORDER BY dueDate ASC")
    fun getUpcomingBills(today: Date = Date(), futureDate: Date): LiveData<List<Bill>>
    
    @Query("SELECT SUM(amount) FROM bills WHERE isPaid = 0 AND isReceivable = 0")
    suspend fun getTotalUnpaidExpenses(): BigDecimal?
    
    @Query("SELECT SUM(amount) FROM bills WHERE isPaid = 0 AND isReceivable = 1")
    suspend fun getTotalUnpaidIncome(): BigDecimal?
    
    @Query("SELECT SUM(amount) FROM bills WHERE isPaid = 1 AND isReceivable = 0 AND paidDate BETWEEN :startDate AND :endDate")
    suspend fun getTotalPaidExpensesBetweenDates(startDate: Date, endDate: Date): BigDecimal?
    
    @Query("SELECT SUM(amount) FROM bills WHERE isPaid = 1 AND isReceivable = 1 AND paidDate BETWEEN :startDate AND :endDate")
    suspend fun getTotalPaidIncomeBetweenDates(startDate: Date, endDate: Date): BigDecimal?
    
    @Query("SELECT category, SUM(amount) as total FROM bills WHERE isPaid = 1 AND paidDate BETWEEN :startDate AND :endDate GROUP BY category")
    suspend fun getExpensesByCategory(startDate: Date, endDate: Date): List<CategoryExpense>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: Bill): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBills(bills: List<Bill>)
    
    @Update
    suspend fun updateBill(bill: Bill)
    
    @Delete
    suspend fun deleteBill(bill: Bill)
    
    @Query("DELETE FROM bills WHERE id = :billId")
    suspend fun deleteBillById(billId: Long)
    
    @Query("UPDATE bills SET isPaid = :isPaid, paidDate = :paidDate WHERE id = :billId")
    suspend fun markBillAsPaid(billId: Long, isPaid: Boolean, paidDate: Date?)
    
    @Query("SELECT COUNT(*) FROM bills WHERE isPaid = 0 AND dueDate < :today")
    suspend fun getOverdueBillsCount(today: Date = Date()): Int
    
    @Query("SELECT COUNT(*) FROM bills WHERE isPaid = 0 AND dueDate BETWEEN :today AND :futureDate")
    suspend fun getUpcomingBillsCount(today: Date = Date(), futureDate: Date): Int
}

data class CategoryExpense(
    val category: String,
    val total: BigDecimal
)