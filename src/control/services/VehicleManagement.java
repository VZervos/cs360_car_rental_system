package control.services;

import database.Database;
import representation.utils.Date;
import representation.utils.Utils;

import java.sql.*;
import java.util.*;

import java.time.LocalDate;

class VehicleAccident {
    public String vehicleAccident(String VID) {
        verifyArguments(VID);

        double extraCost = 0;
        boolean hasInsurance = false;

        String vehicle;
        if (VID.startsWith("BIKE")) {
            vehicle = "Bicycle";
        } else if (VID.startsWith("SKATE")) {
            vehicle = "Skate";
        } else if (VID.length() == 8) {
            vehicle = "Car";
        } else if (VID.length() == 7) {
            vehicle = "Motorbike";
        } else {
            return "This plate does not exist with a valid vehicle type";
        }

        Connection con = Database.connection();
        String RID = "0";
        try {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs;

            String query = String.format("UPDATE %s SET State = 'Damaged' WHERE VId = '%s'", vehicle, VID);
            stmt.executeUpdate(query);

            //find RId
            query = String.format("SELECT RId, PaymentAmount, HasInsurance, Cost FROM Rent WHERE VId = '%s' AND Returned = false", VID);
            rs = stmt.executeQuery(query);

            float cost = 0;
            if (rs.next()) {
                hasInsurance = rs.getBoolean("HasInsurance");
                extraCost = 3 * rs.getDouble("PaymentAmount");
                RID = rs.getString("RId");
                cost = rs.getFloat("Cost");
            } else {
                assert (false);
            }

            //find new Car
            query = String.format("SELECT VId FROM %s WHERE State = 'Available'", vehicle);
            Statement stmt2 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt2.executeQuery(query);

            rs.beforeFirst();
            if (rs.next()) {
                String nVID = rs.getString("VId");

                //set new car as rented
                query = String.format("UPDATE %s SET State = 'Rented' WHERE VId = '%s'", vehicle, nVID);
                stmt.executeUpdate(query);

                //update the rent
                query = String.format("UPDATE Rent SET VId = '%s', Cost = %s WHERE RId = '%s'", nVID, cost+500, RID);
                stmt.executeUpdate(query);

                return "Accident submitted! " +
                        (hasInsurance ? ("Thanks to your insurance, no extra cost was charged! Your new " + vehicle + " is: " + nVID) : ("You had no insurance, therefore your credit card has been charged! "
                                + "Cost: " + extraCost + "$"));

            } else {
                query = String.format("UPDATE Rent SET Returned = true, PaymentAmount = %s, Cost = %s WHERE RId = '%s'", extraCost, cost+500, RID);
                stmt.executeUpdate(query);

                return "Accident submitted! " +
                        (hasInsurance ? ("Thanks to your insurance, no extra cost was charged! ") : ("You had no insurance, therefore your credit card has been charged! "
                                + "Cost: " + extraCost + "$"));
            }

        } catch (SQLException e) {
            return e.toString();
        } catch (NullPointerException e) {
            return e.toString();
        }
    }

    private void verifyArguments(String VID) {
        assert (!VID.isBlank());
    }
}

class VehicleDamage {
    public String vehicleDamage(String VID) {
        verifyArguments(VID);

        String newVehicleInfo;

        String vehicle;
        if (VID.startsWith("BIKE")) {
            vehicle = "Bicycle";
        } else if (VID.startsWith("SKATE")) {
            vehicle = "Skate";
        } else if (VID.length() == 8) {
            vehicle = "Car";
        } else if (VID.length() == 7) {
            vehicle = "Motorbike";
        } else {
            return "This plate does not exist with a valid vehicle type";
        }

        Connection con = Database.connection();
        try {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs;

            String query = String.format("UPDATE %s SET State = 'Service' WHERE VId = '%s'", vehicle, VID);
            stmt.executeUpdate(query);

            query = String.format("Select Cost FROM Rent WHERE VId = '%s' AND Returned = false", VID);
            rs = stmt.executeQuery(query);

            float cost = 0;
            if(rs.next()) {
                cost = rs.getFloat("Cost");
            } else {
                assert(false);
            }

            //find new Car
            query = String.format("SELECT VId FROM %s WHERE State = 'Available'", vehicle);
            rs = stmt.executeQuery(query);

            rs.beforeFirst();
            if (rs.next()) {
                newVehicleInfo = rs.getString("VId");

                //set new car as rented
                query = String.format("UPDATE %s SET State = 'Rented' WHERE VId = '%s'", vehicle, newVehicleInfo);
                stmt.executeUpdate(query);

                //update the rent
                query = String.format("UPDATE Rent SET VId = '%s', Cost = %s WHERE VId = '%s' AND Returned = false", newVehicleInfo, cost+100, VID);
                stmt.executeUpdate(query);

                return "Vehicle damage submitted! This is your new vehicle:\n" + newVehicleInfo;
            } else {
                query = String.format("UPDATE Rent SET Returned = true, Cost = %s WHERE VId = '%s' AND Returned = false", cost+100, VID);
                stmt.executeUpdate(query);

                return "Vehicle damage submitted! We did not found any available " + vehicle + " to provide";
            }
        } catch (SQLException e) {
            return e.toString();
        } catch (NullPointerException e) {
            return e.toString();
        }

    }

    private void verifyArguments(String VID) {
        assert (!VID.isBlank());
    }
}

class MostWantedVehicle {
    public String mostWantedVehicle(String vehicleType) {
        if (Objects.equals(vehicleType, "Bike"))
            vehicleType = "Bicycle";
        Map<String, Integer> vehiclesRents = new HashMap<>();
        Map<String, String> vehicleTypes = new HashMap<>();
        Connection con = Database.connection();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs;
            String tableName = vehicleType;

            String query = "SELECT Rent.VId FROM Rent JOIN " + tableName + " ON Rent.VId = " + tableName + ".VId";  // Get all VIDs

            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String VID = rs.getString("VId");
                vehiclesRents.put(VID, vehiclesRents.getOrDefault(VID, 0) + 1);
            }


            int max = -1;
            String maxVID = "";
            for (Map.Entry<String, Integer> entry : vehiclesRents.entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    maxVID = entry.getKey();
                }
            }

            System.out.println("Most wanted vehicle: " + maxVID + " with " + max + " rents");

            return maxVID + " with " + max + " rents";
        } catch (SQLException e) {
            return e.toString();
        } catch (NullPointerException e) {
            return e.toString();
        }
    }
}

class MoreOptions {
    public String[] moreOptions(int operation, List<String> parameters, VehicleManagement vehicleManagementInst) {
        verifyArguments(operation);
        String[] result = checkArguments(operation, parameters);
        if (result != null)
            return result;

        List<String> results = new ArrayList<>();
        String[][] allVehicles;
        String[][] allRentals;
        String fromDate;
        String untilDate;
        try {
            switch (operation) {
                case 0:
                    vehicleManagementInst.updateVehiclesInfo();
                    allVehicles = vehicleManagementInst.getAllVehiclesInfo();
                    for (int i = 1; i < allVehicles.length; i++) {
                        if (Objects.equals(parameters.get(1), "Car") && allVehicles[i][0].contains("CAR") && Objects.equals(allVehicles[i][6], parameters.get(0)))
                            results.add("VID: " + allVehicles[i][0] + " [" + allVehicles[i][6] + "]\n");
                        else if (Objects.equals(parameters.get(1), "Motorbike") && allVehicles[i][0].contains("MBI") && Objects.equals(allVehicles[i][5], parameters.get(0)))
                            results.add("VID: " + allVehicles[i][0] + " [" + allVehicles[i][5] + "]\n");
                        else if (Objects.equals(parameters.get(1), "Skate") && allVehicles[i][0].contains("SKATE") && Objects.equals(allVehicles[i][5], parameters.get(0)))
                            results.add("VID: " + allVehicles[i][0] + " [" + allVehicles[i][5] + "]\n");
                        else if (Objects.equals(parameters.get(1), "Bike") && allVehicles[i][0].contains("BIKE") && Objects.equals(allVehicles[i][5], parameters.get(0)))
                            results.add("VID: " + allVehicles[i][0] + " [" + allVehicles[i][5] + "]\n");
                    }
                    break;
                case 1:
                    fromDate = parameters.get(0);
                    untilDate = parameters.get(1);
                    int[] datefrom_components = Arrays.stream(fromDate.split("/"))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    int[] dateuntil_components = Arrays.stream(untilDate.split("/"))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    int dayfrom = datefrom_components[0];
                    int monthfrom = datefrom_components[1];
                    int yearfrom = datefrom_components[2];
                    int dayuntil = dateuntil_components[0];
                    int monthuntil = dateuntil_components[1];
                    int yearuntil = dateuntil_components[2];
                    allRentals = Utils.convertResultSetToArray(Database.retrieveAllRents(), new HashMap<>() {{
                        put("RId", "string");
                        put("Day", "string");
                        put("Month", "string");
                        put("Year", "string");
                        put("Hours", "string");
                        put("PaymentAmount", "float");
                        put("HasInsurance", "boolean");
                        put("VId", "string");
                        put("DId", "string");
                        put("CId", "string");
                        put("Cost", "string");
                    }});
                    for (int i = 1; i < allRentals.length; i++) {
                        String[] r = allRentals[i];
                        // allRents:  VId Month Year Hours HasInsurance RId PaymentAmount Day DId CId
                        System.out.println('\n' + r[0] + " " + r[1] + " " + r[2] + " " + r[3] + " " + r[4] + " " + r[5] + " " + r[6] + " " + r[7] + " " + r[8] + " " + r[9]);
                        int rent_start_day = Integer.parseInt(r[7]);
                        int rent_start_month = Integer.parseInt(r[1]);
                        int rent_start_year = Integer.parseInt(r[2]);
                        int rent_start_hours_duration = Integer.parseInt(r[3]);
                        int rent_end_day = rent_start_day + rent_start_hours_duration / 24;
                        int rent_end_month = rent_start_month;
                        if (rent_end_day > 31) {
                            rent_end_day = 1;
                            rent_end_month++;
                        }
                        int rent_end_year = rent_start_year;
                        if (rent_end_month > 12) {
                            rent_end_month = 1;
                            rent_end_year++;
                        }
                        System.out.println("\nRent start: " + rent_start_day + "/" + rent_start_month + "/" + rent_start_year);
                        System.out.println("Rent end: " + rent_end_day + "/" + rent_end_month + "/" + rent_end_year);
                        System.out.println("\nFrom: " + dayfrom + "/" + monthfrom + "/" + yearfrom);
                        System.out.println("Until: " + dayuntil + "/" + monthuntil + "/" + yearuntil + "\n");
                        LocalDate RentStart = LocalDate.of(rent_start_year, rent_start_month, rent_start_day);
                        LocalDate RentEnd = LocalDate.of(rent_end_year, rent_end_month, rent_end_day);
                        LocalDate From = LocalDate.of(yearfrom, monthfrom, dayfrom);
                        LocalDate Until = LocalDate.of(yearuntil, monthuntil, dayuntil);
                        if (doPeriodsOverlap(RentStart, RentEnd, From, Until)) {
                            results.add("RId: " + r[0] + " [" + r[7] + "/" + r[1] + "/" + r[2] + " - " + rent_end_day + "/" + rent_end_month + "/" + rent_end_year + "]\n");
                        }
                    }
                    break;
                case 2:
                    allRentals = Utils.convertResultSetToArray(Database.retrieveAllRents(), new HashMap<>() {{
                        put("RId", "string");
                        put("Day", "string");
                        put("Month", "string");
                        put("Year", "string");
                        put("Hours", "string");
                        put("PaymentAmount", "float");
                        put("HasInsurance", "boolean");
                        put("VId", "string");
                        put("DId", "string");
                        put("CId", "string");
                        put("Cost", "string");
                    }});
                    int sum = 0;
                    int items = 0;
                    int max = -1;
                    int min = -1;
                    for (String[] r : allRentals) {
                        if ((Objects.equals(parameters.get(0), "Car") && r[0].contains("CAR"))
                                || (Objects.equals(parameters.get(0), "Motorbike") && r[0].contains("MBI"))
                                || (Objects.equals(parameters.get(0), "Skate") && r[0].contains("SKATE"))
                                || (Objects.equals(parameters.get(0), "Bicycle") && r[0].contains("BIKE"))
                        ) {
                            int hours = Integer.parseInt(r[3]);
                            sum += hours;
                            items++;
                            if (max < hours || max == -1)
                                max = hours;
                            if (min > hours || min == -1)
                                min = hours;
                        }
                    }
                    if (Objects.equals(parameters.get(1), "Maximum"))
                        return new String[]{"Maximum rental hours: " + (max > -1 ? max : 0)};
                    else if (Objects.equals(parameters.get(1), "Minimum"))
                        return new String[]{"Minimum rental hours: " + (min > -1 ? min : 0)};
                    else if (Objects.equals(parameters.get(1), "Average"))
                        return new String[]{"Average rental hours: " + (items > 0 ? sum / items : 0)};
                    else
                        assert (false);
                    break;
                case 3:
                    try {
                        fromDate = parameters.get(1);
                        untilDate = parameters.get(2);
                        String vehicleType = parameters.get(0);

                        datefrom_components = Arrays.stream(fromDate.split("/"))
                                .mapToInt(Integer::parseInt)
                                .toArray();

                        dateuntil_components = Arrays.stream(untilDate.split("/"))
                                .mapToInt(Integer::parseInt)
                                .toArray();

                        dayfrom = datefrom_components[0];
                        monthfrom = datefrom_components[1];
                        yearfrom = datefrom_components[2];

                        dayuntil = dateuntil_components[0];
                        monthuntil = dateuntil_components[1];
                        yearuntil = dateuntil_components[2];

                        allRentals = Utils.convertResultSetToArray(Database.retrieveAllRents(), new HashMap<>() {{
                            put("RId", "string");
                            put("Day", "string");
                            put("Month", "string");
                            put("Year", "string");
                            put("Hours", "string");
                            put("PaymentAmount", "float");
                            put("HasInsurance", "boolean");
                            put("VId", "string");
                            put("DId", "string");
                            put("CId", "string");
                            put("Cost", "string");
                        }});
                        vehicleManagementInst.updateVehiclesInfo();
                        allVehicles = vehicleManagementInst.getAllVehiclesInfo();

                        IncomePerType_and_Date inc = new IncomePerType_and_Date();
                        float income = inc.IncomePerType_and_Date(vehicleType, dayfrom, monthfrom, yearfrom, dayuntil, monthuntil, yearuntil);
                        System.out.println("Income: " + income);
                        results.add("Income: " + income + "\n");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    fromDate = parameters.get(0);
                    untilDate = parameters.get(1);
                    int[] datefrom_components2 = Arrays.stream(fromDate.split("/"))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    int[] dateuntil_components2 = Arrays.stream(untilDate.split("/"))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    int dayfrom2 = datefrom_components2[0];
                    int monthfrom2 = datefrom_components2[1];
                    int yearfrom2 = datefrom_components2[2];
                    int dayuntil2 = dateuntil_components2[0];
                    int monthuntil2 = dateuntil_components2[1];
                    int yearuntil2 = dateuntil_components2[2];
                    float cost = 0;
                    allRentals = Utils.convertResultSetToArray(Database.retrieveAllRents(), new HashMap<>() {{
                        put("RId", "string");
                        put("Day", "string");
                        put("Month", "string");
                        put("Year", "string");
                        put("Hours", "string");
                        put("PaymentAmount", "float");
                        put("HasInsurance", "boolean");
                        put("VId", "string");
                        put("DId", "string");
                        put("CId", "string");
                        put("Cost", "string");
                    }});
                    for (int i = 1; i < allRentals.length; i++) {
                        String[] r = allRentals[i];
                        // allRents:  VId Month Year Hours HasInsurance RId PaymentAmount Day DId CId
                        System.out.println('\n' + r[0] + " " + r[1] + " " + r[2] + " " + r[3] + " " + r[4] + " " + r[5] + " " + r[6] + " " + r[7] + " " + r[8] + " " + r[9]);
                        int rent_start_day = Integer.parseInt(r[7]);
                        int rent_start_month = Integer.parseInt(r[1]);
                        int rent_start_year = Integer.parseInt(r[2]);
                        int rent_start_hours_duration = Integer.parseInt(r[3]);
                        int rent_end_day = rent_start_day + rent_start_hours_duration / 24;
                        int rent_end_month = rent_start_month;
                        if (rent_end_day > 31) {
                            rent_end_day = 1;
                            rent_end_month++;
                        }
                        int rent_end_year = rent_start_year;
                        if (rent_end_month > 12) {
                            rent_end_month = 1;
                            rent_end_year++;
                        }
                        System.out.println("\nRent start: " + rent_start_day + "/" + rent_start_month + "/" + rent_start_year);
                        System.out.println("Rent end: " + rent_end_day + "/" + rent_end_month + "/" + rent_end_year);
                        System.out.println("\nFrom: " + dayfrom2 + "/" + monthfrom2 + "/" + yearfrom2);
                        System.out.println("Until: " + dayuntil2 + "/" + monthuntil2 + "/" + yearuntil2 + "\n");
                        LocalDate RentStart = LocalDate.of(rent_start_year, rent_start_month, rent_start_day);
                        LocalDate RentEnd = LocalDate.of(rent_end_year, rent_end_month, rent_end_day);
                        LocalDate From = LocalDate.of(yearfrom2, monthfrom2, dayfrom2);
                        LocalDate Until = LocalDate.of(yearuntil2, monthuntil2, dayuntil2);
                        if (doPeriodsOverlap(RentStart, RentEnd, From, Until)) {
                            cost += Float.parseFloat(r[8]);
                        }
                    }
                    results.add(Float.toString(cost));
                    break;
                case 5:
                    String vehicleType = parameters.get(0);
                    System.out.println(vehicleType);
                    MostWantedVehicle mostWantedVehicle = new MostWantedVehicle();
                    String mostWantedVehicleResult = mostWantedVehicle.mostWantedVehicle(vehicleType);
                    results.add(mostWantedVehicleResult);
                    break;
                case 6:
                    StringBuilder resultSetString = new StringBuilder();
                    try {
                        ResultSet queryResults = Database.connect().createStatement().executeQuery(parameters.get(0));
                        ResultSetMetaData queryMetadata = queryResults.getMetaData();
                        int columns = queryMetadata.getColumnCount();
                        while (queryResults.next()) {
                            for (int i = 1; i <= columns; i++) {
                                resultSetString.append(queryMetadata.getColumnName(i)).append(": ");
                                resultSetString.append(queryResults.getString(queryMetadata.getColumnName(i)));
                                if (i < columns)
                                    resultSetString.append(", ");
                            }
                            resultSetString.append("\n");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    return new String[]{resultSetString.toString()};
                default:
                    assert (false);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (results.size() == 0) {
            results.add("No results were found!");
        }

        return results.toArray(new String[0]);
    }

    public static boolean doPeriodsOverlap(LocalDate RentStart, LocalDate RentEnd, LocalDate From, LocalDate Until) {
        return !RentStart.isAfter(Until) && !RentEnd.isBefore(From);
    }

    private String[] checkArguments(int operation, List<String> parameters) {
        if (operation == 1 || operation == 3 || operation == 4) {
            int i = (operation == 1 || operation == 4) ? 0 : 1;
            String fromDate = parameters.get(i++);
            String untilDate = parameters.get(i);
            boolean hasFromDate = !fromDate.isBlank();
            boolean hasUntilDate = !untilDate.isBlank();
            if (!hasFromDate || !Date.isDateValid(fromDate) || !hasUntilDate && !Date.isDateValid(untilDate))
                return new String[]{"Invalid date information given! Please check again."};
        }
        return null;
    }

    private void verifyArguments(int operation) {
        assert (operation >= 0 && operation <= 6);
    }
}

class VehicleSupplement {
    public String supply(String vehicleType, String VID, String brand, String model, String color, String autonomy, String rentalCost, String insuranceCost, String carType, String passengersLimit) {
        verifyArguments(vehicleType, VID, brand, model, color, autonomy, rentalCost, insuranceCost);
        String result = checkArguments(vehicleType, autonomy, rentalCost, insuranceCost, passengersLimit);
        if (result != null)
            return result;

        try {
            Connection con = Database.connect();
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs;
            String query = null;
            String[] PlateParts;
            switch (vehicleType) {
                case "Car":
                    PlateParts = VID.split("-");
                    if (PlateParts.length != 2 || PlateParts[0].length() != 3 || PlateParts[1].length() != 4 || !Utils.isInt(PlateParts[1])) {
                        return "Invalid plate number";
                    }

                    query = String.format("SELECT * FROM Car WHERE VId = '%s'", VID);
                    rs = stmt.executeQuery(query);

                    rs.beforeFirst();
                    if (rs.next()) {
                        query = String.format("UPDATE Car SET State = '%s', Color = '%s', KmDriven = %s, RentalCost = %s, InsuranceCost = %s WHERE VId = '%s'", "Available", color, Float.parseFloat(autonomy), rentalCost, insuranceCost, VID);
                    } else {
                        query = String.format("INSERT INTO Car (VId, Brand, Model, Color, KmDriven, RentalCost, InsuranceCost, State, Type, PassengerLimit) VALUES ('%s', '%s', '%s', '%s', %s, %s, %s, 'Available', '%s', %s)", VID, brand, model, color, Float.parseFloat(autonomy), rentalCost, insuranceCost, carType, passengersLimit);
                    }

                    break;
                case "Motorbike":
                    PlateParts = VID.split("-");
                    if (PlateParts.length != 2 || PlateParts[0].length() != 3 || PlateParts[1].length() != 3 || !Utils.isInt(PlateParts[1])) {
                        return "Invalid plate number";
                    }

                    query = String.format("SELECT * FROM Motorbike WHERE VId = '%s'", VID);
                    rs = stmt.executeQuery(query);

                    rs.beforeFirst();
                    if (rs.next()) {
                        query = String.format("UPDATE Motorbike SET State = '%s', Color = '%s', KmDriven = %s, RentalCost = %s, InsuranceCost = %s WHERE VId = '%s'", "Available", color, Float.parseFloat(autonomy), rentalCost, insuranceCost, VID);
                    } else {
                        query = String.format("INSERT INTO Motorbike (VId, Brand, Model, Color, KmDriven, RentalCost, InsuranceCost, State) VALUES ('%s', '%s', '%s', '%s', %s, %s, %s, 'Available')", VID, brand, model, color, Float.parseFloat(autonomy), rentalCost, insuranceCost);
                    }
                    break;
                case "Bicycle":
                    PlateParts = VID.split("BIKE");
                    if (PlateParts.length != 2 || !Utils.isInt(PlateParts[1]) || !VID.startsWith("BIKE")) {
                        return "Invalid plate number";
                    }

                    query = String.format("SELECT * FROM Bicycle WHERE VId = '%s'", VID);
                    rs = stmt.executeQuery(query);

                    rs.beforeFirst();
                    if (rs.next()) {
                        query = String.format("UPDATE Bicycle SET State = '%s', Color = '%s', KmDriven = %s, RentalCost = %s, InsuranceCost = %s WHERE VId = '%s'", "Available", color, Float.parseFloat(autonomy), rentalCost, insuranceCost, VID);
                    } else {
                        query = String.format("INSERT INTO Bicycle (VId, Brand, Model, Color, KmDriven, RentalCost, InsuranceCost, State) VALUES ('%s', '%s', '%s', '%s', %s, %s, %s, 'Available')", VID, brand, model, color, Float.parseFloat(autonomy), rentalCost, insuranceCost);
                    }
                    break;
                case "Skate":
                    PlateParts = VID.split("SKATE");
                    if (PlateParts.length != 2 || !Utils.isInt(PlateParts[1]) || !VID.startsWith("SKATE")) {
                        return "Invalid plate number";
                    }

                    query = String.format("SELECT * FROM Skate WHERE VId = '%s'", VID);
                    rs = stmt.executeQuery(query);

                    rs.beforeFirst();
                    if (rs.next()) {
                        query = String.format("UPDATE Skate SET State = '%s', Color = '%s', KmDriven = %s, RentalCost = %s, InsuranceCost = %s WHERE VId = '%s'", "Available", color, Float.parseFloat(autonomy), rentalCost, insuranceCost, VID);
                    } else {
                        query = String.format("INSERT INTO Skate (VId, Brand, Model, Color, KmDriven, RentalCost, InsuranceCost, State) VALUES ('%s', '%s', '%s', '%s', %s, %s, %s, 'Available')", VID, brand, model, color, Float.parseFloat(autonomy), rentalCost, insuranceCost);
                    }
                    break;
                default:
                    assert (false);
            }

            stmt.executeUpdate(query);
        } catch (SQLException e) {
            return e.toString();
        } catch (NullPointerException e) {
            return e.toString();
        }

        return null;
    }

    private String checkArguments(String vehicleType, String autonomy, String rentalCost, String insuranceCost, String passengersLimit) {
        if (!Utils.isInt(autonomy) || Integer.parseInt(autonomy) < 0 || !Utils.isDouble(rentalCost) || Double.parseDouble(rentalCost) < 0 || !Utils.isDouble(insuranceCost) || Double.parseDouble(insuranceCost) < 0 || (vehicleType == "Car" && (!Utils.isInt(passengersLimit) || Integer.parseInt(passengersLimit) < 0)))
            return "Invalid vehicle information given! Please check again.";
        return null;
    }

    private void verifyArguments(String vehicleType, String plate, String brand, String model, String color, String autonomy, String rentalCost, String insuranceCost) {
        assert (!vehicleType.isBlank() && !plate.isBlank() && !brand.isBlank() && !model.isBlank() && !color.isBlank() && !autonomy.isBlank() && !rentalCost.isBlank() && !insuranceCost.isBlank());
    }

}

class VehicleRepair {
    public String repair(String vehicle, String operation, String fromDate, String untilDate) {
        verifyArguments(vehicle, operation, fromDate, untilDate);
        String result = checkArguments(vehicle, operation, fromDate, untilDate);
        if (result != null)
            return result;

        String vehicleType = findType(vehicle);

        try {
            Connection con = Database.connect();

            String query = String.format("SELECT * FROM %s WHERE VId = '%s'", vehicleType, vehicle);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(query);

            rs.beforeFirst();
            if (rs.next()) {
                query = String.format("UPDATE %s SET State = '%s' WHERE VId = '%s'", vehicleType, operation, vehicle);
                stmt.executeUpdate(query);
            } else {
                return "This Vehicle does not exist";
            }

        } catch (SQLException e) {
            return e.toString();
        } catch (NullPointerException e) {
            return e.toString();
        }

        return null;
    }

    private String findType(String vehicle) {
        String vehicleType = searchType(vehicle, "Car");
        if (vehicleType != "Car") {
            vehicleType = searchType(vehicle, "Skate");
            if (vehicleType != "Skate") {
                vehicleType = searchType(vehicle, "Motorcycle");
                if (vehicleType != "Motorcycle") {
                    vehicleType = searchType(vehicle, "Bicycle");
                }
            }
        }

        return vehicleType;
    }

    private String searchType(String vehicle, String type) {
        try {
            Connection con = Database.connect();

            String query = String.format("SELECT * FROM %s WHERE VId = '%s'", type, vehicle);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(query);

            rs.beforeFirst();
            if (rs.next()) {
                return type;
            }
        } catch (SQLException e) {
            return e.toString();
        } catch (NullPointerException e) {
            return e.toString();
        }

        return "";
    }

    private String checkArguments(String vehicle, String operation,String fromDate, String untilDate) {
        if (!Date.isDateValid(fromDate) || !Date.isDateValid(untilDate))
            return "Invalid operation completion date given! Please check again.";
        return null;
    }

    private void verifyArguments(String vehicle, String operation, String fromDate, String untilDate) {
        assert (!vehicle.isBlank() && !operation.isBlank() && !fromDate.isBlank() && !untilDate.isBlank());
    }
}

public class VehicleManagement {
    private String[][] cars;
    private String[][] motorbikes;
    private String[][] skates;
    private String[][] bicycles;

    private final VehicleAccident vehicleAccident;
    private final VehicleDamage vehicleDamage;
    private final VehicleSupplement vehicleSupplement;
    private final VehicleRepair vehicleRepair;
    private final MoreOptions moreOptions;

    public VehicleManagement() {
        vehicleAccident = new VehicleAccident();
        vehicleDamage = new VehicleDamage();
        vehicleSupplement = new VehicleSupplement();
        vehicleRepair = new VehicleRepair();
        moreOptions = new MoreOptions();
    }

    public String vehicleAccident(String VID) {
        return this.vehicleAccident.vehicleAccident(VID);
    }

    public String vehicleDamage(String VID) {
        return this.vehicleDamage.vehicleDamage(VID);
    }

    public String supply(String vehicleType, String plate, String brand, String model, String color, String autonomy, String rentalCost, String insuranceCost, String carType, String passengersLimit) {
        return this.vehicleSupplement.supply(vehicleType, plate, brand, model, color, autonomy, rentalCost, insuranceCost, carType, passengersLimit);
    }

    public String repair(String vehicle, String operation, String fromDate, String untilDate) {
        return this.vehicleRepair.repair(vehicle, operation, fromDate, untilDate);
    }

    public String[] moreOptions(int queryOption, List<String> parameters) {
        return this.moreOptions.moreOptions(queryOption, parameters, this);
    }

    public String[][] getAllVehiclesInfo() {
        List<String[][]> data = new ArrayList<>();
        data.add(cars);
        data.add(motorbikes);
        data.add(skates);
        data.add(bicycles);

        String[][] temp = new String[data.get(0).length + data.get(1).length + data.get(2).length + data.get(3).length][];
        int i = 0;
        for (String[][] d : data) {
            for (int j = 0; j < d.length; j++) {
                temp[i++] = d[j];
            }
        }
        return temp;
    }

    public String[] getAllVehiclesIds() {
        List<String> vehicleIds = new ArrayList<>();
        try {
            ResultSet cars = Database.retrieveAllCars();
            ResultSet motorbikes = Database.retrieveAllMotorbikes();
            ResultSet skates = Database.retrieveAllSkates();
            ResultSet bicycles = Database.retrieveAllBicycles();
            while (cars.next()) {
                vehicleIds.add(cars.getString("VId"));
            }
            while (motorbikes.next()) {
                vehicleIds.add(motorbikes.getString("VId"));
            }
            while (skates.next()) {
                vehicleIds.add(skates.getString("VId"));
            }
            while (bicycles.next()) {
                vehicleIds.add(bicycles.getString("VId"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicleIds.toArray(new String[0]);
    }

    public String[] getAvailableVehiclesIds() {
        List<String> vehicleIds = new ArrayList<>();
        updateVehiclesInfo();
        for (int i = 1; i < cars.length; i++) {
            if (Objects.equals(cars[i][6], "Available")) {
                vehicleIds.add(cars[i][0]);
            }
        }
        for (int i = 1; i < motorbikes.length; i++) {
            if (Objects.equals(motorbikes[i][5], "Available")) {
                vehicleIds.add(motorbikes[i][0]);
            }
        }
        for (int i = 1; i < skates.length; i++) {
            if (Objects.equals(skates[i][5], "Available"))
                vehicleIds.add(skates[i][0]);
        }
        for (int i = 1; i < bicycles.length; i++) {
            if (Objects.equals(bicycles[i][5], "Available"))
                vehicleIds.add(bicycles[i][0]);
        }
        return vehicleIds.toArray(new String[0]);
    }

    public String[][] getVehicleInfo(String VId) {
        updateVehiclesInfo();
        String[][] vehicleInfo = new String[2][];
        boolean found = false;
        found = findVehicle(VId, vehicleInfo, false, cars);
        found = findVehicle(VId, vehicleInfo, found, motorbikes);
        found = findVehicle(VId, vehicleInfo, found, skates);
        found = findVehicle(VId, vehicleInfo, found, bicycles);
        if (!found)
            return null;
        return new String[][]{vehicleInfo[0], vehicleInfo[1]};
    }

    private boolean findVehicle(String VId, String[][] vehicleInfo, boolean found, String[][] vehiclesInfoArray) {
        if (!found) {
            for (int i = 1; i < vehiclesInfoArray.length; i++) {
                if (Objects.equals(VId, vehiclesInfoArray[i][0])) {
                    found = true;
                    vehicleInfo[0] = vehiclesInfoArray[0];
                    vehicleInfo[1] = vehiclesInfoArray[i];
                }
            }
        }
        return found;
    }

    public void updateVehiclesInfo() {
        try {
            cars = Utils.convertResultSetToArray(Database.retrieveAllCars(), new HashMap<>() {{
                put("VId", "string");
                put("Brand", "string");
                put("Model", "string");
                put("Color", "string");
                put("KmDriven", "string");
                put("RentalCost", "float");
                put("State", "string");
                put("InsuranceCost", "int");
                put("Type", "string");
                put("PassengerLimit", "string");
            }});
            motorbikes = Utils.convertResultSetToArray(Database.retrieveAllMotorbikes(), new HashMap<>() {{
                put("VId", "string");
                put("Brand", "string");
                put("Model", "string");
                put("Color", "string");
                put("KmDriven", "string");
                put("RentalCost", "float");
                put("State", "string");
                put("InsuranceCost", "int");
            }});
            skates = Utils.convertResultSetToArray(Database.retrieveAllSkates(), new HashMap<>() {{
                put("VId", "string");
                put("Brand", "string");
                put("Model", "string");
                put("Color", "string");
                put("KmDriven", "string");
                put("RentalCost", "float");
                put("State", "string");
                put("InsuranceCost", "int");
            }});
            bicycles = Utils.convertResultSetToArray(Database.retrieveAllBicycles(), new HashMap<>() {{
                put("VId", "string");
                put("Brand", "string");
                put("Model", "string");
                put("Color", "string");
                put("KmDriven", "string");
                put("RentalCost", "float");
                put("State", "string");
                put("InsuranceCost", "int");
            }});
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
