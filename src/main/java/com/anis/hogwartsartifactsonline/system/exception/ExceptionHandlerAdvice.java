package com.anis.hogwartsartifactsonline.system.exception;

import com.anis.hogwartsartifactsonline.artifact.ArtifactNotFountException;
import com.anis.hogwartsartifactsonline.system.Result;
import com.anis.hogwartsartifactsonline.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ArtifactNotFountException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleArtifactNotFoundException(ArtifactNotFountException ex) {
        return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());

    }
}
