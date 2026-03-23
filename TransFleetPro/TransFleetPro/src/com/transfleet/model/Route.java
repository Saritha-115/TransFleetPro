/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.transfleet.model;

/**
 *
 * @author USER
 */
public class Route {
    private int routeId;
    private String routeName;
    private String startPoint;
    private String endPoint;
    private double distanceKm;
    private double estimatedTimeHours;
    private double tollCost;
    private double baseRate;
    private String trafficLevel;
    
    // Constructors
    public Route() {}
    
    // Getters and Setters
    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }
    
    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }
    
    public String getStartPoint() { return startPoint; }
    public void setStartPoint(String startPoint) { this.startPoint = startPoint; }
    
    public String getEndPoint() { return endPoint; }
    public void setEndPoint(String endPoint) { this.endPoint = endPoint; }
    
    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
    
    public double getEstimatedTimeHours() { return estimatedTimeHours; }
    public void setEstimatedTimeHours(double estimatedTimeHours) { this.estimatedTimeHours = estimatedTimeHours; }
    
    public double getTollCost() { return tollCost; }
    public void setTollCost(double tollCost) { this.tollCost = tollCost; }
    
    public double getBaseRate() { return baseRate; }
    public void setBaseRate(double baseRate) { this.baseRate = baseRate; }
    
    public String getTrafficLevel() { return trafficLevel; }
    public void setTrafficLevel(String trafficLevel) { this.trafficLevel = trafficLevel; }
    
    @Override
    public String toString() {
        return routeId + " - " + routeName;
    }
}
