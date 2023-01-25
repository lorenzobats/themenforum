package de.hsos.swa.application.util;

public class Result<T> {

    private T data;

    private ResultStatus status;
    private boolean isSuccessful;
    private String errorMessage = "";

    public Result() {}

    public static <T> Result<T> isSuccessful(T data) {
        Result<T> result = new Result<>();
        result.data = data;
        result.status = ResultStatus.SUCCESS;
        result.isSuccessful = true;
        return result;
    }

    public static <T> Result<T> error(String errorMessage) {
        Result<T> result = new Result<>();
        result.errorMessage = errorMessage;
        result.isSuccessful = false;
        return result;
    }

    public static <T> Result<T> notFound() {
        Result<T> result = new Result<>();
        result.status = ResultStatus.NOT_FOUND;
        result.isSuccessful = false;
        return result;
    }

    public static <T> Result<T> exception() {
        Result<T> result = new Result<>();
        result.status = ResultStatus.EXCEPTION;
        result.isSuccessful = false;
        return result;
    }

    public T getData() {
        if(this.isSuccessful || data != null) {
            return data;
        } else {
            throw new NullPointerException("Data isn't initialized");
        }
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public String getMessage() {
        return errorMessage;
    }


}