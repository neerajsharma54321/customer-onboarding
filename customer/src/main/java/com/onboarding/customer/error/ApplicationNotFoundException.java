package com.onboarding.customer.error;

public class ApplicationNotFoundException extends RuntimeException {
	public ApplicationNotFoundException(String message) {
		super(message);
	}
}
