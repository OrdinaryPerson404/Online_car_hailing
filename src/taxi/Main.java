package taxi;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FadeTransition 技术演示 - 网约车订单系统
 * 
 * 演示重点：
 * 1. FadeTransition 的基本用法
 * 2. 淡入、淡出、交替动画
 * 3. 链式动画（setOnFinished）
 * 4. 在网约车场景中的应用
 */
public class Main extends Application {

    // 业务对象
    private Passenger passenger;
    private Driver driver;
    private Order currentOrder;
    private OrderStatus[] statusSequence;
    private int currentStatusIndex;

    // UI 组件
    private Label lblOrderId;
    private Label lblStatus;
    private Label lblPassenger;
    private Label lblDriver;
    private Label lblStart;
    private Label lblEnd;
    private Label lblDistance;
    private Label lblFare;
    
    private VBox cardBox;  // 订单卡片容器（应用动画的对象）
    private Button btnNext;
    private Button btnReset;
    private Button btnFadeIn;
    private Button btnFadeOut;
    private Button btnAlternate;
    private Slider sliderDuration;
    private Label lblDuration;
    
    private TextArea txtLog;  // 日志区域

    @Override
    public void start(Stage primaryStage) {
        initBusinessData();
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #f0f0f0;");

        // 标题
        Label title = createTitle("网约车订单系统 - FadeTransition 演示");
        root.setTop(title);
        BorderPane.setMargin(title, new Insets(0, 0, 15, 0));

        // 左侧：订单信息区
        VBox infoPanel = createInfoPanel();
        
        // 右侧：动画控制区
        VBox controlPanel = createControlPanel();
        
        // 底部：日志区
        txtLog = createLogArea();
        
        // 组合左右面板
        HBox centerBox = new HBox(15, infoPanel, controlPanel);
        root.setCenter(centerBox);
        root.setBottom(txtLog);
        BorderPane.setMargin(txtLog, new Insets(15, 0, 0, 0));

        Scene scene = new Scene(root, 900, 650);
        primaryStage.setTitle("FadeTransition 技术演示 - 网约车系统");
        primaryStage.setScene(scene);
        primaryStage.show();

        log("系统启动成功！点击按钮体验 FadeTransition 动画效果");
        updateDisplay();
    }

    /**
     * 初始化业务数据
     */
    private void initBusinessData() {
        passenger = new Passenger("P001", "张三", "13800138001", 100.0);
        Vehicle vehicle = new Vehicle("京A12345", "丰田卡罗拉");
        driver = new Driver("D001", "王师傅", "13900139001", vehicle);
        
        Location start = new Location("北京市朝阳区建国路100号");
        Location end = new Location("北京市海淀区中关村大街200号");
        currentOrder = new Order("ORD001", passenger, start, end, 8.5);
        
        statusSequence = new OrderStatus[] {
            OrderStatus.CREATED,
            OrderStatus.ACCEPTED,
            OrderStatus.ON_TRIP,
            OrderStatus.ARRIVED,
            OrderStatus.PAID
        };
        currentStatusIndex = 0;
    }

    /**
     * 创建标题
     */
    private Label createTitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    /**
     * 创建左侧订单信息面板
     */
    private VBox createInfoPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
        panel.setPrefWidth(350);

        Label titleLabel = new Label("订单详情");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #34495e;");

        cardBox = new VBox(8);
        cardBox.setPadding(new Insets(10));
        cardBox.setStyle("-fx-background-color: #ecf0f1; -fx-border-radius: 5;");

        lblOrderId = createInfoItem("订单编号:");
        lblStatus = createInfoItem("订单状态:");
        lblPassenger = createInfoItem("乘　　客:");
        lblDriver = createInfoItem("司　　机:");
        lblStart = createInfoItem("起　　点:");
        lblEnd = createInfoItem("终　　点:");
        lblDistance = createInfoItem("里　　程:");
        lblFare = createInfoItem("费　　用:");

        cardBox.getChildren().addAll(
            lblOrderId, lblStatus, lblPassenger, lblDriver,
            lblStart, lblEnd, lblDistance, lblFare
        );

        panel.getChildren().addAll(titleLabel, cardBox);
        return panel;
    }

    /**
     * 创建信息项 Label
     */
    private Label createInfoItem(String prefix) {
        Label label = new Label(prefix);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50;");
        return label;
    }

    /**
     * 创建右侧控制面板
     */
    private VBox createControlPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
        panel.setPrefWidth(450);

        // 状态切换区
        Label section1 = createSectionTitle("① 订单状态切换（链式动画）");
        btnNext = createButton("下一步 →", "#3498db", this::handleNextStep);
        btnReset = createButton("重置", "#95a5a6", this::handleReset);
        HBox box1 = new HBox(10, btnNext, btnReset);

        // 基础动画演示区
        Label section2 = createSectionTitle("② 基础动画演示");
        btnFadeIn = createButton("淡入效果", "#2ecc71", this::handleFadeIn);
        btnFadeOut = createButton("淡出效果", "#e74c3c", this::handleFadeOut);
        btnAlternate = createButton("交替动画", "#f39c12", this::handleAlternate);
        HBox box2 = new HBox(10, btnFadeIn, btnFadeOut, btnAlternate);

        // 动画参数调整区
        Label section3 = createSectionTitle("③ 动画时长调整");
        sliderDuration = new Slider(0.5, 3.0, 1.0);
        sliderDuration.setShowTickLabels(true);
        sliderDuration.setShowTickMarks(true);
        sliderDuration.setMajorTickUnit(0.5);
        sliderDuration.setBlockIncrement(0.1);
        sliderDuration.valueProperty().addListener((obs, oldVal, newVal) -> {
            lblDuration.setText(String.format("当前时长: %.1f 秒", newVal.doubleValue()));
        });
        lblDuration = new Label("当前时长: 1.0 秒");
        lblDuration.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d;");
        VBox box3 = new VBox(5, sliderDuration, lblDuration);

        // 技术说明区
        Label section4 = createSectionTitle("④ FadeTransition 核心属性");
        TextArea techNote = new TextArea();
        techNote.setEditable(false);
        techNote.setWrapText(true);
        techNote.setPrefHeight(120);
        techNote.setText(
            "FadeTransition 是 JavaFX 的淡入淡出动画类\n\n" +
            "核心属性:\n" +
            "• node: 要应用动画的节点\n" +
            "• fromValue: 起始透明度 (0.0=完全透明, 1.0=完全不透明)\n" +
            "• toValue: 结束透明度\n" +
            "• duration: 动画持续时间\n" +
            "• autoReverse: 是否自动反向播放\n" +
            "• cycleCount: 循环次数 (Timeline.INDEFINITE=无限循环)"
        );
        techNote.setStyle("-fx-font-size: 12px; -fx-font-family: 'Consolas';");

        panel.getChildren().addAll(
            section1, box1,
            section2, box2,
            section3, box3,
            section4, techNote
        );
        return panel;
    }

    /**
     * 创建区域标题
     */
    private Label createSectionTitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        return label;
    }

    /**
     * 创建按钮
     */
    private Button createButton(String text, String color, Runnable action) {
        Button button = new Button(text);
        button.setStyle(String.format(
            "-fx-background-color: %s; -fx-text-fill: white; " +
            "-fx-font-size: 13px; -fx-padding: 8 15; -fx-border-radius: 5;", color
        ));
        button.setOnAction(e -> action.run());
        return button;
    }

    /**
     * 创建日志区域
     */
    private TextArea createLogArea() {
        TextArea area = new TextArea();
        area.setEditable(false);
        area.setPrefHeight(100);
        area.setStyle("-fx-font-size: 12px; -fx-font-family: 'Consolas'; -fx-background-color: #2c3e50; -fx-text-fill: #ecf0f1;");
        return area;
    }

    /**
     * 记录日志
     */
    private void log(String message) {
        String timestamp = java.time.LocalTime.now().toString().substring(0, 8);
        txtLog.appendText("[" + timestamp + "] " + message + "\n");
        txtLog.setScrollTop(Double.MAX_VALUE);
    }

    /**
     * 更新显示
     */
    private void updateDisplay() {
        lblOrderId.setText("订单编号: " + currentOrder.getOrderId());
        lblStatus.setText("订单状态: " + currentOrder.getStatus());
        lblPassenger.setText("乘　　客: " + currentOrder.getPassenger().getName());
        lblDriver.setText("司　　机: " + (currentOrder.getDriver() != null ? currentOrder.getDriver().getName() : "待分配"));
        lblStart.setText("起　　点: " + currentOrder.getStart().getAddress());
        lblEnd.setText("终　　点: " + currentOrder.getEnd().getAddress());
        lblDistance.setText("里　　程: " + currentOrder.getDistance() + " km");
        lblFare.setText("费　　用: ¥" + String.format("%.2f", currentOrder.getFare()));
    }

    /**
     * 处理下一步按钮（链式动画演示）
     */
    private void handleNextStep() {
        if (currentStatusIndex >= statusSequence.length) {
            log("订单已完成所有状态流转，请点击重置");
            return;
        }

        OrderStatus nextStatus = statusSequence[currentStatusIndex];
        
        log("准备切换到状态: " + nextStatus);
        
        // 步骤1: 淡出旧卡片
        FadeTransition fadeOut = createFadeOut(cardBox);
        fadeOut.setOnFinished(event -> {
            // 步骤2: 更新业务逻辑
            updateBusinessLogic(nextStatus);
            updateDisplay();
            
            // 步骤3: 淡入新卡片
            FadeTransition fadeIn = createFadeIn(cardBox);
            fadeIn.setOnFinished(e -> {
                log("✓ 状态切换完成: " + nextStatus);
                
                // 如果是 ARRIVED 状态，自动计算费用
                if (nextStatus == OrderStatus.ARRIVED) {
                    currentOrder.calculateFare();
                    updateDisplay();
                    log("  费用已计算: ¥" + String.format("%.2f", currentOrder.getFare()));
                }
                
                // 如果是 PAID 状态，显示完成提示
                if (nextStatus == OrderStatus.PAID) {
                    log("✓ 订单流程全部完成！");
                }
            });
            fadeIn.play();
        });
        
        fadeOut.play();
        currentStatusIndex++;
    }

    /**
     * 更新业务逻辑
     */
    private void updateBusinessLogic(OrderStatus status) {
        switch (status) {
            case CREATED:
                log("订单已创建");
                break;
            case ACCEPTED:
                driver.acceptOrder(currentOrder);
                log("司机 " + driver.getName() + " 已接单");
                break;
            case ON_TRIP:
                currentOrder.startTrip();
                log("行程已开始");
                break;
            case ARRIVED:
                currentOrder.endTrip();
                log("行程已结束，等待支付");
                break;
            case PAID:
                currentOrder.pay();
                log("乘客已支付，司机收入: ¥" + String.format("%.2f", driver.getTodayIncome()));
                break;
        }
    }

    /**
     * 处理重置按钮
     */
    private void handleReset() {
        log("========== 重置订单 ==========");
        initBusinessData();
        currentStatusIndex = 0;
        updateDisplay();
        cardBox.setOpacity(1.0);
        log("订单已重置为初始状态");
    }

    /**
     * 处理淡入按钮
     */
    private void handleFadeIn() {
        log("执行淡入动画 (fromValue=0.0 → toValue=1.0)");
        cardBox.setOpacity(0.0);
        FadeTransition fadeIn = createFadeIn(cardBox);
        fadeIn.setOnFinished(e -> log("✓ 淡入动画完成"));
        fadeIn.play();
    }

    /**
     * 处理淡出按钮
     */
    private void handleFadeOut() {
        log("执行淡出动画 (fromValue=1.0 → toValue=0.0)");
        cardBox.setOpacity(1.0);
        FadeTransition fadeOut = createFadeOut(cardBox);
        fadeOut.setOnFinished(e -> log("✓ 淡出动画完成"));
        fadeOut.play();
    }

    /**
     * 处理交替动画按钮
     */
    private void handleAlternate() {
        log("执行交替动画 (autoReverse=true, cycleCount=2)");
        cardBox.setOpacity(1.0);
        
        FadeTransition alternate = new FadeTransition();
        alternate.setNode(cardBox);
        alternate.setFromValue(1.0);
        alternate.setToValue(0.0);
        alternate.setDuration(Duration.seconds(sliderDuration.getValue()));
        alternate.setAutoReverse(true);
        alternate.setCycleCount(2);
        
        alternate.setOnFinished(e -> {
            log("✓ 交替动画完成 (淡出→淡入)");
            cardBox.setOpacity(1.0);
        });
        
        alternate.play();
    }

    /**
     * 创建淡入动画
     */
    private FadeTransition createFadeIn(javafx.scene.Node node) {
        FadeTransition ft = new FadeTransition();
        ft.setNode(node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setDuration(Duration.seconds(sliderDuration.getValue()));
        return ft;
    }

    /**
     * 创建淡出动画
     */
    private FadeTransition createFadeOut(javafx.scene.Node node) {
        FadeTransition ft = new FadeTransition();
        ft.setNode(node);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setDuration(Duration.seconds(sliderDuration.getValue()));
        return ft;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
