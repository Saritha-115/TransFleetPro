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
public class Driver {
    private int driverId;
    private String name;
    private String contact;
    private String licenseNumber;
    private String licenseType;
    private Date dateOfHire;
    private int experienceYears;
    private String status;
    private double performanceRating;
    
    // Constructors
    public Driver() {}
    
    public Driver(int driverId, String name, String licenseNumber, String status) {
        this.driverId = driverId;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.status = status;
    }
    
    // Getters and Setters
    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    public String getLicenseType() { return licenseType; }
    public void setLicenseType(String licenseType) { this.licenseType = licenseType; }
    
    public Date getDateOfHire() { return dateOfHire; }
    public void setDateOfHire(Date dateOfHire) { this.dateOfHire = dateOfHire; }
    
    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public double getPerformanceRating() { return performanceRating; }
    public void setPerformanceRating(double performanceRating) { this.performanceRating = performanceRating; }
    
    @Override
    public String toString() {
        return driverId + " - " + name + " (" + status + ")";
    }
}
