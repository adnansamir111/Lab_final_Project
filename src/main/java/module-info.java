module com.example.javaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    requires java.sql;
    //requires org.sqlite.jdbc;   // ✅ add this for SQLite

    opens com.example.javaproject to javafx.fxml;
    exports com.example.javaproject;
    exports com.example.javaproject.all_class;
    opens com.example.javaproject.all_class to javafx.fxml;
}
