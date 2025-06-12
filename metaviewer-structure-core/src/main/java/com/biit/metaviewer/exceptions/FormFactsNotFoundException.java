package com.biit.metaviewer.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.metaviewer.logger.LoggedException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class FormFactsNotFoundException extends LoggedException {

    @Serial
    private static final long serialVersionUID = -1693088206424719197L;

    public FormFactsNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type, HttpStatus.NOT_FOUND);
    }

    public FormFactsNotFoundException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.WARNING, HttpStatus.NOT_FOUND);
    }

    public FormFactsNotFoundException(Class<?> clazz) {
        this(clazz, "Comment not found");
    }

    public FormFactsNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
