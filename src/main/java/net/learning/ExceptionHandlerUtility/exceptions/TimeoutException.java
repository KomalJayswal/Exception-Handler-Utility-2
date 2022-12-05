package net.learning.ExceptionHandlerUtility.exceptions;

import net.learning.ExceptionHandlerUtility.model.DataInputs;
import net.learning.ExceptionHandlerUtility.model.Errors;

import java.util.List;

public class TimeoutException extends DataInputs {

    public TimeoutException(List<Errors> errorMessage) {
        this.errorMessage = errorMessage;
    }
}
