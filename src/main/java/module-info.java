module ph.edu.dlsu.lbycpob.love_dramatic_piano_academy {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens ph.edu.dlsu.lbycpob.love_dramatic_piano_academy to javafx.fxml;
    exports ph.edu.dlsu.lbycpob.love_dramatic_piano_academy;
}