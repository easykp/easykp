package com.mebill.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val color: String = "#2196F3", // Default blue color
    val icon: String = "category", // Icon name
    val isDefault: Boolean = false
)

object DefaultCategories {
    val categories = listOf(
        Category(name = "Utilities", description = "Electric, Gas, Water", color = "#FF9800", icon = "bolt", isDefault = true),
        Category(name = "Rent/Mortgage", description = "Housing payments", color = "#4CAF50", icon = "home", isDefault = true),
        Category(name = "Insurance", description = "Health, Auto, Life", color = "#2196F3", icon = "shield", isDefault = true),
        Category(name = "Credit Cards", description = "Credit card payments", color = "#F44336", icon = "credit_card", isDefault = true),
        Category(name = "Subscriptions", description = "Netflix, Spotify, etc.", color = "#9C27B0", icon = "subscriptions", isDefault = true),
        Category(name = "Phone/Internet", description = "Telecom services", color = "#607D8B", icon = "phone", isDefault = true),
        Category(name = "Transportation", description = "Car payments, Transit", color = "#795548", icon = "directions_car", isDefault = true),
        Category(name = "Healthcare", description = "Medical expenses", color = "#E91E63", icon = "local_hospital", isDefault = true),
        Category(name = "Education", description = "School, Courses", color = "#3F51B5", icon = "school", isDefault = true),
        Category(name = "Other", description = "Miscellaneous bills", color = "#757575", icon = "more_horiz", isDefault = true)
    )
}