package taxi;

public class Location implements Validatable {
    private String address;
    public Location(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }
    @Override
    public boolean validate() {
        return address != null && !address.trim().isEmpty();
    }
    @Override
    public String toString() {
        return address;
    }
}
