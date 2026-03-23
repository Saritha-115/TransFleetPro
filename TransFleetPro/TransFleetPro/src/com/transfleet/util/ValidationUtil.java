package com.transfleet.util;

import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 * Validation Utility Class
 * Contains all validation methods
 */
public class ValidationUtil {
    
    // Regular expressions
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,50}$");
    private static final Pattern CONTACT_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern LICENSE_PATTERN = Pattern.compile("^[A-Z]{2}[0-9]{6}$");
    private static final Pattern REGISTRATION_PATTERN = Pattern.compile("^[A-Z]{2}-[A-Z]{3}-[0-9]{4}$");
    
    /**
     * Validate name (alphabets and spaces only)
     */
    public static boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            showError("Name cannot be empty!");
            return false;
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            showError("Name must contain only alphabets and spaces (2-50 characters)!");
            return false;
        }
        return true;
    }
    
    /**
     * Validate contact number (10 digits)
     */
    public static boolean validateContact(String contact) {
        if (contact == null || contact.trim().isEmpty()) {
            showError("Contact number cannot be empty!");
            return false;
        }
        if (!CONTACT_PATTERN.matcher(contact).matches()) {
            showError("Contact number must be exactly 10 digits!");
            return false;
        }
        return true;
    }
    
    /**
     * Validate email
     */
    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return true; // Email is optional
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showError("Invalid email format!");
            return false;
        }
        return true;
    }
    
    /**
     * Validate address (minimum 10 characters)
     */
    public static boolean validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            showError("Address cannot be empty!");
            return false;
        }
        if (address.trim().length() < 10) {
            showError("Address must be at least 10 characters long!");
            return false;
        }
        return true;
    }
    
    /**
     * Validate license number format
     */
    public static boolean validateLicenseNumber(String license) {
        if (license == null || license.trim().isEmpty()) {
            showError("License number cannot be empty!");
            return false;
        }
        if (!LICENSE_PATTERN.matcher(license).matches()) {
            showError("License number format: XX123456 (2 letters + 6 digits)!");
            return false;
        }
        return true;
    }
    
    /**
     * Validate vehicle registration number
     */
    public static boolean validateRegistrationNumber(String regNumber) {
        if (regNumber == null || regNumber.trim().isEmpty()) {
            showError("Registration number cannot be empty!");
            return false;
        }
        if (!REGISTRATION_PATTERN.matcher(regNumber).matches()) {
            showError("Registration format: XX-XXX-1234 (e.g., WP-CAB-1234)!");
            return false;
        }
        return true;
    }
    
    /**
     * Validate positive number
     */
    public static boolean validatePositiveNumber(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            showError(fieldName + " cannot be empty!");
            return false;
        }
        try {
            double num = Double.parseDouble(value);
            if (num <= 0) {
                showError(fieldName + " must be a positive number!");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showError(fieldName + " must be a valid number!");
            return false;
        }
    }
    
    /**
     * Validate non-negative number
     */
    public static boolean validateNonNegativeNumber(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            showError(fieldName + " cannot be empty!");
            return false;
        }
        try {
            double num = Double.parseDouble(value);
            if (num < 0) {
                showError(fieldName + " cannot be negative!");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showError(fieldName + " must be a valid number!");
            return false;
        }
    }
    
    /**
     * Validate range
     */
    public static boolean validateRange(String value, String fieldName, double min, double max) {
        if (!validatePositiveNumber(value, fieldName)) {
            return false;
        }
        try {
            double num = Double.parseDouble(value);
            if (num < min || num > max) {
                showError(fieldName + " must be between " + min + " and " + max + "!");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate not empty
     */
    public static boolean validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            showError(fieldName + " cannot be empty!");
            return false;
        }
        return true;
    }
    
    /**
     * Validate combo box selection
     */
    public static boolean validateComboBox(Object selected, String fieldName) {
        if (selected == null) {
            showError("Please select a " + fieldName + "!");
            return false;
        }
        return true;
    }
    
    /**
     * Validate date not in past
     */
    public static boolean validateFutureDate(java.util.Date date, String fieldName) {
        if (date == null) {
            showError(fieldName + " cannot be empty!");
            return false;
        }
        java.util.Date today = new java.util.Date();
        if (date.before(today)) {
            showError(fieldName + " cannot be in the past!");
            return false;
        }
        return true;
    }
    
    /**
     * Validate date not in future
     */
    public static boolean validatePastDate(java.util.Date date, String fieldName) {
        if (date == null) {
            showError(fieldName + " cannot be empty!");
            return false;
        }
        java.util.Date today = new java.util.Date();
        if (date.after(today)) {
            showError(fieldName + " cannot be in the future!");
            return false;
        }
        return true;
    }
    
    /**
     * Validate capacity
     */
    public static boolean validateCapacity(double packageWeight, double vehicleCapacity) {
        if (packageWeight > vehicleCapacity) {
            showError("Package weight (" + packageWeight + " kg) exceeds vehicle capacity (" + vehicleCapacity + " kg)!");
            return false;
        }
        return true;
    }
    
    /**
     * Show error message
     */
    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Show success message
     */
    public static void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show confirmation dialog
     */
    public static boolean showConfirmation(String message) {
        int option = JOptionPane.showConfirmDialog(null, message, "Confirm", JOptionPane.YES_NO_OPTION);
        return option == JOptionPane.YES_OPTION;
    }
}