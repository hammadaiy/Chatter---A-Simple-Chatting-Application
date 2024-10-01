module com.example.chatter {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chatter to javafx.fxml;
    exports com.example.chatter;
}