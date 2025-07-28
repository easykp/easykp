package com.mebill.app.data.database

import androidx.room.TypeConverter
import com.mebill.app.data.model.Priority
import com.mebill.app.data.model.RecurringType
import java.math.BigDecimal
import java.util.*

class Converters {
    
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
    
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toString()
    }
    
    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }
    
    @TypeConverter
    fun fromRecurringType(value: RecurringType): String {
        return value.name
    }
    
    @TypeConverter
    fun toRecurringType(value: String): RecurringType {
        return RecurringType.valueOf(value)
    }
    
    @TypeConverter
    fun fromPriority(value: Priority): String {
        return value.name
    }
    
    @TypeConverter
    fun toPriority(value: String): Priority {
        return Priority.valueOf(value)
    }
}