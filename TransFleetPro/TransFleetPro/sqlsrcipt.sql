-- TransFleet Pro Database Schema
-- MySQL Database Creation Script

CREATE DATABASE IF NOT EXISTS transfleet_db;
USE transfleet_db;

-- 1. Customers Table
CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    business_name VARCHAR(150),
    contact VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    address TEXT NOT NULL,
    customer_type ENUM('Regular', 'Premium', 'Corporate') DEFAULT 'Regular',
    credit_limit DECIMAL(10, 2) DEFAULT 50000.00,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_customer_type (customer_type),
    INDEX idx_contact (contact)
);

-- 2. Drivers Table
CREATE TABLE drivers (
    driver_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    contact VARCHAR(15) NOT NULL,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    license_type ENUM('LightVehicle', 'HeavyVehicle', 'All') NOT NULL,
    date_of_hire DATE NOT NULL,
    experience_years INT DEFAULT 0,
    status ENUM('Available', 'On-Duty', 'Off-Duty') DEFAULT 'Available',
    performance_rating DECIMAL(3, 2) DEFAULT 5.00,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_status (status),
    INDEX idx_license_type (license_type)
);

-- 3. Vehicles Table
CREATE TABLE vehicles (
    vehicle_id INT PRIMARY KEY AUTO_INCREMENT,
    registration_number VARCHAR(50) UNIQUE NOT NULL,
    vehicle_type ENUM('Truck', 'Van', 'Bike', 'Car') NOT NULL,
    capacity_weight DECIMAL(10, 2) NOT NULL,
    capacity_volume DECIMAL(10, 2) NOT NULL,
    fuel_type ENUM('Petrol', 'Diesel', 'Electric') NOT NULL,
    fuel_efficiency DECIMAL(5, 2) NOT NULL,
    purchase_date DATE NOT NULL,
    last_maintenance_date DATE,
    status ENUM('Available', 'In-Use', 'Under-Maintenance') DEFAULT 'Available',
    current_location VARCHAR(200),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_status (status),
    INDEX idx_vehicle_type (vehicle_type)
);

-- 4. Routes Table
CREATE TABLE routes (
    route_id INT PRIMARY KEY AUTO_INCREMENT,
    route_name VARCHAR(150) NOT NULL,
    start_point VARCHAR(200) NOT NULL,
    end_point VARCHAR(200) NOT NULL,
    distance_km DECIMAL(8, 2) NOT NULL,
    estimated_time_hours DECIMAL(5, 2) NOT NULL,
    toll_cost DECIMAL(8, 2) DEFAULT 0.00,
    base_rate DECIMAL(8, 2) NOT NULL,
    traffic_level ENUM('Low', 'Medium', 'High') DEFAULT 'Medium',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_start_end (start_point, end_point)
);

-- 5. Orders Table
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    driver_id INT,
    vehicle_id INT,
    route_id INT,
    pickup_address TEXT NOT NULL,
    delivery_address TEXT NOT NULL,
    package_weight DECIMAL(10, 2) NOT NULL,
    package_volume DECIMAL(10, 2) NOT NULL,
    package_type VARCHAR(100),
    delivery_priority ENUM('Normal', 'Express', 'Same-Day') DEFAULT 'Normal',
    estimated_distance DECIMAL(8, 2),
    estimated_time DECIMAL(5, 2),
    delivery_cost DECIMAL(10, 2) NOT NULL,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    expected_delivery_date DATE NOT NULL,
    actual_delivery_date DATETIME,
    order_status ENUM('Pending', 'Assigned', 'In-Transit', 'Delivered', 'Cancelled') DEFAULT 'Pending',
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (driver_id) REFERENCES drivers(driver_id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id),
    FOREIGN KEY (route_id) REFERENCES routes(route_id),
    INDEX idx_order_status (order_status),
    INDEX idx_order_date (order_date),
    INDEX idx_customer (customer_id)
);

-- 6. Expenses Table
CREATE TABLE expenses (
    expense_id INT PRIMARY KEY AUTO_INCREMENT,
    vehicle_id INT NOT NULL,
    order_id INT,
    expense_type ENUM('Fuel', 'Maintenance', 'Toll', 'Other') NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    expense_date DATE NOT NULL,
    description TEXT,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    INDEX idx_expense_date (expense_date),
    INDEX idx_vehicle (vehicle_id)
);

-- 7. Payments Table
CREATE TABLE payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    customer_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    payment_method ENUM('Cash', 'Card', 'UPI', 'BankTransfer') NOT NULL,
    payment_status ENUM('Pending', 'Completed', 'Failed') DEFAULT 'Pending',
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    INDEX idx_payment_status (payment_status),
    INDEX idx_payment_date (payment_date)
);

-- Insert Sample Data

-- Sample Customers
INSERT INTO customers (name, business_name, contact, email, address, customer_type, credit_limit) VALUES
('John Doe', 'Doe Enterprises', '9876543210', 'john@doe.com', '123 Main St, Colombo', 'Corporate', 100000.00),
('Jane Smith', 'Smith Logistics', '9876543211', 'jane@smith.com', '456 Park Ave, Colombo', 'Premium', 75000.00),
('Robert Brown', NULL, '9876543212', 'robert@gmail.com', '789 Lake Rd, Kandy', 'Regular', 50000.00),
('Emily Davis', 'Davis Retail', '9876543213', 'emily@davis.com', '321 Hill St, Galle', 'Corporate', 150000.00),
('Michael Wilson', NULL, '9876543214', 'michael@yahoo.com', '654 Beach Rd, Negombo', 'Regular', 50000.00);

-- Sample Drivers
INSERT INTO drivers (name, contact, license_number, license_type, date_of_hire, experience_years, status, performance_rating) VALUES
('Kamal Perera', '9871234560', 'DL001234', 'All', '2020-01-15', 5, 'Available', 4.80),
('Nimal Silva', '9871234561', 'DL001235', 'HeavyVehicle', '2019-06-20', 6, 'Available', 4.60),
('Sunil Fernando', '9871234562', 'DL001236', 'LightVehicle', '2021-03-10', 3, 'Available', 4.90),
('Ajith Kumar', '9871234563', 'DL001237', 'All', '2018-08-05', 7, 'On-Duty', 4.70),
('Prasad Jayasinghe', '9871234564', 'DL001238', 'HeavyVehicle', '2022-01-12', 2, 'Available', 4.50);

-- Sample Vehicles
INSERT INTO vehicles (registration_number, vehicle_type, capacity_weight, capacity_volume, fuel_type, fuel_efficiency, purchase_date, last_maintenance_date, status, current_location) VALUES
('WP-CAB-1234', 'Truck', 5000.00, 50.00, 'Diesel', 8.50, '2020-01-01', '2024-12-15', 'Available', 'Colombo'),
('WP-CAC-5678', 'Van', 1500.00, 20.00, 'Diesel', 12.00, '2021-05-10', '2024-11-20', 'Available', 'Kandy'),
('WP-CAD-9012', 'Bike', 50.00, 0.50, 'Petrol', 40.00, '2022-03-15', '2024-10-05', 'Available', 'Galle'),
('WP-CAE-3456', 'Car', 500.00, 5.00, 'Petrol', 15.00, '2021-08-20', '2024-12-01', 'In-Use', 'Negombo'),
('WP-CAF-7890', 'Truck', 7000.00, 70.00, 'Diesel', 7.00, '2019-11-11', '2024-09-10', 'Available', 'Colombo');

-- Sample Routes
INSERT INTO routes (route_name, start_point, end_point, distance_km, estimated_time_hours, toll_cost, base_rate, traffic_level) VALUES
('Colombo to Kandy', 'Colombo', 'Kandy', 115.00, 3.50, 250.00, 3500.00, 'Medium'),
('Colombo to Galle', 'Colombo', 'Galle', 119.00, 2.50, 300.00, 3800.00, 'Low'),
('Kandy to Jaffna', 'Kandy', 'Jaffna', 285.00, 6.00, 500.00, 8000.00, 'Medium'),
('Colombo to Negombo', 'Colombo', 'Negombo', 37.00, 1.00, 0.00, 1200.00, 'High'),
('Galle to Matara', 'Galle', 'Matara', 44.00, 1.50, 100.00, 1500.00, 'Low');

-- Sample Orders
INSERT INTO orders (customer_id, driver_id, vehicle_id, route_id, pickup_address, delivery_address, package_weight, package_volume, package_type, delivery_priority, estimated_distance, estimated_time, delivery_cost, expected_delivery_date, order_status) VALUES
(1, 1, 1, 1, '123 Main St, Colombo', '789 Temple Rd, Kandy', 450.00, 5.00, 'Electronics', 'Express', 115.00, 3.50, 4500.00, '2026-01-22', 'Assigned'),
(2, 2, 2, 2, '456 Park Ave, Colombo', '321 Beach Rd, Galle', 120.00, 2.00, 'Documents', 'Normal', 119.00, 2.50, 3200.00, '2026-01-23', 'Pending'),
(3, NULL, NULL, NULL, '789 Lake Rd, Kandy', '654 Market St, Colombo', 80.00, 1.50, 'Clothing', 'Same-Day', 115.00, 3.50, 5000.00, '2026-01-21', 'Pending'),
(4, 4, 4, 4, '321 Hill St, Galle', '111 Sea View, Negombo', 200.00, 3.00, 'Food Items', 'Express', 150.00, 4.00, 5500.00, '2026-01-22', 'In-Transit'),
(1, 3, 3, 5, '123 Main St, Colombo', '222 Station Rd, Matara', 25.00, 0.30, 'Parcels', 'Normal', 163.00, 4.00, 4200.00, '2026-01-24', 'Pending');

-- Sample Expenses
INSERT INTO expenses (vehicle_id, order_id, expense_type, amount, expense_date, description) VALUES
(1, 1, 'Fuel', 2500.00, '2026-01-20', 'Fuel for Colombo-Kandy route'),
(1, 1, 'Toll', 250.00, '2026-01-20', 'Highway toll charges'),
(2, 2, 'Fuel', 1800.00, '2026-01-21', 'Fuel for Colombo-Galle route'),
(4, 4, 'Fuel', 2200.00, '2026-01-21', 'Fuel for delivery'),
(1, NULL, 'Maintenance', 5000.00, '2024-12-15', 'Regular service and oil change');

-- Sample Payments
INSERT INTO payments (order_id, customer_id, amount, payment_method, payment_status) VALUES
(1, 1, 4500.00, 'BankTransfer', 'Completed'),
(4, 4, 5500.00, 'Card', 'Completed'),
(2, 2, 3200.00, 'UPI', 'Pending');