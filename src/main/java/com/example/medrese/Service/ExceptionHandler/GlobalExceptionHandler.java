package com.example.medrese.Service.ExceptionHandler;


import com.example.medrese.Core.Util.Exceptions.ArticleAlreadyExsitsException;
import com.example.medrese.Core.Util.Exceptions.Global.ErrorResponse;
import com.example.medrese.Core.Util.Exceptions.Global.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
       log.error(ex);
        List<String> errors = ex.getBindingResult()
                .getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public  ResponseEntity<Object> handleException(NotFoundException ex) {
        log.error(ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public  ResponseEntity<Object> handleException(HttpMessageNotReadableException ex) {
        ErrorResponse response = new ErrorResponse(ex.getLocalizedMessage().split(":")[0], HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public  ResponseEntity<Object> handleException(IllegalArgumentException ex) {
        log.error(ex);
        ErrorResponse response = new ErrorResponse(ex.getLocalizedMessage().split(":")[0], HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public  ResponseEntity<Object> handleException(HttpRequestMethodNotSupportedException ex) {
        log.error(ex);
        ErrorResponse response = new ErrorResponse(ex.getLocalizedMessage().split(":")[0], HttpStatus.METHOD_NOT_ALLOWED.value());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public  ResponseEntity<Object> handleException(DataIntegrityViolationException ex) {
        // get the message from the exception that which column is already exsits
        log.error(ex);
        ErrorResponse response = new ErrorResponse(ex.getMostSpecificCause().getMessage()
                .split("=")[1]
                .replaceAll("[()]","'"), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ArticleAlreadyExsitsException.class)
    public  ResponseEntity<Object> handleArticleException(ArticleAlreadyExsitsException ex) {
        log.error(ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public  ResponseEntity<Object> handleArticleException(Exception ex) {
        log.error(ex);
        ex.printStackTrace();
        ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


}
