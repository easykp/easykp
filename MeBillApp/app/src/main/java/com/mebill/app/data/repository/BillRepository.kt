package com.mebill.app.data.repository

import androidx.lifecycle.LiveData
import com.mebill.app.data.database.BillDao
import com.mebill.app.data.database.CategoryDao
import com.mebill.app.data.database.CategoryExpense
import com.mebill.app.data.model.Bill
import com.mebill.app.data.model.Category
import com.mebill.app.data.model.Priority
import com.mebill.app.data.model.RecurringType
import java.math.BigDecimal
import java.util.*

class BillRepository(
    private val billDao: BillDao,
    private val categoryDao: CategoryDao
) {
    
    // Bill operations
    fun getAllBills(): LiveData<List<Bill>> = billDao.getAllBills()
    
    fun getUnpaidBills(): LiveData<List<Bill>> = billDao.getUnpaidBills()
    
    fun getPaidBills(): LiveData<List<Bill>> = billDao.getPaidBills()
    
    fun getOverdueBills(): LiveData<List<Bill>> = billDao.getOverdueBills()
    
    fun getBillsBetweenDates(startDate: Date, endDate: Date): LiveData<List<Bill>> = 
        billDao.getBillsBetweenDates(startDate, endDate)
    
    fun getBillsByCategory(category: String): LiveData<List<Bill>> = 
        billDao.getBillsByCategory(category)
    
    fun getBillsByType(isReceivable: Boolean): LiveData<List<Bill>> = 
        billDao.getBillsByType(isReceivable)
    
    fun getBillsByPriority(priority: Priority): LiveData<List<Bill>> = 
        billDao.getBillsByPriority(priority)
    
    fun searchBills(searchQuery: String): LiveData<List<Bill>> = 
        billDao.searchBills(searchQuery)
    
    fun getUpcomingBills(daysAhead: Int = 7): LiveData<List<Bill>> {
        val today = Date()
        val futureDate = Calendar.getInstance().apply {
            time = today
            add(Calendar.DAY_OF_MONTH, daysAhead)
        }.time
        return billDao.getUpcomingBills(today, futureDate)
    }
    
    suspend fun getBillById(billId: Long): Bill? = billDao.getBillById(billId)
    
    suspend fun insertBill(bill: Bill): Long = billDao.insertBill(bill)
    
    suspend fun updateBill(bill: Bill) = billDao.updateBill(bill)
    
    suspend fun deleteBill(bill: Bill) = billDao.deleteBill(bill)
    
    suspend fun deleteBillById(billId: Long) = billDao.deleteBillById(billId)
    
    suspend fun markBillAsPaid(billId: Long, isPaid: Boolean, paidDate: Date? = if (isPaid) Date() else null) = 
        billDao.markBillAsPaid(billId, isPaid, paidDate)
    
    // Statistics
    suspend fun getTotalUnpaidExpenses(): BigDecimal = billDao.getTotalUnpaidExpenses() ?: BigDecimal.ZERO
    
    suspend fun getTotalUnpaidIncome(): BigDecimal = billDao.getTotalUnpaidIncome() ?: BigDecimal.ZERO
    
    suspend fun getTotalPaidExpensesBetweenDates(startDate: Date, endDate: Date): BigDecimal = 
        billDao.getTotalPaidExpensesBetweenDates(startDate, endDate) ?: BigDecimal.ZERO
    
    suspend fun getTotalPaidIncomeBetweenDates(startDate: Date, endDate: Date): BigDecimal = 
        billDao.getTotalPaidIncomeBetweenDates(startDate, endDate) ?: BigDecimal.ZERO
    
    suspend fun getExpensesByCategory(startDate: Date, endDate: Date): List<CategoryExpense> = 
        billDao.getExpensesByCategory(startDate, endDate)
    
    suspend fun getOverdueBillsCount(): Int = billDao.getOverdueBillsCount()
    
    suspend fun getUpcomingBillsCount(daysAhead: Int = 7): Int {
        val today = Date()
        val futureDate = Calendar.getInstance().apply {
            time = today
            add(Calendar.DAY_OF_MONTH, daysAhead)
        }.time
        return billDao.getUpcomingBillsCount(today, futureDate)
    }
    
    // Category operations
    fun getAllCategories(): LiveData<List<Category>> = categoryDao.getAllCategories()
    
    suspend fun getCategoryById(categoryId: Long): Category? = categoryDao.getCategoryById(categoryId)
    
    suspend fun getCategoryByName(name: String): Category? = categoryDao.getCategoryByName(name)
    
    suspend fun insertCategory(category: Category): Long = categoryDao.insertCategory(category)
    
    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)
    
    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)
    
    suspend fun deleteCategoryById(categoryId: Long) = categoryDao.deleteCategoryById(categoryId)
    
    // Recurring bill management
    suspend fun createRecurringBills(bill: Bill) {
        if (!bill.isRecurring || bill.recurringType == RecurringType.NONE) return
        
        val bills = mutableListOf<Bill>()
        val calendar = Calendar.getInstance()
        calendar.time = bill.dueDate
        
        // Create next 12 instances of recurring bill
        repeat(12) {
            when (bill.recurringType) {
                RecurringType.DAILY -> calendar.add(Calendar.DAY_OF_MONTH, 1)
                RecurringType.WEEKLY -> calendar.add(Calendar.WEEK_OF_YEAR, 1)
                RecurringType.BI_WEEKLY -> calendar.add(Calendar.WEEK_OF_YEAR, 2)
                RecurringType.MONTHLY -> calendar.add(Calendar.MONTH, 1)
                RecurringType.QUARTERLY -> calendar.add(Calendar.MONTH, 3)
                RecurringType.SEMI_ANNUALLY -> calendar.add(Calendar.MONTH, 6)
                RecurringType.ANNUALLY -> calendar.add(Calendar.YEAR, 1)
                RecurringType.NONE -> return
            }
            
            bills.add(
                bill.copy(
                    id = 0, // Let database generate new ID
                    dueDate = calendar.time,
                    isPaid = false,
                    paidDate = null,
                    createdAt = Date(),
                    updatedAt = Date()
                )
            )
        }
        
        billDao.insertBills(bills)
    }
    
    // Dashboard data
    suspend fun getDashboardData(): DashboardData {
        val overdueBillsCount = getOverdueBillsCount()
        val upcomingBillsCount = getUpcomingBillsCount()
        val totalUnpaidExpenses = getTotalUnpaidExpenses()
        val totalUnpaidIncome = getTotalUnpaidIncome()
        
        return DashboardData(
            overdueBillsCount = overdueBillsCount,
            upcomingBillsCount = upcomingBillsCount,
            totalUnpaidExpenses = totalUnpaidExpenses,
            totalUnpaidIncome = totalUnpaidIncome
        )
    }
}

data class DashboardData(
    val overdueBillsCount: Int,
    val upcomingBillsCount: Int,
    val totalUnpaidExpenses: BigDecimal,
    val totalUnpaidIncome: BigDecimal
)