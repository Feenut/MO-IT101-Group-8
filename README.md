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
