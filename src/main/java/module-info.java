module com.mambastu {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires static lombok;
    requires javafx.base;
    requires java.desktop;
    requires Jamepad;


    opens com.mambastu to javafx.fxml;
    exports com.mambastu;
}
