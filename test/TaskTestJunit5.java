import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTestJunit5 {
    private Task task;

    @BeforeEach
    void setup(){
        task = new Task(1, "Buy groceries");
        task.setStatus(Status.TODO);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void getId() {
        assertEquals(1,task.getId(), "Task ID should be 1");
    }

    @Test
    void setId() {
        task.setId(2);
        assertEquals(2,task.getId(), "Task ID should be updated to 2");
    }

    @Test
    void getDescription() {
        assertEquals("Buy groceries", task.getDescription(), "Description should match");
    }

    @Test
    void setDescription() {
        task.setDescription("Buy groceries and cook");
        assertEquals("Buy groceries and cook", task.getDescription(), "Description should be updated");
    }

    @Test
    void getStatus() {
        assertEquals(Status.TODO, task.getStatus(), "Initial status should be TODO");
    }

    @Test
    void setStatus() {
        task.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, task.getStatus(), "Status should be updated to IN_PROGRESS");
    }

    @Test
    void getCreatedAt() {
        assertNotNull(task.getCreatedAt(), "CreatedAt should not be null");
    }

    @Test
    void setCreatedAt() {
        LocalDateTime newTime = LocalDateTime.of(2025, 1, 1, 12, 0);
        task.setCreatedAt(newTime);
        assertEquals(newTime, task.getCreatedAt(), "CreatedAt should be updated");
    }

    @Test
    void getUpdatedAt() {
        assertNotNull(task.getUpdatedAt(), "UpdatedAt should not be null");
    }

    @Test
    void setUpdatedAt() {
        LocalDateTime newTime = LocalDateTime.of(2025, 1, 1, 12, 0);
        task.setUpdatedAt(newTime);
        assertEquals(newTime, task.getUpdatedAt(), "UpdatedAt should be updated");
    }
}