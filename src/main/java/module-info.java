module com.mambastu {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires lombok;
    requires javafx.base;
    requires java.desktop;
    requires org.apache.logging.log4j;

    opens com.mambastu to javafx.fxml;
    exports com.mambastu;
}
