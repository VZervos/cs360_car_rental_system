package control.services;

import control.Control;
import database.Database;
import representation.entities.people.Customer;
import representation.utils.Address;
import representation.utils.Date;
import representation.utils.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class UserLogin {

    public Boolean login(String username, String password) {
        if (!checkArguments(username, password))
            return false;

        //we have to do the length checkiing

        Connection con = Database.connection();
        Boolean output = false;

        try {

            String query = String.format("SELECT * FROM Customer c, CreditCard cc, PersonalInfo p WHERE c.CId = '%s' AND c.password = '%s' AND cc.CardNumber = c.CardNumber AND cc.CVV = c.CVV AND p.TIN = c.TIN", username, password);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs_customer = stmt.executeQuery(query);

            rs_customer.beforeFirst();
            if (rs_customer.next()) {
                Control.setCurrentCustomer(new Customer(rs_customer.getString("CId"), rs_customer.getString("TIN"), rs_customer.getString("FirstName"), rs_customer.getString("LastName"), rs_customer.getString("BirthDay"), rs_customer.getString("BirthMonth"), rs_customer.getString("BirthYear"), rs_customer.getString("mail"), rs_customer.getString("CardNumber"), rs_customer.getString("CVV"), rs_customer.getString("ExpirationMonth"), rs_customer.getString("ExpirationYear"), rs_customer.getString("CardFirstName"), rs_customer.getString("CardLastName"), rs_customer.getString("City"), rs_customer.getString("StreetName"), rs_customer.getString("StreetNumber"), rs_customer.getString("TK"), rs_customer.getString("password") ));
                output = true;
                return true;
            } else {
                output = false;
                return false;
            }

        } catch (SQLException e) {
            System.err.println(e.toString());
        } catch (NullPointerException e) {
            System.err.println(e.toString());
        }

        return output;
    }

    private boolean checkArguments(String username, String password) {
        return !username.isBlank() && !password.isBlank();
    }
}

class CreditCardRegistration {
    public CreditCardRegistration() { }

    private void verifyConstructorArguments(String cardNumber, String CVV, String expirationMonth, String expirationYear, String cardFirstName, String cardLastName) {
        assert (!cardNumber.isBlank() && !CVV.isBlank() && !expirationMonth.isBlank() && !expirationYear.isBlank() && !cardFirstName.isBlank() && !cardLastName.isBlank());
    }

    public String register(String cardNumber, String CVV, String expirationMonth, String expirationYear, String cardFirstName, String cardLastName) {
        verifyConstructorArguments(cardNumber, CVV, expirationMonth, expirationYear, cardFirstName, cardLastName);
        //we have to do the length checking
        Connection con = Database.connection();
        String output = null;
        try {

            String query = String.format("SELECT * FROM CreditCard WHERE cardNumber = '%s' AND CVV = '%s'", cardNumber, CVV);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(query);

            rs.beforeFirst();
            if (rs.next()) {
                query = String.format("UPDATE CreditCard SET ExpirationMonth = '%s', ExpirationYear = '%s', CardFirstName = '%s', CardLastName = '%s' WHERE CardNumber = '%s' AND CVV = '%s'", expirationMonth, expirationYear, cardFirstName, cardLastName, cardNumber, CVV);
            } else {
                query = String.format("INSERT INTO CreditCard (CardNumber, CVV, ExpirationMonth, ExpirationYear, CardFirstName, CardLastName) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')", cardNumber, CVV, expirationMonth, expirationYear, cardFirstName, cardLastName);
            }

            stmt.executeUpdate(query);
        } catch (SQLException e) {
            output = e.toString();
            return e.toString();
        } catch (NullPointerException e) {
            output = e.toString();
            return e.toString();
        }

        return output;
    }
}

class PersonalInfoRegistration {
    public PersonalInfoRegistration() {
    }

    private void verifyConstructorArguments(String TIN, String firstName, String lastName, String birthDay, String birthMonth, String birthYear) {
        assert (!TIN.isBlank() && !firstName.isBlank() && !lastName.isBlank() && !birthDay.isBlank() && !birthMonth.isBlank() && !birthYear.isBlank());
    }

    public String register(String TIN, String firstName, String lastName, String birthDay, String birthMonth, String birthYear) {
        verifyConstructorArguments(TIN, firstName, lastName, birthDay, birthMonth, birthYear);

        //we have to do the length checking

        Connection con = Database.connection();
        String output = null;
        try {

            String query = String.format("SELECT TIN FROM PersonalInfo WHERE TIN = '%s'", TIN);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(query);

            rs.beforeFirst();
            if (rs.next()) {
                query = String.format("UPDATE PersonalInfo SET FirstName = '%s', LastName = '%s', BirthDay = '%s', BirthMonth = '%s', BirthYear = '%s' WHERE TIN = '%s'", firstName, lastName, birthDay, birthMonth, birthYear, TIN);
            } else {
                query = String.format("INSERT INTO PersonalInfo (TIN, FirstName, LastName, BirthDay, BirthMonth, BirthYear) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')", TIN, firstName, lastName, birthDay, birthMonth, birthYear);
            }

            stmt.executeUpdate(query);
        } catch (SQLException e) {
            output = e.toString();
            return e.toString();
        } catch (NullPointerException e) {
            output = e.toString();
            return e.toString();
        }

        return output;
    }
}

class DriverLicenseRegistration {
    public DriverLicenseRegistration() { }

    private void verifyConstructorArguments(String DLNumber, String TIN, String expirationDate) {
        assert (!DLNumber.isBlank() && !TIN.isBlank() && !expirationDate.isBlank());
    }

    public String register(String DLNumber, String DId, String expirationDate, boolean CanDriveCar, boolean CanDriveMotorbike) {
        verifyConstructorArguments(DLNumber, DId, expirationDate);

        //we have to do the length checking

        Date date = Date.toDate(expirationDate);

        Connection con = Database.connection();
        String output = null;
        try {

            String query = String.format("SELECT DLNumber FROM DriverLicense WHERE DLNumber = '%s'", DLNumber);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(query);

            rs.beforeFirst();
            if (rs.next()) {
                query = String.format("UPDATE DriverLicense SET ExpirationDay = '%s', ExpirationMonth = '%s', ExpirationYear = '%s', CanDriveCar = %s, CanDriveMotorbike = %s WHERE DLNumber = '%s' AND DId = '%s'", date.getDay(), date.getMonth(), date.getYear(), CanDriveCar, CanDriveMotorbike, DLNumber, DId);
            } else {
                query = String.format("INSERT INTO DriverLicense (DLNumber, DId, ExpirationDay, ExpirationMonth, ExpirationYear, CanDriveCar, CanDriveMotorbike) VALUES ('%s', '%s', '%s', '%s', '%s', %s, %s)", DLNumber, DId, date.getDay(), date.getMonth(), date.getYear(), CanDriveCar, CanDriveMotorbike);
            }

            stmt.executeUpdate(query);
        } catch (SQLException e) {
            return e.toString();
        } catch (NullPointerException e) {
            return e.toString();
        }

        return output;
    }
}

class CustomerRegistration {
    public CustomerRegistration() {}

    private void verifyArguments(String TIN, String firstname, String lastname, String email, String birthday, String address, String postalCode, String username, String password, String cardNumber, String CVV, String expirationMonth, String expirationYear, String cardFirstName, String cardLastName) {
        assert (!TIN.isBlank() && !firstname.isBlank() && !lastname.isBlank() && !email.isBlank() && !birthday.isBlank() && !address.isBlank() && !postalCode.isBlank() && !username.isBlank() && !password.isBlank() && !cardNumber.isBlank() && !CVV.isBlank() && !expirationMonth.isBlank() && !expirationYear.isBlank() && !cardFirstName.isBlank() && !cardLastName.isBlank());
    }

    public String register(String TIN, String firstname, String lastname, String email, String birthday, String address, String postalCode, String username, String password, String cardNumber, String CVV, String expirationMonth, String expirationYear, String cardFirstName, String cardLastName) {
        verifyArguments(TIN, firstname, lastname, email, birthday, address, postalCode, username, password, cardNumber, CVV, expirationMonth, expirationYear, cardFirstName, cardLastName);
        String result = checkArguments(username, email, birthday, postalCode, cardNumber, CVV, address, expirationMonth, expirationYear);
        if (result != null)
            return result;

        //we have to do the length checking

        Date date = Date.toDate(birthday);
        Address address1 = Address.toAddress(address);

        PersonalInfoRegistration pir = new PersonalInfoRegistration();
        result = pir.register(TIN, firstname, lastname, Integer.toString(date.getDay()), Integer.toString(date.getMonth()), Integer.toString(date.getYear()) );
        if(result != null) {
            return result;
        }

        CreditCardRegistration cc = new CreditCardRegistration();
        result = cc.register(cardNumber, CVV, expirationMonth, expirationYear, cardFirstName, cardLastName);
        if(result != null) {
            return result;
        }

        Connection con = Database.connection();
        String output = null;
        try {

            String query;
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            if (TINAppears(TIN) == 1) {
                query = String.format("UPDATE Customer SET mail = '%s', CardNumber = '%s', CVV = '%s', City = '%s', StreetName = '%s', StreetNumber = '%s', TK = '%s', password = '%s' WHERE TIN = '%s' AND CId = '%s'", email, cardNumber, CVV, address1.getCity(), address1.getStreetName(), address1.getStreetNumber(), postalCode, password, TIN, username);
            }
            else {
                query = String.format("INSERT INTO Customer (CId, TIN, mail, CardNumber, CVV, City, StreetName, StreetNumber, TK, password) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", username, TIN, email, cardNumber, CVV, address1.getCity(), address1.getStreetName(), Integer.toString(address1.getStreetNumber()), postalCode, password);
            }

            stmt.executeUpdate(query);
            assert(isTINUnique(TIN));
            assert(isCIdUnique(username));
        } catch (SQLException e) {
            output = e.toString();
            return e.toString();
        } catch (NullPointerException e) {
            output = e.toString();
            return e.toString();
        }

        return output;
    }

    private String checkArguments(String username, String email, String birthday, String postalCode, String cardNumber, String cvv, String address, String expirationMonth, String expirationYear) {
        if (CIdAppears(username) != 0)
             return "The username already exists!";
        else if (!email.contains("@") || !Date.isDateValid(birthday) || !Utils.isInt(postalCode))
            return "Invalid personal information given! Please check again.";
        else if (!Utils.isInt(cardNumber) || !Utils.isInt(cvv) || !Date.isMonthValid(expirationMonth) || !Date.isYearValid(expirationYear))
            return "Invalid card information given! Please check again.";
        else if (!Address.isAddressValid(address))
            return "Invalid street information given! Please check again.";
        else
            return null;
    }

    private boolean isTINUnique(String TIN) {
        return TINAppears(TIN) == 1;
    }

    private boolean isCIdUnique(String TIN) {
        return CIdAppears(TIN) == 1;
    }

    private int TINAppears(String TIN) {
        int count = 0;
        Connection con = Database.connection();
        try {

            String query = String.format("SELECT TIN FROM Customer WHERE TIN = '%s'", TIN);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(query);

            rs.beforeFirst();
            while(rs.next()) {
                count++;
            }

        } catch (SQLException e) {
            System.err.println(e.toString());
        } catch (NullPointerException e) {
            System.err.println(e.toString());
        }

        return count;
    }

    private int CIdAppears(String TIN) {
        int count = 0;
        Connection con = Database.connection();
        try {
             con = Database.connect();

            String query = String.format("SELECT CId FROM Customer WHERE CId = '%s'", TIN);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(query);

            rs.beforeFirst();
            while(rs.next()) {
                count++;
            }

        } catch (SQLException e) {
            System.err.println(e.toString());
        } catch (NullPointerException e) {
            System.err.println(e.toString());
        }

        return count;
    }

}

class DriverRegistration {
    private static final int MAX_ID_LENGTH = 5;
    private static int idCounter;

    public DriverRegistration() {
        idCounter = 1;

        Connection con = Database.connection();
        try {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query = String.format("SELECT DId FROM Driver");
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

    private void verifyArguments(String TIN, String firstname, String lastname, String birthday) {
        assert (!TIN.isBlank() && !firstname.isBlank() && !lastname.isBlank() && !birthday.isBlank());
    }

    public String register(String TIN, String firstname, String lastname, String birthday, boolean License, String DLNumber, String expirationDate, boolean CanDriveCar, boolean CanDriveMotorbike) {
        verifyArguments(TIN, firstname, lastname, birthday);
        String result = checkArguments(birthday);
        if (result != null)
            return result;

        //we have to do the length checking

        Date date = Date.toDate(birthday);

        PersonalInfoRegistration pir = new PersonalInfoRegistration();
        result = pir.register(TIN, firstname, lastname, Integer.toString(date.getDay()), Integer.toString(date.getMonth()), Integer.toString(date.getYear()));
        if (result != null) {
            return result;
        }

        Connection con = Database.connection();
        String output = null;
        String DId;
        try {

            String query;
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            if (TINAppears(TIN) == 0) {
                DId = generateID();
                query = String.format("INSERT INTO Driver (DId, TIN) VALUES ('%s', '%s')", DId, TIN);
                stmt.executeUpdate(query);
            }
            else {
                DId = findDId(TIN);
            }

            if(License) {
                DriverLicenseRegistration dlr = new DriverLicenseRegistration();
                result = dlr.register(DLNumber, DId, expirationDate, CanDriveCar, CanDriveMotorbike);
                if (result != null) {
                    return result;
                }
            }
        } catch (SQLException e) {
            output = e.toString();
            return e.toString();
        } catch (NullPointerException e) {
            output = e.toString();
            return e.toString();
        }

        return output;
    }


    private String checkArguments(String birthday) {
        if (!Date.isDateValid(birthday))
            return "Invalid personal information given! Please check again.";
        else
            return null;
    }

    private int TINAppears(String TIN) {
        int count = 0;
        Connection con = Database.connection();
        try {

            String query = String.format("SELECT TIN FROM Driver WHERE TIN = '%s'", TIN);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(query);

            rs.beforeFirst();
            while (rs.next()) {
                count++;
            }

        } catch (SQLException e) {
            System.err.println(e.toString());
        } catch (NullPointerException e) {
            System.err.println(e.toString());
        }
        return count;
    }

    private String findDId(String TIN) {
        Connection con = Database.connection();
        String output = null;
        try {

            String query = String.format("SELECT DId FROM Driver WHERE TIN = '%s'", TIN);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(query);

            rs.beforeFirst();
            if (rs.next()) {
                output = rs.getString("DId");
                return output;
            }

            assert(false);
        } catch (SQLException e) {
            output = e.toString();
            return e.toString();
        } catch (NullPointerException e) {
            output = e.toString();
            return e.toString();
        }

        return output;
    }
}

public class UserManagement {
    private final CustomerRegistration CustomerRegistration;
    private final DriverRegistration DriverRegistration;
    private final UserLogin userLogin;

    public UserManagement() {
        CustomerRegistration = new CustomerRegistration();
        DriverRegistration = new DriverRegistration();
        userLogin = new UserLogin();
    }

    public String registerCustomer(String TIN, String firstname, String lastname, String email, String birthday, String address, String postalCode, String username, String password, String cardNumber, String CVV, String expirationMonth, String expirationYear, String cardFirstName, String cardLastName) {
        return this.CustomerRegistration.register(TIN, firstname, lastname, email, birthday, address, postalCode, username, password, cardNumber, CVV, expirationMonth, expirationYear, cardFirstName, cardLastName);
    }

    public String registerDriver(String TIN, String firstname, String lastname, String birthday, boolean License, String DLNumber, String expirationDate, boolean CanDriveCar, boolean CanDriveMotorbike) {
        return this.DriverRegistration.register(TIN, firstname, lastname, birthday, License, DLNumber, expirationDate, CanDriveCar, CanDriveMotorbike);
    }

    public Boolean login(String username, String password) {
        return this.userLogin.login(username, password);
    }

    public Boolean hasRentedCars() {
        String CId = Control.getCurrentCustomer().getCId();

        boolean output = false;
        Connection con = Database.connection();
        try {

            String query = String.format("SELECT * FROM Rent WHERE CId = '%s' AND Returned = false", CId);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(query);

            rs.beforeFirst();
            if (rs.next()) {
                output = true;
                return true;
            }
            else {
                output = false;
                return false;
            }

        } catch (SQLException e) {
            System.err.println(e.toString());
        } catch (NullPointerException e) {
            System.err.println(e.toString());
        }

        return output;
    }
}
