package com.transfleet.view;

import com.transfleet.dao.CustomerDAO;
import com.transfleet.model.Customer;
import com.transfleet.util.ValidationUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Customer Management UI
 */
public class CustomerManagementUI extends JFrame {
    
    private JTextField txtName, txtBusinessName, txtContact, txtEmail, txtCreditLimit;
    private JTextArea txtAddress;
    private JComboBox<String> cmbCustomerType;
    private JButton btnSave, btnUpdate, btnDelete, btnClear;
    private JTable tblCustomers;
    private DefaultTableModel tableModel;
    private CustomerDAO customerDAO;
    private Customer currentCustomer;
    
    // Search field - now a class member
    private JTextField txtSearch;
    
    public CustomerManagementUI() {
        customerDAO = new CustomerDAO();
        initComponents();
        loadCustomers();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Customer Management");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel("Customer Management");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Form Panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.WEST);
        
        // Table Panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        panel.setPreferredSize(new Dimension(400, 500));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Name
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Name:*"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(20);
        panel.add(txtName, gbc);
        row++;
        
        // Business Name
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Business Name:"), gbc);
        gbc.gridx = 1;
        txtBusinessName = new JTextField(20);
        panel.add(txtBusinessName, gbc);
        row++;
        
        // Contact
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Contact:*"), gbc);
        gbc.gridx = 1;
        txtContact = new JTextField(20);
        panel.add(txtContact, gbc);
        row++;
        
        // Email
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);
        row++;
        
        // Address
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Address:*"), gbc);
        gbc.gridx = 1;
        txtAddress = new JTextArea(4, 20);
        txtAddress.setLineWrap(true);
        JScrollPane sp = new JScrollPane(txtAddress);
        panel.add(sp, gbc);
        row++;
        
        // Customer Type
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Customer Type:"), gbc);
        gbc.gridx = 1;
        String[] types = {"Regular", "Premium", "Corporate"};
        cmbCustomerType = new JComboBox<>(types);
        panel.add(cmbCustomerType, gbc);
        row++;
        
        // Credit Limit
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Credit Limit (Rs.):"), gbc);
        gbc.gridx = 1;
        txtCreditLimit = new JTextField(20);
        txtCreditLimit.setText("50000.00");
        panel.add(txtCreditLimit, gbc);
        row++;
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSave = new JButton("Save");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        
        btnSave.addActionListener(e -> saveCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
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
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Customers List"));
        
        String[] columns = {"ID", "Name", "Business", "Contact", "Email", "Type", "Credit Limit"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblCustomers = new JTable(tableModel);
        tblCustomers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    loadCustomerToForm();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblCustomers);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Search Panel - Fixed implementation
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20); // Now a class member
        JButton btnSearch = new JButton("Search");
        JButton btnRefresh = new JButton("Refresh");
        
        // Fixed search functionality
        btnSearch.addActionListener(e -> performSearch());
        
        // Add Enter key support for search
        txtSearch.addActionListener(e -> performSearch());
        
        // Improved refresh - clears search box too
        btnRefresh.addActionListener(e -> {
            txtSearch.setText(""); // Clear search box
            loadCustomers(); // Reload all customers
        });
        
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    /**
     * Performs search using the DAO's search method
     */
    private void performSearch() {
        String keyword = txtSearch.getText().trim();
        
        if (keyword.isEmpty()) {
            loadCustomers(); // Show all if search is empty
            return;
        }
        
        searchCustomers(keyword);
        
        // Show search results count
        int resultCount = tableModel.getRowCount();
        JOptionPane.showMessageDialog(this, 
            "Found " + resultCount + " customer(s) matching '" + keyword + "'",
            "Search Results", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private boolean validateForm() {
        if (!ValidationUtil.validateName(txtName.getText())) return false;
        if (!ValidationUtil.validateContact(txtContact.getText())) return false;
        if (!ValidationUtil.validateEmail(txtEmail.getText())) return false;
        if (!ValidationUtil.validateAddress(txtAddress.getText())) return false;
        if (!ValidationUtil.validatePositiveNumber(txtCreditLimit.getText(), "Credit Limit")) return false;
        
        // Check if contact already exists
        int excludeId = currentCustomer != null ? currentCustomer.getCustomerId() : 0;
        if (customerDAO.contactExists(txtContact.getText(), excludeId)) {
            JOptionPane.showMessageDialog(this, "Contact number already exists!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void saveCustomer() {
        if (!validateForm()) return;
        
        Customer customer = new Customer();
        customer.setName(txtName.getText());
        customer.setBusinessName(txtBusinessName.getText());
        customer.setContact(txtContact.getText());
        customer.setEmail(txtEmail.getText());
        customer.setAddress(txtAddress.getText());
        customer.setCustomerType((String) cmbCustomerType.getSelectedItem());
        customer.setCreditLimit(Double.parseDouble(txtCreditLimit.getText()));
        
        if (customerDAO.addCustomer(customer)) {
            ValidationUtil.showSuccess("Customer added successfully!");
            clearForm();
            loadCustomers();
        }
    }
    
    private void updateCustomer() {
        if (currentCustomer == null || !validateForm()) return;
        
        currentCustomer.setName(txtName.getText());
        currentCustomer.setBusinessName(txtBusinessName.getText());
        currentCustomer.setContact(txtContact.getText());
        currentCustomer.setEmail(txtEmail.getText());
        currentCustomer.setAddress(txtAddress.getText());
        currentCustomer.setCustomerType((String) cmbCustomerType.getSelectedItem());
        currentCustomer.setCreditLimit(Double.parseDouble(txtCreditLimit.getText()));
        
        if (customerDAO.updateCustomer(currentCustomer)) {
            ValidationUtil.showSuccess("Customer updated successfully!");
            clearForm();
            loadCustomers();
        }
    }
    
    private void deleteCustomer() {
        if (currentCustomer == null) return;
        
        if (ValidationUtil.showConfirmation("Are you sure you want to delete this customer?")) {
            if (customerDAO.deleteCustomer(currentCustomer.getCustomerId())) {
                ValidationUtil.showSuccess("Customer deleted successfully!");
                clearForm();
                loadCustomers();
            }
        }
    }
    
    private void loadCustomerToForm() {
        int row = tblCustomers.getSelectedRow();
        if (row < 0) return;
        
        int customerId = (int) tableModel.getValueAt(row, 0);
        currentCustomer = customerDAO.getCustomerById(customerId);
        
        if (currentCustomer != null) {
            txtName.setText(currentCustomer.getName());
            txtBusinessName.setText(currentCustomer.getBusinessName());
            txtContact.setText(currentCustomer.getContact());
            txtEmail.setText(currentCustomer.getEmail());
            txtAddress.setText(currentCustomer.getAddress());
            cmbCustomerType.setSelectedItem(currentCustomer.getCustomerType());
            txtCreditLimit.setText(String.valueOf(currentCustomer.getCreditLimit()));
            
            btnSave.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
        }
    }
    
    private void loadCustomers() {
        tableModel.setRowCount(0);
        List<Customer> customers = customerDAO.getAllCustomers();
        
        for (Customer c : customers) {
            Object[] row = {
                c.getCustomerId(),
                c.getName(),
                c.getBusinessName(),
                c.getContact(),
                c.getEmail(),
                c.getCustomerType(),
                "Rs. " + c.getCreditLimit()
            };
            tableModel.addRow(row);
        }
    }
    
    private void searchCustomers(String keyword) {
        if (keyword.trim().isEmpty()) {
            loadCustomers();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Customer> customers = customerDAO.searchCustomers(keyword);
        
        for (Customer c : customers) {
            Object[] row = {
                c.getCustomerId(),
                c.getName(),
                c.getBusinessName(),
                c.getContact(),
                c.getEmail(),
                c.getCustomerType(),
                "Rs. " + c.getCreditLimit()
            };
            tableModel.addRow(row);
        }
    }
    
    private void clearForm() {
        txtName.setText("");
        txtBusinessName.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        cmbCustomerType.setSelectedIndex(0);
        txtCreditLimit.setText("50000.00");
        
        currentCustomer = null;
        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }
}