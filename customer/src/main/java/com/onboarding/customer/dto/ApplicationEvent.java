package com.onboarding.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApplicationEvent {
	private String applicationId;
	private String legalName;
	private String legalStructure;
	private String countryOfIncorporation;
	private String registrationNumber;
	private String industryType;
	private String status;
}
