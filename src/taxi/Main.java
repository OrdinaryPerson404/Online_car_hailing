package taxi;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class Main extends Application {

    private Passenger passenger1;
    private Driver driver1;
    private Order currentOrder;
    private TextArea logArea;
    
    private Button acceptBtn;
    private Button startBtn;
    private Button endBtn;
    private Button payBtn;

    @Override
    public void start(Stage primaryStage) {
        initializeData();
        
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        
        Label titleLabel = new Label("🚗 网约车订单管理系统");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        VBox infoBox = createInfoPanel();
        
        HBox buttonBox = createActionButtons();
        
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(200);
        logArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        logArea.appendText("系统初始化完成...\n");
        logArea.appendText("乘客 " + passenger1.getName() + " 已创建订单\n");
        logArea.appendText("等待司机接单...\n\n");
        
        root.getChildren().addAll(titleLabel, infoBox, buttonBox, new Label("操作日志:"), logArea);
        
        Scene scene = new Scene(root, 700, 650);
        primaryStage.setTitle("网约车系统 - FadeTransition 交互演示");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void initializeData() {
        passenger1 = new Passenger("1", "张三", "130", 100.0);
        Vehicle vehicle1 = new Vehicle("京A88888", "丰田卡罗拉");
        driver1 = new Driver("D001", "王师傅", "13900139001", vehicle1);
        
        Location start = new Location("我家");
        Location end = new Location("NCHU");
        currentOrder = passenger1.createOrder("ORD001", start, end, 8.5);
    }
    
    private VBox createInfoPanel() {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 15; -fx-background-radius: 10;");
        
        Label orderLabel = new Label("订单信息: " + currentOrder.getOrderId());
        orderLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        Label statusLabel = new Label("当前状态: " + currentOrder.getStatus());
        statusLabel.setId("statusLabel");
        statusLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #e74c3c;");
        
        Label detailLabel = new Label(
            String.format("起点: %s → 终点: %s | 里程: %.1fkm | 费用: ¥%.2f", 
                currentOrder.getStart().getAddress(),
                currentOrder.getEnd().getAddress(),
                currentOrder.getDistance(),
                currentOrder.getFare())
        );
        detailLabel.setStyle("-fx-font-size: 12px;");
        
        box.getChildren().addAll(orderLabel, statusLabel, detailLabel);
        return box;
    }
    
    private HBox createActionButtons() {
        acceptBtn = createAnimatedButton("👨‍✈️ 司机接单", "#3498db");
        startBtn = createAnimatedButton("🚀 开始行程", "#f39c12");
        endBtn = createAnimatedButton("🏁 结束行程", "#9b59b6");
        payBtn = createAnimatedButton("💰 乘客支付", "#27ae60");
        
        acceptBtn.setOnAction(e -> handleAccept());
        startBtn.setOnAction(e -> handleStartTrip());
        endBtn.setOnAction(e -> handleEndTrip());
        payBtn.setOnAction(e -> handlePayment());
        
        updateButtonStates();
        
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(acceptBtn, startBtn, endBtn, payBtn);
        return box;
    }
    
    private Button createAnimatedButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle(String.format(
            "-fx-font-size: 13px; -fx-padding: 12 20; " +
            "-fx-background-color: %s; -fx-text-fill: white; " +
            "-fx-background-radius: 8; -fx-cursor: hand;",
            color
        ));
        
        // 效果 A: 鼠标悬停时淡出（幽灵按钮效果）
        btn.setOnMouseEntered(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(200), btn);
            ft.setToValue(0.6);
            ft.play();
        });
        
        btn.setOnMouseExited(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(200), btn);
            ft.setToValue(1.0);
            ft.play();
        });
        
        return btn;
    }
    
    private void handleAccept() {
        logArea.appendText("\n>>> 司机尝试接单...\n");
        boolean success = driver1.acceptOrder(currentOrder);
        
        if (success) {
            // 效果 B: 成功时按钮闪烁庆祝
            triggerSuccessAnimation(acceptBtn);
            logArea.appendText("✓ 接单成功！司机: " + driver1.getName() + "\n");
        } else {
            logArea.appendText("✗ 接单失败\n");
        }
        
        updateUI();
    }
    
    private void handleStartTrip() {
        logArea.appendText("\n>>> 开始行程...\n");
        boolean success = driver1.startTrip(currentOrder);
        
        if (success) {
            triggerSuccessAnimation(startBtn);
            logArea.appendText("✓ 行程已开始\n");
        } else {
            logArea.appendText("✗ 开始行程失败\n");
        }
        
        updateUI();
    }
    
    private void handleEndTrip() {
        logArea.appendText("\n>>> 结束行程...\n");
        boolean success = driver1.endTrip(currentOrder);
        
        if (success) {
            triggerSuccessAnimation(endBtn);
            logArea.appendText(String.format("✓ 行程结束，费用: ¥%.2f\n", currentOrder.getFare()));
        } else {
            logArea.appendText("✗ 结束行程失败\n");
        }
        
        updateUI();
    }
    
    private void handlePayment() {
        logArea.appendText("\n>>> 乘客支付...\n");
        boolean success = passenger1.pay(currentOrder);
        
        if (success) {
            triggerSuccessAnimation(payBtn);
            logArea.appendText("✓ 支付成功！\n");
            logArea.appendText(String.format("  乘客余额: ¥%.2f\n", passenger1.getBalance()));
            logArea.appendText(String.format("  司机收入: ¥%.2f\n", driver1.getTodayIncome()));
        } else {
            logArea.appendText("✗ 支付失败（余额不足）\n");
        }
        
        updateUI();
    }
    
    private void triggerSuccessAnimation(Button btn) {
        // 效果 B: 点击后高频循环闪烁（庆祝效果）
        FadeTransition ft = new FadeTransition(Duration.millis(80), btn);
        ft.setFromValue(1.0);
        ft.setToValue(0.3);
        ft.setCycleCount(6);
        ft.setAutoReverse(true);
        ft.play();
    }
    
    private void updateUI() {
        updateButtonStates();
        updateStatusDisplay();
    }
    
    private void updateButtonStates() {
        OrderStatus status = currentOrder.getStatus();
        
        acceptBtn.setDisable(status != OrderStatus.CREATED);
        startBtn.setDisable(status != OrderStatus.ACCEPTED);
        endBtn.setDisable(status != OrderStatus.ON_TRIP);
        payBtn.setDisable(status != OrderStatus.ARRIVED);
        
        acceptBtn.setOpacity(status != OrderStatus.CREATED ? 0.4 : 1.0);
        startBtn.setOpacity(status != OrderStatus.ACCEPTED ? 0.4 : 1.0);
        endBtn.setOpacity(status != OrderStatus.ON_TRIP ? 0.4 : 1.0);
        payBtn.setOpacity(status != OrderStatus.ARRIVED ? 0.4 : 1.0);
    }
    
    private void updateStatusDisplay() {
        logArea.appendText("[状态更新] " + currentOrder.getStatus() + "\n");
        logArea.appendText(currentOrder.toString() + "\n");
        logArea.appendText(driver1.toString() + "\n");
        logArea.appendText(passenger1.toString() + "\n\n");
        logArea.setScrollTop(Double.MAX_VALUE);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
