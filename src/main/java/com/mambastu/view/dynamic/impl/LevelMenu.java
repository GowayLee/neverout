package com.mambastu.view.dynamic.impl;

import com.mambastu.controller.PropStoreController;
import com.mambastu.controller.context.dto.Context;
import com.mambastu.controller.listener.LevelMenuListener;
import com.mambastu.gameobjects.prop.BaseProp;
import com.mambastu.gameobjects.weapon.BaseWeapon;
import com.mambastu.resource.media.impl.ImageManager;
import com.mambastu.view.dynamic.DynamicMenu;

import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LevelMenu implements DynamicMenu{
    private final LevelMenuListener listener;

    private final StackPane root;
    private final Context ctx;

    private BaseProp prop1;
    private BaseProp prop2;
    private BaseProp prop3;
    private final SimpleIntegerProperty refreshCount;
    private final SimpleIntegerProperty killCount;
    private final SimpleIntegerProperty coin;
    private BaseWeapon weapon;

    private final HBox storeLayout;
    private final VBox dataLayout;
    private final Pane menuPane;

    public LevelMenu(StackPane root, Context ctx, LevelMenuListener listener) {
        this.listener = listener;
        this.root = root;
        this.refreshCount = new SimpleIntegerProperty();
        this.killCount = new SimpleIntegerProperty();
        this.coin = new SimpleIntegerProperty();
        this.ctx = ctx;
        this.menuPane = new Pane();
        this.storeLayout = new HBox();
        this.dataLayout = new VBox();
    }

    @Override
    public void init() { // 初始化
        updateShop();
        buildLayout();
        menuPane.setStyle("-fx-background-color: black;");
        menuPane.setOpacity(0.85);
    }

    @Override
    public void update() { // 更新菜单内容
        refreshCount.set(3);
        bindProperties();
        updateShop();
        bulidShopLayout(); // 重建商店
        bulidDataLayout(); // 重建数据面板
    }

    @Override
    public void show() {
        root.getChildren().remove(menuPane);
        root.getChildren().add(menuPane); // 确保菜单在游戏场景之上(StackPane的栈顶)
    }

    @Override
    public void hide() {
        root.getChildren().remove(menuPane);
    }

    private void bindProperties() { // 绑定属性，例如生命值、分数等
        weapon = ctx.getLevelConfig().getPlayer().getWeapon();
        killCount.bind(ctx.getLevelRecord().getKillCount());
        coin.bindBidirectional(ctx.getGlobalRecord().getCoins()); // 绑定金币数量，允许双向绑定，以便在商店中购买物品时更新金币数量。
    }

    private void updateShop() { // 创建商店物品
        this.prop1 = PropStoreController.getInstance().getRandProp();
        this.prop2 = PropStoreController.getInstance().getRandProp();
        this.prop3 = PropStoreController.getInstance().getRandProp();
    }

    private void buildLayout() {
        menuPane.getChildren().clear();
        bulidShopLayout();
        bulidDataLayout();
        Text passText = new Text("Level Clear!");
        passText.setFill(Color.WHITE); // 设置文本颜色为白色
        passText.setFont(new Font("Segoe Script", 120));
        passText.layoutXProperty()
                .bind(menuPane.widthProperty().subtract(passText.layoutBoundsProperty().get().getWidth()).divide(2));
        passText.layoutYProperty()
                .bind(menuPane.heightProperty().subtract(passText.layoutBoundsProperty().get().getHeight()).divide(5)); // 偏上5分之一

        Text nextBtn = new Text("NEXT LEVEL");
        nextBtn.setFont(Font.font("Segoe Script", FontWeight.BOLD, 48));
        nextBtn.layoutXProperty()
                .bind(menuPane.widthProperty().subtract(nextBtn.layoutBoundsProperty().get().getWidth()).divide(2.1));
        nextBtn.layoutYProperty()
                .bind(menuPane.heightProperty().subtract(nextBtn.layoutBoundsProperty().get().getHeight()).multiply(0.86));
        nextBtn.setFill(Color.WHITE);
        nextBtn.setOpacity(0.8);

        nextBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            nextBtn.setOpacity(1.0);
            nextBtn.styleProperty().bind(Bindings.concat(
                    "-fx-font-family: 'Segoe Script'; ",
                    "-fx-font-weight: bolder; ",
                    "-fx-font-size: ", "66 px;"));
        });

        nextBtn.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            nextBtn.setOpacity(0.8);
            nextBtn.styleProperty().bind(Bindings.concat(
                    "-fx-font-family: 'Segoe Script'; ",
                    "-fx-font-weight: bold; ",
                    "-fx-font-size: ", "48 px;"));
        });

        nextBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            listener.startLevel();
        });

        Text refreshBtn = new Text("Refresh Shop!");
        refreshBtn.setFont(Font.font("Segoe Script", 32));
        refreshBtn.layoutXProperty()
                .bind(menuPane.widthProperty().subtract(refreshBtn.layoutBoundsProperty().get().getWidth()).multiply(0.85));
        refreshBtn.layoutYProperty()
                .bind(menuPane.heightProperty().subtract(refreshBtn.layoutBoundsProperty().get().getHeight()).multiply(0.3));
        refreshBtn.setFill(Color.WHITE);
        refreshBtn.setOpacity(0.8);

        refreshBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            refreshBtn.setOpacity(1.0);
            refreshBtn.styleProperty().bind(Bindings.concat(
                    "-fx-font-family: 'Segoe Script'; ",
                    "-fx-font-size: ", "44 px;"));
        });

        refreshBtn.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            refreshBtn.setOpacity(0.8);
            refreshBtn.styleProperty().bind(Bindings.concat(
                    "-fx-font-family: 'Segoe Script'; ",
                    "-fx-font-size: ", "32 px;"));
        });

        refreshBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (refreshCount.get() > 0) {
                updateShop();
                bulidShopLayout();
                refreshCount.set(refreshCount.get() - 1); // 每次点击减少一次刷新次数。
            }
        });

        menuPane.getChildren().addAll(nextBtn, refreshBtn, storeLayout, passText, dataLayout);
    }

    private void bulidShopLayout() { // 创建商店布局
        storeLayout.getChildren().clear(); // 确保每次调用时清除之前的商品。
        VBox item1 = createShopItem(prop1.getDisplayImage(), prop1.getDescription(), prop1.getPrice(), prop1);
        VBox item2 = createShopItem(prop2.getDisplayImage(), prop2.getDescription(), prop2.getPrice(), prop2);
        VBox item3 = createShopItem(prop3.getDisplayImage(), prop3.getDescription(), prop3.getPrice(), prop3);

        // 将三个商品添加到水平布局
        storeLayout.getChildren().addAll(item1, item2, item3);
        storeLayout.setSpacing(100);
        storeLayout.setPadding(new Insets(50));
        storeLayout.setAlignment(Pos.CENTER);
        // 计算HBox的中心位置
        storeLayout.layoutXProperty().bind(menuPane.widthProperty().subtract(storeLayout.widthProperty()).divide(2));
        storeLayout.layoutYProperty()
                .bind(menuPane.heightProperty().subtract(storeLayout.heightProperty()).divide(2).subtract(50));
    }

    private VBox createShopItem(Image displayImage, String description, Integer price, BaseProp prop) {
        VBox itemLayout = new VBox();

        ImageView imageView = new ImageView(displayImage);
        imageView.setFitWidth(190);
        imageView.setFitHeight(190);

        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(150);
        descriptionLabel.setAlignment(Pos.CENTER); // 确保文本居中显示。

        Label priceLabel = new Label("Price: " + Integer.toString(price));
        priceLabel.setStyle("-fx-text-fill: white;");
        priceLabel.setFont(new Font("Segoe Script", 28)); // 设置字体大小和样式

        Text buyBtn = new Text("BUY IT");
        buyBtn.setFill(Color.WHITE);
        buyBtn.styleProperty().bind(Bindings.concat(
                "-fx-font-family: 'Segoe Script'; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", "36 px;"));

        buyBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            buyBtn.styleProperty().bind(Bindings.concat(
                    "-fx-font-family: 'Segoe Script'; ",
                    "-fx-font-weight: bolder; ",
                    "-fx-font-size: ", "46 px;"));
        });

        buyBtn.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            buyBtn.styleProperty().bind(Bindings.concat(
                    "-fx-font-family: 'Segoe Script'; ",
                    "-fx-font-weight: bold; ",
                    "-fx-font-size: ", "36 px;"));
        });

        buyBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (buyItem(prop)) {
                imageView.setImage(ImageManager.getInstance().getImg("noItemImage", "System", "LevelMenu"));
                imageView.setFitWidth(150);
                imageView.setFitHeight(190);
                descriptionLabel.setText("???????????????");
                priceLabel.setText("Price: ??? "); // 购买后，将价格和描述设置为问号。
                buyBtn.setText("SOLD OUT");
                buyBtn.setOpacity(0.5);
                buyBtn.setDisable(true); // 禁用购买按钮，防止重复购买。
            }
        });

        // 将所有组件添加到垂直布局
        itemLayout.getChildren().addAll(imageView, descriptionLabel, priceLabel, buyBtn);
        itemLayout.setSpacing(30);
        itemLayout.setAlignment(Pos.CENTER);

        return itemLayout;
    }

    private boolean buyItem(BaseProp prop) { // 检查玩家是否有足够的钱购买道具，并执行购买逻辑
        if (coin.get() >= prop.getPrice()) {
            coin.set(coin.get() - prop.getPrice()); // 更新金币数量
            prop.updateValue(ctx.getLevelConfig().getPlayer()); // 更新玩家属性值，例如增加生命值、攻击力等。
            bindProperties();
            bulidDataLayout();
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("YOU ARE TOO POOR");
            alert.setHeaderText("Don't have enough coins to buy !");
            ImageView imageView = new ImageView(
                    ImageManager.getInstance().getImg("dieImage", "Player", "LaughPlayer"));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            alert.setGraphic(imageView);
            alert.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(event -> alert.close());
            delay.play();
            return false;
        }
    }

    private void bulidDataLayout() { // 创建数据布局，例如得分、生命值等。
        dataLayout.getChildren().clear();

        dataLayout.setSpacing(10); // 设置数据项之间的间距
        dataLayout.setAlignment(Pos.BOTTOM_LEFT); // 设置VBox内部子项居中对齐

        Label refreshLabel = new Label();
        refreshLabel.textProperty().bind(refreshCount.asString("Refresh Shop Chance: %d"));
        refreshLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bolder;");
        refreshLabel.setFont(new Font("Arial", 28));

        Label coinLabel = new Label();
        coinLabel.textProperty().bind(coin.asString("Coin: %d"));
        coinLabel.setStyle("-fx-text-fill: yellow; -fx-font-weight: bolder;");
        coinLabel.setFont(new Font("Arial", 28));

        Label MaxHPLabel = new Label();
        MaxHPLabel.textProperty().bind(ctx.getLevelConfig().getPlayer().getMaxHP().asString("MaxHP: %d"));
        MaxHPLabel.setStyle("-fx-text-fill: Green;");
        MaxHPLabel.setFont(new Font("Arial", 24));

        Label speedLabel = new Label();
        speedLabel.textProperty().bind(ctx.getLevelConfig().getPlayer().getSpeed().asString("Speed: %.1f"));
        speedLabel.setStyle("-fx-text-fill: Red;");
        speedLabel.setFont(new Font("Arial", 24));

        Label skillCDLabel = new Label();
        skillCDLabel.textProperty().bind(ctx.getLevelConfig().getPlayer().getSkillCD().asString("SkillCD: %.2fs"));
        skillCDLabel.setStyle("-fx-text-fill: yellow;");
        skillCDLabel.setFont(new Font("Arial", 24));

        dataLayout.getChildren().addAll(refreshLabel, coinLabel, MaxHPLabel, speedLabel, skillCDLabel); // 将金币和最大生命值标签添加到数据布局中

        if (weapon != null) {
            Label damageLabel = new Label();
            damageLabel.textProperty().bind(weapon.getDamage().asString("Damage: %d"));
            damageLabel.setStyle("-fx-text-fill: white;");
            damageLabel.setFont(new Font("Arial", 24));

            Label coolTimeLabel = new Label();
            coolTimeLabel.textProperty().bind(weapon.getCoolTime().asString("WeaponCD: %.2fms"));
            coolTimeLabel.setStyle("-fx-text-fill: white;");
            coolTimeLabel.setFont(new Font("Arial", 24));

            Label bulletSpeedLabel = new Label();
            bulletSpeedLabel.textProperty().bind(weapon.getBulletSpeed().asString("BulletSpeed: %.2f"));
            bulletSpeedLabel.setStyle("-fx-text-fill: white;");
            bulletSpeedLabel.setFont(new Font("Arial", 24));

            Label rangeLabel = new Label();
            rangeLabel.textProperty().bind(weapon.getRange().asString("BulletRange: %.2f"));
            rangeLabel.setStyle("-fx-text-fill: white;");
            rangeLabel.setFont(new Font("Arial", 24));

            dataLayout.getChildren().addAll(damageLabel, coolTimeLabel, bulletSpeedLabel, rangeLabel);
        } else {
            Label noWeaponLabel = new Label("No Weapon Equipped"); // 如果没有武器，显示没有装备武器的提示
            noWeaponLabel.setStyle("-fx-text-fill: white;");
            noWeaponLabel.setFont(new Font("Arial", 24));
            dataLayout.getChildren().add(noWeaponLabel); // 将提示添加到数据布局中
        }

        dataLayout.setLayoutX(10);
        dataLayout.setLayoutY(10);
    }
}
