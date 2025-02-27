package utils;

import components.Status;
import components.Task;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskUtils {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<Task> parseJson(String json){
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

    private static Task parseTask(String taskJson){
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
                String key = matcher.group(1);
                String value = matcher.group(2);

                if (key == null) {
                    key = matcher.group(3);
                    value = matcher.group(4);
                }

                if (key != null) {
                    if (key.equals("id")) {
                        id = Integer.parseInt(value);
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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime createdAt = LocalDateTime.parse(createdAtStr, formatter);
            LocalDateTime updatedAt = LocalDateTime.parse(updatedAtStr, formatter);

            Task task = new Task(id, description);
            task.setId(id);
            task.setStatus(Status.valueOf(status.toUpperCase()));
            task.setCreatedAt(createdAt);
            task.setUpdatedAt(updatedAt);
            return task;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String convertTasksToJson(List<Task> tasks) {
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


    public static void printManual() {
        StringBuilder sb=new StringBuilder();
        sb.append("----------MANUAL----------\n");
        sb.append("*Adding a new task:\n");
        sb.append("add <id> \"<description>\"\n");
        sb.append("*Updating an existing task:\n");
        sb.append("update <id>\n");
        sb.append("*Deleting an existing task:\n");
        sb.append("delete <id>\n");
        sb.append("*Marking a task as in progress or done:\n");
        sb.append("mark-in-progress <id>\n");
        sb.append("mark-done <id>\n");
        sb.append("*Listing all tasks:\n");
        sb.append("list\n");
        sb.append("*Listing tasks by status:\n");
        sb.append("list done\n");
        sb.append("list todo\n");
        sb.append("list in-progress\n");
        System.out.println(sb);
    }


}
