import java.io.IOException;
import java.util.List;
import java.util.Scanner;
public class TaskCLI {
    TaskManagerCRUD manager = new TaskManagerCRUD();

    public void executeCommand(String command) throws IOException {
        List<String> token= ParserLexer.parseCommand(command);

        String action= token.get(0);
        int id;

        switch(action){
            case "man": //manual
                printManual();
                break;
            case "add":
                manager.addTask(token.get(1));
                break;
            case "update":
                id= Integer.parseInt(token.get(1));
                manager.updateTask(id, token.get(2));
                break;
            case "delete":
                id= Integer.parseInt(token.get(1));
                manager.deleteTask(id);
                break;
            case "mark-in-progress":
                id= Integer.parseInt(token.get(1));
                manager.markInProgress(id);
                break;
            case "mark-done":
                id= Integer.parseInt(token.get(1));
                manager.markDone(id);
                break;
            case "list":
                if(token.size()==1){
                    manager.listAllTasks();
                }else{
                    manager.listTasksByStatus(token.get(1));
                }
                break;
            default:
                System.out.println("Invalid command! Type man to see options.");
                break;

        }
    }


    private void printManual() {
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

    public static void main(String[] args) throws IOException {
        TaskCLI cli= new TaskCLI();
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n");
        System.out.println("-----Welcome to your Task Tracker-----");
        System.out.println("Type man for help");
        System.out.println("---------------------------------------");


        while(true){
            System.out.println("task-cli> ");
            String command = scanner.nextLine();
            cli.executeCommand(command);
        }
    }


}
