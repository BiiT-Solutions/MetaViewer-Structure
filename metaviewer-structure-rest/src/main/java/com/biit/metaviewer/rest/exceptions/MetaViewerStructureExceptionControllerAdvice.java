package com.biit.metaviewer.rest.exceptions;

import com.biit.kafka.exceptions.InvalidEventException;
import com.biit.metaviewer.exceptions.FormFactsNotFoundException;
import com.biit.metaviewer.exceptions.InvalidFormException;
import com.biit.metaviewer.logger.MetaViewerLogger;
import com.biit.server.exceptions.ErrorResponse;
import com.biit.server.exceptions.NotFoundException;
import com.biit.server.exceptions.ServerExceptionControllerAdvice;
import com.biit.usermanager.client.exceptions.ElementNotFoundException;
import com.biit.usermanager.client.exceptions.InvalidConfigurationException;
import com.biit.usermanager.client.exceptions.InvalidValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MetaViewerStructureExceptionControllerAdvice extends ServerExceptionControllerAdvice {


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundException(Exception ex) {
        MetaViewerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse("NOT_FOUND", "not_found", ex), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(InvalidFormException.class)
    public ResponseEntity<Object> invalidFormException(Exception ex) {
        MetaViewerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse("BAD_REQUEST", "invalid_form", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEventException.class)
    public ResponseEntity<Object> invalidEventException(Exception ex) {
        MetaViewerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "cannot_connect_to_kafka", ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<Object> elementNotFoundException(Exception ex) {
        MetaViewerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "not_found", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidConfigurationException.class)
    public ResponseEntity<Object> invalidConfigurationException(Exception ex) {
        MetaViewerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "invalid_configuration_exception", ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<Object> invalidValueException(Exception ex) {
        MetaViewerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "invalid_parameter", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FormFactsNotFoundException.class)
    public ResponseEntity<Object> formFactsNotFoundException(Exception ex) {
        MetaViewerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "facts_not_found", ex), HttpStatus.NOT_FOUND);
    }
}


