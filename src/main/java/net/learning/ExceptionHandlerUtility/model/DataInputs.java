package net.learning.ExceptionHandlerUtility.model;

import lombok.Getter;

@Getter
public class DataInputs extends RuntimeException {

    protected int httpStatus;
    protected String errorMessage;
}
