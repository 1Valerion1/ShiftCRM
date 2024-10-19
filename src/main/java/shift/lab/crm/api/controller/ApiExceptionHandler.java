package shift.lab.crm.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import shift.lab.crm.core.exception.BadRequestException;
import shift.lab.crm.core.exception.ConflictException;
import shift.lab.crm.core.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handle(NotFoundException exception) {
        return new ResponseEntity<>( exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handle(ConflictException exception) {
        return new ResponseEntity<>( exception.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handle(BadRequestException exception) {
        return new ResponseEntity<>( exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatchExceptions(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "Неверный формат параметра: " + ex.getName();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
