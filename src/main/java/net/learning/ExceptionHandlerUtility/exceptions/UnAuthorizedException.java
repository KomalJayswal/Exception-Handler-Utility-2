package net.learning.ExceptionHandlerUtility.exceptions;

import net.learning.ExceptionHandlerUtility.model.DataInputs;
import net.learning.ExceptionHandlerUtility.model.Errors;

import java.util.List;

public class UnAuthorizedException extends DataInputs {
    public UnAuthorizedException(List<Errors> errorMessage) {
        this.errorMessage = errorMessage;
    }
}
