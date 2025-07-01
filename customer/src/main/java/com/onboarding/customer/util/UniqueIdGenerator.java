package com.onboarding.customer.util;

import org.springframework.stereotype.Component;

@Component
public class UniqueIdGenerator {
	private static String suffix = "ONBOARD";
	
	public String generate() {
		return suffix + (System.currentTimeMillis());
	}
}
