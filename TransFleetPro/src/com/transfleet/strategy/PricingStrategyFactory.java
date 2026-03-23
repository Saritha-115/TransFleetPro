/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.transfleet.strategy;

/**
 *
 * @author USER
 */
public class PricingStrategyFactory {
    public static PricingStrategy getStrategy(String customerType) {
        switch (customerType) {
            case "Premium":
                return new PremiumPricing();
            case "Corporate":
                return new CorporatePricing();
            case "Regular":
            default:
                return new StandardPricing();
        }
    }
}
