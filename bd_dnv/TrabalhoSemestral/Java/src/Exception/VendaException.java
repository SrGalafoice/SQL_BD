package Exception;

public class VendaException extends RuntimeException {
    public VendaException(Exception e) {
      super(e);
    }
}
