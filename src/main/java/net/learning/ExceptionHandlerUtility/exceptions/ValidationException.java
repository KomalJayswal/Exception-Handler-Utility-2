package net.learning.ExceptionHandlerUtility.exceptions;

import net.learning.ExceptionHandlerUtility.model.DataInputs;
import net.learning.ExceptionHandlerUtility.model.Errors;

import java.util.List;

public class ValidationException extends DataInputs {

    public ValidationException(List<Errors> errorMessage) {

        this.errorMessage = errorMessage;
    }
}
