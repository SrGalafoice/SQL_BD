package Exception;

public class EmpresaException extends RuntimeException {
    public EmpresaException(Exception e) {
        super(e);
    }
}

