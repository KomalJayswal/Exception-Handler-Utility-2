# Exception Handler Utility

This is a utility tool which can be used in other services for **handling the user-defined or custom exceptions for both checked and unchecked exceptions**

## Problem Statement

Design a Generic Custom Exception Handler Utility.
It should be used as a JAR is multiple Microservices in order to avoid handling custom exceptions in each Individual Microservice. 
Expected Error Response Structure (for example):-
```json
Header contains the HTTP Status : 400
{
    "method": "GET",
    "requestUri": "/sample/getData",
    "statusCode": "BAD_REQUEST",
    "timestamp": "2022-06-16T09:24:15",
    "errors": [
        {
            "errorMessage": "<customized error>"
        }
    ]
}
```
### Requirement
HTTP Status and Error message for each of the following Exceptions are pre-defined. Consumers/Users are only allowed to provide the error messages. Created individual handler for each of the following exceptions :
* DataNotFoundException
* UnAuthorizedException
* AccessDeniedException
* ServerException
* NoHandlerFoundException
* BindException
* AccessDeniedException
* MissingServletRequestParameterException, ConstraintViolationException, HttpMediaTypeException, MissingRequestHeaderException, HttpMessageNotReadableException
* ResourceAccessException
* Exception 
* TimeoutException

Use https://github.com/KomalJayswal/IntegrateExceptionHandler02 to test.



