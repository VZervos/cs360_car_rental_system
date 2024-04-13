package control.services;

import com.mysql.cj.util.StringUtils;
import control.Control;
import database.Database;
import representation.entities.Vehicle;
import representation.entities.people.Customer;
import representation.entities.vehicles.Bicycle;
import representation.entities.vehicles.Car;
import representation.entities.vehicles.Motorbike;
import representation.entities.vehicles.Skate;
import representation.utils.Date;
import representation.utils.Utils;
import representation.utils.VehicleStatus;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

class VehicleReturn {
    class SelectedRental {
        private Vehicle selectedVehicle;
        private representation.operations.Rental selectedRental;
        public Vehicle getSelectedVehicle() {
            return selectedVehicle;
        }

        public void setSelectedVehicle(Vehicle selectedVehicle) {
            this.selectedVehicle = selectedVehicle;
        }

        public String getExtraCostAndHours(String currentDate, String currentHour) {
            String result = checkArguments(currentDate, currentHour);
            if (result != null)
                return result;
            int extraHours = Date.getDaysDifference(currentDate, selectedRental.getRentDate().toString());
            return extraHours * selectedVehicle.getRentalCost() + "$ for " + extraHours + " extra hours.";
        }

        public float getExtraCost(String currentDate) {
            float extraHours = Date.getDaysDifference(selectedRental.getRentDate().toString(), currentDate);
            return extraHours * selectedVehicle.getRentalCost();
        }

        private String checkArguments(String currentDate, String currentHour) {
            if (!Date.isDateValid(currentDate) || !Utils.isInt(currentHour) || Integer.parseInt(currentHour) < 0 || Integer.parseInt(currentHour) >= 24)
                return "Invalid current date provided. Please check again!";
            return null;
        }

        public SelectedRental(String VID) {
            System.out.println("I am in");
            String[][] rentalInfo = Control.rentalManagement().getRentalInfo(VID);
            assert (rentalInfo != null);
            selectedRental = new representation.operations.Rental(rentalInfo[1][6], Integer.parseInt(rentalInfo[1][8]), Integer.parseInt(rentalInfo[1][1]), Integer.parseInt(rentalInfo[1][3]), Integer.parseInt(rentalInfo[1][4]), Float.parseFloat(rentalInfo[1][7]), rentalInfo[1][0], rentalInfo[1][9], rentalInfo[1][10]);

            String[][] vehicleInfo = Control.vehicleManagement().getVehicleInfo(VID);
            assert (vehicleInfo != null);
            Vehicle vehicle = null;
            String vidRetrieved = vehicleInfo[1][0];
            if(VID.startsWith("BIKE"))
                vehicle = new Bicycle(vidRetrieved, vehicleInfo[1][1], vehicleInfo[1][9], vehicleInfo[1][7], Float.parseFloat(vehicleInfo[1][4]), Float.parseFloat(vehicleInfo[1][2]), Integer.parseInt(vehicleInfo[1][5]), VehicleStatus.valueOf(vehicleInfo[1][6].toUpperCase()));
            else if(VID.startsWith("SKATE"))
                vehicle = new Skate(vidRetrieved, vehicleInfo[1][1], vehicleInfo[1][9], vehicleInfo[1][7], Float.parseFloat(vehicleInfo[1][4]), Float.parseFloat(vehicleInfo[1][2]), Integer.parseInt(vehicleInfo[1][5]), VehicleStatus.valueOf(vehicleInfo[1][6].toUpperCase()));
            else if(VID.length() == 8)
                vehicle = new Car(vidRetrieved, vehicleInfo[1][1], vehicleInfo[1][9], vehicleInfo[1][7], Float.parseFloat(vehicleInfo[1][4]), Float.parseFloat(vehicleInfo[1][2]), Integer.parseInt(vehicleInfo[1][5]), VehicleStatus.valueOf(vehicleInfo[1][6].toUpperCase()), vehicleInfo[1][3], Integer.parseInt(vehicleInfo[1][8]));
            else if(VID.length() == 7)
                vehicle = new Motorbike(vidRetrieved, vehicleInfo[1][1], vehicleInfo[1][9], vehicleInfo[1][7], Float.parseFloat(vehicleInfo[1][4]), Float.parseFloat(vehicleInfo[1][2]), Integer.parseInt(vehicleInfo[1][5]), VehicleStatus.valueOf(vehicleInfo[1][6].toUpperCase()));
            else assert (false);
            selectedVehicle = vehicle;
            assert(selectedVehicle != null);

        }
    }
    private SelectedRental selectedRental;

    public String vehicleReturn(String currentDate, String currentHour) {
        String result = getExtraCostAndHours(currentDate, currentHour);
        //float cost = selectedRental.getExtraCost(currentDate);

        if (result.contains("Invalid"))
            return result;
//        } else if(selectedRental.getExtraCost(currentDate) < 0) {
//            //System.out.println(selectedRental.getExtraCost(currentDate));
//            return "Invalid Incompatible date";
//        }
        float extraCost = Float.parseFloat(result.substring(0, result.indexOf('$')));

        String VID = selectedRental.getSelectedVehicle().getVID();
        String vehicle;

        if(VID.startsWith("BIKE")) {
            vehicle = "Bicycle";
        } else if(VID.startsWith("SKATE")) {
            vehicle = "Skate";
        } else if(VID.length() == 8) {
            vehicle = "Car";
        } else if(VID.length() == 7) {
            vehicle = "Motorbike";
        } else {
            return "This plate does not exist with a valid vehicle type";
        }

        // TODO: Update database about return

        Connection con = Database.connection();
        try {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query;
            String[][] rentalRetrieved = Control.rentalManagement().getRentalInfo(VID);
            float prevCost = Float.parseFloat(rentalRetrieved[1][7]);//vehicleRetrieved.getFloat("PaymentAmount");
            query = String.format("UPDATE Rent SET Returned = true, PaymentAmount = %s WHERE VId = '%s' AND Returned = false", prevCost + extraCost, VID);
            stmt.executeUpdate(query);

            query = String.format("UPDATE %s SET State = 'Available' WHERE VId = '%s'", vehicle, VID);
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            return e.toString();
        } catch (NullPointerException e) {
            return e.toString();
        }

        return result;
    }

    public String getExtraCostAndHours(String currentDate, String currentHour) {
        return selectedRental.getExtraCostAndHours(currentDate, currentHour);
    }

    public void updateSelectedVehicle(String VID) {
        selectedRental = new SelectedRental(VID);
    }
}

class Rental {
    private static final int MAX_ID_LENGTH = 5;
    private static int idCounter;

    public Rental() {
        idCounter = 1;

        Connection con = Database.connection();
        try {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query = String.format("SELECT RId FROM Rent");
            ResultSet rs = stmt.executeQuery(query);

            rs.beforeFirst();
            while (rs.next()) {
                idCounter++;
            }
        } catch (SQLException e) {
            System.err.println(e.toString());
        } catch (NullPointerException e) {
            System.err.println(e.toString());
        }
    }

    private String generateID() {
        String idNum = String.valueOf(idCounter++);
        StringBuilder idConstructor = new StringBuilder();
        idConstructor.append("0".repeat(MAX_ID_LENGTH - idNum.length()));
        idConstructor.append(idNum);
        return idConstructor.toString();
    }

    public String rent(String VID, String date, String days, String hours, boolean isInsuranced, String TIN, String driverFirstName, String driverLastName, String driverBirthday, String driverLicenseNumber, String driverExpirationDate, boolean canDriveCarValue, boolean canDriveMotorbikeValue) {
        Customer customer = Control.getCurrentCustomer();
        validateArguments(VID, date, days, hours, driverFirstName, driverLastName, driverBirthday, customer);
        String result = checkArguments(VID, date, days, hours, TIN, driverFirstName, driverLastName, driverBirthday, driverLicenseNumber, driverExpirationDate, canDriveCarValue, canDriveMotorbikeValue);
        if (result != null)
            return result;

        String vehicle;
        boolean License = !driverLicenseNumber.isBlank() && !driverExpirationDate.isBlank();
        if(VID.startsWith("BIKE")) {
            vehicle = "Bicycle";
        } else if(VID.startsWith("SKATE")) {
            vehicle = "Skate";
        } else if(VID.length() == 8) {
            vehicle = "Car";
            if(!License || !canDriveCarValue) return "Driver License is necesary to rent a car";
        } else if(VID.length() == 7) {
            vehicle = "Motorbike";
            if(!License || !canDriveMotorbikeValue) return "Driver License is necesary to rent a motorbike";
        } else {
            return "This plate does not exist with a valid vehicle type";
        }

        Date date2 = Date.toDate(date);

        UserManagement um = Control.userManagement();
        um.registerDriver(TIN, driverFirstName, driverLastName, driverBirthday, License, driverLicenseNumber, driverExpirationDate, canDriveCarValue, canDriveMotorbikeValue);

        double rentCost = (Integer.parseInt(days)*24 + Integer.parseInt(hours));


        String DId = null;

        Connection con = Database.connection();
        try {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String query = String.format("SELECT DId FROM Driver WHERE TIN = '%s'", TIN);
            ResultSet rs = stmt.executeQuery(query);

            rs.beforeFirst();
            if (rs.next()) {
                DId = rs.getString("DId");
            } else {
                assert(false);
            }

            query = String.format("SELECT RentalCost, InsuranceCost FROM %s WHERE VId = '%s'", vehicle, VID);
            rs = stmt.executeQuery(query);

            rs.beforeFirst();
            if (rs.next()) {
                rentCost *= rs.getDouble("RentalCost");
                if(isInsuranced) {
                    rentCost += rs.getDouble("InsuranceCost");
                }
            } else {
                assert(false);
            }

            query = String.format("INSERT INTO Rent (RId, Day, Month, Year, Hours, PaymentAmount, HasInsurance, Returned, VId, DId, CId, Cost) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %s, false, '%s', '%s', '%s', 0)", generateID(), date2.getDay(), date2.getMonth(), date2.getYear(), (Integer.parseInt(days)*24 + Integer.parseInt(hours)), rentCost, isInsuranced, VID, DId, customer.getCId());
            stmt.executeUpdate(query);

            query = String.format("UPDATE %s SET State = 'Rented' WHERE VId = '%s'", vehicle, VID);
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            return e.toString();
        } catch (NullPointerException e) {
            return e.toString();
        }

        return "Success! " + VID + " was rented for " + days + " days and " + hours + "hours. Your credit card has been charged " + rentCost + "$. Thank you!";
    }

    private String checkArguments(String VID, String date, String days, String hours, String driverTIN, String driverFirstName, String driverLastName, String driverBirthday, String driverLicenseNumber, String driverExpirationDate, boolean canDriveCarValue, boolean canDriveMotorbikeValue) {
        if (!Date.isDateValid(date) || !Utils.isInt(days) || !Utils.isInt(hours))
            return "Invalid general information given. Please check again your information.";
        else if (driverTIN.isBlank() || driverFirstName.isBlank() || driverLastName.isBlank() || driverBirthday.isBlank() || !Date.isDateValid(driverBirthday) || Integer.parseInt(String.valueOf(driverBirthday.charAt(3) + driverBirthday.charAt(4))) < 16) {
            return "Invalid driver credentials given. Please check again your information.";
        } else if (!(!(VID.contains("CAR") || VID.contains("MBI")) || ((!VID.contains("CAR") || canDriveCarValue) && (!VID.contains("MBI") || canDriveMotorbikeValue) && Integer.parseInt(String.valueOf(driverBirthday.charAt(3) + driverBirthday.charAt(4))) >= 18 && !driverLicenseNumber.isBlank() && !driverExpirationDate.isBlank() && Date.isDateValid(driverExpirationDate)))) {
            return "Invalid driver license credentials given. Please check again your information.";
        }
        return null;
    }

    private void validateArguments(String VID, String date, String days, String hours, String driverFirstName, String driverLastName, String driverBirthday, Customer customer) {
        assert (!VID.isBlank() && !date.isBlank() && !days.isBlank() && !hours.isBlank() && !driverFirstName.isBlank() && !driverLastName.isBlank() && !driverBirthday.isBlank() && !customer.getCId().isBlank() && !customer.getCity().isBlank() && !customer.getStreetName().isBlank() && !customer.getCreditCard().getCardFirstName().isBlank() && !customer.getCreditCard().getCardLastName().isBlank() && !customer.getCreditCard().getCardNumber().isBlank() && !customer.getCreditCard().getCVV().isBlank() && !customer.getCreditCard().getExpirationMonth().isBlank() && !customer.getCreditCard().getExpirationYear().isBlank() && !customer.getMail().isBlank() && !customer.getPersonalInfo().getBirthDay().isBlank() && !customer.getPersonalInfo().getBirthMonth().isBlank() && !customer.getPersonalInfo().getBirthYear().isBlank() && !customer.getPersonalInfo().getFirstName().isBlank() && !customer.getPersonalInfo().getLastName().isBlank() && !customer.getPersonalInfo().getTIN().isBlank());
    }
}

public class RentalManagement {
    private String[][] rentals;
    private final VehicleReturn vehicleReturn;
    private final Rental rental;

    public RentalManagement() {
        vehicleReturn = new VehicleReturn();
        rental = new Rental();
    }

    public void updateRentals() {
        try {
            rentals = Utils.convertResultSetToArray(Database.retrieveAllRentals(), new HashMap<>() {{
                put("RId", "string");
                put("Day", "string");
                put("Month", "string");
                put("Year", "string");
                put("Hours", "string");
                put("PaymentAmount", "string");
                put("HasInsurance", "string");
                put("Returned", "string");
                put("VId", "string");
                put("DId", "string");
                put("CId", "string");
            }});

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String vehicleReturn(String currentDate, String currentHour) {
        return vehicleReturn.vehicleReturn(currentDate, currentHour);
    }

    public String getVehicleReturnExtraCostAndHours(String currentDate, String currentHour) {
        return vehicleReturn.getExtraCostAndHours(currentDate, currentHour);
    }

    public void updateVehicleReturnSelectedVehicle(String VID) {
        vehicleReturn.updateSelectedVehicle(VID);
    }

    public String rental(String VID, String date, String days, String hours, boolean isInsuranced, String TIN, String driverFirstName, String driverLastName, String driverBirthday, String driverLicenseNumber, String driverExpirationDate, boolean canDriveCarValue, boolean canDriveMotorbikeValue) {
        return rental.rent(VID, date, days, hours, isInsuranced, TIN, driverFirstName, driverLastName, driverBirthday, driverLicenseNumber, driverExpirationDate, canDriveCarValue, canDriveMotorbikeValue);
    }

    public String[] getVehiclesRentedByCustomer(String CId) {
        updateRentals();
        List<String> vehiclesRented = new ArrayList<>();
        for (int i = 1; i< rentals.length ; i++) {
            if (Objects.equals(CId, rentals[i][10])) {
                vehiclesRented.add(rentals[i][0]);
            }
        }
        return vehiclesRented.toArray(new String[0]);
    }

    public String[][] getRentalInfo(String VId) {
        updateRentals();
        String[][] rentalInfo = new String[2][];
        boolean found = false;
        rentalInfo[0] = rentals[0];
        for (String[] r : rentals) {
            if (Objects.equals(r[0], VId)) {
                found = true;
                rentalInfo[1] = r;
            }
        }
        if (!found)
            return null;
        return new String[][]{rentalInfo[0], rentalInfo[1]};
    }

}
