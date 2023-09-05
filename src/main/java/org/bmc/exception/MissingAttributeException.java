package org.bmc.exception;

public class MissingAttributeException extends RuntimeException {
    public MissingAttributeException(String message) {
        super(message);
    }
}