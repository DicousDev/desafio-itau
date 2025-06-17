package br.com.desafioitau.order.controlleradvice;

import br.com.desafioitau.order.exception.EntityInvalidRuntimeException;
import br.com.desafioitau.order.exception.EntityNotFoundRuntimeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    private final String DETAILS_400_BAD_REQUEST = "Payload error.";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ApiResponseError response = createApiResponse(DETAILS_400_BAD_REQUEST, status.value(), request);
        Map<String, String> fieldNameValue = new HashMap<>();
        for(FieldError field : ex.getBindingResult().getFieldErrors()) {
            fieldNameValue.put(field.getField(), field.getDefaultMessage());
        }

        fieldNameValue.entrySet().stream()
                .forEach(x -> response.addValidationError(new ValidationError(x.getKey(), x.getValue())));
        return handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return createResponse("Payload formatting error.", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EntityInvalidRuntimeException.class)
    public ResponseEntity<Object> entidadeInvalidaException(EntityInvalidRuntimeException ex, WebRequest request) {
        return createResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(EntityNotFoundRuntimeException.class)
    public ResponseEntity<Object> naoEncontradoException(EntityNotFoundRuntimeException ex, WebRequest request) {
        return createResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({NullPointerException.class, IllegalStateException.class})
    public ResponseEntity<Object> internalServerException(Throwable throwable, WebRequest request) {
        return createResponse("Unexpected server error", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> createResponse(String details, HttpStatus status, WebRequest request) {
        var response = createApiResponse(details, status.value(), request);
        return new ResponseEntity<>(response, status);
    }

    private ApiResponseError createApiResponse(String details, int httpStatus, WebRequest request) {
        return new ApiResponseError(
                details,
                httpStatus,
                ((ServletWebRequest) request).getRequest().getRequestURI().toString(),
                LocalDateTime.now());
    }
}
