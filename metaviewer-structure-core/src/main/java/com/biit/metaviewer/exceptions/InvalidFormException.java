package com.biit.metaviewer.exceptions;

import java.io.Serial;

public class InvalidFormException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7007411147705660023L;

    public InvalidFormException(String message) {
        super(message);
    }
}
