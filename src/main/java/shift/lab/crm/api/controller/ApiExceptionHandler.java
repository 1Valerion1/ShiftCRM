package shift.lab.crm.api.controller;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import shift.lab.crm.core.exception.SellerNotFoundException;


@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(SellerNotFoundException.class)
    public ResponseEntity<Object> handle(SellerNotFoundException exception) {
        return new ResponseEntity<>("Не найдено: " + exception.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

}
