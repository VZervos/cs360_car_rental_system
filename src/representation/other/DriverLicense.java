package representation.other;

public class DriverLicense {
    private String DLNumber;
    private String DID;
    private String expirationDay;
    private String expirationMonth;
    private String expirationYear;
    private Boolean canDriveCar;
    private Boolean canDriveMotorbike;

    private void verifyConstructorArguments(String DLNumber, String DID, String expirationDay, String expirationMonth, String expirationYear, Boolean canDriveCar, Boolean canDriveMotorbike) {
        assert (!DLNumber.isBlank() && !DID.isBlank() && !expirationDay.isBlank() && !expirationMonth.isBlank() && !expirationYear.isBlank() && canDriveCar != null && canDriveMotorbike != null);
    }

    public DriverLicense(String DLNumber, String DID, String expirationDay, String expirationMonth, String expirationYear, Boolean canDriveCar, Boolean canDriveMotorbike) {
        verifyConstructorArguments(DLNumber, DID, expirationDay, expirationMonth, expirationYear, canDriveCar, canDriveMotorbike);
        this.DLNumber = DLNumber;
        this.DID = DID;
        this.expirationDay = expirationDay;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.canDriveCar = canDriveCar;
        this.canDriveMotorbike = canDriveMotorbike;
    }

    public String getDLNumber() {
        return DLNumber;
    }

    public void setDLNumber(String DLNumber) {
        this.DLNumber = DLNumber;
    }

    public String getDID() {
        return DID;
    }

    public void setDID(String DID) {
        this.DID = DID;
    }

    public String getExpirationDay() {
        return expirationDay;
    }

    public void setExpirationDay(String expirationDay) {
        this.expirationDay = expirationDay;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }

    public Boolean getCanDriveCar() {
        return canDriveCar;
    }

    public void setCanDriveCar(Boolean canDriveCar) {
        this.canDriveCar = canDriveCar;
    }

    public Boolean getCanDriveMotorbike() {
        return canDriveMotorbike;
    }

    public void setCanDriveMotorbike(Boolean canDriveMotorbike) {
        this.canDriveMotorbike = canDriveMotorbike;
    }
}