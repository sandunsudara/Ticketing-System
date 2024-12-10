package org.example.model;

/**
 * A generic response class that encapsulates the result of an operation.
 * @param <T> The type of the data to be included in the response.
 */
public class Response<T> {
    private boolean isSuccess;
    private String status;
    private String message;
    private T data;

    /**
     * Constructor to initialize a response with the given parameters.
     * @param isSuccess indicates if the operation was successful
     * @param status the status code of the response
     * @param message a message providing more details about the response
     * @param data the actual data of the response, of generic type T
     */
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
