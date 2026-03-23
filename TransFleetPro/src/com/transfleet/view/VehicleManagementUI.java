package com.transfleet.view;

import com.transfleet.dao.*;
import com.transfleet.model.*;
import com.transfleet.util.ValidationUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Vehicle Management UI
 */
public class VehicleManagementUI extends JFrame {
    
    private JTextField txtRegNumber, txtCapacityWeight, txtCapacityVolume, txtFuelEfficiency, txtLocation;
    private JComboBox<String> cmbVehicleType, cmbFuelType, cmbStatus;
    private JSpinner datePurchase, dateMaintenance;
    private JButton btnSave, btnUpdate, btnDelete, btnClear;
    private JTable tblVehicles;
    private DefaultTableModel tableModel;
    private VehicleDAO vehicleDAO;
    private Vehicle currentVehicle;
    
    public VehicleManagementUI() {
        vehicleDAO = new VehicleDAO();
        initComponents();
        loadVehicles();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Vehicle Management");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel("Fleet Management");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.WEST);
        
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Vehicle Details"));
        panel.setPreferredSize(new Dimension(420, 500));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        addField(panel, gbc, row++, "Registration No:*", txtRegNumber = new JTextField(20));
        
        String[] types = {"Truck", "Van", "Bike", "Car"};
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Vehicle Type:*"), gbc);
        gbc.gridx = 1;
        cmbVehicleType = new JComboBox<>(types);
        panel.add(cmbVehicleType, gbc);
        row++;
        
        addField(panel, gbc, row++, "Capacity Weight (kg):*", txtCapacityWeight = new JTextField(20));
        addField(panel, gbc, row++, "Capacity Volume (m³):*", txtCapacityVolume = new JTextField(20));
        
        String[] fuelTypes = {"Petrol", "Diesel", "Electric"};
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Fuel Type:*"), gbc);
        gbc.gridx = 1;
        cmbFuelType = new JComboBox<>(fuelTypes);
        panel.add(cmbFuelType, gbc);
        row++;
        
        addField(panel, gbc, row++, "Fuel Efficiency (km/l):*", txtFuelEfficiency = new JTextField(20));
        
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Purchase Date:*"), gbc);
        datePurchase = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor1 = new JSpinner.DateEditor(datePurchase, "dd-MM-yyyy");
        datePurchase.setEditor(editor1);
        gbc.gridx = 1;
        panel.add(datePurchase, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Last Maintenance:"), gbc);
        dateMaintenance = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor2 = new JSpinner.DateEditor(dateMaintenance, "dd-MM-yyyy");
        dateMaintenance.setEditor(editor2);
        gbc.gridx = 1;
        panel.add(dateMaintenance, gbc);
        row++;
        
        String[] statuses = {"Available", "In-Use", "Under-Maintenance"};
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        cmbStatus = new JComboBox<>(statuses);
        panel.add(cmbStatus, gbc);
        row++;
        
        addField(panel, gbc, row++, "Current Location:", txtLocation = new JTextField(20));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSave = new JButton("Save");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        
        btnSave.addActionListener(e -> saveVehicle());
        btnUpdate.addActionListener(e -> updateVehicle());
        btnDelete.addActionListener(e -> deleteVehicle());
        btnClear.addActionListener(e -> clearForm());
        
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Vehicles List"));
        
        String[] columns = {"ID", "Reg No", "Type", "Capacity", "Fuel", "Efficiency", "Status", "Location"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblVehicles = new JTable(tableModel);
        tblVehicles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) loadVehicleToForm();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblVehicles);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadVehicles());
        searchPanel.add(btnRefresh);
        panel.add(searchPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private boolean validateForm() {
        if (!ValidationUtil.validateRegistrationNumber(txtRegNumber.getText())) return false;
        if (!ValidationUtil.validatePositiveNumber(txtCapacityWeight.getText(), "Capacity Weight")) return false;
        if (!ValidationUtil.validatePositiveNumber(txtCapacityVolume.getText(), "Capacity Volume")) return false;
        if (!ValidationUtil.validateRange(txtFuelEfficiency.getText(), "Fuel Efficiency", 5, 50)) return false;
        return true;
    }
    
    private void saveVehicle() {
        if (!validateForm()) return;
        
        Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber(txtRegNumber.getText().toUpperCase());
        vehicle.setVehicleType((String) cmbVehicleType.getSelectedItem());
        vehicle.setCapacityWeight(Double.parseDouble(txtCapacityWeight.getText()));
        vehicle.setCapacityVolume(Double.parseDouble(txtCapacityVolume.getText()));
        vehicle.setFuelType((String) cmbFuelType.getSelectedItem());
        vehicle.setFuelEfficiency(Double.parseDouble(txtFuelEfficiency.getText()));
        vehicle.setPurchaseDate((Date) datePurchase.getValue());
        vehicle.setLastMaintenanceDate((Date) dateMaintenance.getValue());
        vehicle.setStatus((String) cmbStatus.getSelectedItem());
        vehicle.setCurrentLocation(txtLocation.getText());
        
        if (vehicleDAO.addVehicle(vehicle)) {
            ValidationUtil.showSuccess("Vehicle added successfully!");
            clearForm();
            loadVehicles();
        }
    }
    
    private void updateVehicle() {
        if (currentVehicle == null || !validateForm()) return;
        
        currentVehicle.setRegistrationNumber(txtRegNumber.getText().toUpperCase());
        currentVehicle.setVehicleType((String) cmbVehicleType.getSelectedItem());
        currentVehicle.setCapacityWeight(Double.parseDouble(txtCapacityWeight.getText()));
        currentVehicle.setCapacityVolume(Double.parseDouble(txtCapacityVolume.getText()));
        currentVehicle.setFuelType((String) cmbFuelType.getSelectedItem());
        currentVehicle.setFuelEfficiency(Double.parseDouble(txtFuelEfficiency.getText()));
        currentVehicle.setPurchaseDate((Date) datePurchase.getValue());
        currentVehicle.setLastMaintenanceDate((Date) dateMaintenance.getValue());
        currentVehicle.setStatus((String) cmbStatus.getSelectedItem());
        currentVehicle.setCurrentLocation(txtLocation.getText());
        
        if (vehicleDAO.updateVehicle(currentVehicle)) {
            ValidationUtil.showSuccess("Vehicle updated successfully!");
            clearForm();
            loadVehicles();
        }
    }
    
    private void deleteVehicle() {
        if (currentVehicle == null) return;
        
        if (ValidationUtil.showConfirmation("Delete this vehicle?")) {
            if (vehicleDAO.deleteVehicle(currentVehicle.getVehicleId())) {
                ValidationUtil.showSuccess("Vehicle deleted!");
                clearForm();
                loadVehicles();
            }
        }
    }
    
    private void loadVehicleToForm() {
        int row = tblVehicles.getSelectedRow();
        if (row < 0) return;
        
        int vehicleId = (int) tableModel.getValueAt(row, 0);
        currentVehicle = vehicleDAO.getVehicleById(vehicleId);
        
        if (currentVehicle != null) {
            txtRegNumber.setText(currentVehicle.getRegistrationNumber());
            cmbVehicleType.setSelectedItem(currentVehicle.getVehicleType());
            txtCapacityWeight.setText(String.valueOf(currentVehicle.getCapacityWeight()));
            txtCapacityVolume.setText(String.valueOf(currentVehicle.getCapacityVolume()));
            cmbFuelType.setSelectedItem(currentVehicle.getFuelType());
            txtFuelEfficiency.setText(String.valueOf(currentVehicle.getFuelEfficiency()));
            datePurchase.setValue(currentVehicle.getPurchaseDate());
            if (currentVehicle.getLastMaintenanceDate() != null) {
                dateMaintenance.setValue(currentVehicle.getLastMaintenanceDate());
            }
            cmbStatus.setSelectedItem(currentVehicle.getStatus());
            txtLocation.setText(currentVehicle.getCurrentLocation());
            
            btnSave.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
        }
    }
    
    private void loadVehicles() {
        tableModel.setRowCount(0);
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        
        for (Vehicle v : vehicles) {
            Object[] row = {
                v.getVehicleId(),
                v.getRegistrationNumber(),
                v.getVehicleType(),
                v.getCapacityWeight() + " kg",
                v.getFuelType(),
                v.getFuelEfficiency() + " km/l",
                v.getStatus(),
                v.getCurrentLocation()
            };
            tableModel.addRow(row);
        }
    }
    
    private void clearForm() {
        txtRegNumber.setText("");
        cmbVehicleType.setSelectedIndex(0);
        txtCapacityWeight.setText("");
        txtCapacityVolume.setText("");
        cmbFuelType.setSelectedIndex(0);
        txtFuelEfficiency.setText("");
        datePurchase.setValue(new Date());
        dateMaintenance.setValue(new Date());
        cmbStatus.setSelectedIndex(0);
        txtLocation.setText("");
        
        currentVehicle = null;
        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }
}