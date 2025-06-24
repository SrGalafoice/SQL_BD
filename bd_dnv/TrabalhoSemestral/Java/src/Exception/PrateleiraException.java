package Exception;

public class PrateleiraException extends RuntimeException {
    public PrateleiraException(Exception e) {
        super(e);
    }
}
