package representation.utils;

public class Address {
    private String city;
    private String streetName;
    private int streetNumber;

    public Address(String streetName, String streetNumber, String city) {
        int streetNumberInt = Integer.parseInt(streetNumber);
        verifyConstructorArguments(city, streetName, streetNumber);
        this.city = city;
        this.streetName = streetName;
        this.streetNumber = streetNumberInt;
    }

    public String toString() {
        return this.streetName + " " + Integer.toString(this.streetNumber) + " " + this.city;
    }

    public static Address toAddress(String address) {
        if(!isAddressValid(address)) {
            return null;
        }

        String[] arr = address.split(" ", 3);
        return new Address(arr[0], arr[1], arr[2]);
    }

    public static boolean isAddressValid(String address) {
        String[] addressParts = address.split(" ");
        return addressParts.length == 3 && isCityValid(addressParts[2]) && isStreetNameValid(addressParts[0]) && isStreetNumberValid(addressParts[1]);
    }

    public static boolean isCityValid(String city) {
        return !city.isEmpty();
    }

    public static boolean isStreetNameValid(String month) {
        return !month.isEmpty();
    }

    public static boolean isStreetNumberValid(String city) {
        return !city.isEmpty() && Utils.isInt(city);
    }

    private void verifyConstructorArguments(String city, String streetName, String streetNumber) {
        assert(!city.isBlank() && !streetName.isBlank() && Utils.isInt(streetNumber));
    }

    public int getStreetNumber() {
        return this.streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetName() {
        return this.streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
