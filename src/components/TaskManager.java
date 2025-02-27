package components;

import utils.ErrorUtils;
import utils.TaskUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private static String FILE_PATH= "tasks.json";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private List<Task> tasks;


    public TaskManager(){
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
            return json.isEmpty() ? new ArrayList<>(): TaskUtils.parseJson(json);
        }catch(IOException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveTasksToFile(){
        try{
            String json = TaskUtils.convertTasksToJson(tasks);
            Files.write(Paths.get(FILE_PATH), json.getBytes());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void addTask(String description){
        int newId= tasks.size()+1;
        tasks.add(new Task(newId, description));
        saveTasksToFile();
        System.out.println("java.Task added successfully (ID:" + newId+")");
    }

    public void updateTask(int id, String newDescription){
        tasks.stream()
                .filter( task->task.getId()==id )
                .findFirst()
                .ifPresentOrElse ( task -> {
                    task.setDescription(newDescription);
                    task.setUpdatedAt(LocalDateTime.now());
                    saveTasksToFile();
                },
                        ()-> {
                    throw ErrorUtils.taskNotFoundError(id);
                        }
                );
    }

    public void deleteTask(int id){
        tasks.stream()
                .filter( task-> task.getId()==id )
                .findFirst()
                .ifPresentOrElse( task -> {
                    tasks.remove(task);
                    saveTasksToFile();
                },
                        ()-> {
                            throw ErrorUtils.taskNotFoundError(id);
                        }
                        );
    }

    public void markInProgress(int id){
        tasks.stream()
                .filter( task-> task.getId()==id )
                .findFirst()
                .ifPresentOrElse( task-> {
                    task.setStatus(Status.IN_PROGRESS);
                    saveTasksToFile();
                },
                        ()-> {
                            throw ErrorUtils.taskNotFoundError(id);
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
                            saveTasksToFile();
                        },
                        () -> {
                            throw ErrorUtils.taskNotFoundError(id);
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
        System.out.printf("%-5s %-30s %-15s %-20s %-20s%n", "ID", "Description", "java.Status", "Created At", "Last Updated at");
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
java.Task Tracker is a simple CLI application for managing tasks that helps users to track what they need to do, what they're working on, and what they've completed.
 */