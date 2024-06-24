module com.mambastu {
    requires javafx.controls;
    requires static lombok;
    requires java.desktop;
    requires org.apache.logging.log4j;
    requires org.json;
    requires Jamepad;
    requires jdk.jsobject;
    requires gdx;

    opens com.mambastu to javafx.fxml;
    exports com.mambastu;
}

// java --module-path ".\lib" --add-modules javafx.controls -jar neverout-1.0-SNAPSHOT.jar
