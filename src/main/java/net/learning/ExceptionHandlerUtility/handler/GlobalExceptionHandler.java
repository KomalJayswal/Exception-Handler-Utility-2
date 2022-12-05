package net.learning.ExceptionHandlerUtility.handler;

import lombok.extern.slf4j.Slf4j;
import net.learning.ExceptionHandlerUtility.exceptions.*;
import net.learning.ExceptionHandlerUtility.model.ErrorResponse;
import net.learning.ExceptionHandlerUtility.model.Errors;
import net.learning.ExceptionHandlerUtility.utils.Constants;
import net.learning.ExceptionHandlerUtility.utils.ExceptionHandlerHelperUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Global Exception Handler that handles generic as well as Custom Errors/Exceptions
 */
@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    /**
     * Handler Method to return Error Response Object for ValidationException
     *
     * @param validationException
     *            to take as input
     *
     * @return the constructed ErrorResponse
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleOhmValidationException(ValidationException validationException,
                                                                         ServletWebRequest servletWebRequest) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .method(Objects.requireNonNull(servletWebRequest.getHttpMethod()))
                        .requestUri(servletWebRequest.getRequest().getRequestURI())
                        .statusCode(HttpStatus.BAD_REQUEST.name())
                        .timestamp(ExceptionHandlerHelperUtil.currentTimeStamp())
                        .errors(validationException.getErrorMessage())
                        .build(),HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler Method to return Error Response Object for DataNotFoundException
     *
     * @param dataNotFoundException
     *            to take as input
     *
     * @return the constructed ErrorResponse
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOhmDataNotFoundException(DataNotFoundException dataNotFoundException,
                                                                           ServletWebRequest servletWebRequest) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .method(Objects.requireNonNull(servletWebRequest.getHttpMethod()))
                        .requestUri(servletWebRequest.getRequest().getRequestURI())
                        .statusCode(HttpStatus.NOT_FOUND.name())
                        .timestamp(ExceptionHandlerHelperUtil.currentTimeStamp())
                        .errors(dataNotFoundException.getErrorMessage())
                        .build(),HttpStatus.NOT_FOUND);
    }

    /**
     * Handler Method to return Error Response Object for UnAuthorizedException
     *
     * @param unAuthorizedException
     *            to take as input
     *
     * @return the constructed ErrorResponse
     */
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleOhmUnAuthorizedException(UnAuthorizedException unAuthorizedException,
                                                                           ServletWebRequest servletWebRequest) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .method(Objects.requireNonNull(servletWebRequest.getHttpMethod()))
                        .requestUri(servletWebRequest.getRequest().getRequestURI())
                        .statusCode(HttpStatus.UNAUTHORIZED.name())
                        .timestamp(ExceptionHandlerHelperUtil.currentTimeStamp())
                        .errors(unAuthorizedException.getErrorMessage())
                        .build(),HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handler Method to return Error Response Object for AccessDeniedException
     *
     * @param accessDeniedException
     *            to take as input
     *
     * @return the constructed ErrorResponse
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleOhmAccessDeniedException(AccessDeniedException accessDeniedException,
                                                                           ServletWebRequest servletWebRequest) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .method(Objects.requireNonNull(servletWebRequest.getHttpMethod()))
                        .requestUri(servletWebRequest.getRequest().getRequestURI())
                        .statusCode(HttpStatus.FORBIDDEN.name())
                        .timestamp(ExceptionHandlerHelperUtil.currentTimeStamp())
                        .errors(accessDeniedException.getErrorMessage())
                        .build(),HttpStatus.FORBIDDEN);
    }

    /**
     * Handler Method to return Error Response Object for ServerException
     *
     * @param serverException
     *            to take as input
     *
     * @return the constructed ErrorResponse
     */
    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ErrorResponse> handleOhmServerException(ServerException serverException,
                                                                     ServletWebRequest servletWebRequest) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .method(Objects.requireNonNull(servletWebRequest.getHttpMethod()))
                        .requestUri(servletWebRequest.getRequest().getRequestURI())
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                        .timestamp(ExceptionHandlerHelperUtil.currentTimeStamp())
                        .errors(serverException.getErrorMessage())
                        .build(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handler Method to return Error Response Object for no mapping resource exceptions
     *
     * @param noHandlerFoundException
     *            to take as input
     *
     * @return the constructed ErrorResponse
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(NoHandlerFoundException noHandlerFoundException,
                                                                        ServletWebRequest servletWebRequest) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .method(Objects.requireNonNull(servletWebRequest.getHttpMethod()))
                        .requestUri(servletWebRequest.getRequest().getRequestURI())
                        .statusCode(HttpStatus.NOT_FOUND.name())
                        .timestamp(ExceptionHandlerHelperUtil.currentTimeStamp())
                        .errors(List.of(Errors.builder().errorMessage(noHandlerFoundException.getMessage()).build()))
                        .build(),
                HttpStatus.NOT_FOUND);
    }

    /**
     * Handler Method to return Error Response Object for Spring binding related exceptions
     *
     * @param bindException
     *            to take as input
     *
     * @return the constructed ErrorResponse
     */
    @ExceptionHandler({ BindException.class, MethodArgumentNotValidException.class })
    public ResponseEntity<ErrorResponse> handleBindException(BindException bindException,
                                                                ServletWebRequest servletWebRequest) {

        List<Errors> errorMessage = ExceptionHandlerHelperUtil.createErrorListFromValidationErrors(bindException.getBindingResult().getAllErrors());

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .method(Objects.requireNonNull(servletWebRequest.getHttpMethod()))
                        .requestUri(servletWebRequest.getRequest().getRequestURI())
                        .statusCode(HttpStatus.BAD_REQUEST.name())
                        .timestamp(ExceptionHandlerHelperUtil.currentTimeStamp())
                        .errors(errorMessage)
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler Method to return Error Response Object for AccessDeniedException Errors/Exceptions
     *
     * @param accessDeniedException
     *            to take as input
     *
     * @return the constructed ErrorResponse
     */
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException accessDeniedException,
                                                                        ServletWebRequest servletWebRequest) {

        return new ResponseEntity<>(ErrorResponse.builder()
                    .method(Objects.requireNonNull(servletWebRequest.getHttpMethod()))
                    .requestUri(servletWebRequest.getRequest().getRequestURI())
                    .statusCode(HttpStatus.FORBIDDEN.name())
                    .timestamp(ExceptionHandlerHelperUtil.currentTimeStamp())
                    .errors(List.of(Errors.builder().errorMessage(Constants.ACCESS_DENIED_ERROR_MESSAGE).build())).build(),
                HttpStatus.FORBIDDEN);
    }

    /**
     * ExceptionHandler for handling all MissingServletRequestParameter or ConstraintViolationException Exceptions.
     *
     * @param exception
     *            take any instance of MissingServletRequestParameterException or ConstraintViolationException or
     *            HttpMediaTypeException as input
     *
     * @return the constructed ErrorResponse
     */
    @ExceptionHandler({ MissingServletRequestParameterException.class, ConstraintViolationException.class,
            HttpMediaTypeException.class, MissingRequestHeaderException.class, HttpMessageNotReadableException.class })
    public ResponseEntity<ErrorResponse> handleRequestException(Exception exception,
                                                                   ServletWebRequest servletWebRequest) {
        List<Errors> errorList;
        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception;
            List<String> errorsMessages = constraintViolationException.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage).collect(Collectors.toList());
            errorList = errorsMessages.stream().map(
                            e -> Errors.builder().errorMessage(e).build())
                    .collect(Collectors.toList());
        } else if (exception instanceof HttpMessageNotReadableException) {
            errorList = List.of(Errors.builder()
                    .errorMessage(exception.getMessage().split(Constants.SEPARATOR)[0]).build());
        } else {
            errorList = List.of(Errors.builder()
                    .errorMessage(exception.getMessage()).build());
        }
        return new ResponseEntity<>(ErrorResponse.builder()
                .method(HttpMethod.valueOf(ExceptionHandlerHelperUtil.validateServletWebRequestForHttpMethod(servletWebRequest)))
                .requestUri(ExceptionHandlerHelperUtil.validateServletWebRequestForUri(servletWebRequest))
                .statusCode(HttpStatus.BAD_REQUEST.name())
                .timestamp(ExceptionHandlerHelperUtil.currentTimeStamp())
                .errors(List.of(Errors.builder().errorMessage(Constants.ACCESS_DENIED_ERROR_MESSAGE).build())).build(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler Method to return Error Response Object for ResourceAccessException Errors/Exceptions
     *
     * @param resourceAccessException
     *            to take as input
     *
     * @return the constructed ErrorResponse
     */
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> handleResourceAccessException(ResourceAccessException resourceAccessException,
                                                                          ServletWebRequest servletWebRequest) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .method(HttpMethod.valueOf(ExceptionHandlerHelperUtil.validateServletWebRequestForHttpMethod(servletWebRequest)))
                .requestUri(ExceptionHandlerHelperUtil.validateServletWebRequestForUri(servletWebRequest))
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .timestamp(ExceptionHandlerHelperUtil.currentTimeStamp())
                .errors(List.of(Errors.builder().errorMessage(resourceAccessException.getMessage()).build())).build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handler Method to return Error Response Object for all unhandled Exceptions
     *
     * @param exception
     *            take any instance of Exception as input
     *
     * @return the constructed ErrorResponse
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception, ServletWebRequest servletWebRequest) {

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timestamp(ExceptionHandlerHelperUtil.currentTimeStamp())
                        .method(HttpMethod.valueOf(ExceptionHandlerHelperUtil.validateServletWebRequestForHttpMethod(servletWebRequest)))
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                        .requestUri(ExceptionHandlerHelperUtil.validateServletWebRequestForUri(servletWebRequest))
                        .errors(List.of(Errors.builder().errorMessage("Unexpected error occurred").build()))
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handler Method to return Error Response Object for Time out Exceptions
     *
     * @param ohmTimeoutException
     *            take any instance of Exception as input
     *
     * @return the constructed ErrorResponse
     */
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorResponse> handleException(TimeoutException ohmTimeoutException,
                                                            ServletWebRequest servletWebRequest) {

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timestamp(ExceptionHandlerHelperUtil.currentTimeStamp())
                        .method(HttpMethod.valueOf(ExceptionHandlerHelperUtil.validateServletWebRequestForHttpMethod(servletWebRequest)))
                        .statusCode(HttpStatus.GATEWAY_TIMEOUT.name())
                        .requestUri(ExceptionHandlerHelperUtil.validateServletWebRequestForUri(servletWebRequest))
                        .errors(List.of(Errors.builder().errorMessage("Timeout while processing the request").build()))
                        .build(),
                HttpStatus.GATEWAY_TIMEOUT);

    }

}