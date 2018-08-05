package org.stackit.shop;

import org.stackit.StackItException;

public class StackItShopException extends StackItException {
    public StackItShopException() {
    }

    public StackItShopException(String message) {
        super(message);
    }

    public StackItShopException(String message, Throwable cause) {
        super(message, cause);
    }

    public StackItShopException(Throwable cause) {
        super(cause);
    }

    public StackItShopException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
