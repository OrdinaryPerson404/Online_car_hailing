package taxi;

public class Driver extends Person {
    private boolean available;//是否可用
    private double todayIncome;
    private Vehicle vehicle;
    public Driver(String id, String name, String phone, Vehicle vehicle) {
        super(id, name, phone);
        this.vehicle = vehicle;
        this.available = true;
        this.todayIncome = 0.0;
    }
    public boolean acceptOrder(Order order) {
        if (!available) {
            System.out.println("[司机] " + name + " 当前不可接单（正在服务中）");
            return false;
        }
        boolean success = order.acceptBy(this);
        if (success) {
            available = false;
            System.out.println("[司机] " + name + " 已接单: " + order.getOrderId());
        }
        return success;
    }//接单的汇报
    public boolean startTrip(Order order) {
        if (!order.getDriver().equals(this)) {
            System.out.println("司机" + name + " 不是该订单的司机");
            return false;
        }
        boolean success = order.startTrip();
        if (success) {
            System.out.println("司机"+ name + " 开始行程: " + order.getOrderId());
        }
        return success;
    }//开始行程的汇报
    public boolean endTrip(Order order) {
        if (!order.getDriver().equals(this)) {
            System.out.println("司机" + name + " 不是该订单的司机，无法结束行程");
            return false;
        }
        boolean success = order.endTrip();
        if (success) {
            available = true;
            System.out.println("[司机] " + name + " 结束行程: " + order.getOrderId() + ", 费用:" + String.format("%.2f", order.getFare()));
        }
        return success;
    }//结束行程的汇报
    public void addIncome(double amount) {
        todayIncome += amount;
    }
    public boolean isAvailable() {
        return available;
    }
    public double getTodayIncome() {
        return todayIncome;
    }
    public Vehicle getVehicle() {
        return vehicle;
    }
    @Override
    public String getRole() {
        return "DRIVER";
    }
    @Override
    public boolean validate() {
        return super.validate() && vehicle != null;
    }
    @Override
    public String toString() {
        return "司机[" + name + ", 车辆: " + vehicle + "可用: " + (available ? "是" : "否") +"今日收入" + String.format("%.2f", todayIncome) + "]";
    }
}
