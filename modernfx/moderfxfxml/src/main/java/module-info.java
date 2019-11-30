module org.daydevjv.modernfx.moderfxfxml {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.daydevjv.modernfx.moderfxfxml to javafx.fxml;
    exports org.daydevjv.modernfx.moderfxfxml;
}