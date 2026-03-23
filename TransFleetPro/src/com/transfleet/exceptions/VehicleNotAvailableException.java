package com.transfleet.exceptions;

/**
 * Custom exception for vehicle availability issues
 */
public class VehicleNotAvailableException extends Exception {
    public VehicleNotAvailableException(String message) {
        super(message);
    }
}

/**
 * Custom exception for driver availability issues
 */
class DriverNotAvailableException extends Exception {
    public DriverNotAvailableException(String message) {
        super(message);
    }
}

/**
 * Custom exception for invalid routes
 */
class InvalidRouteException extends Exception {
    public InvalidRouteException(String message) {
        super(message);
    }
}

/**
 * Custom exception for capacity issues
 */
class CapacityExceededException extends Exception {
    public CapacityExceededException(String message) {
        super(message);
    }
}

/**
 * Custom exception for credit limit issues
 */
class InsufficientCreditException extends Exception {
    public InsufficientCreditException(String message) {
        super(message);
    }
}

/**
 * Custom exception for validation errors
 */
class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}