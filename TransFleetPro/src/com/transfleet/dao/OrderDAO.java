package com.transfleet.dao;

import com.transfleet.config.DatabaseConnection;
import com.transfleet.model.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Data Access Object for Order operations
 */
public class OrderDAO {
    
    private Connection conn;
    
    public OrderDAO() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Add new order
     */
    public boolean addOrder(Order order) {
        String sql = "INSERT INTO orders (customer_id, driver_id, vehicle_id, route_id, pickup_address, delivery_address, package_weight, package_volume, package_type, delivery_priority, estimated_distance, estimated_time, delivery_cost, expected_delivery_date, order_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, order.getCustomerId());
            
            if (order.getDriverId() != null) {
                pst.setInt(2, order.getDriverId());
            } else {
                pst.setNull(2, Types.INTEGER);
            }
            
            if (order.getVehicleId() != null) {
                pst.setInt(3, order.getVehicleId());
            } else {
                pst.setNull(3, Types.INTEGER);
            }
            
            if (order.getRouteId() != null) {
                pst.setInt(4, order.getRouteId());
            } else {
                pst.setNull(4, Types.INTEGER);
            }
            
            pst.setString(5, order.getPickupAddress());
            pst.setString(6, order.getDeliveryAddress());
            pst.setDouble(7, order.getPackageWeight());
            pst.setDouble(8, order.getPackageVolume());
            pst.setString(9, order.getPackageType());
            pst.setString(10, order.getDeliveryPriority());
            pst.setDouble(11, order.getEstimatedDistance());
            pst.setDouble(12, order.getEstimatedTime());
            pst.setDouble(13, order.getDeliveryCost());
            pst.setDate(14, new java.sql.Date(order.getExpectedDeliveryDate().getTime()));
            pst.setString(15, order.getOrderStatus());
            
            int result = pst.executeUpdate();
            
            if (result > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    order.setOrderId(rs.getInt(1));
                }
                
                // Update vehicle and driver status if assigned
                if (order.getVehicleId() != null) {
                    updateVehicleStatus(order.getVehicleId(), "In-Use");
                }
                if (order.getDriverId() != null) {
                    updateDriverStatus(order.getDriverId(), "On-Duty");
                }
                
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error adding order: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update existing order
     */
    public boolean updateOrder(Order order) {
        String sql = "UPDATE orders SET customer_id=?, driver_id=?, vehicle_id=?, route_id=?, pickup_address=?, delivery_address=?, package_weight=?, package_volume=?, package_type=?, delivery_priority=?, estimated_distance=?, estimated_time=?, delivery_cost=?, expected_delivery_date=?, order_status=? WHERE order_id=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            // Get old assignment to free resources
            Order oldOrder = getOrderById(order.getOrderId());
            
            pst.setInt(1, order.getCustomerId());
            
            if (order.getDriverId() != null) {
                pst.setInt(2, order.getDriverId());
            } else {
                pst.setNull(2, Types.INTEGER);
            }
            
            if (order.getVehicleId() != null) {
                pst.setInt(3, order.getVehicleId());
            } else {
                pst.setNull(3, Types.INTEGER);
            }
            
            if (order.getRouteId() != null) {
                pst.setInt(4, order.getRouteId());
            } else {
                pst.setNull(4, Types.INTEGER);
            }
            
            pst.setString(5, order.getPickupAddress());
            pst.setString(6, order.getDeliveryAddress());
            pst.setDouble(7, order.getPackageWeight());
            pst.setDouble(8, order.getPackageVolume());
            pst.setString(9, order.getPackageType());
            pst.setString(10, order.getDeliveryPriority());
            pst.setDouble(11, order.getEstimatedDistance());
            pst.setDouble(12, order.getEstimatedTime());
            pst.setDouble(13, order.getDeliveryCost());
            pst.setDate(14, new java.sql.Date(order.getExpectedDeliveryDate().getTime()));
            pst.setString(15, order.getOrderStatus());
            pst.setInt(16, order.getOrderId());
            
            boolean updated = pst.executeUpdate() > 0;
            
            if (updated) {
                // Free old resources if changed
                if (oldOrder != null) {
                    if (oldOrder.getVehicleId() != null && !oldOrder.getVehicleId().equals(order.getVehicleId())) {
                        updateVehicleStatus(oldOrder.getVehicleId(), "Available");
                    }
                    if (oldOrder.getDriverId() != null && !oldOrder.getDriverId().equals(order.getDriverId())) {
                        updateDriverStatus(oldOrder.getDriverId(), "Available");
                    }
                }
                
                // Assign new resources
                if (order.getVehicleId() != null) {
                    updateVehicleStatus(order.getVehicleId(), "In-Use");
                }
                if (order.getDriverId() != null) {
                    updateDriverStatus(order.getDriverId(), "On-Duty");
                }
                
                // If order is delivered or cancelled, free resources
                if (order.getOrderStatus().equals("Delivered") || order.getOrderStatus().equals("Cancelled")) {
                    if (order.getVehicleId() != null) {
                        updateVehicleStatus(order.getVehicleId(), "Available");
                    }
                    if (order.getDriverId() != null) {
                        updateDriverStatus(order.getDriverId(), "Available");
                    }
                }
            }
            
            return updated;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error updating order: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete order
     */
    public boolean deleteOrder(int orderId) {
        // First get the order to free resources
        Order order = getOrderById(orderId);
        
        String sql = "DELETE FROM orders WHERE order_id=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, orderId);
            boolean deleted = pst.executeUpdate() > 0;
            
            if (deleted && order != null) {
                // Free vehicle and driver
                if (order.getVehicleId() != null) {
                    updateVehicleStatus(order.getVehicleId(), "Available");
                }
                if (order.getDriverId() != null) {
                    updateDriverStatus(order.getDriverId(), "Available");
                }
            }
            
            return deleted;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error deleting order: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get order by ID
     */
    public Order getOrderById(int orderId) {
        String sql = "SELECT o.*, c.name as customer_name, d.name as driver_name, v.registration_number " +
                     "FROM orders o " +
                     "LEFT JOIN customers c ON o.customer_id = c.customer_id " +
                     "LEFT JOIN drivers d ON o.driver_id = d.driver_id " +
                     "LEFT JOIN vehicles v ON o.vehicle_id = v.vehicle_id " +
                     "WHERE o.order_id=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, orderId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return extractOrderFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all orders
     */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, c.name as customer_name, d.name as driver_name, v.registration_number " +
                     "FROM orders o " +
                     "LEFT JOIN customers c ON o.customer_id = c.customer_id " +
                     "LEFT JOIN drivers d ON o.driver_id = d.driver_id " +
                     "LEFT JOIN vehicles v ON o.vehicle_id = v.vehicle_id " +
                     "ORDER BY o.order_id DESC";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    /**
     * Get orders by status
     */
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, c.name as customer_name, d.name as driver_name, v.registration_number " +
                     "FROM orders o " +
                     "LEFT JOIN customers c ON o.customer_id = c.customer_id " +
                     "LEFT JOIN drivers d ON o.driver_id = d.driver_id " +
                     "LEFT JOIN vehicles v ON o.vehicle_id = v.vehicle_id " +
                     "WHERE o.order_status=? " +
                     "ORDER BY o.order_id DESC";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, status);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    /**
     * Get order count by status
     */
    public int getOrderCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM orders WHERE order_status=?";
        
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
    
    /**
     * Get today's revenue
     */
    public double getTodayRevenue() {
        String sql = "SELECT SUM(delivery_cost) FROM orders WHERE DATE(order_date) = CURDATE() AND order_status IN ('Delivered', 'In-Transit', 'Assigned')";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    /**
     * Helper methods
     */
    private void updateVehicleStatus(int vehicleId, String status) {
        String sql = "UPDATE vehicles SET status=? WHERE vehicle_id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, status);
            pst.setInt(2, vehicleId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void updateDriverStatus(int driverId, String status) {
        String sql = "UPDATE drivers SET status=? WHERE driver_id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, status);
            pst.setInt(2, driverId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Extract Order from ResultSet
     */
    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setCustomerId(rs.getInt("customer_id"));
        
        int driverId = rs.getInt("driver_id");
        order.setDriverId(rs.wasNull() ? null : driverId);
        
        int vehicleId = rs.getInt("vehicle_id");
        order.setVehicleId(rs.wasNull() ? null : vehicleId);
        
        int routeId = rs.getInt("route_id");
        order.setRouteId(rs.wasNull() ? null : routeId);
        
        order.setPickupAddress(rs.getString("pickup_address"));
        order.setDeliveryAddress(rs.getString("delivery_address"));
        order.setPackageWeight(rs.getDouble("package_weight"));
        order.setPackageVolume(rs.getDouble("package_volume"));
        order.setPackageType(rs.getString("package_type"));
        order.setDeliveryPriority(rs.getString("delivery_priority"));
        order.setEstimatedDistance(rs.getDouble("estimated_distance"));
        order.setEstimatedTime(rs.getDouble("estimated_time"));
        order.setDeliveryCost(rs.getDouble("delivery_cost"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setExpectedDeliveryDate(rs.getDate("expected_delivery_date"));
        order.setActualDeliveryDate(rs.getTimestamp("actual_delivery_date"));
        order.setOrderStatus(rs.getString("order_status"));
        
        // Additional fields
        order.setCustomerName(rs.getString("customer_name"));
        order.setDriverName(rs.getString("driver_name"));
        order.setVehicleRegNumber(rs.getString("registration_number"));
        
        return order;
    }
}