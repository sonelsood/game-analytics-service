
package com.fsb.game.analytics.service.server.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fsb.game.analytics.service.core.exception.dto.ErrorResponseDto;
import com.fsb.game.analytics.service.core.exception.dto.InvalidParameterDto;

import lombok.extern.slf4j.Slf4j;

/**
 * providing AOP exception handling for the every request
 */
@Slf4j
@ControllerAdvice(basePackages = "com.fsb.game.analytics.service.server")
public class GameExceptionAdvice extends ResponseEntityExceptionHandler {

    /**
     * Handles the uncaught {@link Exception} exceptions and returns a JSON formatted response.
     *
     * @param ex the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleUncaughtException(final Exception ex, final ServletWebRequest request) {
        log(ex, request);
        final ErrorResponseDto errorResponseDto = ErrorResponseUtil.build(Exception.class.getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }

    /**
     * Handles the uncaught {@link BusinessException} exceptions and returns a JSON formatted response.
     *
     * @param ex the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @ExceptionHandler({ BusinessException.class })
    public ResponseEntity<Object> handleCustomUncaughtBusinessException(final BusinessException ex,
            final ServletWebRequest request) {
        log(ex, request);
        final ErrorResponseDto errorResponseDto = ErrorResponseUtil.build(ex.getCode(), ex.getMessage(),
                ex.getHttpStatus(), ex.getInvalidParameters());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponseDto);
    }

    /**
     * Handles the uncaught {@link ApplicationException} exceptions and returns a JSON formatted response.
     *
     * @param ex the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @ExceptionHandler({ ApplicationException.class })
    public ResponseEntity<Object> handleCustomUncaughtApplicationException(final ApplicationException ex,
            final ServletWebRequest request) {
        log(ex, request);
        final ErrorResponseDto errorResponseDto = ErrorResponseUtil.build(ex.getCode(), ex.getMessage(),
                ex.getHttpStatus());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponseDto);
    }

    /**
     * Handles the uncaught {@link ConstraintViolationException} exceptions and returns a JSON formatted response.
     *
     * @param ex the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException ex,
            final ServletWebRequest request) {
        log(ex, request);

        final List<InvalidParameterDto> invalidParameters = new ArrayList<>();
        ex.getConstraintViolations().forEach(constraintViolation -> {
            final Iterator<Path.Node> it = constraintViolation.getPropertyPath().iterator();
            if (it.hasNext()) {
                try {
                    it.next();
                    final Path.Node n = it.next();
                    final InvalidParameterDto invalidParameter = new InvalidParameterDto();
                    invalidParameter.setErrorField(n.getName());
                    invalidParameter.setErrorMessage(constraintViolation.getMessage());
                    invalidParameters.add(invalidParameter);
                } catch (final Exception e) {
                    log.warn("[Advocatus] Can't extract the information about constraint violation");
                }
            }
        });

        final ErrorResponseDto errorResponseDto = ErrorResponseUtil.build(
                ConstraintViolationException.class.getSimpleName(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST, invalidParameters);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    /**
     * Handles the uncaught {@link HttpMessageNotReadableException} exceptions and returns a JSON formatted response.
     *
     * @param ex the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log(ex, (ServletWebRequest) request);
        String message = ex.getMessage();
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ifx = (InvalidFormatException) ex.getCause();
            if (ifx.getTargetType() != null && ifx.getTargetType().isEnum()) {
                message = String.format("Invalid enum value: '%s' for the field: '%s'. The value must be one of: %s.",
                        ifx.getValue(), ifx.getPath().get(ifx.getPath().size() - 1).getFieldName(),
                        Arrays.toString(ifx.getTargetType().getEnumConstants()));
            }
        }
        final ErrorResponseDto errorResponseDto = ErrorResponseUtil
                .build(HttpMessageNotReadableException.class.getSimpleName(), message, HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    /**
     * Handles the uncaught {@link HttpRequestMethodNotSupportedException} exceptions and returns a JSON formatted
     * response.
     *
     * @param ex the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {
        log(ex, (ServletWebRequest) request);
        final ErrorResponseDto errorResponseDto = ErrorResponseUtil.build(
                HttpRequestMethodNotSupportedException.class.getSimpleName(), ex.getMessage(),
                HttpStatus.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponseDto);
    }

    /**
     * Handles the uncaught {@link MethodArgumentNotValidException} exceptions and returns a JSON formatted response.
     *
     * @param ex the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log(ex, (ServletWebRequest) request);
        final List<InvalidParameterDto> invalidParameters = ex
                .getBindingResult().getFieldErrors().stream().map(fieldError -> InvalidParameterDto.builder()
                        .errorField(fieldError.getField()).errorMessage(fieldError.getDefaultMessage()).build())
                .collect(Collectors.toList());

        final ErrorResponseDto errorResponseDto = ErrorResponseUtil.build(
                MethodArgumentNotValidException.class.getSimpleName(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST, invalidParameters);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    /**
     * Handles the uncaught {@link NoSuchElementException} exceptions and returns a JSON formatted response.
     *
     * @param ex the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @ExceptionHandler(value = { NoSuchElementException.class })
    public ResponseEntity<Object> handleNoSuchElementException(final NoSuchElementException ex,
            final ServletWebRequest request) {
        log(ex, request);
        final ErrorResponseDto errorResponseDto = ErrorResponseUtil.build(Exception.class.getSimpleName(),
                ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    /**
     * Handles the uncaught {@link ServletRequestBindingException} exceptions and returns a JSON formatted response.
     *
     * @param ex the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(final ServletRequestBindingException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log(ex, (ServletWebRequest) request);

        final String missingParameter;
        final String missingParameterType;

        if (ex instanceof MissingRequestHeaderException) {
            missingParameter = ((MissingRequestHeaderException) ex).getHeaderName();
            missingParameterType = "header";
        } else if (ex instanceof MissingServletRequestParameterException) {
            missingParameter = ((MissingServletRequestParameterException) ex).getParameterName();
            missingParameterType = "query";
        } else if (ex instanceof MissingPathVariableException) {
            missingParameter = ((MissingPathVariableException) ex).getVariableName();
            missingParameterType = "path";
        } else {
            missingParameter = "unknown";
            missingParameterType = "unknown";
        }

        final InvalidParameterDto missingParameterDto = InvalidParameterDto.builder().errorField(missingParameter)
                .errorMessage(
                        String.format("Missing %s parameter with name '%s'", missingParameterType, missingParameter))
                .build();

        final ErrorResponseDto errorResponseDto = ErrorResponseUtil.build(
                ServletRequestBindingException.class.getSimpleName(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST, Collections.singletonList(missingParameterDto));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    /**
     * Handles the uncaught {@link TypeMismatchException} exceptions and returns a JSON formatted response.
     *
     * @param ex the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers,
            final HttpStatus status, final WebRequest request) {
        log(ex, (ServletWebRequest) request);

        String parameter = ex.getPropertyName();
        if (ex instanceof MethodArgumentTypeMismatchException) {
            parameter = ((MethodArgumentTypeMismatchException) ex).getName();
        }

        final ErrorResponseDto errorResponseDto = ErrorResponseUtil.build(TypeMismatchException.class.getSimpleName(),
                String.format("Unexpected type specified for '%s' parameter. Required '%s'", parameter,
                        ex.getRequiredType()),
                HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    /**
     * Handles the uncaught {@link MissingPathVariableException} exceptions and returns a JSON formatted response.
     *
     * @param ex the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(final MissingPathVariableException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        return handleServletRequestBindingException(ex, headers, status, request);
    }

    /**
     * Handles the uncaught {@link MissingServletRequestParameterException} exceptions and returns a JSON formatted
     * response.
     *
     * @param ex the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {
        log(ex, (ServletWebRequest) request);
        return handleServletRequestBindingException(ex, headers, status, request);
    }

    private void log(final Exception ex, final ServletWebRequest request) {
        final Optional<HttpMethod> httpMethod;
        final Optional<String> requestUrl;

        final Optional<ServletWebRequest> possibleIncomingNullRequest = Optional.ofNullable(request);
        if (possibleIncomingNullRequest.isPresent()) {
            // get the HTTP Method
            httpMethod = Optional.ofNullable(possibleIncomingNullRequest.get().getHttpMethod());
            if (Optional.ofNullable(possibleIncomingNullRequest.get().getRequest()).isPresent()) {
                // get the Request URL
                requestUrl = Optional.of(possibleIncomingNullRequest.get().getRequest().getRequestURL().toString());
            } else {
                requestUrl = Optional.empty();
            }
        } else {
            httpMethod = Optional.empty();
            requestUrl = Optional.empty();
        }

        log.error("Request {} {} failed with exception reason: {}",
                (httpMethod.isPresent() ? httpMethod.get() : "'null'"), (requestUrl.orElse("'null'")), ex.getMessage(),
                ex);
    }
}
