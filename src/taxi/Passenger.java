package taxi;
public class Passenger extends Person {
    private double balance;
    public Passenger(String id, String name, String phone, double balance) {
        super(id, name, phone);
        this.balance = balance;
    }
    public boolean deduct(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }
    public double getBalance() {
        return balance;
    }
    public boolean pay(Order order) {
        return order.pay();
    }
    public Order createOrder(String orderId, Location start, Location end, double distance) {
        return new Order(orderId, this, start, end, distance);
    }
    @Override
    public String getRole() {
        return "PASSENGER";
    }
    @Override
    public String toString() {
        return "乘客[" + name + ", 余额:" + String.format("%.2f", balance) + "]";
    }
}
