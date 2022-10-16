module artenes.todo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    exports io.github.artenes.app;
    exports io.github.artenes.domain;
    opens io.github.artenes.domain to com.fasterxml.jackson.databind, javafx.fxml;
    opens io.github.artenes.app to com.fasterxml.jackson.databind, javafx.fxml;
}