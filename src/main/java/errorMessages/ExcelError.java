package errorMessages;

import com.google.gson.annotations.Expose;

public class ExcelError extends ErrorMessage {
    private static final int EXCEL_ERROR = -1;

    @Expose private final int line;

    public ExcelError(int line) {
        super(ExcelError.EXCEL_ERROR);
        this.line = line;
    }

    public int getLine() {
        return this.line;
    }

    @Override
    protected void initCodeMessages() {
        super.initCodeMessages();
        this.getCodeMessages().put(ExcelError.EXCEL_ERROR, "Excel error");
    }
}
