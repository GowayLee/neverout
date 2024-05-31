module com.mambastu {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mambastu to javafx.fxml;
    exports com.mambastu;
}
