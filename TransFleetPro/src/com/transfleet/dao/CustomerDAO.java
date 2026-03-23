package com.transfleet.dao;

import com.transfleet.config.DatabaseConnection;
import com.transfleet.model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Data Access Object for Customer operations
 */
public class CustomerDAO {
    
    private Connection conn;
    
    public CustomerDAO() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Add new customer
     */
    public boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (name, business_name, contact, email, address, customer_type, credit_limit) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, customer.getName());
            pst.setString(2, customer.getBusinessName());
            pst.setString(3, customer.getContact());
            pst.setString(4, customer.getEmail());
            pst.setString(5, customer.getAddress());
            pst.setString(6, customer.getCustomerType());
            pst.setDouble(7, customer.getCreditLimit());
            
            int result = pst.executeUpdate();
            
            if (result > 0) {
                // Get generated customer ID
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    customer.setCustomerId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error adding customer: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update existing customer
     */
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET name=?, business_name=?, contact=?, email=?, address=?, customer_type=?, credit_limit=? WHERE customer_id=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, customer.getName());
            pst.setString(2, customer.getBusinessName());
            pst.setString(3, customer.getContact());
            pst.setString(4, customer.getEmail());
            pst.setString(5, customer.getAddress());
            pst.setString(6, customer.getCustomerType());
            pst.setDouble(7, customer.getCreditLimit());
            pst.setInt(8, customer.getCustomerId());
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error updating customer: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete customer
     */
    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM customers WHERE customer_id=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, customerId);
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error deleting customer: " + e.getMessage() + 
                "\nCustomer may have existing orders.", 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get customer by ID
     */
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, customerId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return extractCustomerFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all customers
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY customer_id DESC";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                customers.add(extractCustomerFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    
    /**
     * Search customers by name or contact
     */
    public List<Customer> searchCustomers(String keyword) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE name LIKE ? OR contact LIKE ? OR business_name LIKE ?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            pst.setString(3, searchPattern);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                customers.add(extractCustomerFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    
    /**
     * Check if contact number exists
     */
    public boolean contactExists(String contact, int excludeCustomerId) {
        String sql = "SELECT COUNT(*) FROM customers WHERE contact=? AND customer_id!=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, contact);
            pst.setInt(2, excludeCustomerId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get customer count by type
     */
    public int getCustomerCountByType(String type) {
        String sql = "SELECT COUNT(*) FROM customers WHERE customer_type=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, type);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Helper method to extract Customer from ResultSet
     */
    private Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setName(rs.getString("name"));
        customer.setBusinessName(rs.getString("business_name"));
        customer.setContact(rs.getString("contact"));
        customer.setEmail(rs.getString("email"));
        customer.setAddress(rs.getString("address"));
        customer.setCustomerType(rs.getString("customer_type"));
        customer.setCreditLimit(rs.getDouble("credit_limit"));
        customer.setCreatedDate(rs.getTimestamp("created_date"));
        return customer;
    }
}