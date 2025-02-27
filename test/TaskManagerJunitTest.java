import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import components.TaskManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerJunitTest {
    private TaskManager manager= new TaskManager();

    @BeforeEach
    void setUp() throws IOException{
        if (!Files.exists(Paths.get(TaskManager.getFilePath()))) {
            Files.createFile(Paths.get(TaskManager.getFilePath()));
        }
        Files.write(Paths.get(TaskManager.getFilePath()), "[]".getBytes());
        manager = new TaskManager();
    }

    @Test
    void addTask() throws IOException {
        manager.addTask("Buy groceries");
        manager.listAllTasks();
        String fileContent = new String(Files.readAllBytes(Paths.get(TaskManager.getFilePath())));
        assertTrue(fileContent.contains("Buy groceries"), "java.Task should be in the file");
    }

    @Test
    void updateTask() throws IOException {
        manager.addTask("Buy groceries");
        manager.updateTask(1, "Buy groceries and cook dinner");
        manager.listAllTasks();
        String fileContent = new String(Files.readAllBytes(Paths.get(TaskManager.getFilePath())));
        assertTrue(fileContent.contains("Buy groceries and cook dinner"),"java.Task description should be updated." );
    }

    @Test
    void deleteTask() throws IOException {
        manager.addTask("Buy groceries");
        manager.addTask("Cook dinner");
        manager.deleteTask(1);
        manager.listAllTasks();
        String fileContent = new String(Files.readAllBytes(Paths.get(TaskManager.getFilePath())));
        assertFalse(fileContent.contains("Buy groceries"), "java.Task should be deleted.");
    }

    @Test
    void markInProgress() throws IOException {
        manager.addTask("Buy groceries");
        manager.markInProgress(1);
        manager.listAllTasks();
        String fileContent = new String(Files.readAllBytes(Paths.get(TaskManager.getFilePath())));
        assertTrue(fileContent.contains("IN_PROGRESS"), "java.Task status should be 'IN_PROGRESS'.");

    }

    @Test
    void markDone() throws IOException {
        manager.addTask("Buy groceries");
        manager.markDone(1);
        manager.listAllTasks();
        String fileContent = new String(Files.readAllBytes(Paths.get(TaskManager.getFilePath())));
        assertTrue(fileContent.contains("DONE"), "java.Task status should be 'DONE'.");

    }


    @Test
    void listAllTasks() throws IOException {
        manager.addTask("Buy groceries");
        manager.listAllTasks();
        String fileContent = new String(Files.readAllBytes(Paths.get(TaskManager.getFilePath())));
        assertTrue(fileContent.contains("Buy groceries"), "Tasks should include Buy groceries");
    }



    @Test
    void listTasksByStatus() throws IOException {
        manager.addTask("Buy groceries");
        manager.addTask("Cook dinner");
        manager.markDone(1);
        manager.listTasksByStatus("done");
        manager.listAllTasks();
        String fileContent = new String(Files.readAllBytes(Paths.get(TaskManager.getFilePath())));
        assertTrue(fileContent.contains("Buy groceries"), "java.Task with 'DONE' status should be displayed.");
    }
}