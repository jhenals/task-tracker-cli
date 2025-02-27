public class TaskTest {
    public static void main(String[] args) {
        testTaskCreation();
        testTaskStatusUpdate();
        System.out.println("All tests passed!");
    }

    static void testTaskCreation() {
        Task task = new Task(1, "Buy groceries");
        assert task.getId() == 1 : "Task ID should be 1";
        assert task.getDescription().equals("Buy groceries") : "Description should match";
    }

    static void testTaskStatusUpdate() {
        Task task = new Task(1, "Buy groceries");
        task.setStatus(Status.IN_PROGRESS);
        assert task.getStatus() == Status.IN_PROGRESS : "Task status should be IN_PROGRESS";
    }
}
