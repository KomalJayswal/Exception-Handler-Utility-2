package net.learning.ExceptionHandlerUtility.exceptions;

import net.learning.ExceptionHandlerUtility.model.DataInputs;
import net.learning.ExceptionHandlerUtility.model.Errors;

import java.util.List;

public class DataNotFoundException extends DataInputs {

    public DataNotFoundException(List<Errors> errorMessage) {
        this.errorMessage = errorMessage;
    }
}

