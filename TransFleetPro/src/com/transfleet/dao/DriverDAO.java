/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.transfleet.dao;

import com.transfleet.config.DatabaseConnection;
import com.transfleet.model.Driver;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author USER
 */
public class DriverDAO {
    
    private Connection conn;
    
    public DriverDAO() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }
    
    public boolean addDriver(Driver driver) {
        String sql = "INSERT INTO drivers (name, contact, license_number, license_type, date_of_hire, experience_years, status, performance_rating) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, driver.getName());
            pst.setString(2, driver.getContact());
            pst.setString(3, driver.getLicenseNumber());
            pst.setString(4, driver.getLicenseType());
            pst.setDate(5, new java.sql.Date(driver.getDateOfHire().getTime()));
            pst.setInt(6, driver.getExperienceYears());
            pst.setString(7, driver.getStatus());
            pst.setDouble(8, driver.getPerformanceRating());
            
            int result = pst.executeUpdate();
            if (result > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    driver.setDriverId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding driver: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateDriver(Driver driver) {
        String sql = "UPDATE drivers SET name=?, contact=?, license_number=?, license_type=?, date_of_hire=?, experience_years=?, status=?, performance_rating=? WHERE driver_id=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, driver.getName());
            pst.setString(2, driver.getContact());
            pst.setString(3, driver.getLicenseNumber());
            pst.setString(4, driver.getLicenseType());
            pst.setDate(5, new java.sql.Date(driver.getDateOfHire().getTime()));
            pst.setInt(6, driver.getExperienceYears());
            pst.setString(7, driver.getStatus());
            pst.setDouble(8, driver.getPerformanceRating());
            pst.setInt(9, driver.getDriverId());
            
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating driver: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteDriver(int driverId) {
        String sql = "DELETE FROM drivers WHERE driver_id=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, driverId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting driver: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Driver> getAllDrivers() {
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM drivers ORDER BY driver_id DESC";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                drivers.add(extractDriverFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }
    
    public List<Driver> getAvailableDrivers() {
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM drivers WHERE status='Available' ORDER BY name";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                drivers.add(extractDriverFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }
    
    public Driver getDriverById(int driverId) {
        String sql = "SELECT * FROM drivers WHERE driver_id=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, driverId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return extractDriverFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public int getDriverCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM drivers WHERE status=?";
        
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
    
    private Driver extractDriverFromResultSet(ResultSet rs) throws SQLException {
        Driver driver = new Driver();
        driver.setDriverId(rs.getInt("driver_id"));
        driver.setName(rs.getString("name"));
        driver.setContact(rs.getString("contact"));
        driver.setLicenseNumber(rs.getString("license_number"));
        driver.setLicenseType(rs.getString("license_type"));
        driver.setDateOfHire(rs.getDate("date_of_hire"));
        driver.setExperienceYears(rs.getInt("experience_years"));
        driver.setStatus(rs.getString("status"));
        driver.setPerformanceRating(rs.getDouble("performance_rating"));
        return driver;
    }
}
