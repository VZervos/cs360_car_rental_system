package representation.other;

public class CreditCard {
    private String cardNumber;
    private String CVV;
    private String expirationMonth;
    private String expirationYear;
    private String cardFirstName;
    private String cardLastName;

    private void verifyConstructorArguments(String cardNumber, String CVV, String expirationMonth, String expirationYear, String cardFirstName, String cardLastName) {
        assert (!cardNumber.isBlank() && !CVV.isBlank() && !expirationMonth.isBlank() && !expirationYear.isBlank() && !cardFirstName.isBlank() && !cardLastName.isBlank());
    }

    public CreditCard(String cardNumber, String CVV, String expirationMonth, String expirationYear, String cardFirstName, String cardLastName) {
        verifyConstructorArguments(cardNumber, CVV, expirationMonth, expirationYear, cardFirstName, cardLastName);
        this.cardNumber = cardNumber;
        this.CVV = CVV;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.cardFirstName = cardFirstName;
        this.cardLastName = cardLastName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCVV() {
        return CVV;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
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

    public String getCardFirstName() {
        return cardFirstName;
    }

    public void setCardFirstName(String cardFirstName) {
        this.cardFirstName = cardFirstName;
    }

    public String getCardLastName() {
        return cardLastName;
    }

    public void setCardLastName(String cardLastName) {
        this.cardLastName = cardLastName;
    }
}