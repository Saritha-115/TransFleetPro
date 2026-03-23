package com.transfleet.reports;

import com.transfleet.config.DatabaseConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * JasperReports Generator Class
 * Handles report compilation, generation, and viewing
 */
public class ReportGenerator {
    
    private Connection conn;
    
    public ReportGenerator() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Generate and display Fleet Performance Report
     */
    public void generateFleetPerformanceReport() {
        try {
            // Load report template from resources
            InputStream reportStream = getClass().getResourceAsStream("/reports/fleet_performance_report.jrxml");
            
            if (reportStream == null) {
                JOptionPane.showMessageDialog(null, 
                    "Report template not found!\nPlease ensure fleet_performance_report.jrxml is in resources/reports/ folder.", 
                    "Report Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Compile report
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            
            // Set parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_TITLE", "Fleet Performance & Profitability Analysis");
            parameters.put("COMPANY_NAME", "TransFleet Pro Management System");
            
            // Fill report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            
            // Display report in viewer
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle("Fleet Performance Report");
            viewer.setVisible(true);
            
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, 
                "Error generating report: " + e.getMessage(), 
                "Report Generation Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Generate and export Fleet Performance Report to PDF
     */
    public void exportFleetReportToPDF(String outputPath) {
        try {
            InputStream reportStream = getClass().getResourceAsStream("/reports/fleet_performance_report.jrxml");
            
            if (reportStream == null) {
                JOptionPane.showMessageDialog(null, 
                    "Report template not found!", 
                    "Report Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Compile report
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            
            // Set parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_TITLE", "Fleet Performance & Profitability Analysis");
            parameters.put("COMPANY_NAME", "TransFleet Pro Management System");
            
            // Fill report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            
            // Export to PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
            
            JOptionPane.showMessageDialog(null, 
                "Report exported successfully to:\n" + outputPath, 
                "Export Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, 
                "Error exporting report: " + e.getMessage(), 
                "Export Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Print report directly to printer
     */
    public void printFleetReport() {
        try {
            InputStream reportStream = getClass().getResourceAsStream("/reports/fleet_performance_report.jrxml");
            
            if (reportStream == null) {
                JOptionPane.showMessageDialog(null, 
                    "Report template not found!", 
                    "Report Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_TITLE", "Fleet Performance & Profitability Analysis");
            parameters.put("COMPANY_NAME", "TransFleet Pro Management System");
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            
            // Print report
            JasperPrintManager.printReport(jasperPrint, true);
            
            JOptionPane.showMessageDialog(null, 
                "Report sent to printer successfully!", 
                "Print Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, 
                "Error printing report: " + e.getMessage(), 
                "Print Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
