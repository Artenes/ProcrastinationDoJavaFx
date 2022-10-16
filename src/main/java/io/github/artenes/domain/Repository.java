package io.github.artenes.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Repository {

    private static final String DB_NAME = "storage.json";

    private final Path dbPath;
    private final JsonTasksParser parser;

    public Repository(Path dbPath, JsonTasksParser parser) {
        this.dbPath = dbPath.resolve(DB_NAME);
        this.parser = parser;
    }

    public List<Task> getAll() {
        try {
            String json = Files.readString(dbPath);
            return parser.fromJson(json);
        } catch (IOException exception) {
            return new ArrayList<>();
        }
    }

    public void save(Task task) throws IOException {
        List<Task> tasks = getAll();
        int index = tasks.indexOf(task);
        if (index != -1) {
            tasks.set(index, task);
        } else {
            tasks.add(task);
        }
        String json = parser.toJson(tasks);
        Files.writeString(dbPath, json);
    }

    public Task find(String id) throws NoSuchElementException {
        List<Task> tasks = getAll();
        return tasks.stream().filter(task -> task.getId().equals(id)).findFirst().orElseThrow();
    }

}
