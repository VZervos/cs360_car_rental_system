package database;

import java.sql.*;
import javax.swing.JOptionPane;

public class Database {
    private static Connection conn;

    public static Connection connection() {
        return Database.conn;
    }

    public static Statement createStatement() throws SQLException {
        assert (conn != null);
        return connect().createStatement();
    }

    public static ResultSet retrieveAllCars() throws SQLException {
        return createStatement().executeQuery("SELECT * FROM Car");
    }

    public static ResultSet retrieveAllMotorbikes() throws SQLException {
        return createStatement().executeQuery("SELECT * FROM Motorbike");
    }

    public static ResultSet retrieveAllSkates() throws SQLException {
        return createStatement().executeQuery("SELECT * FROM Skate");
    }

    public static ResultSet retrieveAllBicycles() throws SQLException {
        return createStatement().executeQuery("SELECT * FROM Bicycle");
    }

    public static ResultSet retrieveAllRentals() throws SQLException {
        return createStatement().executeQuery("SELECT * FROM Rent WHERE Returned = false");
    }

    public static ResultSet retrieveAllRents() throws SQLException {
        return createStatement().executeQuery("SELECT * FROM Rent");
    }

    public static Connection connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/";
            String username = "admin";
            String password = "admin";
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);

            // Create 'mydatabase' if it doesn't exist
            Statement stmt = conn.createStatement();
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS mydatabase";
            stmt.executeUpdate(createDatabaseQuery);
            stmt.close();

            url += "mydatabase";
            conn = DriverManager.getConnection(url, username, password);

            return conn;
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, e);
        }

        return null;
    }

    public static void createTableIfNotExists(Connection conn, String tableName, String createTableQuery) throws SQLException {
        Statement stmt = conn.createStatement();
        if (!tableExists(conn, tableName)) {
            stmt.executeUpdate(createTableQuery);
            System.out.println("Table '" + tableName + "' created successfully!");
        } else {
            System.out.println("Table '" + tableName + "' already exists. Continuing...");
        }
        stmt.close();
    }

    public static boolean tableExists(Connection conn, String tableName) throws SQLException {
        return conn.getMetaData().getTables(null, null, tableName, null).next();
    }

    public Database() {
        Statement stmt = null;
        conn = connect();

        try {
            assert conn != null;
            stmt = conn.createStatement();

            createTableIfNotExists(conn, "Car", "CREATE TABLE Car ("
                    + "VId VARCHAR(255), "
                    + "Brand VARCHAR(255), "
                    + "Model VARCHAR(255), "
                    + "Color VARCHAR(255), "
                    + "KmDriven INT, "
                    + "RentalCost DECIMAL(10, 2), "
                    + "State VARCHAR(255), "
                    + "InsuranceCost DECIMAL(10, 2), "
                    + "Type VARCHAR(255), "
                    + "PassengerLimit INT, "
                    + "PRIMARY KEY (VId)"
                    + ")");


            createTableIfNotExists(conn, "Motorbike", "CREATE TABLE Motorbike ("
                    + "VId VARCHAR(255), "
                    + "Brand VARCHAR(255), "
                    + "Model VARCHAR(255), "
                    + "Color VARCHAR(255), "
                    + "KmDriven INT, "
                    + "RentalCost DECIMAL(10, 2), "
                    + "State VARCHAR(255), "
                    + "InsuranceCost DECIMAL(10, 2), "
                    + "PRIMARY KEY (VId)"
                    + ")");

            createTableIfNotExists(conn, "Bicycle", "CREATE TABLE Bicycle ("
                    + "VId VARCHAR(255), "
                    + "Brand VARCHAR(255), "
                    + "Model VARCHAR(255), "
                    + "Color VARCHAR(255), "
                    + "KmDriven INT, "
                    + "RentalCost DECIMAL(10, 2), "
                    + "InsuranceCost DECIMAL(10, 2), "
                    + "State VARCHAR(255), "
                    + "PRIMARY KEY (VId)"
                    + ")");

            createTableIfNotExists(conn, "Skate", "CREATE TABLE Skate ("
                    + "VId VARCHAR(255), "
                    + "Brand VARCHAR(255), "
                    + "Model VARCHAR(255), "
                    + "Color VARCHAR(255), "
                    + "KmDriven INT, "
                    + "RentalCost DECIMAL(10, 2), "
                    + "InsuranceCost DECIMAL(10, 2), "
                    + "State VARCHAR(255), "
                    + "PRIMARY KEY (VId)"
                    + ")");

            createTableIfNotExists(conn, "Rent", "CREATE TABLE Rent ("
                    + "RId VARCHAR(255), "
                    + "Day VARCHAR(255), "
                    + "Month VARCHAR(255), "
                    + "Year VARCHAR(255), "
                    + "Hours VARCHAR(255), "
                    + "PaymentAmount DECIMAL(10, 2), "
                    + "HasInsurance BOOLEAN,"
                    + "Returned BOOLEAN,"
                    + "VId VARCHAR(255), "
                    + "DId VARCHAR(255), "
                    + "CId VARCHAR(255), "
                    + "Cost DECIMAL(10, 2),"
                    + "PRIMARY KEY (RId)"
                    + ")");


            createTableIfNotExists(conn, "Driver", "CREATE TABLE Driver ("
                    + "DId VARCHAR(255), "
                    + "TIN VARCHAR(255), "
                    + "PRIMARY KEY (DId)"
                    + ")");

            createTableIfNotExists(conn, "Customer", "CREATE TABLE Customer ("
                    + "CId VARCHAR(255), "
                    + "TIN VARCHAR(255), "
                    + "mail VARCHAR(255), "
                    + "CardNumber VARCHAR(255), "
                    + "CVV VARCHAR(255), "
                    + "City VARCHAR(255), "
                    + "StreetName VARCHAR(255), "
                    + "StreetNumber VARCHAR(255), "
                    + "TK VARCHAR(255), "
                    + "password VARCHAR(255), "
                    + "PRIMARY KEY (CId)"
                    + ")");

            createTableIfNotExists(conn, "DriverLicense", "CREATE TABLE DriverLicense ("
                    + "DLNumber VARCHAR(255), "
                    + "DId VARCHAR(255), "
                    + "ExpirationDay VARCHAR(255), "
                    + "ExpirationMonth VARCHAR(255), "
                    + "ExpirationYear VARCHAR(255), "
                    + "CanDriveCar BOOLEAN, "
                    + "CanDriveMotorbike BOOLEAN, "
                    + "PRIMARY KEY (DLNumber)"
                    + ")");

            createTableIfNotExists(conn, "CreditCard", "CREATE TABLE CreditCard ("
                    + "CardNumber VARCHAR(255), "
                    + "CVV VARCHAR(255), "
                    + "ExpirationMonth VARCHAR(255), "
                    + "ExpirationYear VARCHAR(255), "
                    + "CardFirstName VARCHAR(255), "
                    + "CardLastName VARCHAR(255), "
                    + "PRIMARY KEY (CardNumber, CVV)"
                    + ")");

            createTableIfNotExists(conn, "PersonalInfo", "CREATE TABLE PersonalInfo ("
                    + "TIN VARCHAR(255), "
                    + "FirstName VARCHAR(255), "
                    + "LastName VARCHAR(255), "
                    + "BirthDay VARCHAR(255), "
                    + "BirthMonth VARCHAR(255), "
                    + "BirthYear VARCHAR(255), "
                    + "PRIMARY KEY (TIN)"
                    + ")");


            String insertInitialCarsData = "INSERT IGNORE INTO Car (VId, Brand, Model, Color, KmDriven, RentalCost, InsuranceCost, State, Type, PassengerLimit) VALUES "
                    + "('CAR-1230', 'Toyota', 'Yaris', 'Black', 100, 20.00, 15.20, 'Rented', 'Hatchback', 5), "
                    + "('CAR-4560', 'Honda', 'Civic', 'Silver', 120, 25.50, 15.20, 'Available', 'Sedan', 5), "
                    + "('CAR-7890', 'Ford', 'Focus', 'Blue', 80, 18.50, 15.20, 'Damaged', 'Hatchback', 5), "
                    + "('CAR-1010', 'Chevrolet', 'Malibu', 'Red', 150, 30.00, 15.20, 'Available', 'Sedan', 5), "
                    + "('CAR-2020', 'Nissan', 'Altima', 'White', 200, 35.75, 15.20, 'Available', 'Sedan', 5)";
            stmt.executeUpdate(insertInitialCarsData);
            System.out.println("Initial data inserted into 'Car' table!");

            String insertInitialMotorbikesData = "INSERT IGNORE INTO Motorbike (VId, Brand, Model, Color, KmDriven, RentalCost, InsuranceCost, State) VALUES "
                    + "('MBI-123', 'Harley-Davidson', 'Street 750', 'Black', 50, 15.00, 10.50, 'Available'), "
                    + "('MBI-456', 'Honda', 'CBR600RR', 'Red', 80, 20.50, 10.50, 'Available'), "
                    + "('MBI-789', 'Yamaha', 'YZF-R1', 'Blue', 60, 18.75, 10.50, 'Rented'), "
                    + "('MBI-101', 'Kawasaki', 'Ninja 300', 'Green', 45, 12.00, 10.50, 'Available'), "
                    + "('MBI-202', 'Suzuki', 'GSX-R750', 'Yellow', 70, 22.25, 10.50, 'Available')";
            stmt.executeUpdate(insertInitialMotorbikesData);
            System.out.println("Initial data inserted into 'Motorbike' table!");

            String insertInitialBicyclesData = "INSERT IGNORE INTO Bicycle (VId, Brand, Model, Color, KmDriven, RentalCost, InsuranceCost, State) VALUES "
                    + "('BIKE1', 'Raleigh', 'Mountain Bike', 'Green', 20, 5.00, 10.00, 'Available'), "
                    + "('BIKE2', 'Schwinn', 'Road Bike', 'Blue', 25, 7.50, 10.00, 'Available'), "
                    + "('BIKE3', 'Giant', 'Hybrid Bike', 'Black', 15, 4.50, 10.00, 'Rented'), "
                    + "('BIKE4', 'Trek', 'City Bike', 'Red', 18, 6.00, 10.00, 'Available'), "
                    + "('BIKE5', 'Cannondale', 'Electric Bike', 'White', 30, 8.75, 10.00, 'Available')";
            stmt.executeUpdate(insertInitialBicyclesData);
            System.out.println("Initial data inserted into 'Bicycle' table!");

            String insertInitialSkateData = "INSERT IGNORE INTO Skate (VId, Brand, Model, Color, KmDriven, RentalCost, InsuranceCost, State) VALUES "
                    + "('SKATE1', 'Rollerblade', 'Inline Skates', 'Blue', 5, 2.50, 10.00, 'Available'), "
                    + "('SKATE2', 'Razor', 'Quad Skates', 'Pink', 8, 3.75, 10.00, 'Available'), "
                    + "('SKATE3', 'K2', 'Fitness Skates', 'Black', 7, 3.00, 10.00, 'Rented'), "
                    + "('SKATE4', 'Bauer', 'Hockey Skates', 'Red', 10, 4.00, 10.00, 'Available'), "
                    + "('SKATE5', 'Element', 'Skateboard', 'Green', 3, 1.50, 10.00, 'Available')";
            stmt.executeUpdate(insertInitialSkateData);
            System.out.println("Initial data inserted into 'Skate' table!");

            String insertInitialRentData = "INSERT IGNORE INTO Rent (RId, Day, Month, Year, Hours, PaymentAmount, HasInsurance, Returned, VId, DId, CId, Cost) VALUES "
                    + "('RENT1', '10', '01', '2024', 4, 25.00, true, false, 'CAR-1230', 'DRIVER1', 'CUSTOMER1', 0.0), "
                    + "('RENT2', '15', '02', '2024', 3, 18.50, false, false, 'MBI-789', 'DRIVER2', 'CUSTOMER2', 0.0), "
                    + "('RENT3', '20', '03', '2024', 2, 8.00, true, false, 'BIKE3', 'DRIVER3', 'CUSTOMER3', 0.0), "
                    + "('RENT4', '25', '04', '2024', 5, 30.00, false, false, 'SKATE3', 'DRIVER4', 'CUSTOMER4', 0.0), "
                    + "('RENT5', '05', '05', '2024', 1, 5.50, false, true, 'CAR-7890', 'DRIVER5', 'CUSTOMER5', 10.0)";
            stmt.executeUpdate(insertInitialRentData);
            System.out.println("Initial data inserted into 'Rent' table!");

            String insertInitialDriverData = "INSERT IGNORE INTO Driver (DId, TIN) VALUES "
                    + "('DRIVER1', 'TIN1'), "
                    + "('DRIVER2', 'TIN2'), "
                    + "('DRIVER3', 'TIN3'), "
                    + "('DRIVER4', 'TIN3'), "
                    + "('DRIVER5', 'TIN5')";
            stmt.executeUpdate(insertInitialDriverData);
            System.out.println("Initial data inserted into 'Driver' table!");

            String insertInitialCustomerData = "INSERT IGNORE INTO Customer (CId, TIN, mail, CardNumber, CVV, City, StreetName, StreetNumber, TK, password) VALUES "
                    + "('CUSTOMER1', 'TIN1', 'customer1@example.com', '1234567890123456', '123', 'City1', 'Street1', '123', 'TK123', '0000'), "
                    + "('CUSTOMER2', 'TIN2', 'customer2@example.com', '9876543210987654', '456', 'City2', 'Street2', '456', 'TK456', '0000'), "
                    + "('CUSTOMER3', 'TIN3', 'customer3@example.com', '1111222233334444', '789', 'City3', 'Street3', '789', 'TK789', '1234'),"
                    + "('CUSTOMER4', 'TIN4', 'customer4@example.com', '5555666677778888', '234', 'City4', 'Street4', '101', 'TK101', '1234'), "
                    + "('CUSTOMER5', 'TIN5', 'customer5@example.com', '9999000011112222', '567', 'City5', 'Street5', '202', 'TK202', '0000'), "
                    + "('owner', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL), "
                    + "('client', 'TIN6', 'customer6@example.com', '9999000011112222', '567', 'City6', 'Street6', '201', 'TK203', 'c123')";
            stmt.executeUpdate(insertInitialCustomerData);
            System.out.println("Initial data inserted into 'Customer' table!");

            String insertInitialDriverLicenseData = "INSERT IGNORE INTO DriverLicense (DLNumber, DId, ExpirationDay, ExpirationMonth, ExpirationYear, CanDriveCar, CanDriveMotorbike) VALUES "
                    + "('DL1', 'DRIVER1', 10, 1, 2025, true, false), "
                    + "('DL2', 'DRIVER2', 15, 2, 2024, true, true), "
                    + "('DL3', 'DRIVER3', 20, 3, 2023, true, true), "
                    + "('DL4', 'DRIVER4', 25, 4, 2022, false, true), "
                    + "('DL5', 'DRIVER5', 5, 5, 2026, true, false)";
            stmt.executeUpdate(insertInitialDriverLicenseData);
            System.out.println("Initial data inserted into 'DriverLicense' table!");

            String insertInitialCreditCardData = "INSERT IGNORE INTO CreditCard (CardNumber, CVV, ExpirationMonth, ExpirationYear, CardFirstName, CardLastName) VALUES "
                    + "('1234567890123456', '123', 12, 2025, 'John', 'Doe'), "
                    + "('9876543210987654', '456', 11, 2024, 'Jane', 'Smith'), "
                    + "('1111222233334444', '789', 10, 2023, 'Robert', 'Johnson'), "
                    + "('5555666677778888', '234', 9, 2022, 'Emily', 'Williams'), "
                    + "('9999000011112222', '567', 8, 2026, 'Michael', 'Jones')";
            stmt.executeUpdate(insertInitialCreditCardData);
            System.out.println("Initial data inserted into 'CreditCard' table!");

            String insertInitialPersonalInfo = "INSERT IGNORE INTO PersonalInfo (TIN, FirstName, LastName, BirthDay, BirthMonth, BirthYear) VALUES "
                    + "('TIN1', 'John', 'Doe', 15, 6, 1990), "
                    + "('TIN2', 'Jane', 'Smith', 20, 8, 1985), "
                    + "('TIN3', 'Robert', 'Johnson', 5, 2, 1992), "
                    + "('TIN4', 'Emily', 'Williams', 10, 11, 1988), "
                    + "('TIN5', 'Michael', 'Jones', 25, 4, 1995), "
                    + "('TIN6', 'John', 'Jones', 25, 8, 1985)";
            stmt.executeUpdate(insertInitialPersonalInfo);
            System.out.println("Initial data inserted into 'PersonalInfo' table!");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
