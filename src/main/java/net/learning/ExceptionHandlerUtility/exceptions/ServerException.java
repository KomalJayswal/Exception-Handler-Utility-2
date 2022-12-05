package net.learning.ExceptionHandlerUtility.exceptions;

import net.learning.ExceptionHandlerUtility.model.DataInputs;
import net.learning.ExceptionHandlerUtility.model.Errors;

import java.util.List;

public class ServerException extends DataInputs {
    public ServerException(List<Errors> errorMessage) {
        this.errorMessage = errorMessage;
    }
}
