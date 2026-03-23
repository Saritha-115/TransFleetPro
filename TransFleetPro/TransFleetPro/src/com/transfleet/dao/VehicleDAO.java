
package com.transfleet.dao;

import com.transfleet.config.DatabaseConnection;
import com.transfleet.model.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Data Access Object for Vehicle operations
 */
public class VehicleDAO {
    
    private Connection conn;
    
    public VehicleDAO() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }
    
    public boolean addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (registration_number, vehicle_type, capacity_weight, capacity_volume, fuel_type, fuel_efficiency, purchase_date, status, current_location) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, vehicle.getRegistrationNumber());
            pst.setString(2, vehicle.getVehicleType());
            pst.setDouble(3, vehicle.getCapacityWeight());
            pst.setDouble(4, vehicle.getCapacityVolume());
            pst.setString(5, vehicle.getFuelType());
            pst.setDouble(6, vehicle.getFuelEfficiency());
            pst.setDate(7, new java.sql.Date(vehicle.getPurchaseDate().getTime()));
            pst.setString(8, vehicle.getStatus());
            pst.setString(9, vehicle.getCurrentLocation());
            
            int result = pst.executeUpdate();
            if (result > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    vehicle.setVehicleId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding vehicle: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateVehicle(Vehicle vehicle) {
        String sql = "UPDATE vehicles SET registration_number=?, vehicle_type=?, capacity_weight=?, capacity_volume=?, fuel_type=?, fuel_efficiency=?, purchase_date=?, last_maintenance_date=?, status=?, current_location=? WHERE vehicle_id=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, vehicle.getRegistrationNumber());
            pst.setString(2, vehicle.getVehicleType());
            pst.setDouble(3, vehicle.getCapacityWeight());
            pst.setDouble(4, vehicle.getCapacityVolume());
            pst.setString(5, vehicle.getFuelType());
            pst.setDouble(6, vehicle.getFuelEfficiency());
            pst.setDate(7, new java.sql.Date(vehicle.getPurchaseDate().getTime()));
            pst.setDate(8, vehicle.getLastMaintenanceDate() != null ? new java.sql.Date(vehicle.getLastMaintenanceDate().getTime()) : null);
            pst.setString(9, vehicle.getStatus());
            pst.setString(10, vehicle.getCurrentLocation());
            pst.setInt(11, vehicle.getVehicleId());
            
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating vehicle: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteVehicle(int vehicleId) {
        String sql = "DELETE FROM vehicles WHERE vehicle_id=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, vehicleId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting vehicle: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles ORDER BY vehicle_id DESC";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicles.add(extractVehicleFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }
    
    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE status='Available' ORDER BY vehicle_type";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicles.add(extractVehicleFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }
    
    public Vehicle getVehicleById(int vehicleId) {
        String sql = "SELECT * FROM vehicles WHERE vehicle_id=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, vehicleId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return extractVehicleFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public int getVehicleCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM vehicles WHERE status=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, status);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private Vehicle extractVehicleFromResultSet(ResultSet rs) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(rs.getInt("vehicle_id"));
        vehicle.setRegistrationNumber(rs.getString("registration_number"));
        vehicle.setVehicleType(rs.getString("vehicle_type"));
        vehicle.setCapacityWeight(rs.getDouble("capacity_weight"));
        vehicle.setCapacityVolume(rs.getDouble("capacity_volume"));
        vehicle.setFuelType(rs.getString("fuel_type"));
        vehicle.setFuelEfficiency(rs.getDouble("fuel_efficiency"));
        vehicle.setPurchaseDate(rs.getDate("purchase_date"));
        vehicle.setLastMaintenanceDate(rs.getDate("last_maintenance_date"));
        vehicle.setStatus(rs.getString("status"));
        vehicle.setCurrentLocation(rs.getString("current_location"));
        return vehicle;
    }
}