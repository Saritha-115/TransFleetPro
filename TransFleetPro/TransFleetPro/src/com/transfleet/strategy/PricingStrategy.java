package com.transfleet.strategy;

/**
 * Strategy Pattern for Pricing
 * Different pricing strategies based on customer type and priority
 */
public interface PricingStrategy {
    double calculatePrice(double baseRate, double distance, String priority);
}

/**
 * Standard Pricing for Regular customers
 */
class StandardPricing implements PricingStrategy {
    @Override
    public double calculatePrice(double baseRate, double distance, String priority) {
        double price = baseRate + (distance * 15); // Rs. 15 per km
        
        // Priority multiplier
        switch (priority) {
            case "Same-Day":
                price *= 1.5;
                break;
            case "Express":
                price *= 1.3;
                break;
            case "Normal":
            default:
                price *= 1.0;
                break;
        }
        
        return Math.round(price * 100.0) / 100.0;
    }
}

/**
 * Premium Pricing for Premium customers (10% discount)
 */
class PremiumPricing implements PricingStrategy {
    @Override
    public double calculatePrice(double baseRate, double distance, String priority) {
        double price = baseRate + (distance * 15); // Rs. 15 per km
        
        // Priority multiplier
        switch (priority) {
            case "Same-Day":
                price *= 1.5;
                break;
            case "Express":
                price *= 1.3;
                break;
            case "Normal":
            default:
                price *= 1.0;
                break;
        }
        
        // 10% discount for premium customers
        price *= 0.90;
        
        return Math.round(price * 100.0) / 100.0;
    }
}

/**
 * Corporate Pricing for Corporate customers (15% discount + volume benefits)
 */
class CorporatePricing implements PricingStrategy {
    @Override
    public double calculatePrice(double baseRate, double distance, String priority) {
        double price = baseRate + (distance * 15); // Rs. 15 per km
        
        // Priority multiplier
        switch (priority) {
            case "Same-Day":
                price *= 1.5;
                break;
            case "Express":
                price *= 1.3;
                break;
            case "Normal":
            default:
                price *= 1.0;
                break;
        }
        
        // 15% discount for corporate customers
        price *= 0.85;
        
        return Math.round(price * 100.0) / 100.0;
    }
}
