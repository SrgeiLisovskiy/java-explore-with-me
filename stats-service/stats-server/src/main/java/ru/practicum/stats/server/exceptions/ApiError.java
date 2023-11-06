package ru.practicum.stats.server.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError {
    private HttpStatus status;
    private String error;

    private String description;

    private String stackTrace;

    public ApiError(HttpStatus status, String error, String description, String stackTrace) {
        this.status = status;
        this.error = error;
        this.description = description;
        this.stackTrace = stackTrace;

    }

    public ApiError(String error) {
        this.error = error;
    }

}

