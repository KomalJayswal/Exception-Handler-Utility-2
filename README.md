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


## Getting Started With Solution

1. Please install Java 11 and maven in your system.
2. Create a springboot project using https://start.spring.io. <br>
* Select `Maven Project` 
* Add Dependency : `lombok and spring-web` 
* Group : `net.learning`
* Artifact : `ExceptionHandlerUtility`


3. Create a input request model as a child class of `RuntimeException`.
```java
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DataInputs extends RuntimeException {

  protected HttpStatus httpStatus;
  protected String errorMessage;
} 
```
4. Create an Exception class as a child class of `DataInputs` and super child class of `RuntimeException`.
```java
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends DataInputs {
  public CustomException(HttpStatus httpStatus, String errorMessage) {
    this.httpStatus = httpStatus;
    this.errorMessage = errorMessage;
  }
}
```
<I>Or you can also merge the `DataInputs` and `CustomException` class into one comman class. </I>

5. Create `Errors` Model contains the error message.
```java
import lombok.*;

@Getter
@Builder
@Setter
@Data
public class Errors {

    protected String errorMessage;
}
```

6. Create `ErrorResponse` Model that contains 
* fields of the error response structure
* DateTimeFormatter to fetch the current data-time
* parameterize constructors to set each value of the fields
```java

import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 * Class representing an API error in order to provide details of exceptions thrown back to the client
 */
@Data
public class ErrorResponse {

  private HttpMethod method;
  private String requestUri;
  private HttpStatus statusCode;
  private String timestamp;
  private List<Errors> errors;

  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  public ErrorResponse(){

  }

  public ErrorResponse(HttpMethod method, String requestUri, HttpStatus statusCode) {
    this();
    this.method = method;
    this.requestUri = requestUri;
    this.statusCode = statusCode;
    timestamp = dateFormatter.format(LocalDateTime.now(ZoneOffset.UTC));
    this.errors = buildErrors();
  }

  public List<Errors> buildErrors(){
    return List.of(Errors.builder().errorMessage("customized error").build());
  }
}
```

7. Create `GlobalExceptionHandler` class which contains the Handler method for `CustomException`.
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleOhmValidationException(CustomException customException,
                                                                    ServletWebRequest servletWebRequest) {
    ErrorResponse apiError = new ErrorResponse(servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getRequestURI(),
            customException.getHttpStatus());

    return ResponseEntity
            .status(customException.getHttpStatus())
            .body(apiError);
  }
} 
```

8. Now, execute `mvn clean install` command to create the JAR

9. Once, build is done, you can now see the repository JAR is created in your .m2 folder

**Congratulation! Exception Handler JAR is successfully created.** 

Now, Import this as a maven jar dependency in your applications
(_where you want to use the custom exception handler_ )
```bash
<dependency>
    <groupId>net.learning</groupId>
    <artifactId>ExceptionHandlerUtility</artifactId>
    <version>1.0.0</version>
 </dependency>
```
You can refer [IntegrateCustomException](https://github.com/KomalJayswal/IntegrateCustomException.git) to check the steps to integrate the created exception Handler.

[//]: # (## References)

## FAQs


