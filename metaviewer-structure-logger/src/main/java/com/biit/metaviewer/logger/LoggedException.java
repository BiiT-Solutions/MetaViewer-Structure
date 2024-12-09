package com.biit.metaviewer.logger;


import com.biit.logger.ExceptionType;
import org.springframework.http.HttpStatus;

public class LoggedException extends RuntimeException {
    private HttpStatus status;

    public LoggedException(Class<?> clazz, String message, ExceptionType type) {
        this(clazz, message, type, null);
    }

    public LoggedException(Class<?> clazz, String message, Throwable e, ExceptionType type, HttpStatus status) {
        this(clazz, message, type, status);
        MetaViewerLogger.errorMessage(clazz, e);
    }

    protected LoggedException(Class<?> clazz, String message, ExceptionType type, HttpStatus status) {
        super(message);
        this.status = status;
        final String className = clazz.getName();
        switch (type) {
            case INFO:
               MetaViewerLogger.info(className, message);
                break;
            case WARNING:
                MetaViewerLogger.warning(className, message);
                break;
            case SEVERE:
                MetaViewerLogger.severe(className, message);
                break;
            default:
                MetaViewerLogger.debug(className, message);
                break;
        }
    }

    protected LoggedException(Class<?> clazz, Throwable e, HttpStatus status) {
        this(clazz, e);
        this.status = status;
    }

    protected LoggedException(Class<?> clazz, Throwable e) {
        super(e);
        MetaViewerLogger.errorMessage(clazz, e);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
