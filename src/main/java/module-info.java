module com.mambastu {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires lombok;
    requires javafx.base;
    requires java.desktop;

    opens com.mambastu to javafx.fxml;
    exports com.mambastu;
}
