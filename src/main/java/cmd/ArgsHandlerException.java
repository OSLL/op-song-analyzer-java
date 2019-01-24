package cmd;

public class ArgsHandlerException extends  Exception {
    public ArgsHandlerException(String message) {
        super(message);
    }

    public  ArgsHandlerException(Throwable cause) {
        super(cause);
    }

    public  ArgsHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
