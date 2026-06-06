package taxi;

public class FareCalculator {
    private static final double BASE_FARE = 10.0;//基本费用
    private static final double BASE_DISTANCE = 3.0;//基本距离
    private static final double EXTRA_RATE = 5.0;//额外费用
    public static double calculate(double distance) {
        if (distance <= BASE_DISTANCE) {
            return BASE_FARE;
        } else {
            double extra = Math.ceil(distance - BASE_DISTANCE);
            return BASE_FARE + extra * EXTRA_RATE;
        }
    }
}
