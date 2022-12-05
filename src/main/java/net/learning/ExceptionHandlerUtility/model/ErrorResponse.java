package net.learning.ExceptionHandlerUtility.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 * Class representing an API error in order to provide details of exceptions thrown back to the client
 */
@Data
@Builder
public class ErrorResponse {

    private HttpMethod method;
    private String requestUri;
    private String statusCode;
    private String timestamp;
    private List<Errors> errors;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public ErrorResponse(){

    }

    public ErrorResponse(HttpMethod method, String requestUri, String statusCode, List<Errors> errors) {
        this();
        this.method = method;
        this.requestUri = requestUri;
        this.statusCode = statusCode;
        timestamp = dateFormatter.format(LocalDateTime.now(ZoneOffset.UTC));
        this.errors = errors;
    }

}
