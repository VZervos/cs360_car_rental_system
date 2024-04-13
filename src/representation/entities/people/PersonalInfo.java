package representation.entities.people;

import representation.utils.Date;

public class PersonalInfo {
    private String TIN;
    private String firstName;
    private String lastName;
    private String birthDay, birthMonth, birthYear;

    private void verifyConstructorArguments(String TIN, String firstName, String lastName, String birthDay, String birthMonth, String birthYear) {
        assert (!TIN.isBlank() && !firstName.isBlank() && !lastName.isBlank() && Date.isDateValid(birthDay + birthMonth + birthYear));
    }

    public PersonalInfo(String TIN, String firstName, String lastName, String birthDay, String birthMonth, String birthYear) {
        verifyConstructorArguments(TIN, firstName, lastName, birthDay, birthMonth, birthYear);
        this.TIN = TIN;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.birthMonth = birthMonth;
        this.birthYear = birthYear;
    }

    public String getTIN() {
        return TIN;
    }

    public void setTIN(String TIN) {
        this.TIN = TIN;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(String birthMonth) {
        this.birthMonth = birthMonth;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }
}