package com.transfleet.view;

import com.transfleet.config.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Fleet Performance & Profitability Analysis Report
 * Major Report using multiple tables for decision making
 */
public class FleetPerformanceReport extends JFrame {
    
    private Connection conn;
    private JTextArea txtExecutiveSummary;
    private JTable tblVehicleAnalysis, tblDriverPerformance, tblRouteEfficiency;
    private DefaultTableModel vehicleModel, driverModel, routeModel;
    private JButton btnGenerate, btnExport, btnPrint, btnJasperReport;
    private com.transfleet.reports.ReportGenerator reportGenerator;
    
   public FleetPerformanceReport() {
        conn = DatabaseConnection.getInstance().getConnection();
        reportGenerator = new com.transfleet.reports.ReportGenerator();
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Fleet Performance & Profitability Analysis Report");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel("Fleet Performance & Profitability Analysis");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tabbed Pane for different sections
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Executive Summary Tab
        JPanel summaryPanel = createExecutiveSummaryPanel();
        tabbedPane.addTab("Executive Summary", summaryPanel);
        
        // Vehicle Analysis Tab
        JPanel vehiclePanel = createVehicleAnalysisPanel();
        tabbedPane.addTab("Vehicle-wise Analysis", vehiclePanel);
        
        // Driver Performance Tab
        JPanel driverPanel = createDriverPerformancePanel();
        tabbedPane.addTab("Driver Performance", driverPanel);
        
        // Route Efficiency Tab
        JPanel routePanel = createRouteEfficiencyPanel();
        tabbedPane.addTab("Route Efficiency", routePanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Button Panel
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnGenerate = new JButton("Internal Report");
        btnJasperReport = new JButton("Generate JasperReport");
        btnExport = new JButton("Export to PDF");
        btnPrint = new JButton("Print Report");
        
        btnGenerate.addActionListener(e -> generateReport());
        btnJasperReport.addActionListener(e -> reportGenerator.generateFleetPerformanceReport());
        btnExport.addActionListener(e -> exportToPDF());
        btnPrint.addActionListener(e -> reportGenerator.printFleetReport());
        
        btnGenerate.setPreferredSize(new Dimension(150, 35));
        btnJasperReport.setPreferredSize(new Dimension(200, 35));
        btnExport.setPreferredSize(new Dimension(150, 35));
        btnPrint.setPreferredSize(new Dimension(150, 35));
        
        // Style the Jasper button (GREEN!)
        btnJasperReport.setBackground(new Color(46, 204, 113));
        btnJasperReport.setForeground(Color.WHITE);
        btnJasperReport.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        buttonPanel.add(btnGenerate);
        buttonPanel.add(btnJasperReport);
        buttonPanel.add(btnExport);
        buttonPanel.add(btnPrint);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createExecutiveSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Executive Summary"));
        
        txtExecutiveSummary = new JTextArea(20, 80);
        txtExecutiveSummary.setEditable(false);
        txtExecutiveSummary.setFont(new Font("Courier New", Font.PLAIN, 12));
        txtExecutiveSummary.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(txtExecutiveSummary);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createVehicleAnalysisPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Vehicle-wise Profitability Analysis"));
        
        String[] columns = {
            "Vehicle ID", "Registration", "Type", "Orders Completed", 
            "Distance (km)", "Revenue (Rs.)", "Fuel Cost (Rs.)", 
            "Maintenance (Rs.)", "Net Profit (Rs.)", "Utilization %"
        };
        
        vehicleModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblVehicleAnalysis = new JTable(vehicleModel);
        tblVehicleAnalysis.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        JScrollPane scrollPane = new JScrollPane(tblVehicleAnalysis);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createDriverPerformancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Driver Performance Matrix"));
        
        String[] columns = {
            "Driver ID", "Name", "Orders Completed", "On-time %", 
            "Distance (km)", "Avg Rating", "Revenue Generated (Rs.)", "Status"
        };
        
        driverModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblDriverPerformance = new JTable(driverModel);
        JScrollPane scrollPane = new JScrollPane(tblDriverPerformance);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRouteEfficiencyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Route Efficiency Analysis"));
        
        String[] columns = {
            "Route ID", "Route Name", "Frequency Used", "Total Distance (km)", 
            "Avg Time (hrs)", "Avg Cost (Rs.)", "Success Rate %", "Profitability Score"
        };
        
        routeModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblRouteEfficiency = new JTable(routeModel);
        JScrollPane scrollPane = new JScrollPane(tblRouteEfficiency);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void generateReport() {
        try {
            generateExecutiveSummary();
            generateVehicleAnalysis();
            generateDriverPerformance();
            generateRouteEfficiency();
            
            JOptionPane.showMessageDialog(this, 
                "Report generated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error generating report: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void generateExecutiveSummary() throws SQLException {
        StringBuilder summary = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        
        summary.append("╔══════════════════════════════════════════════════════════════════════════╗\n");
        summary.append("║          FLEET PERFORMANCE & PROFITABILITY ANALYSIS REPORT              ║\n");
        summary.append("║                    TransFleet Pro Management System                      ║\n");
        summary.append("╚══════════════════════════════════════════════════════════════════════════╝\n\n");
        
        summary.append("Report Generated: ").append(sdf.format(new java.util.Date())).append("\n");
        summary.append("═══════════════════════════════════════════════════════════════════════════\n\n");
        
        // Overall Statistics
        summary.append("EXECUTIVE SUMMARY\n");
        summary.append("─────────────────────────────────────────────────────────────────────────\n\n");
        
        // Total Orders
        String sql1 = "SELECT COUNT(*) as total, " +
                      "SUM(CASE WHEN order_status='Delivered' THEN 1 ELSE 0 END) as delivered, " +
                      "SUM(delivery_cost) as total_revenue " +
                      "FROM orders";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql1);
        
        if (rs.next()) {
            summary.append("Total Orders: ").append(rs.getInt("total")).append("\n");
            summary.append("Orders Delivered: ").append(rs.getInt("delivered")).append("\n");
            summary.append("Total Revenue: Rs. ").append(String.format("%.2f", rs.getDouble("total_revenue"))).append("\n\n");
        }
        
        // Total Expenses
        String sql2 = "SELECT SUM(amount) as total_expenses FROM expenses";
        rs = stmt.executeQuery(sql2);
        double totalExpenses = 0;
        if (rs.next()) {
            totalExpenses = rs.getDouble("total_expenses");
            summary.append("Total Operational Expenses: Rs. ").append(String.format("%.2f", totalExpenses)).append("\n");
        }
        
        // Calculate Profit
        String sql3 = "SELECT SUM(delivery_cost) as revenue FROM orders WHERE order_status='Delivered'";
        rs = stmt.executeQuery(sql3);
        if (rs.next()) {
            double revenue = rs.getDouble("revenue");
            double profit = revenue - totalExpenses;
            summary.append("Net Profit/Loss: Rs. ").append(String.format("%.2f", profit)).append("\n");
            summary.append("Profit Margin: ").append(String.format("%.2f%%", (profit/revenue)*100)).append("\n\n");
        }
        
        // Fleet Statistics
        summary.append("─────────────────────────────────────────────────────────────────────────\n");
        summary.append("FLEET STATISTICS\n\n");
        
        String sql4 = "SELECT " +
                      "COUNT(*) as total_vehicles, " +
                      "SUM(CASE WHEN status='Available' THEN 1 ELSE 0 END) as available, " +
                      "SUM(CASE WHEN status='In-Use' THEN 1 ELSE 0 END) as in_use, " +
                      "SUM(CASE WHEN status='Under-Maintenance' THEN 1 ELSE 0 END) as maintenance " +
                      "FROM vehicles";
        rs = stmt.executeQuery(sql4);
        
        if (rs.next()) {
            summary.append("Total Vehicles: ").append(rs.getInt("total_vehicles")).append("\n");
            summary.append("  - Available: ").append(rs.getInt("available")).append("\n");
            summary.append("  - In Use: ").append(rs.getInt("in_use")).append("\n");
            summary.append("  - Under Maintenance: ").append(rs.getInt("maintenance")).append("\n\n");
        }
        
        // Driver Statistics
        String sql5 = "SELECT " +
                      "COUNT(*) as total_drivers, " +
                      "SUM(CASE WHEN status='Available' THEN 1 ELSE 0 END) as available, " +
                      "SUM(CASE WHEN status='On-Duty' THEN 1 ELSE 0 END) as on_duty, " +
                      "AVG(performance_rating) as avg_rating " +
                      "FROM drivers";
        rs = stmt.executeQuery(sql5);
        
        if (rs.next()) {
            summary.append("Total Drivers: ").append(rs.getInt("total_drivers")).append("\n");
            summary.append("  - Available: ").append(rs.getInt("available")).append("\n");
            summary.append("  - On Duty: ").append(rs.getInt("on_duty")).append("\n");
            summary.append("Average Driver Rating: ").append(String.format("%.2f", rs.getDouble("avg_rating"))).append("/5.00\n\n");
        }
        
        summary.append("─────────────────────────────────────────────────────────────────────────\n");
        summary.append("KEY INSIGHTS & RECOMMENDATIONS\n\n");
        
        // Most Profitable Vehicle Type
        String sql6 = "SELECT v.vehicle_type, COUNT(o.order_id) as orders, SUM(o.delivery_cost) as revenue " +
                      "FROM vehicles v " +
                      "LEFT JOIN orders o ON v.vehicle_id = o.vehicle_id " +
                      "WHERE o.order_status='Delivered' " +
                      "GROUP BY v.vehicle_type " +
                      "ORDER BY revenue DESC LIMIT 1";
        rs = stmt.executeQuery(sql6);
        
        if (rs.next()) {
            summary.append("Most Profitable Vehicle Type: ").append(rs.getString("vehicle_type"))
                   .append(" (Rs. ").append(String.format("%.2f", rs.getDouble("revenue"))).append(")\n");
        }
        
        // Top Performing Driver
        String sql7 = "SELECT d.name, COUNT(o.order_id) as orders, d.performance_rating " +
                      "FROM drivers d " +
                      "LEFT JOIN orders o ON d.driver_id = o.driver_id " +
                      "WHERE o.order_status='Delivered' " +
                      "GROUP BY d.driver_id, d.name, d.performance_rating " +
                      "ORDER BY orders DESC LIMIT 1";
        rs = stmt.executeQuery(sql7);
        
        if (rs.next()) {
            summary.append("Top Performing Driver: ").append(rs.getString("name"))
                   .append(" (").append(rs.getInt("orders")).append(" orders, Rating: ")
                   .append(String.format("%.2f", rs.getDouble("performance_rating"))).append(")\n\n");
        }
        
        summary.append("─────────────────────────────────────────────────────────────────────────\n");
        summary.append("DECISION SUPPORT ANALYSIS\n\n");
        summary.append("1. Fleet Expansion: Analyze vehicle utilization rates below.\n");
        summary.append("2. Route Optimization: Review route efficiency metrics.\n");
        summary.append("3. Driver Training: Identify drivers with ratings < 4.0.\n");
        summary.append("4. Cost Reduction: Focus on vehicles with high maintenance costs.\n");
        summary.append("5. Revenue Growth: Prioritize high-demand routes and vehicle types.\n");
        
        summary.append("\n═══════════════════════════════════════════════════════════════════════════\n");
        
        txtExecutiveSummary.setText(summary.toString());
    }
    
    private void generateVehicleAnalysis() throws SQLException {
        vehicleModel.setRowCount(0);
        
        String sql = "SELECT " +
                     "v.vehicle_id, v.registration_number, v.vehicle_type, " +
                     "COUNT(o.order_id) as total_orders, " +
                     "COALESCE(SUM(o.estimated_distance), 0) as total_distance, " +
                     "COALESCE(SUM(o.delivery_cost), 0) as total_revenue, " +
                     "COALESCE(SUM(e.amount), 0) as total_expenses " +
                     "FROM vehicles v " +
                     "LEFT JOIN orders o ON v.vehicle_id = o.vehicle_id AND o.order_status='Delivered' " +
                     "LEFT JOIN expenses e ON v.vehicle_id = e.vehicle_id " +
                     "GROUP BY v.vehicle_id, v.registration_number, v.vehicle_type " +
                     "ORDER BY total_revenue DESC";
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            int vehicleId = rs.getInt("vehicle_id");
            String regNumber = rs.getString("registration_number");
            String type = rs.getString("vehicle_type");
            int orders = rs.getInt("total_orders");
            double distance = rs.getDouble("total_distance");
            double revenue = rs.getDouble("total_revenue");
            double expenses = rs.getDouble("total_expenses");
            
            // Calculate fuel cost (estimated)
            double fuelCost = expenses * 0.6; // Assume 60% is fuel
            double maintenanceCost = expenses * 0.4; // 40% is maintenance
            double netProfit = revenue - expenses;
            
            // Calculate utilization (orders / total days * 100)
            double utilization = (orders > 0) ? Math.min((orders / 30.0) * 100, 100) : 0;
            
            Object[] row = {
                vehicleId,
                regNumber,
                type,
                orders,
                String.format("%.2f", distance),
                String.format("%.2f", revenue),
                String.format("%.2f", fuelCost),
                String.format("%.2f", maintenanceCost),
                String.format("%.2f", netProfit),
                String.format("%.1f%%", utilization)
            };
            
            vehicleModel.addRow(row);
        }
    }
    
    private void generateDriverPerformance() throws SQLException {
        driverModel.setRowCount(0);
        
        String sql = "SELECT " +
                     "d.driver_id, d.name, d.status, d.performance_rating, " +
                     "COUNT(o.order_id) as total_orders, " +
                     "COALESCE(SUM(o.estimated_distance), 0) as total_distance, " +
                     "COALESCE(SUM(o.delivery_cost), 0) as revenue_generated, " +
                     "SUM(CASE WHEN o.order_status='Delivered' THEN 1 ELSE 0 END) as delivered " +
                     "FROM drivers d " +
                     "LEFT JOIN orders o ON d.driver_id = o.driver_id " +
                     "GROUP BY d.driver_id, d.name, d.status, d.performance_rating " +
                     "ORDER BY total_orders DESC";
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            int driverId = rs.getInt("driver_id");
            String name = rs.getString("name");
            String status = rs.getString("status");
            double rating = rs.getDouble("performance_rating");
            int orders = rs.getInt("total_orders");
            int delivered = rs.getInt("delivered");
            double distance = rs.getDouble("total_distance");
            double revenue = rs.getDouble("revenue_generated");
            
            // Calculate on-time delivery percentage
            double onTimePercent = (orders > 0) ? (delivered / (double) orders) * 100 : 100;
            
            Object[] row = {
                driverId,
                name,
                orders,
                String.format("%.1f%%", onTimePercent),
                String.format("%.2f", distance),
                String.format("%.2f", rating),
                String.format("%.2f", revenue),
                status
            };
            
            driverModel.addRow(row);
        }
    }
    
    private void generateRouteEfficiency() throws SQLException {
        routeModel.setRowCount(0);
        
        String sql = "SELECT " +
                     "r.route_id, r.route_name, " +
                     "COUNT(o.order_id) as frequency, " +
                     "r.distance_km as route_distance, " +
                     "r.estimated_time_hours as avg_time, " +
                     "r.base_rate + r.toll_cost as avg_cost, " +
                     "SUM(CASE WHEN o.order_status='Delivered' THEN 1 ELSE 0 END) as successful, " +
                     "COUNT(o.order_id) as total " +
                     "FROM routes r " +
                     "LEFT JOIN orders o ON r.route_id = o.route_id " +
                     "GROUP BY r.route_id, r.route_name, r.distance_km, r.estimated_time_hours, r.base_rate, r.toll_cost " +
                     "ORDER BY frequency DESC";
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            int routeId = rs.getInt("route_id");
            String routeName = rs.getString("route_name");
            int frequency = rs.getInt("frequency");
            double distance = rs.getDouble("route_distance");
            double avgTime = rs.getDouble("avg_time");
            double avgCost = rs.getDouble("avg_cost");
            int successful = rs.getInt("successful");
            int total = rs.getInt("total");
            
            // Calculate success rate
            double successRate = (total > 0) ? (successful / (double) total) * 100 : 0;
            
            // Calculate profitability score (0-10 scale based on frequency and success rate)
            double profitScore = (frequency > 0) ? Math.min((frequency / 10.0) + (successRate / 20.0), 10) : 0;
            
            Object[] row = {
                routeId,
                routeName,
                frequency,
                String.format("%.2f", distance * frequency),
                String.format("%.2f", avgTime),
                String.format("%.2f", avgCost),
                String.format("%.1f%%", successRate),
                String.format("%.1f/10", profitScore)
            };
            
            routeModel.addRow(row);
        }
    }

    /**
     * Export to PDF using JasperReports
     */
    private void exportToPDF() {
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Save Report as PDF");
        fileChooser.setSelectedFile(new java.io.File("Fleet_Performance_Report.pdf"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
            String outputPath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!outputPath.toLowerCase().endsWith(".pdf")) {
                outputPath += ".pdf";
            }
            reportGenerator.exportFleetReportToPDF(outputPath);
        }
    }
}