package com.mambastu.ui;

import com.mambastu.controller.level.context.dto.Context;
import com.mambastu.listener.LevelMenuListener;
import com.mambastu.material.pojo.prop.BaseProp;
import com.mambastu.material.pojo.weapon.BaseWeapon;
import com.mambastu.material.pojo.weapon.StandardRifle;
import com.mambastu.material.resource.ResourceManager;
import com.mambastu.util.PropStore;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LevelMenu {
    private final LevelMenuListener listener;

    private final StackPane root;
    private final Context ctx;

    private BaseProp prop1;
    private BaseProp prop2;
    private BaseProp prop3;
    private final SimpleIntegerProperty killCount;
    private final SimpleIntegerProperty coin;
    private BaseWeapon weapon;

    private final HBox storeLayout;
    private final VBox dataLayout;
    private final Pane menuPane;

    public LevelMenu(StackPane root, Context ctx, LevelMenuListener listener) {
        this.listener = listener;
        this.root = root;
        this.killCount = new SimpleIntegerProperty();
        this.coin = new SimpleIntegerProperty();
        this.ctx = ctx;
        this.menuPane = new Pane();
        this.storeLayout = new HBox();
        this.dataLayout = new VBox();
    }

    public void init() { // 初始化
        updateShop();
        buildLayout();
        menuPane.setStyle("-fx-background-color: black;");
        menuPane.setOpacity(0.85);
    }

    public void update() { // 更新菜单内容
        bindProperties();
        updateShop();
        bulidShopLayout(); // 重建商店
        bulidDataLayout(); // 重建数据面板
    }

    public void show() {
        root.getChildren().remove(menuPane);
        root.getChildren().add(menuPane); // 确保菜单在游戏场景之上(StackPane的栈顶)
    }

    public void hide() {
        root.getChildren().remove(menuPane);
    }

    private void bindProperties() { // 绑定属性，例如生命值、分数等
        weapon = ctx.getLevelConfig().getPlayer().getWeapon();
        killCount.bind(ctx.getLevelRecord().getKillCount());
        coin.bindBidirectional(ctx.getGlobalRecord().getCoins()); // 绑定金币数量，允许双向绑定，以便在商店中购买物品时更新金币数量。
    }

    private void updateShop() { // 创建商店物品
        this.prop1 = PropStore.getInstance().getRandProp();
        this.prop2 = PropStore.getInstance().getRandProp();
        this.prop3 = PropStore.getInstance().getRandProp();
    }

    private void buildLayout() {
        bulidShopLayout();
        bulidDataLayout();
        Text passText = new Text("Level Clear!");
        passText.setFill(Color.WHITE); // 设置文本颜色为白色
        passText.setFont(new Font("Segoe Script", 120));
        passText.layoutXProperty()
                .bind(menuPane.widthProperty().subtract(passText.layoutBoundsProperty().get().getWidth()).divide(2));
        passText.layoutYProperty()
                .bind(menuPane.heightProperty().subtract(passText.layoutBoundsProperty().get().getHeight()).divide(5)); // 偏上5分之一

        Button nextLevelBtn = new Button("Next Level!");
        nextLevelBtn.layoutXProperty()
                .bind(menuPane.widthProperty().subtract(nextLevelBtn.layoutBoundsProperty().get().getWidth()).divide(3)); // 偏左3分之一
        nextLevelBtn.layoutYProperty()
                .bind(menuPane.heightProperty().subtract(nextLevelBtn.layoutBoundsProperty().get().getHeight()).divide(5).multiply(4)); // 偏下5分之一
        nextLevelBtn.setOnAction(e -> {
            listener.startLevel();
        });

        Button weaponBtn = new Button("点我就送一刀999!");
        weaponBtn.layoutXProperty()
                .bind(menuPane.widthProperty().subtract(weaponBtn.layoutBoundsProperty().get().getWidth()).divide(3).multiply(2)); // 偏右3分之一
                weaponBtn.layoutYProperty()
                .bind(menuPane.heightProperty().subtract(weaponBtn.layoutBoundsProperty().get().getHeight()).divide(5).multiply(4)); // 偏下5分之一
        weaponBtn.setOnAction(e -> {
            ctx.getLevelConfig().getPlayer().setWeapon(new StandardRifle());
        });

        menuPane.getChildren().addAll(nextLevelBtn, weaponBtn, storeLayout, passText, dataLayout);
    }

    private void bulidShopLayout() { // 创建商店布局
        storeLayout.getChildren().clear(); // 确保每次调用时清除之前的商品。
        VBox item1 = createShopItem(prop1.getDisplayImage(), prop1.getClass().toString(), prop1.getPrice(), prop1);
        VBox item2 = createShopItem(prop2.getDisplayImage(), prop2.getClass().toString(), prop2.getPrice(), prop2);
        VBox item3 = createShopItem(prop3.getDisplayImage(), prop3.getClass().toString(), prop3.getPrice(), prop3);

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

    private VBox createShopItem(Image displayImage, String name, Integer price, BaseProp prop) {
        VBox itemLayout = new VBox();

        ImageView imageView = new ImageView(displayImage);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);

        Label descriptionLabel = new Label(name);
        descriptionLabel.setStyle("-fx-text-fill: white;");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(150);

        Label priceLabel = new Label("Price: " + Integer.toString(price));
        priceLabel.setStyle("-fx-text-fill: white;");
        priceLabel.setFont(new Font("Segoe Script", 16)); // 设置字体大小和样式

        Button buyButton = new Button("Buy it!");
        buyButton.setOnAction(e -> { // 购买按钮的点击事件处理程序
            if (buyItem(prop)) {
                imageView.setImage(ResourceManager.getInstance().getImg("noItemImage", "System", "LevelMenu"));
                imageView.setFitWidth(150);
                imageView.setFitHeight(190);
                descriptionLabel.setText("???????????????");
                priceLabel.setText("Price: ??? "); // 购买后，将价格和描述设置为问号。
                buyButton.setDisable(true); // 禁用购买按钮，防止重复购买。
            }
        });

        // 将所有组件添加到垂直布局
        itemLayout.getChildren().addAll(imageView, descriptionLabel, priceLabel, buyButton);
        itemLayout.setSpacing(20);
        itemLayout.setAlignment(Pos.CENTER);

        return itemLayout;
    }

    private boolean buyItem(BaseProp prop) { // 检查玩家是否有足够的钱购买道具，并执行购买逻辑
        if (coin.get() >= prop.getPrice()) {
            coin.set(coin.get() - prop.getPrice()); // 更新金币数量
            prop.updateValue(ctx.getLevelConfig().getPlayer()); // 更新玩家属性值，例如增加生命值、攻击力等。
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("YOU ARE TOO POOR");
            alert.setHeaderText("Don't have enough coins to buy !");
            ImageView imageView = new ImageView(ResourceManager.getInstance().getImg("dieImage", "Player", "Player1"));
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
        
        Label coinLabel = new Label();
        coinLabel.textProperty().bind(coin.asString("Coin: %d"));
        coinLabel.setStyle("-fx-text-fill: yellow;");
        coinLabel.setFont(new Font("Arial", 28));

        Label MaxHPLabel = new Label();
        MaxHPLabel.textProperty().bind(ctx.getLevelConfig().getPlayer().getMaxHP().asString("MaxHP: %d"));
        MaxHPLabel.setStyle("-fx-text-fill: Green;");
        MaxHPLabel.setFont(new Font("Arial", 24));

        dataLayout.getChildren().addAll(coinLabel, MaxHPLabel); // 将金币和最大生命值标签添加到数据布局中

        if (weapon != null) {
            Label damageLabel = new Label();
            damageLabel.textProperty().bind(weapon.getDamage().asString("Damage: %d"));
            damageLabel.setStyle("-fx-text-fill: white;");
            damageLabel.setFont(new Font("Arial", 24));

            Label coolTimeLabel = new Label();
            coolTimeLabel.textProperty().bind(weapon.getCoolTime().asString("WeaponCD: %.2f"));
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
