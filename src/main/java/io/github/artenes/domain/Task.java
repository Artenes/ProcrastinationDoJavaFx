package io.github.artenes.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Task {

    private String id;
    private String name;
    private String date;

    public Task(String name, String createdAt) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.name = name;
        this.date = createdAt;
    }

    public Task(String id, String name, String createdAt) {
        this.id = id;
        this.name = name;
        this.date = createdAt;
    }

    public Task() {
        this.id = "";
        this.name = "";
        this.date = "";
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

}
