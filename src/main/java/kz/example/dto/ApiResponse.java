package kz.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kz.example.enums.Status;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	private Status status;

	private LocalDateTime timestamp;

	private String error;

	private String message;

	private String path;

	@JsonInclude
	private T data;

	public ApiResponse(Status status, T data) {
		this.status = status;
		this.data = data;
	}

	public ApiResponse(Status status) {
		this.status = status;
	}

	public ApiResponse(Status status, LocalDateTime timestamp, String error, String message, String path) {
		this.status = status;
		this.timestamp = timestamp;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public Status getStatus() {
		return status;
	}

	public ApiResponse<T> setStatus(Status status) {
		this.status = status;
		return this;
	}

	public T getData() {
		return data;
	}

	public ApiResponse<T> setData(T data) {
		this.data = data;
		return this;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public ApiResponse<T> setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public String getError() {
		return error;
	}

	public ApiResponse<T> setError(String error) {
		this.error = error;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ApiResponse<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public String getPath() {
		return path;
	}

	public ApiResponse<T> setPath(String path) {
		this.path = path;
		return this;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ApiResponse.class.getSimpleName() + "[", "]")
			.add("status=" + status)
			.add("timestamp=" + timestamp)
			.add("error='" + error + "'")
			.add("message='" + message + "'")
			.add("path='" + path + "'")
			.add("data=" + data)
			.toString();
	}
}
