package br.com.projeto.template.controlleradvice;

import br.com.projeto.template.exception.EntidadeInvalidaRuntimeException;
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

    private final String DETAILS_400_BAD_REQUEST = "Erro no payload.";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ApiResponseError response = criaApiResponse(DETAILS_400_BAD_REQUEST, status.value(), request);
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
        return criaResponse("Problema na formatação do payload.", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EntidadeInvalidaRuntimeException.class)
    public ResponseEntity<Object> entidadeInvalidaException(EntidadeInvalidaRuntimeException ex, WebRequest request) {
        return criaResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler({NullPointerException.class, IllegalStateException.class})
    public ResponseEntity<Object> internalServerException(Throwable throwable, WebRequest request) {
        return criaResponse("Erro inesperado do servidor", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> criaResponse(String details, HttpStatus status, WebRequest request) {
        var response = criaApiResponse(details, status.value(), request);
        return new ResponseEntity<>(response, status);
    }

    private ApiResponseError criaApiResponse(String details, int httpStatus, WebRequest request) {
        return new ApiResponseError(
                details,
                httpStatus,
                ((ServletWebRequest) request).getRequest().getRequestURI().toString(),
                LocalDateTime.now());
    }
}
