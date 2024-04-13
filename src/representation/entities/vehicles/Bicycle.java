package representation.entities.vehicles;

import representation.entities.Vehicle;
import representation.utils.VehicleStatus;

public class Bicycle extends Vehicle {
    private String bicycleNumber;
    public String getBicycleNumber() {
        return bicycleNumber;
    }

    public void setBicycleNumber(String bicycleNumber) {
        this.bicycleNumber = bicycleNumber;
    }

    public Bicycle(String bicycleNumber, String brand, String model, String color, float rentalCost, float insuranceCost, int kmsDriven, VehicleStatus status) {
        super(bicycleNumber, brand, model, color, rentalCost, insuranceCost, kmsDriven, status);
        this.bicycleNumber = bicycleNumber;
    }
}