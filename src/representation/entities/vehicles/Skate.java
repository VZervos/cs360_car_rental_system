package representation.entities.vehicles;

import representation.entities.Vehicle;
import representation.utils.VehicleStatus;

public class Skate extends Vehicle {
    private String skateNumber;
    public String getSkateNumber() {
        return skateNumber;
    }

    public void setSkateNumber(String skateNumber) {
        this.skateNumber = skateNumber;
    }


    public Skate(String skateNumber, String brand, String model, String color, float rentalCost, float insuranceCost, int kmsDriven, VehicleStatus status) {
        super(skateNumber, brand, model, color, rentalCost, insuranceCost, kmsDriven, status);
        this.skateNumber = skateNumber;
    }
}
