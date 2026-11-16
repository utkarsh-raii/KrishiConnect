package com.krishiconnect.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private PaginationMeta pagination;
    private List<ValidationErrorDto> errors;

    public ApiResponse() {}

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(boolean success, String message, T data, PaginationMeta pagination) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.pagination = pagination;
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, null, data);
    }

    public static <T> ApiResponse<T> paginated(T data, PaginationMeta pagination) {
        return new ApiResponse<>(true, null, data, pagination);
    }

    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> resp = new ApiResponse<>();
        resp.setSuccess(false);
        resp.setMessage(message);
        return resp;
    }

    public static <T> ApiResponse<T> validationError(String message, List<ValidationErrorDto> errors) {
        ApiResponse<T> resp = new ApiResponse<>();
        resp.setSuccess(false);
        resp.setMessage(message);
        resp.setErrors(errors);
        return resp;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public PaginationMeta getPagination() { return pagination; }
    public void setPagination(PaginationMeta pagination) { this.pagination = pagination; }

    public List<ValidationErrorDto> getErrors() { return errors; }
    public void setErrors(List<ValidationErrorDto> errors) { this.errors = errors; }
}
