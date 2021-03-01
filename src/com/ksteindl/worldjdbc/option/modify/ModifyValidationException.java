package com.ksteindl.worldjdbc.option.modify;

public class ModifyValidationException extends RuntimeException{

    public ModifyValidationException(String message) {
        super(message);
    }

    public ModifyValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
