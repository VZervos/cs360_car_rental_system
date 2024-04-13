package representation.entities.vehicles;

import representation.entities.Vehicle;
import representation.utils.VehicleStatus;

public class Motorbike extends Vehicle {
    private String plateNumber;
    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Motorbike(String plateNumber, String brand, String model, String color, float rentalCost, float insuranceCost, int kmsDriven, VehicleStatus status) {
        super(plateNumber, brand, model, color, rentalCost, insuranceCost, kmsDriven, status);
        this.plateNumber = plateNumber;
    }
}