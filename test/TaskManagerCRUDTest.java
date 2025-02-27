import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerCRUDTest {
    private TaskManagerCRUD manager;
    private static final String TEST_FILE_PATH = "test_tasks.json";

    @BeforeEach
    void setUp() throws IOException{
        if (!Files.exists(Paths.get(TEST_FILE_PATH))) {
            Files.createFile(Paths.get(TEST_FILE_PATH));
        }
        manager = new TaskManagerCRUD(TEST_FILE_PATH);

        Files.write(Paths.get(TEST_FILE_PATH), "[]".getBytes());   }

    @Test
    void addTask() throws IOException {
        manager.addTask("Buy groceries");
        manager.listAllTasks();
        String fileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH)));
        assertTrue(fileContent.contains("Buy groceries"), "Task should be in the file");
    }

    @Test
    void updateTask() throws IOException {
        manager.addTask("Buy groceries");
        manager.updateTask(1, "Buy groceries and cook dinner");
        manager.listAllTasks();
        String fileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH)));
        assertTrue(fileContent.contains("Buy groceries and cook dinner"),"Task description should be updated." );
    }

    @Test
    void deleteTask() throws IOException {
        manager.addTask("Buy groceries");
        manager.deleteTask(1);
        manager.listAllTasks();
        String fileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH)));
        assertFalse(fileContent.contains("Buy groceries"), "Task should be deleted.");
    }

    @Test
    void markInProgress() throws IOException {
        manager.addTask("Buy groceries");
        manager.markInProgress(1);
        manager.listAllTasks();
        String fileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH)));
        assertTrue(fileContent.contains("IN_PROGRESS"), "Task status should be 'IN_PROGRESS'.");

    }

    @Test
    void markDone() throws IOException {
        manager.addTask("Buy groceries");
        manager.markDone(1);
        manager.listAllTasks();
        String fileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH)));
        assertTrue(fileContent.contains("DONE"), "Task status should be 'DONE'.");

    }

    @Test
    void listAllTasks() throws IOException {
        manager.addTask("Buy groceries");
        manager.addTask("Cook dinner");
        manager.listAllTasks();
        String fileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH)));
        //assertTrue(fileContent.contains("Buy groceries"), "Task with 'DONE' status should be displayed.");
    }

    @Test
    void listTasksByStatus() throws IOException {
        manager.addTask("Buy groceries");
        manager.addTask("Cook dinner");
        manager.markDone(1);
        manager.listTasksByStatus("done");
        manager.listAllTasks();
        String fileContent = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH)));
        assertTrue(fileContent.contains("Buy groceries"), "Task with 'DONE' status should be displayed.");
    }
}