package shift.lab.crm.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import shift.lab.crm.core.exception.NotFoundException;


@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handle(NotFoundException exception) {
        return new ResponseEntity<>("Не найдено: " + exception.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

}
