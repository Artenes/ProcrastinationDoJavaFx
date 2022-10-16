package io.github.artenes.app;

import io.github.artenes.domain.JsonTasksParser;
import io.github.artenes.domain.Repository;
import io.github.artenes.domain.Task;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class TasksApplication {

    private final Repository repository;
    private final FXMLController controller;

    private LocalDate today = LocalDate.now();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TasksApplication(FXMLController controller) {
        this.controller = controller;
        Path currentDirectory = Paths.get("").toAbsolutePath();
        repository = new Repository(currentDirectory, new JsonTasksParser());
    }

    public void initialize() {
        controller.setTodayDate(today);
        updateTasks();
    }

    public void setTodayDate(LocalDate newDate) {
        today = newDate;
        refreshSizes();
    }

    public void addTask(String name) {
        try {
            String date = today.format(formatter);
            Task task = new Task(name, date);
            repository.save(task);
            updateTasks();
            controller.clearInputs();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Task task) {
        try {
            repository.save(task);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshSizes() {
        List<Task> tasks = repository.getAll();
        for (int index = 0; index < tasks.size(); index++) {
            Task task = tasks.get(index);
            int days = calculateDaysFrom(task.getDate());
            double newSize = 1 + (days * 0.1);
            controller.setTaskSize(index, newSize);
        }
    }

    private int calculateDaysFrom(String date) {
        String[] dateParts = date.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);
        LocalDate taskDate = LocalDate.of(year, month, day);

        Period period = Period.between(taskDate, today);

        if (period.getDays() < 0) {
            return 0;
        }

        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();

        //approximation that does not take into account months with 31/28 days or leap years
        //Period class up there goes from 0 to 30 days, then back to 0 because it ignores
        //months and years 🤦
        return (years*365) + (months*30) + days;
    }

    private void updateTasks() {
        List<Task> tasks = repository.getAll();
        Collections.reverse(tasks);
        controller.updateTasks(tasks);
    }

}
