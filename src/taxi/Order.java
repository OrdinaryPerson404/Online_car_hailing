package taxi;

public class Order implements Validatable {
    private String orderId;//订单编号
    private Passenger passenger;
    private Driver driver;
    private Location start;
    private Location end;
    private double distance;
    private double fare;
    private OrderStatus status;
    public Order(String orderId, Passenger passenger, Location start, Location end, double distance) {
        this.orderId = orderId;
        this.passenger = passenger;
        this.start = start;
        this.end = end;
        this.distance = distance;
        this.fare = 0.0;
        this.status = OrderStatus.CREATED;
        this.driver = null;
    }
    public boolean acceptBy(Driver driver) {
        if (status != OrderStatus.CREATED) {
            System.out.println( "当前状态为 " + status + "，无法接单");
            return false;
        }
        this.driver = driver;
        this.status = OrderStatus.ACCEPTED;
        System.out.println(" 状态变更: CREATED -> ACCEPTED");
        return true;
    }
    public boolean startTrip() {
        if (status != OrderStatus.ACCEPTED) {
            System.out.println(" 当前状态为 " + status + "，无法开始行程");
            return false;
        }
        this.status = OrderStatus.ON_TRIP;
        System.out.println(" 状态变更: ACCEPTED -> ON_TRIP");
        return true;
    }
    public boolean endTrip() {
        if (status != OrderStatus.ON_TRIP) {
            System.out.println( " 当前状态为 " + status + "，无法结束行程");
            return false;
        }
        calculateFare();
        this.status = OrderStatus.ARRIVED;
        System.out.println(" 状态变更: ON_TRIP -> ARRIVED, 费用: ¥" + String.format("%.2f", fare));
        return true;
    }
    public boolean pay() {
        if (status != OrderStatus.ARRIVED) {
            System.out.println("[订单] " + orderId + " 当前状态为 " + status + "，无法支付");
            return false;
        }
        
        if (!passenger.deduct(fare)) {
            System.out.println("[订单] " + orderId + " 支付失败: 余额不足 (需要 ¥" + String.format("%.2f", fare) + ", 当前余额 ¥" + String.format("%.2f", passenger.getBalance()) + ")");
            return false;
        }
        
        this.status = OrderStatus.PAID;
        driver.addIncome(fare);
        System.out.println("[订单] " + orderId + " 状态变更: ARRIVED -> PAID, 支付成功");
        return true;
    }
    public boolean cancel() {
        if (status != OrderStatus.CREATED && status != OrderStatus.ACCEPTED) {
            System.out.println("[订单] " + orderId + " 当前状态为 " + status + "，无法取消");
            return false;
        }
        this.status = OrderStatus.CANCELLED;
        System.out.println("[订单] " + orderId + " 状态变更: " + "ACCEPTED" + " -> CANCELLED");
        return true;
    }
    public boolean timeout() {
        if (status != OrderStatus.CREATED) {
            System.out.println("[订单] " + orderId + " 当前状态为 " + status + "，无法标记超时");
            return false;
        }
        this.status = OrderStatus.TIMEOUT;
        System.out.println("[订单] " + orderId + " 状态变更: CREATED -> TIMEOUT");
        return true;
    }
    public void calculateFare() {
        this.fare = FareCalculator.calculate(distance);
    }
    @Override
    public boolean validate() {
        return start != null && end != null 
            && distance > 0 
            && start.validate() 
            && end.validate();
    }
    public String getOrderId() {
        return orderId;
    }
    public Passenger getPassenger() {
        return passenger;
    }
    public Driver getDriver() {
        return driver;
    }
    public Location getStart() {
        return start;
    }
    public Location getEnd() {
        return end;
    }
    public double getDistance() {
        return distance;
    }
    public double getFare() {
        return fare;
    }
    public OrderStatus getStatus() {
        return status;
    }
    @Override
    public String toString() {
        return " 状态:" + status + ", 乘客: " + passenger.getName()
            + ", 司机:" + (driver != null ? driver.getName() : "无")
            + ", 起点:" + start.getAddress()
            + ", 终点:" + end.getAddress()
            + ", 里程: " + distance + "km"
            + ", 费用:" + String.format("%.2f", fare) + "]";
    }
}
