# MeBill - Android Bill Management App

MeBill is a comprehensive Android application for managing bills, payments, and expenses. Built with modern Android development practices using Kotlin, it provides a user-friendly interface for tracking due dates, managing recurring bills, and analyzing spending patterns.

## Features

### 📊 Dashboard
- Overview of overdue and upcoming bills
- Total unpaid expenses and income summary
- Quick access to recent bills
- Visual insights with charts and graphs

### 💰 Bill Management
- Add, edit, and delete bills
- Mark bills as paid/unpaid
- Set priority levels (Low, Medium, High, Critical)
- Support for both payable and receivable bills
- Rich bill details including account numbers, websites, phone numbers

### 🔄 Recurring Bills
- Automatic creation of recurring bills
- Multiple recurrence patterns:
  - Daily, Weekly, Bi-Weekly
  - Monthly, Quarterly, Semi-Annually, Annually
- Smart bill generation up to 12 months in advance

### 📅 Calendar View
- Visual calendar showing bill due dates
- Color-coded bill status indicators
- Easy navigation between months
- Quick bill actions from calendar

### 📈 Reports & Analytics
- Expense analysis by category
- Monthly and yearly summaries
- Interactive charts and graphs
- Export capabilities

### 🔔 Notifications
- Smart bill reminders
- Customizable reminder settings
- Overdue bill alerts
- Boot-time notification restoration

### 🏷️ Categories
- 10 pre-defined categories (Utilities, Rent/Mortgage, Insurance, etc.)
- Custom category creation
- Color-coded category system
- Category-based filtering and reporting

## Technical Architecture

### 🏗️ Architecture Pattern
- **MVVM (Model-View-ViewModel)** architecture
- **Repository Pattern** for data abstraction
- **LiveData** for reactive UI updates
- **Room Database** for local data persistence

### 🛠️ Technologies Used
- **Kotlin** - Primary programming language
- **Android Jetpack Components**:
  - Room - Database ORM
  - LiveData - Observable data holder
  - ViewModel - UI-related data management
  - Navigation Component - Fragment navigation
  - ViewBinding - Type-safe view references
  - WorkManager - Background task scheduling

- **Material Design 3** - Modern UI components
- **MPAndroidChart** - Charts and graphs
- **Glide** - Image loading and caching

### 🎨 UI/UX Design
- **Material Design 3** principles
- **Bottom Navigation** for main app sections
- **Floating Action Button** for quick bill creation
- **Responsive design** for different screen sizes
- **Accessibility support** with content descriptions

## Project Structure

```
app/
├── src/main/
│   ├── java/com/mebill/app/
│   │   ├── data/
│   │   │   ├── database/          # Room database, DAOs, converters
│   │   │   ├── model/             # Data models (Bill, Category)
│   │   │   └── repository/        # Repository pattern implementation
│   │   ├── ui/
│   │   │   ├── activities/        # Activities (MainActivity, AddEditBillActivity)
│   │   │   ├── fragments/         # Fragments (Dashboard, Bills, Calendar, Reports)
│   │   │   ├── adapters/          # RecyclerView adapters
│   │   │   └── viewmodels/        # ViewModels for MVVM
│   │   ├── notifications/         # Notification handling
│   │   ├── utils/                 # Utility classes
│   │   └── BillApplication.kt     # Application class
│   └── res/
│       ├── layout/                # XML layouts
│       ├── drawable/              # Icons and graphics
│       ├── values/                # Strings, colors, themes
│       ├── menu/                  # Menu resources
│       └── navigation/            # Navigation graphs
```

## Data Model

### Bill Entity
```kotlin
@Entity(tableName = "bills")
data class Bill(
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
    val priority: Priority = Priority.MEDIUM,
    val isReceivable: Boolean = false,
    // ... additional fields
)
```

### Category Entity
```kotlin
@Entity(tableName = "categories")
data class Category(
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val color: String = "#2196F3",
    val icon: String = "category",
    val isDefault: Boolean = false
)
```

## Setup and Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API level 24 or higher
- Kotlin 1.9.10 or later

### Building the Project
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/mebill-android.git
   cd mebill-android
   ```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Build and run the app:
   ```bash
   ./gradlew assembleDebug
   ```

### Dependencies
The app uses the following major dependencies:
- **Room**: 2.6.0 - Database ORM
- **Navigation Component**: 2.7.4 - Fragment navigation
- **Material Design**: 1.10.0 - UI components
- **MPAndroidChart**: 3.1.0 - Charts and graphs
- **WorkManager**: 2.8.1 - Background tasks
- **Glide**: 4.16.0 - Image loading

## Key Features Implementation

### 1. Database Design
- **Room Database** with type converters for Date and BigDecimal
- **Foreign key relationships** between bills and categories
- **Database callbacks** for seeding default categories

### 2. Notification System
- **AlarmManager** for precise scheduling
- **WorkManager** for reliable background execution
- **Notification channels** for Android O+ compatibility
- **Boot receiver** for notification restoration

### 3. Recurring Bills
- **Smart algorithm** for generating future bill instances
- **Configurable patterns** (daily to annually)
- **Automatic cleanup** of past recurring bills

### 4. Search and Filtering
- **Real-time search** across bill names and descriptions
- **Multiple filter options** (paid, unpaid, overdue, upcoming)
- **Category-based filtering**
- **Priority-based sorting**

## Future Enhancements

### Planned Features
- [ ] **Cloud Sync** - Backup bills to cloud storage
- [ ] **Export/Import** - CSV and PDF export functionality
- [ ] **Budget Tracking** - Monthly budget management
- [ ] **Bill Photos** - Attach receipt images to bills
- [ ] **Widgets** - Home screen widgets for quick overview
- [ ] **Dark Theme** - Full dark mode support
- [ ] **Multi-Currency** - Support for different currencies
- [ ] **Bill Splitting** - Share bills between multiple people

### Technical Improvements
- [ ] **Jetpack Compose** migration for modern UI
- [ ] **Kotlin Multiplatform** for code sharing
- [ ] **Automated Testing** - Unit and UI tests
- [ ] **CI/CD Pipeline** - Automated build and deployment
- [ ] **Performance Optimization** - Database query optimization

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Coding Standards
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Write unit tests for new features
- Follow Material Design guidelines

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Support

For support, email support@mebill.app or create an issue on GitHub.

## Acknowledgments

- **Material Design** team for design guidelines
- **Android Jetpack** for powerful architecture components
- **MPAndroidChart** for beautiful chart implementations
- **Open source community** for inspiration and libraries

---

**MeBill** - Making bill management simple and stress-free! 💰📱