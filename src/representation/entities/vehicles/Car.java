package representation.entities.vehicles;

import representation.entities.Vehicle;
import representation.utils.VehicleStatus;

public class Car extends Vehicle {
    private String plateNumber;
    private String type;
    private int passengersLimit;
    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPassengersLimit() {
        return passengersLimit;
    }

    public void setPassengersLimit(int passengersLimit) {
        this.passengersLimit = passengersLimit;
    }

    private void verifyConstructorArguments(String type, int passengersLimit) {
        assert (!type.isBlank() && passengersLimit > 0);
    }

    public Car(String plateNumber, String brand, String model, String color, float rentalCost, float insuranceCost, int kmsDriven, VehicleStatus status, String type, int passengersLimit) {
        super(plateNumber, brand, model, color, rentalCost, insuranceCost, kmsDriven, status);
        verifyConstructorArguments(type, passengersLimit);
        this.plateNumber = plateNumber;
        this.type = type;
        this.passengersLimit = passengersLimit;
    }
}