package com.onboarding.customer.error;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ErrorResponse {
	private LocalDateTime timestamp;
	private int status;
	private String error;
	private String message;

	public static ErrorResponse buildErrorResponse(String message, HttpStatus status) {
		return ErrorResponse.builder().status(status.value()).message(message).timestamp(LocalDateTime.now())
				.error(status.getReasonPhrase()).build();
	}
}