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
public class Customer {
    private int customerId;
    private String name;
    private String businessName;
    private String contact;
    private String email;
    private String address;
    private String customerType;
    private double creditLimit;
    private Date createdDate;
    
    // Constructors
    public Customer() {}
    
    public Customer(int customerId, String name, String contact, String address, String customerType) {
        this.customerId = customerId;
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.customerType = customerType;
    }
    
    // Getters and Setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getCustomerType() { return customerType; }
    public void setCustomerType(String customerType) { this.customerType = customerType; }
    
    public double getCreditLimit() { return creditLimit; }
    public void setCreditLimit(double creditLimit) { this.creditLimit = creditLimit; }
    
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    
    @Override
    public String toString() {
        return customerId + " - " + name;
    }
}
