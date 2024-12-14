package kz.example.dto;

import org.springframework.http.HttpStatus;

import java.util.StringJoiner;

public class ApiError {

	private HttpStatus status;
	private String message;
	private String error;

	public ApiError(HttpStatus status, String message, String error) {
		this.status = status;
		this.message = message;
		this.error = error;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public ApiError setStatus(HttpStatus status) {
		this.status = status;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ApiError setMessage(String message) {
		this.message = message;
		return this;
	}

	public String getError() {
		return error;
	}

	public ApiError setError(String error) {
		this.error = error;
		return this;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ApiError.class.getSimpleName() + "[", "]")
			.add("status=" + status)
			.add("message='" + message + "'")
			.add("error='" + error + "'")
			.toString();
	}
}
