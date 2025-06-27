package hr.algebra.socialnetwork.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<?> handleException(BadRequestException ex, HttpServletRequest request) {
    var apiError =
        new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now());

    return ResponseEntity.badRequest().body(apiError);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiError> handleException(
      ResourceNotFoundException ex, HttpServletRequest request) {

    var apiError =
        new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DuplicatedResourceException.class)
  public ResponseEntity<ApiError> handleException(
      DuplicatedResourceException ex, HttpServletRequest request) {

    var apiError =
        new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.CONFLICT.value(),
            LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
  }
}
