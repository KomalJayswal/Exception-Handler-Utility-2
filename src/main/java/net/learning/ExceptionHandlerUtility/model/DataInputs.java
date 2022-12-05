package net.learning.ExceptionHandlerUtility.model;

import lombok.Getter;

import java.util.List;

@Getter
public class DataInputs extends RuntimeException {

    protected List<Errors> errorMessage;
}
