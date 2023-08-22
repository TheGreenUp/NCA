package ru.green.nca.exceptions;

import lombok.Data;

@Data
public class ErrorObject {
    private  Integer statusCode;
    private String message;
    private long timestamp;

}
