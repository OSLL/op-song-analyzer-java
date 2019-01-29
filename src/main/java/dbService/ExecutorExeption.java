package dbService;

import java.io.IOException;

public class ExecutorExeption extends IOException {
    public  ExecutorExeption(String message) {
        super(message);
    }

    public  ExecutorExeption(Throwable cause) {
        super(cause);
    }

    public  ExecutorExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
