package representation.entities.people;

import representation.other.DriverLicense;

public class Driver {
    private String DId;
    private PersonalInfo info;
    private DriverLicense license;

    public Driver(String _DId, String _TIN, String _FirstName, String _LastName, String _DLNumber, String _DID, String _ExpirationDay, String _ExpirationMonth, String _ExpirationYear, Boolean _CanDriveCar, Boolean _CanDriveMotorbike, String birthDay, String birthMonth, String birthYear) {
        verifyConstructorArguments(_DId, _TIN, _DLNumber, _DID, _ExpirationDay, _ExpirationMonth, _ExpirationYear, _CanDriveCar, _CanDriveMotorbike);

        this.DId = _DId;
        this.info = new PersonalInfo(_TIN, _FirstName, _LastName, birthDay, birthMonth, birthYear);
        this.license = new DriverLicense(_DLNumber, _DID, _ExpirationDay, _ExpirationMonth, _ExpirationYear, _CanDriveCar, _CanDriveMotorbike);
    }

    private void verifyConstructorArguments(String _DId, String _TIN, String _DLNumber, String _DID, String _ExpirationDay, String _ExpirationMonth, String _ExpirationYear, Boolean _CanDriveCar, Boolean _CanDriveMotorbike) {
        assert(!_DId.isBlank() && !_TIN.isBlank() && !_DLNumber.isBlank() && !_DID.isBlank() && !_ExpirationDay.isBlank() && !_ExpirationMonth.isBlank() && !_ExpirationYear.isBlank() && _CanDriveCar != null && _CanDriveMotorbike != null);
    }

    public String getDId() {
        return this.DId;
    }

    public PersonalInfo getPersonalInfo() {
        return this.info;
    }

    public DriverLicense getDriverLicense() {
        return this.license;
    }

    public void setDId(String _DId) {
        this.DId = _DId;
    }

    public void setPersonalInfo(PersonalInfo _info) {
        this.info = _info;
    }

    public void setDriverLicense(DriverLicense _license) {
        this.license = _license;
    }
}