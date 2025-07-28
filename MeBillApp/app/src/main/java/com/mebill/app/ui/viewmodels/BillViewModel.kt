package com.mebill.app.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.mebill.app.BillApplication
import com.mebill.app.data.model.Bill
import com.mebill.app.data.model.Category
import com.mebill.app.data.model.Priority
import com.mebill.app.data.repository.BillRepository
import com.mebill.app.data.repository.DashboardData
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.*

class BillViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BillRepository

    // LiveData for all bills
    val allBills: LiveData<List<Bill>>
    val unpaidBills: LiveData<List<Bill>>
    val paidBills: LiveData<List<Bill>>
    val overdueBills: LiveData<List<Bill>>
    val upcomingBills: LiveData<List<Bill>>
    val allCategories: LiveData<List<Category>>

    // Dashboard data
    private val _dashboardData = MutableLiveData<DashboardData>()
    val dashboardData: LiveData<DashboardData> = _dashboardData

    // Filter and search state
    private val _currentFilter = MutableLiveData<BillFilter>()
    val currentFilter: LiveData<BillFilter> = _currentFilter

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    // Filtered bills based on current filter and search
    val filteredBills: LiveData<List<Bill>> = MediatorLiveData<List<Bill>>().apply {
        fun updateFilteredBills() {
            val bills = when (_currentFilter.value) {
                BillFilter.ALL -> allBills.value
                BillFilter.PAID -> paidBills.value
                BillFilter.UNPAID -> unpaidBills.value
                BillFilter.OVERDUE -> overdueBills.value
                BillFilter.UPCOMING -> upcomingBills.value
                null -> allBills.value
            } ?: emptyList()

            val query = _searchQuery.value
            value = if (query.isNullOrBlank()) {
                bills
            } else {
                bills.filter { bill ->
                    bill.name.contains(query, ignoreCase = true) ||
                    bill.description.contains(query, ignoreCase = true) ||
                    bill.category.contains(query, ignoreCase = true)
                }
            }
        }

        addSource(allBills) { updateFilteredBills() }
        addSource(paidBills) { updateFilteredBills() }
        addSource(unpaidBills) { updateFilteredBills() }
        addSource(overdueBills) { updateFilteredBills() }
        addSource(upcomingBills) { updateFilteredBills() }
        addSource(_currentFilter) { updateFilteredBills() }
        addSource(_searchQuery) { updateFilteredBills() }
    }

    // Loading and error states
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        val billDao = (application as BillApplication).database.billDao()
        val categoryDao = application.database.categoryDao()
        repository = BillRepository(billDao, categoryDao)

        allBills = repository.getAllBills()
        unpaidBills = repository.getUnpaidBills()
        paidBills = repository.getPaidBills()
        overdueBills = repository.getOverdueBills()
        upcomingBills = repository.getUpcomingBills()
        allCategories = repository.getAllCategories()

        // Initialize filter
        _currentFilter.value = BillFilter.ALL
        _searchQuery.value = ""

        // Load dashboard data
        loadDashboardData()
    }

    fun insertBill(bill: Bill) = viewModelScope.launch {
        try {
            _isLoading.value = true
            val billId = repository.insertBill(bill)
            
            // Create recurring bills if needed
            if (bill.isRecurring) {
                repository.createRecurringBills(bill.copy(id = billId))
            }
            
            loadDashboardData()
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Error saving bill: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun updateBill(bill: Bill) = viewModelScope.launch {
        try {
            _isLoading.value = true
            repository.updateBill(bill)
            loadDashboardData()
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Error updating bill: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun deleteBill(bill: Bill) = viewModelScope.launch {
        try {
            _isLoading.value = true
            repository.deleteBill(bill)
            loadDashboardData()
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Error deleting bill: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun markBillAsPaid(billId: Long, isPaid: Boolean) = viewModelScope.launch {
        try {
            repository.markBillAsPaid(billId, isPaid)
            loadDashboardData()
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Error updating bill status: ${e.message}"
        }
    }

    suspend fun getBillById(billId: Long): Bill? {
        return repository.getBillById(billId)
    }

    fun setFilter(filter: BillFilter) {
        _currentFilter.value = filter
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getBillsByCategory(category: String): LiveData<List<Bill>> {
        return repository.getBillsByCategory(category)
    }

    fun getBillsByPriority(priority: Priority): LiveData<List<Bill>> {
        return repository.getBillsByPriority(priority)
    }

    fun getBillsBetweenDates(startDate: Date, endDate: Date): LiveData<List<Bill>> {
        return repository.getBillsBetweenDates(startDate, endDate)
    }

    private fun loadDashboardData() = viewModelScope.launch {
        try {
            val data = repository.getDashboardData()
            _dashboardData.value = data
        } catch (e: Exception) {
            _errorMessage.value = "Error loading dashboard data: ${e.message}"
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Statistics functions
    suspend fun getTotalUnpaidExpenses(): BigDecimal {
        return repository.getTotalUnpaidExpenses()
    }

    suspend fun getTotalUnpaidIncome(): BigDecimal {
        return repository.getTotalUnpaidIncome()
    }

    suspend fun getExpensesByCategory(startDate: Date, endDate: Date) = 
        repository.getExpensesByCategory(startDate, endDate)

    // Category operations
    fun insertCategory(category: Category) = viewModelScope.launch {
        try {
            repository.insertCategory(category)
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Error saving category: ${e.message}"
        }
    }

    fun updateCategory(category: Category) = viewModelScope.launch {
        try {
            repository.updateCategory(category)
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Error updating category: ${e.message}"
        }
    }

    fun deleteCategory(category: Category) = viewModelScope.launch {
        try {
            repository.deleteCategory(category)
            _errorMessage.value = null
        } catch (e: Exception) {
            _errorMessage.value = "Error deleting category: ${e.message}"
        }
    }
}

enum class BillFilter {
    ALL,
    PAID,
    UNPAID,
    OVERDUE,
    UPCOMING
}