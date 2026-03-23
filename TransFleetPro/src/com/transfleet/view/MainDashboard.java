package com.transfleet.view;

import com.transfleet.dao.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main Dashboard - Entry point of the application
 * Displays key statistics and navigation
 */
public class MainDashboard extends JFrame {
    
    private JPanel statsPanel;
    private JLabel lblActiveOrders, lblAvailableVehicles, lblActiveDrivers, lblTodayRevenue;
    private OrderDAO orderDAO;
    private VehicleDAO vehicleDAO;
    private DriverDAO driverDAO;
    
    public MainDashboard() {
        initComponents();
        initDAO();
        loadDashboardStats();
        setLocationRelativeTo(null);
    }
    
    private void initDAO() {
        orderDAO = new OrderDAO();
        vehicleDAO = new VehicleDAO();
        driverDAO = new DriverDAO();
    }
    
    private void initComponents() {
        setTitle("TransFleet Pro - Transportation Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Top Menu Bar
        createMenuBar();
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(240, 240, 245));
        
        // Statistics Cards Panel
        statsPanel = createStatsPanel();
        contentPanel.add(statsPanel, BorderLayout.NORTH);
        
        // Quick Actions Panel
        JPanel actionsPanel = createQuickActionsPanel();
        contentPanel.add(actionsPanel, BorderLayout.CENTER);
        
        // Recent Activity Panel
        JPanel recentPanel = createRecentActivityPanel();
        contentPanel.add(recentPanel, BorderLayout.SOUTH);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Orders Menu
        JMenu ordersMenu = new JMenu("Orders");
        JMenuItem newOrder = new JMenuItem("New Delivery Order");
        JMenuItem viewOrders = new JMenuItem("View All Orders");
        newOrder.addActionListener(e -> openOrderManagement());
        viewOrders.addActionListener(e -> openOrderManagement());
        ordersMenu.add(newOrder);
        ordersMenu.add(viewOrders);
        
        // Fleet Menu
        JMenu fleetMenu = new JMenu("Fleet");
        JMenuItem manageVehicles = new JMenuItem("Manage Vehicles");
        JMenuItem manageDrivers = new JMenuItem("Manage Drivers");
        manageVehicles.addActionListener(e -> openVehicleManagement());
        manageDrivers.addActionListener(e -> openDriverManagement());
        fleetMenu.add(manageVehicles);
        fleetMenu.add(manageDrivers);
        
        // Customers Menu
        JMenu customersMenu = new JMenu("Customers");
        JMenuItem manageCustomers = new JMenuItem("Manage Customers");
        manageCustomers.addActionListener(e -> openCustomerManagement());
        customersMenu.add(manageCustomers);
        
        // Reports Menu
        JMenu reportsMenu = new JMenu("Reports");
        JMenuItem fleetReport = new JMenuItem("Fleet Performance Report");
        JMenuItem dailyReport = new JMenuItem("Daily Summary");
        fleetReport.addActionListener(e -> openFleetReport());
        dailyReport.addActionListener(e -> JOptionPane.showMessageDialog(this, "Daily Report - Coming Soon!"));
        reportsMenu.add(fleetReport);
        reportsMenu.add(dailyReport);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> showAbout());
        helpMenu.add(about);
        
        menuBar.add(ordersMenu);
        menuBar.add(fleetMenu);
        menuBar.add(customersMenu);
        menuBar.add(reportsMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(41, 128, 185));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("TransFleet Pro Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JButton refreshBtn = new JButton("Refresh Dashboard");
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> loadDashboardStats());
        
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(refreshBtn, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setOpaque(false);
        
        lblActiveOrders = new JLabel("0");
        lblAvailableVehicles = new JLabel("0");
        lblActiveDrivers = new JLabel("0");
        lblTodayRevenue = new JLabel("Rs. 0.00");
        
        panel.add(createStatCard("Active Orders", lblActiveOrders, new Color(52, 152, 219)));
        panel.add(createStatCard("Available Vehicles", lblAvailableVehicles, new Color(46, 204, 113)));
        panel.add(createStatCard("Active Drivers", lblActiveDrivers, new Color(241, 196, 15)));
        panel.add(createStatCard("Today's Revenue", lblTodayRevenue, new Color(231, 76, 60)));
        
        return panel;
    }
    
    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(Color.GRAY);
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(color);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 15, 15));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Quick Actions",
            0, 0, 
            new Font("Segoe UI", Font.BOLD, 16)
        ));
        
        panel.add(createActionButton("New Delivery Order", "Create new order", e -> openOrderManagement()));
        panel.add(createActionButton("Manage Vehicles", "Add/Edit vehicles", e -> openVehicleManagement()));
        panel.add(createActionButton("Manage Drivers", "Add/Edit drivers", e -> openDriverManagement()));
        panel.add(createActionButton("Manage Customers", "Customer database", e -> openCustomerManagement()));
        panel.add(createActionButton("View Reports", "Analytics & Reports", e -> openFleetReport()));
        panel.add(createActionButton("Exit Application", "Close system", e -> exitApplication()));
        
        return panel;
    }
    
    private JButton createActionButton(String title, String description, ActionListener listener) {
        JButton button = new JButton("<html><center><b>" + title + "</b><br><small>" + description + "</small></center></html>");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(listener);
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(52, 152, 219));
            }
        });
        
        return button;
    }
    
    private JPanel createRecentActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "System Status",
            0, 0, 
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        
        JTextArea statusArea = new JTextArea(5, 50);
        statusArea.setEditable(false);
        statusArea.setText("System initialized successfully.\nDatabase connected.\nReady for operations.");
        
        JScrollPane scrollPane = new JScrollPane(statusArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadDashboardStats() {
        try {
            // Active Orders (Pending, Assigned, In-Transit)
            int activeOrders = orderDAO.getOrderCountByStatus("Pending") + 
                              orderDAO.getOrderCountByStatus("Assigned") + 
                              orderDAO.getOrderCountByStatus("In-Transit");
            lblActiveOrders.setText(String.valueOf(activeOrders));
            
            // Available Vehicles
            int availableVehicles = vehicleDAO.getVehicleCountByStatus("Available");
            lblAvailableVehicles.setText(String.valueOf(availableVehicles));
            
            // Active Drivers (On-Duty)
            int activeDrivers = driverDAO.getDriverCountByStatus("On-Duty");
            lblActiveDrivers.setText(String.valueOf(activeDrivers));
            
            // Today's Revenue
            double revenue = orderDAO.getTodayRevenue();
            lblTodayRevenue.setText(String.format("Rs. %.2f", revenue));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading dashboard: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openOrderManagement() {
        OrderManagementUI orderUI = new OrderManagementUI();
        orderUI.setVisible(true);
        orderUI.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                loadDashboardStats();
            }
        });
    }
    
    private void openVehicleManagement() {
        VehicleManagementUI vehicleUI = new VehicleManagementUI();
        vehicleUI.setVisible(true);
        vehicleUI.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                loadDashboardStats();
            }
        });
    }
    
    private void openDriverManagement() {
        DriverManagementUI driverUI = new DriverManagementUI();
        driverUI.setVisible(true);
        driverUI.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                loadDashboardStats();
            }
        });
    }
    
    private void openCustomerManagement() {
        CustomerManagementUI customerUI = new CustomerManagementUI();
        customerUI.setVisible(true);
    }
    
    private void openFleetReport() {
        FleetPerformanceReport report = new FleetPerformanceReport();
        report.setVisible(true);
    }
    
    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            "TransFleet Pro v1.0\n" +
            "Transportation Management System\n\n" +
            "Developed for Fleet & Route Optimization\n" +
            "© 2026 - All Rights Reserved",
            "About TransFleet Pro",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit?",
            "Exit Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            MainDashboard dashboard = new MainDashboard();
            dashboard.setVisible(true);
        });
    }
}
