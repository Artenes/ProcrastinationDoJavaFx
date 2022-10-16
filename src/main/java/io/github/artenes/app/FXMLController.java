package io.github.artenes.app;

import io.github.artenes.domain.Task;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FXMLController implements Initializable {

    private static final int INPUT_DELAY = 1000;

    @FXML
    VBox tasksList;

    @FXML
    DatePicker todayDate;

    @FXML
    TextField fieldName;

    private TasksApplication application;

    private Timer timer = new Timer();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        application = new TasksApplication(this);
        application.initialize();
        fieldName.setOnKeyPressed(this::onEnterPressed);
        todayDate.valueProperty().addListener(this::onTodayChanged);
    }

    public void setTodayDate(LocalDate date) {
        todayDate.setValue(date);
    }

    public void setTaskSize(int index, double newSize) {
        Node taskNode = tasksList.getChildren().get(index);
        taskNode.setStyle("-fx-font-size: " + newSize + "em;");
    }

    public void clearInputs() {
        fieldName.setText("");
    }

    public void updateTasks(List<Task> tasks) {
        tasksList.getChildren().clear();
        for (Task task : tasks) {
            VBox rowLayout = load("item.fxml");
            TextField taskName = (TextField) rowLayout.lookup("#taskName");
            DatePicker taskDate = (DatePicker) rowLayout.lookup("#taskDate");

            taskName.setText(task.getName());
            taskDate.setValue(parseDate(task.getDate()));

            taskName.textProperty().addListener(((observable, oldValue, newValue) -> {
                timer.cancel();
                timer = new Timer();
                timer.schedule(new UpdateTask(task, newValue, application), INPUT_DELAY);
            }));

            tasksList.getChildren().add(rowLayout);
        }
    }

    private void onEnterPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            String typed = fieldName.getText();
            application.addTask(typed);
        }
    }

    private void onTodayChanged(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
        application.setTodayDate(newValue);
    }

    private LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    @SuppressWarnings("SameParameterValue")
    private <T> T load(String name) {
        try {
            return FXMLLoader.load(Objects.requireNonNull(getClass().getResource(name)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final class UpdateTask extends TimerTask {

        private final Task task;
        private final String newValue;
        private final TasksApplication application;

        public UpdateTask(Task task, String newValue, TasksApplication application) {
            this.task = task;
            this.newValue = newValue;
            this.application = application;
        }

        @Override
        public void run() {
            task.setName(newValue);
            application.save(task);
        }
    }

}
