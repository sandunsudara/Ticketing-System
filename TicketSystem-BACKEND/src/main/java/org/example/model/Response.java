package org.example.model;

public class Response<T> {
    private boolean isSuccess;
    private String status;
    private String message;
    private T data;

    public Response(boolean isSuccess, String status, String message, T data) {
        this.isSuccess = isSuccess;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
