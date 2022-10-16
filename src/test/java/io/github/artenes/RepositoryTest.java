package io.github.artenes;

import io.github.artenes.domain.JsonTasksParser;
import io.github.artenes.domain.Repository;
import io.github.artenes.domain.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("ResultOfMethodCallIgnored")
class RepositoryTest {

    private Path testFolder;
    private Repository repository;

    @BeforeEach
    public void setUp() {
        testFolder = Paths.get("repotest");
        repository = new Repository(testFolder, new JsonTasksParser());
        try {
            Files.createDirectory(testFolder);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void getAll_returnsNothing_whenNoTasksWereCreated() {
        List<Task> tasks = repository.getAll();
        assertEquals(0, tasks.size());
    }

    @Test
    public void getAll_returnsAllCreatedTasks() throws IOException {
        Task task1 = new Task("1665868496685", "Task1", "2022-10-14");
        Task task2 = new Task("1995268194685", "Task2", "2022-10-14");
        repository.save(task1);
        repository.save(task2);
        List<Task> tasks = repository.getAll();
        assertEquals(2, tasks.size());
    }

    @Test
    public void save_createsNewTask() throws IOException {
        Task task = new Task("Do something", "2022-10-14");
        repository.save(task);
        Task repoTask = repository.find(task.getId());
        assertNotNull(repoTask);
    }

    @Test
    public void save_updatesExistingTask() throws IOException {
        Task task = new Task("Do something", "2022-10-14");
        repository.save(task);
        task.setName("New Do Something");
        repository.save(task);
        Task repoTask = repository.find(task.getId());
        assertEquals("New Do Something", repoTask.getName());
    }

    @AfterEach
    public void tearDown() {
        try {
            Files.walk(testFolder).map(Path::toFile).forEach(File::delete);
            Files.delete(testFolder);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}