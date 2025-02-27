import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class TaskManagerCRUD {
    private static String FILE_PATH= "tasks.json";
    private List<Task> tasks;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TaskManagerCRUD(){
        this.tasks= loadTasks();
    }

    public TaskManagerCRUD(String filePath) {
        this.FILE_PATH = filePath;
        this.tasks = loadTasks();
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
            String[] fields = taskJson.split(",");

            int id = Integer.parseInt(fields[0].split(":")[1].trim());
            String description = fields[1].split(":")[1].trim();
            description = description.substring(1, description.length() - 1);
            String status = fields[2].split(":")[1].trim().replace("\"", "");
            String createdAt= fields[3].split(":")[1].trim().replace("\"", "");
            String updatedAt= fields[4].split(":")[1].trim().replace("\"", "");
            Task task = new Task( id, description);
            Status st= Status.valueOf(status.toUpperCase());
            task.setStatus(st);
            task.setCreatedAt(LocalDateTime.parse(createdAt,formatter));
            task.setUpdatedAt(LocalDateTime.parse(updatedAt,formatter));
            return task;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void saveTasks(){
        try{
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH));
            writer.write("[");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                // Escape special characters in the description
                String description = task.getDescription().replace("\"", "\\\"");

                writer.write(String.format("{\"id\":%d,\"description\":\"%s\",\"status\":\"%s\",\"createdAt\":\"%s\",\"updatedAt\":\"%s\"}",
                        task.getId(), description, task.getStatus(), task.getCreatedAt(), task.getUpdatedAt()));

                if (i < tasks.size() - 1) {
                    writer.write(",");
                }
            }
            writer.write("]");
        }catch(IOException e){
            e.printStackTrace();
        }
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