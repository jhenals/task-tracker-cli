public class ErrorUtil {
    private ErrorUtil(){
        throw new UnsupportedOperationException("Utility class");
    }

    public static IllegalArgumentException taskNotFoundError(int id){
        return new IllegalArgumentException("Task with ID:"+id+" not found.");
    }
}
