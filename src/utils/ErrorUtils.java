package utils;

public class ErrorUtils {
    private ErrorUtils(){
        throw new UnsupportedOperationException("Utility class");
    }

    public static IllegalArgumentException taskNotFoundError(int id){
        return new IllegalArgumentException("java.Task with ID:"+id+" not found.");
    }
}
