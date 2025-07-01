package com.onboarding.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApplicationLeastResponseDto {
	private String applicationId;
	private String legalName;
	private String legalStructure;
	private String countryOfIncorporation;
	private String registrationNumber;
	private String industryType;
	private String status;
}
