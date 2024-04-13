package representation.entities.people;

import representation.other.CreditCard;

public class Customer {
    private String CId;
    private PersonalInfo info;
	private String mail;
	private CreditCard card;
	private String StreetName, StreetNumber, City, TK;

    private String password;
    public Customer(String _CId, String _TIN, String _FirstName, String _LastName, String _BirthDay, String _BirthMonth, String _BirthYear, String _mail, String _CardNumber, String _CVV, String _ExpirationMonth, String _ExpirationYear, String _CardFirstName, String _CardLastName, String _StreetName, String _StreetNumber, String _City, String _TK, String _password) {
        verifyConstructorArguments(_CId, _TIN, _mail, _CardNumber, _CVV, _ExpirationMonth, _ExpirationYear, _CardFirstName, _CardLastName, _StreetName, _StreetNumber, _City, _TK, _password);
        this.CId = _CId;
        this.info = new PersonalInfo(_TIN, _FirstName, _LastName, _BirthDay, _BirthMonth, _BirthYear);
        this.mail = _mail;
        this.card = new CreditCard(_CardNumber, _CVV, _ExpirationMonth, _ExpirationYear, _CardFirstName, _CardLastName);
        this.StreetName = _StreetName;
        this.StreetNumber = _StreetNumber;
        this.City = _City;
        this.TK = _TK;
        this.password = _password;
    }

    private void verifyConstructorArguments(String _CId, String _TIN, String _mail, String _CardNumber, String _CVV, String _ExpirationMonth, String _ExpirationYear, String _CardFirstName, String _CardLastName, String _StreetName, String _StreetNumber, String _City, String _TK, String _password) {
        assert(!_CId.isBlank() && !_TIN.isBlank() && !_mail.isBlank() && !_CardNumber.isBlank() && !_CVV.isBlank() && !_ExpirationMonth.isBlank() && !_ExpirationYear.isBlank() && !_CardFirstName.isBlank() && !_CardLastName.isBlank() && !_StreetName.isBlank() && !_StreetNumber.isBlank() && !_City.isBlank() && !_TK.isBlank() && !_password.isBlank());
    }

    public String getCId() {
        return this.CId;
    } 

    public PersonalInfo getPersonalInfo() {
        return this.info;
    }

    public String getMail() {
        return this.mail;
    }

    public CreditCard getCreditCard() {
        return this.card;
    }

    public String getStreetName() {
        return this.StreetName;
    }

    public String getStreetNumber() {
        return this.StreetNumber;
    }

    public String getCity() {
        return this.City;
    }

    public String getTK() {
        return this.TK;
    }

    public String getPassword() {return this.password; }

    public void setCId(String _CId) {
        this.CId = _CId;
    } 

    public void setPersonalInfo(PersonalInfo _info) {
        this.info = _info;
    }

    public void setMail(String _mail) {
        this.mail = _mail;
    }

    public void setCreditCard(CreditCard _card ) {
        this.card = _card;
    }

    public void setStreetName(String _StreetName) {
        this.StreetName = _StreetName;
    }

    public void setStreetNumber(String _StreetNumber) {
        this.StreetNumber = _StreetNumber;
    }

    public void setCity(String _City) {
        this.City = _City;
    }

    public void setTK(String _TK) {
        this.TK = _TK;
    }

    public void setPassword(String _password) {this.password = _password; }
}