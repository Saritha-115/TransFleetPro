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
public class Order {
    private int orderId;
    private int customerId;
    private Integer driverId;
    private Integer vehicleId;
    private Integer routeId;
    private String pickupAddress;
    private String deliveryAddress;
    private double packageWeight;
    private double packageVolume;
    private String packageType;
    private String deliveryPriority;
    private double estimatedDistance;
    private double estimatedTime;
    private double deliveryCost;
    private Date orderDate;
    private Date expectedDeliveryDate;
    private Date actualDeliveryDate;
    private String orderStatus;
    
    // Additional fields for display
    private String customerName;
    private String driverName;
    private String vehicleRegNumber;
    
    // Constructors
    public Order() {}
    
    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public Integer getDriverId() { return driverId; }
    public void setDriverId(Integer driverId) { this.driverId = driverId; }
    
    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }
    
    public Integer getRouteId() { return routeId; }
    public void setRouteId(Integer routeId) { this.routeId = routeId; }
    
    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
    
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    
    public double getPackageWeight() { return packageWeight; }
    public void setPackageWeight(double packageWeight) { this.packageWeight = packageWeight; }
    
    public double getPackageVolume() { return packageVolume; }
    public void setPackageVolume(double packageVolume) { this.packageVolume = packageVolume; }
    
    public String getPackageType() { return packageType; }
    public void setPackageType(String packageType) { this.packageType = packageType; }
    
    public String getDeliveryPriority() { return deliveryPriority; }
    public void setDeliveryPriority(String deliveryPriority) { this.deliveryPriority = deliveryPriority; }
    
    public double getEstimatedDistance() { return estimatedDistance; }
    public void setEstimatedDistance(double estimatedDistance) { this.estimatedDistance = estimatedDistance; }
    
    public double getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(double estimatedTime) { this.estimatedTime = estimatedTime; }
    
    public double getDeliveryCost() { return deliveryCost; }
    public void setDeliveryCost(double deliveryCost) { this.deliveryCost = deliveryCost; }
    
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    
    public Date getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public void setExpectedDeliveryDate(Date expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; }
    
    public Date getActualDeliveryDate() { return actualDeliveryDate; }
    public void setActualDeliveryDate(Date actualDeliveryDate) { this.actualDeliveryDate = actualDeliveryDate; }
    
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    
    public String getVehicleRegNumber() { return vehicleRegNumber; }
    public void setVehicleRegNumber(String vehicleRegNumber) { this.vehicleRegNumber = vehicleRegNumber; }
}
