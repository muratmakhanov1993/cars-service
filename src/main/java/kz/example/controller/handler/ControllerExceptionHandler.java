package kz.example.controller.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import kz.example.dto.ApiError;
import kz.example.dto.ApiResponse;
import kz.example.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	private final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Object>> defaultHandler(Exception ex, HttpServletRequest req) {
		return new ResponseEntity<>(
			new ApiResponse<>(
				Status.ERROR,
				LocalDateTime.now(),
				ex.getClass().getSimpleName(),
				ex.getMessage(),
				req.getServletPath()
			),
			HttpStatus.OK
		);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request
	) {
		StringBuilder errors = new StringBuilder();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("\n");
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.append(error.getObjectName()).append(": ").append(error.getDefaultMessage()).append("\n");
		}
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors.toString());
		return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
		MissingServletRequestParameterException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request
	) {
		String error = ex.getParameterName() + " parameter is missing";
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
		log.error("handleMissingServletRequestParameterHandler -> {}", apiError);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		StringBuilder errors = new StringBuilder();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.append(violation.getRootBeanClass().getName())
				.append(" ")
				.append(violation.getPropertyPath())
				.append(": ")
				.append(violation.getMessage())
				.append("\n");
		}
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors.toString());
		log.error("handleConstraintViolation -> {}", apiError);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
		MethodArgumentTypeMismatchException ex,
		WebRequest request
	) {
		String error = ex.getName() + " should be of type " + Objects.requireNonNull(ex.getRequiredType()).getName();
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
		log.error("handleMethodArgumentTypeMismatch -> {}", apiError);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

}