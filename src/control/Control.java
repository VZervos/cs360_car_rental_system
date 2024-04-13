package control;

import control.services.RentalManagement;
import control.services.UserManagement;
import control.services.VehicleManagement;
import database.Database;
import representation.entities.people.Customer;

public class Control {
    private static Database database;
    public static Database database() { return database; }
    private static Customer currentCustomer;
    private static UserManagement userManagement;
    public static UserManagement userManagement() {
        return userManagement;
    }
    private static VehicleManagement vehicleManagement;
    public static VehicleManagement vehicleManagement() {
        return vehicleManagement;
    }
    private static RentalManagement rentalManagement;
    public static RentalManagement rentalManagement() { return  rentalManagement; }

    public static Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public static void setCurrentCustomer(Customer currentCustomer) {
        Control.currentCustomer = currentCustomer;
    }

    public Control() {
        database = new Database();
        currentCustomer = null;
        userManagement = new UserManagement();
        vehicleManagement = new VehicleManagement();
        rentalManagement = new RentalManagement();
    }

}
