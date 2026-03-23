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

class DriverManagementUI extends JFrame {
    
    private JTextField txtName, txtContact, txtLicenseNumber, txtExperience, txtRating;
    private JComboBox<String> cmbLicenseType, cmbStatus;
    private JSpinner dateHire;
    private JButton btnSave, btnUpdate, btnDelete, btnClear;
    private JTable tblDrivers;
    private DefaultTableModel tableModel;
    private DriverDAO driverDAO;
    private Driver currentDriver;
    
    public DriverManagementUI() {
        driverDAO = new DriverDAO();
        initComponents();
        loadDrivers();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Driver Management");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(241, 196, 15));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel("Driver Management");
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
        panel.setBorder(BorderFactory.createTitledBorder("Driver Details"));
        panel.setPreferredSize(new Dimension(380, 500));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        addField(panel, gbc, row++, "Name:*", txtName = new JTextField(20));
        addField(panel, gbc, row++, "Contact:*", txtContact = new JTextField(20));
        addField(panel, gbc, row++, "License Number:*", txtLicenseNumber = new JTextField(20));
        
        String[] licenseTypes = {"LightVehicle", "HeavyVehicle", "All"};
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("License Type:*"), gbc);
        gbc.gridx = 1;
        cmbLicenseType = new JComboBox<>(licenseTypes);
        panel.add(cmbLicenseType, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Date of Hire:*"), gbc);
        dateHire = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateHire, "dd-MM-yyyy");
        dateHire.setEditor(editor);
        gbc.gridx = 1;
        panel.add(dateHire, gbc);
        row++;
        
        addField(panel, gbc, row++, "Experience (years):*", txtExperience = new JTextField(20));
        
        String[] statuses = {"Available", "On-Duty", "Off-Duty"};
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        cmbStatus = new JComboBox<>(statuses);
        panel.add(cmbStatus, gbc);
        row++;
        
        addField(panel, gbc, row++, "Performance Rating:", txtRating = new JTextField(20));
        txtRating.setText("5.00");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSave = new JButton("Save");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        
        btnSave.addActionListener(e -> saveDriver());
        btnUpdate.addActionListener(e -> updateDriver());
        btnDelete.addActionListener(e -> deleteDriver());
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
        panel.setBorder(BorderFactory.createTitledBorder("Drivers List"));
        
        String[] columns = {"ID", "Name", "Contact", "License", "Type", "Experience", "Status", "Rating"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblDrivers = new JTable(tableModel);
        tblDrivers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) loadDriverToForm();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblDrivers);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadDrivers());
        searchPanel.add(btnRefresh);
        panel.add(searchPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private boolean validateForm() {
        if (!ValidationUtil.validateName(txtName.getText())) return false;
        if (!ValidationUtil.validateContact(txtContact.getText())) return false;
        if (!ValidationUtil.validateLicenseNumber(txtLicenseNumber.getText())) return false;
        if (!ValidationUtil.validateNonNegativeNumber(txtExperience.getText(), "Experience")) return false;
        if (!ValidationUtil.validateRange(txtRating.getText(), "Rating", 0, 5)) return false;
        return true;
    }
    
    private void saveDriver() {
        if (!validateForm()) return;
        
        Driver driver = new Driver();
        driver.setName(txtName.getText());
        driver.setContact(txtContact.getText());
        driver.setLicenseNumber(txtLicenseNumber.getText().toUpperCase());
        driver.setLicenseType((String) cmbLicenseType.getSelectedItem());
        driver.setDateOfHire((Date) dateHire.getValue());
        driver.setExperienceYears(Integer.parseInt(txtExperience.getText()));
        driver.setStatus((String) cmbStatus.getSelectedItem());
        driver.setPerformanceRating(Double.parseDouble(txtRating.getText()));
        
        if (driverDAO.addDriver(driver)) {
            ValidationUtil.showSuccess("Driver added successfully!");
            clearForm();
            loadDrivers();
        }
    }
    
    private void updateDriver() {
        if (currentDriver == null || !validateForm()) return;
        
        currentDriver.setName(txtName.getText());
        currentDriver.setContact(txtContact.getText());
        currentDriver.setLicenseNumber(txtLicenseNumber.getText().toUpperCase());
        currentDriver.setLicenseType((String) cmbLicenseType.getSelectedItem());
        currentDriver.setDateOfHire((Date) dateHire.getValue());
        currentDriver.setExperienceYears(Integer.parseInt(txtExperience.getText()));
        currentDriver.setStatus((String) cmbStatus.getSelectedItem());
        currentDriver.setPerformanceRating(Double.parseDouble(txtRating.getText()));
        
        if (driverDAO.updateDriver(currentDriver)) {
            ValidationUtil.showSuccess("Driver updated successfully!");
            clearForm();
            loadDrivers();
        }
    }
    
    private void deleteDriver() {
        if (currentDriver == null) return;
        
        if (ValidationUtil.showConfirmation("Delete this driver?")) {
            if (driverDAO.deleteDriver(currentDriver.getDriverId())) {
                ValidationUtil.showSuccess("Driver deleted!");
                clearForm();
                loadDrivers();
            }
        }
    }
    
    private void loadDriverToForm() {
        int row = tblDrivers.getSelectedRow();
        if (row < 0) return;
        
        int driverId = (int) tableModel.getValueAt(row, 0);
        currentDriver = driverDAO.getDriverById(driverId);
        
        if (currentDriver != null) {
            txtName.setText(currentDriver.getName());
            txtContact.setText(currentDriver.getContact());
            txtLicenseNumber.setText(currentDriver.getLicenseNumber());
            cmbLicenseType.setSelectedItem(currentDriver.getLicenseType());
            dateHire.setValue(currentDriver.getDateOfHire());
            txtExperience.setText(String.valueOf(currentDriver.getExperienceYears()));
            cmbStatus.setSelectedItem(currentDriver.getStatus());
            txtRating.setText(String.valueOf(currentDriver.getPerformanceRating()));
            
            btnSave.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
        }
    }
    
    private void loadDrivers() {
        tableModel.setRowCount(0);
        List<Driver> drivers = driverDAO.getAllDrivers();
        
        for (Driver d : drivers) {
            Object[] row = {
                d.getDriverId(),
                d.getName(),
                d.getContact(),
                d.getLicenseNumber(),
                d.getLicenseType(),
                d.getExperienceYears() + " yrs",
                d.getStatus(),
                d.getPerformanceRating()
            };
            tableModel.addRow(row);
        }
    }
    
    private void clearForm() {
        txtName.setText("");
        txtContact.setText("");
        txtLicenseNumber.setText("");
        cmbLicenseType.setSelectedIndex(0);
        dateHire.setValue(new Date());
        txtExperience.setText("");
        cmbStatus.setSelectedIndex(0);
        txtRating.setText("5.00");
        
        currentDriver = null;
        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }
}