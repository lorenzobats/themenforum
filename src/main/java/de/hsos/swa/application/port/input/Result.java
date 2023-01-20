package de.hsos.swa.application.port.input;

public class Result<T> {

    private T data;
    private boolean isSuccessful;
    private String errorMessage;
    private Exception exception;

    public Result() {}

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.data = data;
        result.isSuccessful = true;
        return result;
    }

    public static <T> Result<T> error(String errorMessage) {
        Result<T> result = new Result<>();
        result.errorMessage = errorMessage;
        result.isSuccessful = false;
        return result;
    }

    public static <T> Result<T> exception(Exception exception) {
        Result<T> result = new Result<>();
        result.isSuccessful = false;
        result.exception = exception;
        return result;
    }

    public T getData() {
        if(this.isSuccessful || data != null) {
            return data;
        } else {
            throw new NullPointerException("Data isn't initialized");
        }
    }

    public Exception getException() {
        return exception;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


}
