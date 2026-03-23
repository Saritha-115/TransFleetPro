/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.transfleet.model;

import java.util.Date;

/**
 *
 * @author USER
 */
public class Vehicle {
    private int vehicleId;
    private String registrationNumber;
    private String vehicleType;
    private double capacityWeight;
    private double capacityVolume;
    private String fuelType;
    private double fuelEfficiency;
    private Date purchaseDate;
    private Date lastMaintenanceDate;
    private String status;
    private String currentLocation;
    
    // Constructors
    public Vehicle() {}
    
    public Vehicle(int vehicleId, String registrationNumber, String vehicleType, String status) {
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
        this.status = status;
    }
    
    // Getters and Setters
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    
    public double getCapacityWeight() { return capacityWeight; }
    public void setCapacityWeight(double capacityWeight) { this.capacityWeight = capacityWeight; }
    
    public double getCapacityVolume() { return capacityVolume; }
    public void setCapacityVolume(double capacityVolume) { this.capacityVolume = capacityVolume; }
    
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    
    public double getFuelEfficiency() { return fuelEfficiency; }
    public void setFuelEfficiency(double fuelEfficiency) { this.fuelEfficiency = fuelEfficiency; }
    
    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }
    
    public Date getLastMaintenanceDate() { return lastMaintenanceDate; }
    public void setLastMaintenanceDate(Date lastMaintenanceDate) { this.lastMaintenanceDate = lastMaintenanceDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }
    
    @Override
    public String toString() {
        return vehicleId + " - " + registrationNumber + " (" + vehicleType + " - " + status + ")";
    }
}
