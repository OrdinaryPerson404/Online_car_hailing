package taxi;

public class Vehicle {
    private String plateNumber;
    private String model;
    public Vehicle(String plateNumber, String model) {
        this.plateNumber = plateNumber;
        this.model = model;
    }
    public String getPlateNumber() {
        return plateNumber;
    }
    public String getModel() {
        return model;
    }
    @Override
    public String toString() {
        return model + " (" + plateNumber + ")";
    }//提供演示
}
