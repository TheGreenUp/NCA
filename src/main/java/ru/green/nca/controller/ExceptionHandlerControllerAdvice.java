package ru.green.nca.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.green.nca.exceptions.*;
/**
 * Обработчик исключений для контроллеров.
 */
@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    /**
     * Обрабатывает исключение {@link ResourceNotFoundException}.
     *
     * @param ex исключение {@link ResourceNotFoundException}.
     * @return Ответ с информацией об ошибке и статусом NOT_FOUND.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorObject> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }
    /**
     * Обрабатывает исключение {@link NoDataFoundException}.
     *
     * @param ex исключение {@link NoDataFoundException}.
     * @return Ответ с информацией об ошибке и статусом NO_CONTENT.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorObject> handleNoDataFoundException(NoDataFoundException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NO_CONTENT.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorObject, HttpStatus.OK);
    }
    /**
     * Обрабатывает исключение {@link ConflictException}.
     *
     * @param ex исключение {@link ConflictException}.
     * @return Ответ с информацией об ошибке и статусом CONFLICT.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorObject> handleConflictException(ConflictException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.CONFLICT.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorObject, HttpStatus.CONFLICT);
    }
    /**
     * Обрабатывает исключение {@link ForbiddenException}.
     *
     * @param ex исключение {@link ForbiddenException}.
     * @return Ответ с информацией об ошибке и статусом FORBIDDEN.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorObject> handleForbiddenException(ForbiddenException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.FORBIDDEN.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorObject, HttpStatus.FORBIDDEN);
    }
}
