package representation.entities;

import representation.utils.VehicleStatus;

public abstract class Vehicle {
    private String VID;
    private String brand;
    private String model;
    private String color;
    private float rentalCost;
    private float insuranceCost;
    private int kmsDriven;
    private VehicleStatus status;

    protected Vehicle(String VID, String brand, String model, String color, float rentalCost, float insuranceCost, int kmsDriven, VehicleStatus status) {
        verifyConstructorArguments(VID, brand, model, color, rentalCost, insuranceCost, kmsDriven, status);
        this.VID = VID;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.rentalCost = rentalCost;
        this.insuranceCost = insuranceCost;
        this.kmsDriven = kmsDriven;
        this.status = status;
    }

    private void verifyConstructorArguments(String VID, String brand, String model, String color, float rentalCost, float insuranceCost, int kmsDriven, VehicleStatus status) {
        assert (!VID.isBlank() && !brand.isBlank() && !model.isBlank() && !color.isBlank() && rentalCost >= 0 && insuranceCost >= 0 && kmsDriven >= 0 && status != null);
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public float getRentalCost() {
        return rentalCost;
    }

    public float getInsuranceCost() {
        return insuranceCost;
    }

    public int getKmsDriven() {
        return kmsDriven;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public String getVID() {
        return VID;
    }

    public void setVID(String VID) {
        this.VID = VID;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setRentalCost(float rentalCost) {
        this.rentalCost = rentalCost;
    }

    public void setInsuranceCost(float insuranceCost) {
        this.insuranceCost = insuranceCost;
    }

    public void setKmsDriven(int kmsDriven) {
        this.kmsDriven = kmsDriven;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

}