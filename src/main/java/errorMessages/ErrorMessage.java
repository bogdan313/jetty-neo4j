package errorMessages;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessage {
    @Expose
    private String message;
    @Expose
    private int code = 0;
    private Map<Integer, String> codeMessages = new HashMap<>();

    public static final int NOT_AUTHORIZED = 1;
    public static final int INCORRECT_PARAMETERS = 2;
    public static final int RECORD_NOT_FOUND = 3;
    public static final int RECORD_EXISTS = 4;
    public static final int PERMISSIONS_DENIED = 5;
    public static final int SERVER_ERROR = 6;
    public static final int INCORRECT_PASSWORD = 7;

    public ErrorMessage(int code) {
        this.code = code;
        this.initCodeMessages();
    }

    public String getMessage() {
        return this.message;
    }
    public int getCode() {
        return this.code;
    }
    public Map<Integer, String> getCodeMessages() {
        return this.codeMessages;
    }

    protected void initCodeMessages() {
        this.codeMessages.put(1, "Not authorized");
        this.codeMessages.put(2, "Incorrect parameters");
        this.codeMessages.put(3, "Record not found");
        this.codeMessages.put(4, "Record already exists");
        this.codeMessages.put(5, "Permissions denied");
        this.codeMessages.put(6, "Server error");
        this.codeMessages.put(7, "Incorrect password");

        this.message = this.getCodeMessages().get(this.code);
    }
}
