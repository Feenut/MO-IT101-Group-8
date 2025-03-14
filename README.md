# CHANGELOG
# Updated 12:19 AM 2/12/25
# Updated 2:39 PM 2/12/25
# -Added CSV for Employee data storage
# -Enhance simple ui
# Updated 12:27 AM 2/13/25
# Added govt deductions
# Updated 12:51 AM 2/18/25
# Added inventory system based on Jhonalyn's code. Tweaked some of the code as it has errors in vscode
# Chnaged the Process Payroll, add employee details, etc. based on Camu
# Checked for error handling.
# Updated 2:19 AM 2/20/25
# Fixed path for payroll csv file: C:\path\to\your\project\ path will depend on your file explorer directory
# Updated 2:40 AM 3/4/25
# MotorPH Payroll System - Changelog

# Major Enhancements

# 1. **Added Basic Deduction Calculation**
   - Implemented `calculateBasicDeduction()` method in Employee class
   - Separates government contributions (SSS, PhilHealth, Pag-IBIG) from tax deductions
   - Improves clarity in payroll reports

# 2. **Added Net Salary Calculation**
   - Implemented `calculateNetSalary()` method in Employee class
   - Properly calculates net pay after all deductions

# 3. **Added Weekly Salary Calculation**
   - Implemented `calculateWeeklySalary()` method in Employee class
   - Added weekly payroll processing option in the admin menu
   - Allows viewing weekly salary equivalent in employee payroll details

# 4. **Enhanced Hours Entry System**
   - Added date range functionality for entering work hours
   - Implemented option to enter same hours for all days in a range
   - Improved user experience for bulk data entry

# 5. **Implemented Employee Login System**
   - Added employee authentication by ID and name
   - Created employee-specific menu with payroll viewing capabilities
   - Enhanced security and user role separation

# Error Handling Improvements

# 1. **Enhanced CSV Data Parsing**
   - Added detailed error messages for invalid CSV data
   - Improved exception handling for date and numeric parsing
   - Better handling of missing or malformed data

# 2. **Improved File I/O Error Handling**
   - Added checks for file existence before reading
   - Better error messages for file access issues
   - Graceful handling of missing files with appropriate user feedback

# 3. **Enhanced User Input Validation**
   - Added comprehensive input validation for all numeric inputs
   - Improved date format validation with clear error messages
   - Added validation for required fields in forms

# Path Configuration Updates

# 1. **Updated File Paths to Absolute Paths**
   - Changed Employees.csv path to: `C:\Users\Johanzen\Documents\PROJECTS IT\MotorPH Payroll System\Employees.csv` THIS WILL DEPEND ON YOUR FILE EXPLORER PATH
   - Changed inventory.csv path to: `C:\Users\Johanzen\Documents\PROJECTS IT\MotorPH Payroll System\inventory.csv` THIS WILL DEPEND ON YOUR FILE EXPLORER PATH
   - Ensures consistent file access across different execution environments

# UI/UX Improvements

# 1. **Enhanced Payroll Reports**
   - Improved formatting of payroll reports with better column alignment
   - Added detailed breakdown of deductions in employee payroll view
   - Added weekly salary information to payroll reports

# 2. **Improved Menu System**
   - Added clearer menu hierarchies with better navigation
   - Enhanced role-based access control (Admin vs Employee)
   - Added more descriptive prompts and instructions

# 3. **Better Feedback Messages**
   - Added confirmation messages for successful operations
   - Improved error messages with more specific information
   - Added data loading/saving status messages

# Code Structure Improvements

# 1. **Enhanced OOP Implementation**
   - Better encapsulation of calculation methods in appropriate classes
   - Improved separation of concerns between UI and business logic
   - More consistent method naming and organization

# 2. **Improved Method Organization**
   - Split large methods into smaller, more focused methods
   - Better organization of related functionality
   - Enhanced code readability and maintainability

# CHANGELOG 3/08/2025
# Payroll Display
Added professional box-drawing characters (╔, ║, ╚, etc.) for clean borders
Implemented consistent column alignment and spacing
Added formatted headers and section dividers
Enhanced monetary value display with PHP prefix and thousand separators
Standardized decimal places to 2 digits for all numeric values
# 2. Time Records Display
Added new grid layout with 6 columns:
Date (MM/DD/YYYY format)
Login Time (HH:mm format)
Logout Time (HH:mm format)
Hours Worked (2 decimal places)
Status (COMPLETE/PARTIAL/ABSENT)
Notes
Added employee information header section
Implemented summary footer with totals and averages
Added status indicators for attendance tracking
# 3. Employee Information Display
Organized into 4 distinct sections:
Personal Information
Employment Information
Government Numbers
Compensation Details
Added clear section headers with double-line borders
Implemented two-column layout for labels and values
Added proper spacing and alignment for readability
# 4. Payroll Processing Information
Enhanced header with company name and system title
Added pay period information display
Implemented grid layout for payroll calculations
Added detailed breakdown sections:
Base Pay calculation
Allowances (Rice, Phone, Clothing)
Deductions (SSS, PhilHealth, Pag-IBIG, Tax)
Net Pay summary
# 5. Error Messages
Standardized error message display with bordered boxes
Added specific error messages for:
Invalid date formats
Employee not found
Date range validation
Data loading errors
# 6. Summary Sections
Added formatted summary displays for:
Total employees processed
Hours worked statistics
Financial summaries
Attendance records
Compensation totals
# 7. Date Handling
Implemented consistent date format display:
Employee view: MM/DD/YYYY
Admin view: yyyy-MM-dd
Added date validation with proper error messages
Enhanced date range display in headers
# 8. General Formatting
Standardized all monetary values with:
PHP prefix
Thousand separators
2 decimal places

# 9. Class Organization
Enhanced Payroll class implementation of FileStorage<Employee> interface
Improved encapsulation of employee data handling
Added proper access modifiers for class members
# 10. Method Refactoring
Split large methods into smaller, focused methods:
processEmployeePayroll: Handles individual payroll calculation
displayPayrollInformation: Manages display formatting
viewPayrollWithDates: Handles date-based payroll viewing
loadAttendanceRecords: Manages attendance data loading
parseCSVLine: Handles CSV parsing logic
# 11. Data Structures
Implemented Map<String, Double> for payroll calculations
Used Map<LocalDate, Employee.AttendanceRecord> for attendance tracking
Added List<Employee> for employee management

# FILEPATH
LINE 29 PAYROLL.JAVA: MAKE SURE NAKA DEPENDE SA FILE EXPLORER NIY YUN FILEPATH
LINE 32 PAYROLL.JAVA: MAKE SURE NAKA DEPENDE SA FILE EXPLORER NIY YUN FILEPATH
LINE 95 MAIN.JAVA: MAKE SURE NAKA DEPENDE SA FILE EXPLORER NIY YUN FILEPATH

# Changelog 3/15/2025 2:34 AM
# Added inline documentation for classes
