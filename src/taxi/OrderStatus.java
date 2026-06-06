package taxi;

public enum OrderStatus {
    CREATED,// 创建
    ACCEPTED,// 已接受
    ON_TRIP,// 行程中
    ARRIVED,// 已到达
    PAID,// 已支付
    CANCELLED,// 已取消
    TIMEOUT// 超时
}
