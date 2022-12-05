package net.learning.ExceptionHandlerUtility.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.learning.ExceptionHandlerUtility.model.Errors;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Generic Exception Handler Helper/Utility Class To build generic exception format instances used while building API
 * Error Message Object
 */
@Slf4j
public class ExceptionHandlerHelperUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();


    private ExceptionHandlerHelperUtil() {
        // No-OP
    }

    /**
     * Method to return the current TimeStamp
     *
     * @return the current time stamp
     */
    public static String currentTimeStamp() {
        return DateTimeFormatter.ofPattern(Constants.TIME_STAMP_FORMAT).format(LocalDateTime.now(ZoneOffset.UTC));
    }

    /**
     * To validate Servlet Web Request For HttpMethod
     *
     * @param servletWebRequest
     *            to take as input
     *
     * @return servlet Web Request HTTP Method
     */
    public static String validateServletWebRequestForHttpMethod(ServletWebRequest servletWebRequest) {
        return Optional.ofNullable(servletWebRequest)
                .filter(servletWebRequest1 -> null != servletWebRequest1.getHttpMethod())
                .map(ServletWebRequest::getHttpMethod).map(Enum::name).orElse(null);
    }

    /**
     * To validate Servlet Web Request For URI
     *
     * @param servletWebRequest
     *            to take as input
     *
     * @return servlet Web Request URI
     */
    public static String validateServletWebRequestForUri(ServletWebRequest servletWebRequest) {
        return Optional.ofNullable(servletWebRequest)
                .filter(swr -> !StringUtils.isEmpty(swr.getRequest().getRequestURI()))
                .map(servWebReq -> servWebReq.getRequest().getRequestURI()).orElse(Constants.EMPTY_STRING);
    }

    public static List<Errors> createErrorListFromValidationErrors(List<ObjectError> errorList) {
        Set<Errors> ohmErrorList = new HashSet<>();
        for (ObjectError objectError : errorList) {
            Errors ohmError = null;
            try {
                if (Objects.requireNonNull(objectError.getDefaultMessage()).startsWith("{")
                        && Objects.requireNonNull(objectError.getDefaultMessage()).endsWith("}")) {
                    ohmError = MAPPER.readValue(objectError.getDefaultMessage(), Errors.class);
                } else if (objectError instanceof FieldError) {
                    FieldError fieldError = (FieldError) objectError;
                    ohmError = Errors.builder()
                            .errorMessage(
                                    fieldError.getField() + " value:" + fieldError.getRejectedValue() + " is not valid")
                            .build();
                } else {
                    ohmError = Errors.builder()
                            .errorMessage(objectError.getDefaultMessage()).build();
                }
            } catch (Exception e) {
                log.error(Arrays.toString(e.getStackTrace()));
            }
            ohmErrorList.add(ohmError);
        }
        return new ArrayList<>(ohmErrorList);
    }
}

