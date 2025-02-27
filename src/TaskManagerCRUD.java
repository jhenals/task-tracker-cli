import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TaskManagerCRUD {
    private static String FILE_PATH= "tasks.json";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private List<Task> tasks;


    public TaskManagerCRUD(){
        this.tasks= loadTasks();
    }

    public static String getFilePath() {
        return FILE_PATH;
    }

    private List<Task> loadTasks(){
        try{
            if(!Files.exists(Paths.get(FILE_PATH))){
                Files.createFile(Paths.get(FILE_PATH));
                Files.write(Paths.get(FILE_PATH), "[]".getBytes());
            }
            String json = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            return json.isEmpty() ? new ArrayList<>(): parseJson(json);
        }catch(IOException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<Task> parseJson(String json){
        List<Task> taskList= new ArrayList<>();
        String[] tasksArray = json.replace("[", "").replace("]", "").split("},");
        for(String taskJson : tasksArray){
            if(!taskJson.trim().isEmpty()){
                taskJson= taskJson.endsWith("}") ? taskJson: taskJson+ "}";
                Task task = parseTask(taskJson);
                if(task != null){
                    taskList.add(task);
                }
            }
        }
        return taskList;
    }

    private Task parseTask(String taskJson){
        try{
            taskJson = taskJson.replace("{", "").replace("}", "").trim();

            Pattern pattern = Pattern.compile("\"([^\"]+)\":\"([^\"]+)\"|\"([^\"]+)\":([0-9]+)");
            Matcher matcher = pattern.matcher(taskJson);

            int id = 0;
            String description = "";
            String status = "";
            String createdAtStr = "";
            String updatedAtStr = "";

            while (matcher.find()) {
                String key = matcher.group(1); // First capturing group (key)
                String value = matcher.group(2); // Second capturing group (value for string fields)

                // If group 1 and group 2 are null, it means the value is an integer (for the `id` field)
                if (key == null) {
                    key = matcher.group(3); // Third capturing group (key for integer fields)
                    value = matcher.group(4); // Fourth capturing group (value for integer fields)
                }

                // Map the JSON fields to the appropriate variables
                if (key != null) {
                    if (key.equals("id")) {
                        id = Integer.parseInt(value);  // Parse the integer for `id`
                    } else if (key.equals("description")) {
                        description = value;
                    } else if (key.equals("status")) {
                        status = value;
                    } else if (key.equals("createdAt")) {
                        createdAtStr = value;
                    } else if (key.equals("updatedAt")) {
                        updatedAtStr = value;
                    }
                }
            }

            // Define the DateTimeFormatter to match the date-time format in the JSON
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Convert createdAt and updatedAt to LocalDateTime objects
            LocalDateTime createdAt = LocalDateTime.parse(createdAtStr, formatter);
            LocalDateTime updatedAt = LocalDateTime.parse(updatedAtStr, formatter);

            // Create the Task object
            Task task = new Task(id, description);
            task.setId(id);
            task.setStatus(Status.valueOf(status.toUpperCase()));  // Assuming Status is an enum
            task.setCreatedAt(createdAt);
            task.setUpdatedAt(updatedAt);
            return task;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void saveTasks(){
        try{
            String json = convertTasksToJson();
            Files.write(Paths.get(FILE_PATH), json.getBytes());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private String convertTasksToJson() {
        // Manually build the JSON string for tasks (simple version)
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            sb.append("{")
                    .append("\"id\":").append(task.getId()).append(",")
                    .append("\"description\":\"").append(task.getDescription()).append("\",")
                    .append("\"status\":\"").append(task.getStatus()).append("\",")
                    .append("\"createdAt\":\"").append(task.getCreatedAt().format(formatter)).append("\",")
                    .append("\"updatedAt\":\"").append(task.getUpdatedAt().format(formatter)).append("\"")
                    .append("}");
            if (i < tasks.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public void addTask(String description){
        int newId= tasks.size()+1;
        tasks.add(new Task(newId, description));
        saveTasks();
        System.out.println("Task added successfully (ID:" + newId+")");
    }

    public void updateTask(int id, String newDescription){
        tasks.stream()
                .filter( task->task.getId()==id )
                .findFirst()
                .ifPresentOrElse ( task -> {
                    task.setDescription(newDescription);
                    task.setUpdatedAt(LocalDateTime.now());
                    saveTasks();
                },
                        ()-> {
                    throw ErrorUtil.taskNotFoundError(id);
                        }
                );
    }

    public void deleteTask(int id){
        tasks.stream()
                .filter( task-> task.getId()==id )
                .findFirst()
                .ifPresentOrElse( task -> {
                    tasks.remove(task);
                    saveTasks();
                },
                        ()-> {
                            throw ErrorUtil.taskNotFoundError(id);
                        }
                        );
    }

    public void markInProgress(int id){
        tasks.stream()
                .filter( task-> task.getId()==id )
                .findFirst()
                .ifPresentOrElse( task-> {
                    task.setStatus(Status.IN_PROGRESS);
                    saveTasks();
                },
                        ()-> {
                            throw ErrorUtil.taskNotFoundError(id);
                        }
                );
    }

    public void markDone(int id){
        tasks.stream()
                .filter( task -> task.getId()==id )
                .findFirst()
                .ifPresentOrElse(
                        task -> {
                            task.setStatus(Status.DONE);
                            saveTasks();
                        },
                        () -> {
                            throw ErrorUtil.taskNotFoundError(id);
                        }
                );
    }

    public void listAllTasks(){
        printTaskTable(tasks);
    }

    public void listTasksByStatus(String st){
        Status status;

        try{
            String formattedStatus = st.toUpperCase().replace("-", "_");
            status = Status.valueOf(formattedStatus);

        }catch (IllegalArgumentException e){
            System.out.println("Invalid status: "+ st);
            return;
        }

        List<Task> tasksToPrint = new ArrayList<>();

        tasks.stream()
                .filter(task -> task.getStatus() == status)
                .forEach( task -> tasksToPrint.add(task));

        printTaskTable(tasksToPrint);
    }

    private void printTaskTable(List<Task> tasks1) {
        System.out.printf("%-5s %-30s %-15s %-20s %-20s%n", "ID", "Description", "Status", "Created At", "Last Updated at");
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        tasks1.forEach(task -> {
            System.out.printf("%-5d %-30s %-15s %-20s %-20s%n",
                    task.getId(),
                    task.getDescription(),
                    task.getStatus(),
                    task.getCreatedAt().format(formatter),
                    task.getUpdatedAt().format(formatter)
            );
        });
    }


}


/*
Task Tracker is a simple CLI application for managing tasks that helps users to track what they need to do, what they're working on, and what they've completed.
 */