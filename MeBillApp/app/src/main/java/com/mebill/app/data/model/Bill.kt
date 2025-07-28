package com.mebill.app.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.util.*

@Entity(tableName = "bills")
@Parcelize
data class Bill(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val amount: BigDecimal,
    val dueDate: Date,
    val category: String,
    val isRecurring: Boolean = false,
    val recurringType: RecurringType = RecurringType.NONE,
    val reminderDays: Int = 3,
    val isPaid: Boolean = false,
    val paidDate: Date? = null,
    val paymentMethod: String = "",
    val notes: String = "",
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val isReceivable: Boolean = false, // true for money coming in, false for money going out
    val priority: Priority = Priority.MEDIUM,
    val accountNumber: String = "",
    val website: String = "",
    val phoneNumber: String = "",
    val confirmationNumber: String = ""
) : Parcelable

enum class RecurringType {
    NONE,
    DAILY,
    WEEKLY,
    BI_WEEKLY,
    MONTHLY,
    QUARTERLY,
    SEMI_ANNUALLY,
    ANNUALLY
}

enum class Priority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}