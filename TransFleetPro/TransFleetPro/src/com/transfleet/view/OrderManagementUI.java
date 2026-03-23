package com.transfleet.view;

import com.transfleet.dao.*;
import com.transfleet.model.*;
import com.transfleet.util.ValidationUtil;
import com.transfleet.strategy.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import com.transfleet.strategy.PricingStrategy;
import com.transfleet.strategy.PricingStrategyFactory;

/**
 * Order Management UI - Main Transaction Interface
 * Complete order lifecycle management with validation
 */
public class OrderManagementUI extends JFrame {
    
    // Form Components
    private JComboBox<Customer> cmbCustomer;
    private JComboBox<Vehicle> cmbVehicle;
    private JComboBox<Driver> cmbDriver;
    private JTextArea txtPickupAddress, txtDeliveryAddress;
    private JTextField txtPackageWeight, txtPackageVolume, txtPackageType;
    private JComboBox<String> cmbPriority, cmbStatus;
    private JTextField txtEstDistance, txtEstTime, txtCost;
    private JSpinner dateExpectedDelivery;
    private JButton btnSave, btnUpdate, btnDelete, btnClear, btnCalculate;
    private JTable tblOrders;
    private DefaultTableModel tableModel;
    
    // Search field - now a class member so it's accessible
    private JTextField txtSearch;
    
    // DAOs
    private OrderDAO orderDAO;
    private CustomerDAO customerDAO;
    private VehicleDAO vehicleDAO;
    private DriverDAO driverDAO;
    
    // Current order for editing
    private Order currentOrder;
    
    // Store all orders for filtering
    private List<Order> allOrders;
    
    public OrderManagementUI() {
        initDAO();
        initComponents();
        loadOrders();
        loadComboBoxData();
        setLocationRelativeTo(null);
    }
    
    private void initDAO() {
        orderDAO = new OrderDAO();
        customerDAO = new CustomerDAO();
        vehicleDAO = new VehicleDAO();
        driverDAO = new DriverDAO();
    }
    
    private void initComponents() {
        setTitle("Order Management - Create & Manage Delivery Orders");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel("Delivery Order Management");
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
        panel.setBorder(BorderFactory.createTitledBorder("Order Details"));
        panel.setPreferredSize(new Dimension(550, 700));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Customer Selection
        addFormField(panel, gbc, row++, "Customer:", cmbCustomer = new JComboBox<>());
        cmbCustomer.addActionListener(e -> calculateCost());
        
        // Package Details
        addFormField(panel, gbc, row++, "Package Weight (kg):", txtPackageWeight = new JTextField(20));
        addFormField(panel, gbc, row++, "Package Volume (m³):", txtPackageVolume = new JTextField(20));
        addFormField(panel, gbc, row++, "Package Type:", txtPackageType = new JTextField(20));
        
        // Addresses
        gbc.gridx = 0; gbc.gridy = row++;
        panel.add(new JLabel("Pickup Address:"), gbc);
        txtPickupAddress = new JTextArea(3, 20);
        txtPickupAddress.setLineWrap(true);
        JScrollPane sp1 = new JScrollPane(txtPickupAddress);
        gbc.gridx = 1;
        panel.add(sp1, gbc);
        
        gbc.gridx = 0; gbc.gridy = row++;
        panel.add(new JLabel("Delivery Address:"), gbc);
        txtDeliveryAddress = new JTextArea(3, 20);
        txtDeliveryAddress.setLineWrap(true);
        JScrollPane sp2 = new JScrollPane(txtDeliveryAddress);
        gbc.gridx = 1;
        panel.add(sp2, gbc);
        
        // Priority
        String[] priorities = {"Normal", "Express", "Same-Day"};
        addFormField(panel, gbc, row++, "Delivery Priority:", cmbPriority = new JComboBox<>(priorities));
        cmbPriority.addActionListener(e -> calculateCost());
        
        // Expected Delivery Date
        gbc.gridx = 0; gbc.gridy = row++;
        panel.add(new JLabel("Expected Delivery:"), gbc);
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateExpectedDelivery = new JSpinner(dateModel);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateExpectedDelivery, "dd-MM-yyyy");
        dateExpectedDelivery.setEditor(editor);
        gbc.gridx = 1;
        panel.add(dateExpectedDelivery, gbc);
        
        // Vehicle Selection
        addFormField(panel, gbc, row++, "Assign Vehicle:", cmbVehicle = new JComboBox<>());
        
        // Driver Selection
        addFormField(panel, gbc, row++, "Assign Driver:", cmbDriver = new JComboBox<>());
        
        // Route Estimation
        addFormField(panel, gbc, row++, "Est. Distance (km):", txtEstDistance = new JTextField(20));
        txtEstDistance.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                calculateCost();
            }
        });
        
        addFormField(panel, gbc, row++, "Est. Time (hrs):", txtEstTime = new JTextField(20));
        
        // Calculate Button
        gbc.gridx = 1; gbc.gridy = row++;
        btnCalculate = new JButton("Calculate Cost");
        btnCalculate.addActionListener(e -> calculateCost());
        panel.add(btnCalculate, gbc);
        
        // Cost Display
        addFormField(panel, gbc, row++, "Delivery Cost (Rs.):", txtCost = new JTextField(20));
        txtCost.setEditable(false);
        txtCost.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtCost.setForeground(new Color(0, 128, 0));
        
        // Status
        String[] statuses = {"Pending", "Assigned", "In-Transit", "Delivered", "Cancelled"};
        addFormField(panel, gbc, row++, "Order Status:", cmbStatus = new JComboBox<>(statuses));
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSave = new JButton("Save Order");
        btnUpdate = new JButton("Update Order");
        btnDelete = new JButton("Delete Order");
        btnClear = new JButton("Clear Form");
        
        btnSave.addActionListener(e -> saveOrder());
        btnUpdate.addActionListener(e -> updateOrder());
        btnDelete.addActionListener(e -> deleteOrder());
        btnClear.addActionListener(e -> clearForm());
        
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        
        gbc.gridx = 0; gbc.gridy = row++;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent component) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        panel.add(component, gbc);
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Orders List"));
        
        String[] columns = {"ID", "Customer", "Package", "Weight", "Priority", "Vehicle", "Driver", "Cost", "Status", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblOrders = new JTable(tableModel);
        tblOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblOrders.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    loadOrderToForm();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblOrders);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Search Panel - Fixed to make txtSearch accessible
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20); // Now a class member
        JButton btnSearch = new JButton("Search");
        JButton btnRefresh = new JButton("Refresh");
        
        // Fixed search functionality
        btnSearch.addActionListener(e -> performSearch());
        
        // Add Enter key support for search
        txtSearch.addActionListener(e -> performSearch());
        
        btnRefresh.addActionListener(e -> {
            txtSearch.setText(""); // Clear search box
            loadOrders(); // Reload all orders
        });
        
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    /**
     * Performs search/filter on orders based on search text
     * Searches by: Order ID, Customer Name, Package Type, Status
     */
    private void performSearch() {
        String searchText = txtSearch.getText().trim().toLowerCase();
        
        if (searchText.isEmpty()) {
            loadOrders(); // Show all if search is empty
            return;
        }
        
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        
        // Filter orders based on search text
        List<Order> filteredOrders = allOrders.stream()
            .filter(order -> {
                // Search in multiple fields
                boolean matchesId = String.valueOf(order.getOrderId()).contains(searchText);
                boolean matchesCustomer = order.getCustomerName() != null && 
                                         order.getCustomerName().toLowerCase().contains(searchText);
                boolean matchesPackage = order.getPackageType() != null && 
                                        order.getPackageType().toLowerCase().contains(searchText);
                boolean matchesStatus = order.getOrderStatus() != null && 
                                       order.getOrderStatus().toLowerCase().contains(searchText);
                boolean matchesPriority = order.getDeliveryPriority() != null && 
                                         order.getDeliveryPriority().toLowerCase().contains(searchText);
                
                return matchesId || matchesCustomer || matchesPackage || matchesStatus || matchesPriority;
            })
            .collect(Collectors.toList());
        
        // Display filtered results
        for (Order order : filteredOrders) {
            Object[] row = {
                order.getOrderId(),
                order.getCustomerName(),
                order.getPackageType(),
                order.getPackageWeight() + " kg",
                order.getDeliveryPriority(),
                order.getVehicleRegNumber() != null ? order.getVehicleRegNumber() : "N/A",
                order.getDriverName() != null ? order.getDriverName() : "N/A",
                "Rs. " + order.getDeliveryCost(),
                order.getOrderStatus(),
                sdf.format(order.getExpectedDeliveryDate())
            };
            tableModel.addRow(row);
        }
        
        // Show search results count
        JOptionPane.showMessageDialog(this, 
            "Found " + filteredOrders.size() + " order(s) matching '" + txtSearch.getText() + "'",
            "Search Results", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void loadComboBoxData() {
        // Load Customers
        List<Customer> customers = customerDAO.getAllCustomers();
        cmbCustomer.removeAllItems();
        cmbCustomer.addItem(null); // Empty option
        for (Customer c : customers) {
            cmbCustomer.addItem(c);
        }
        
        // Load Available Vehicles
        List<Vehicle> vehicles = vehicleDAO.getAvailableVehicles();
        cmbVehicle.removeAllItems();
        cmbVehicle.addItem(null);
        for (Vehicle v : vehicles) {
            cmbVehicle.addItem(v);
        }
        
        // Load Available Drivers
        List<Driver> drivers = driverDAO.getAvailableDrivers();
        cmbDriver.removeAllItems();
        cmbDriver.addItem(null);
        for (Driver d : drivers) {
            cmbDriver.addItem(d);
        }
    }
    
    private void calculateCost() {
        try {
            Customer customer = (Customer) cmbCustomer.getSelectedItem();
            String priority = (String) cmbPriority.getSelectedItem();
            String distanceStr = txtEstDistance.getText();
            
            if (customer == null || priority == null || distanceStr.isEmpty()) {
                return;
            }
            
            double distance = Double.parseDouble(distanceStr);
            double baseRate = 500.0; // Base rate
            
            // Get pricing strategy based on customer type
            PricingStrategy strategy = PricingStrategyFactory.getStrategy(customer.getCustomerType());
            double cost = strategy.calculatePrice(baseRate, distance, priority);
            
            txtCost.setText(String.format("%.2f", cost));
            
        } catch (NumberFormatException e) {
            txtCost.setText("0.00");
        }
    }
    
    private boolean validateForm() {
        if (!ValidationUtil.validateComboBox(cmbCustomer.getSelectedItem(), "Customer")) return false;
        if (!ValidationUtil.validatePositiveNumber(txtPackageWeight.getText(), "Package Weight")) return false;
        if (!ValidationUtil.validatePositiveNumber(txtPackageVolume.getText(), "Package Volume")) return false;
        if (!ValidationUtil.validateNotEmpty(txtPackageType.getText(), "Package Type")) return false;
        if (!ValidationUtil.validateAddress(txtPickupAddress.getText())) return false;
        if (!ValidationUtil.validateAddress(txtDeliveryAddress.getText())) return false;
        if (!ValidationUtil.validatePositiveNumber(txtEstDistance.getText(), "Estimated Distance")) return false;
        if (!ValidationUtil.validatePositiveNumber(txtEstTime.getText(), "Estimated Time")) return false;
        
        // Validate vehicle capacity if vehicle selected
        if (cmbVehicle.getSelectedItem() != null) {
            Vehicle vehicle = (Vehicle) cmbVehicle.getSelectedItem();
            double weight = Double.parseDouble(txtPackageWeight.getText());
            if (!ValidationUtil.validateCapacity(weight, vehicle.getCapacityWeight())) {
                return false;
            }
        }
        
        return true;
    }
    
    private void saveOrder() {
        if (!validateForm()) return;
        
        try {
            Order order = new Order();
            order.setCustomerId(((Customer) cmbCustomer.getSelectedItem()).getCustomerId());
            
            if (cmbVehicle.getSelectedItem() != null) {
                order.setVehicleId(((Vehicle) cmbVehicle.getSelectedItem()).getVehicleId());
            }
            
            if (cmbDriver.getSelectedItem() != null) {
                order.setDriverId(((Driver) cmbDriver.getSelectedItem()).getDriverId());
            }
            
            order.setPickupAddress(txtPickupAddress.getText());
            order.setDeliveryAddress(txtDeliveryAddress.getText());
            order.setPackageWeight(Double.parseDouble(txtPackageWeight.getText()));
            order.setPackageVolume(Double.parseDouble(txtPackageVolume.getText()));
            order.setPackageType(txtPackageType.getText());
            order.setDeliveryPriority((String) cmbPriority.getSelectedItem());
            order.setEstimatedDistance(Double.parseDouble(txtEstDistance.getText()));
            order.setEstimatedTime(Double.parseDouble(txtEstTime.getText()));
            order.setDeliveryCost(Double.parseDouble(txtCost.getText()));
            order.setExpectedDeliveryDate((Date) dateExpectedDelivery.getValue());
            order.setOrderStatus((String) cmbStatus.getSelectedItem());
            
            if (orderDAO.addOrder(order)) {
                ValidationUtil.showSuccess("Order created successfully!\nOrder ID: " + order.getOrderId());
                clearForm();
                loadOrders();
                loadComboBoxData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create order!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void updateOrder() {
        if (currentOrder == null || !validateForm()) return;
        
        try {
            currentOrder.setCustomerId(((Customer) cmbCustomer.getSelectedItem()).getCustomerId());
            
            if (cmbVehicle.getSelectedItem() != null) {
                currentOrder.setVehicleId(((Vehicle) cmbVehicle.getSelectedItem()).getVehicleId());
            } else {
                currentOrder.setVehicleId(null);
            }
            
            if (cmbDriver.getSelectedItem() != null) {
                currentOrder.setDriverId(((Driver) cmbDriver.getSelectedItem()).getDriverId());
            } else {
                currentOrder.setDriverId(null);
            }
            
            currentOrder.setPickupAddress(txtPickupAddress.getText());
            currentOrder.setDeliveryAddress(txtDeliveryAddress.getText());
            currentOrder.setPackageWeight(Double.parseDouble(txtPackageWeight.getText()));
            currentOrder.setPackageVolume(Double.parseDouble(txtPackageVolume.getText()));
            currentOrder.setPackageType(txtPackageType.getText());
            currentOrder.setDeliveryPriority((String) cmbPriority.getSelectedItem());
            currentOrder.setEstimatedDistance(Double.parseDouble(txtEstDistance.getText()));
            currentOrder.setEstimatedTime(Double.parseDouble(txtEstTime.getText()));
            currentOrder.setDeliveryCost(Double.parseDouble(txtCost.getText()));
            currentOrder.setExpectedDeliveryDate((Date) dateExpectedDelivery.getValue());
            currentOrder.setOrderStatus((String) cmbStatus.getSelectedItem());
            
            if (orderDAO.updateOrder(currentOrder)) {
                ValidationUtil.showSuccess("Order updated successfully!");
                clearForm();
                loadOrders();
                loadComboBoxData();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void deleteOrder() {
        if (currentOrder == null) return;
        
        if (ValidationUtil.showConfirmation("Are you sure you want to delete this order?")) {
            if (orderDAO.deleteOrder(currentOrder.getOrderId())) {
                ValidationUtil.showSuccess("Order deleted successfully!");
                clearForm();
                loadOrders();
                loadComboBoxData();
            }
        }
    }
    
    private void loadOrderToForm() {
        int row = tblOrders.getSelectedRow();
        if (row < 0) return;
        
        int orderId = (int) tableModel.getValueAt(row, 0);
        currentOrder = orderDAO.getOrderById(orderId);
        
        if (currentOrder != null) {
            // Load customer
            Customer customer = customerDAO.getCustomerById(currentOrder.getCustomerId());
            cmbCustomer.setSelectedItem(customer);
            
            // Load vehicle
            if (currentOrder.getVehicleId() != null) {
                Vehicle vehicle = vehicleDAO.getVehicleById(currentOrder.getVehicleId());
                cmbVehicle.setSelectedItem(vehicle);
            }
            
            // Load driver
            if (currentOrder.getDriverId() != null) {
                Driver driver = driverDAO.getDriverById(currentOrder.getDriverId());
                cmbDriver.setSelectedItem(driver);
            }
            
            txtPackageWeight.setText(String.valueOf(currentOrder.getPackageWeight()));
            txtPackageVolume.setText(String.valueOf(currentOrder.getPackageVolume()));
            txtPackageType.setText(currentOrder.getPackageType());
            txtPickupAddress.setText(currentOrder.getPickupAddress());
            txtDeliveryAddress.setText(currentOrder.getDeliveryAddress());
            cmbPriority.setSelectedItem(currentOrder.getDeliveryPriority());
            txtEstDistance.setText(String.valueOf(currentOrder.getEstimatedDistance()));
            txtEstTime.setText(String.valueOf(currentOrder.getEstimatedTime()));
            txtCost.setText(String.valueOf(currentOrder.getDeliveryCost()));
            dateExpectedDelivery.setValue(currentOrder.getExpectedDeliveryDate());
            cmbStatus.setSelectedItem(currentOrder.getOrderStatus());
            
            btnSave.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
        }
    }
    
    private void loadOrders() {
        tableModel.setRowCount(0);
        allOrders = orderDAO.getAllOrders(); // Store all orders for filtering
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        
        for (Order order : allOrders) {
            Object[] row = {
                order.getOrderId(),
                order.getCustomerName(),
                order.getPackageType(),
                order.getPackageWeight() + " kg",
                order.getDeliveryPriority(),
                order.getVehicleRegNumber() != null ? order.getVehicleRegNumber() : "N/A",
                order.getDriverName() != null ? order.getDriverName() : "N/A",
                "Rs. " + order.getDeliveryCost(),
                order.getOrderStatus(),
                sdf.format(order.getExpectedDeliveryDate())
            };
            tableModel.addRow(row);
        }
    }
    
    private void clearForm() {
        cmbCustomer.setSelectedIndex(0);
        cmbVehicle.setSelectedIndex(0);
        cmbDriver.setSelectedIndex(0);
        txtPackageWeight.setText("");
        txtPackageVolume.setText("");
        txtPackageType.setText("");
        txtPickupAddress.setText("");
        txtDeliveryAddress.setText("");
        cmbPriority.setSelectedIndex(0);
        txtEstDistance.setText("");
        txtEstTime.setText("");
        txtCost.setText("");
        dateExpectedDelivery.setValue(new Date());
        cmbStatus.setSelectedIndex(0);
        
        currentOrder = null;
        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }
}