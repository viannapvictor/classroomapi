package br.edu.infnet.classroomapi.infrastructure.web.response;

import lombok.Getter;

@Getter
public final class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final ErrorData error;
    private final String message;

    public ApiResponse(boolean success, T data, ErrorData error, String message) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, "Operation completed successfully");
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, null, message);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, null, null, "Operation completed successfully");
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        ErrorData error = new ErrorData(code, message);
        return new ApiResponse<>(false, null, error, null);
    }

    public static <T> ApiResponse<T> error(ErrorData error) {
        return new ApiResponse<>(false, null, error, null);
    }
}