package com.ms_auth.auth.dto;

public class ApiErrorDto {
    private String message;
    private String field;

    public ApiErrorDto() {}

    public ApiErrorDto(String message, String field) {
        this.message = message;
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
