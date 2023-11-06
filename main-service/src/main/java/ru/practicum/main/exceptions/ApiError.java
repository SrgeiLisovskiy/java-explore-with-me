package ru.practicum.main.exceptions;

import lombok.Getter;

@Getter
public class ApiError {
    private int status;
    private String error;

    private String description;

    private String stackTrace;

    public ApiError(int status, String error, String description, String stackTrace) {
        this.status = status;
        this.error = error;
        this.description = description;
        this.stackTrace = stackTrace;

    }

    public ApiError(String error) {
        this.error = error;
    }

}

