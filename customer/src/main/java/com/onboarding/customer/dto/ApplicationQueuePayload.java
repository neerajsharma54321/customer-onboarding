package com.onboarding.customer.dto;

import com.onboarding.customer.dto.enums.AppStatus;
import com.onboarding.customer.entity.Application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApplicationQueuePayload {
	  private String applicationId;
	  private String legalName;
	  private String legalStructure;
	  private String countryOfIncorporation;
	  private String registrationNumber;
	  private String taxId;
	  private String industryType;
	  private String contactPerson;
	  private String contactEmail;
	  private String annualTurnover;
	  private String monthlyVolume;
	  private AppStatus appStatus;
	  
	  public static ApplicationQueuePayload mapTo(Application a) {
		  return ApplicationQueuePayload.builder().applicationId(a.getApplicationId())
				  .legalName(a.getLegalName())
				  .legalStructure(a.getLegalStructure())
				  .countryOfIncorporation(a.getCountryOfIncorporation())
				  .registrationNumber(a.getRegistrationNumber())
				  .taxId(a.getTaxId())
				  .industryType(a.getIndustryType())
				  .contactPerson(a.getContactPerson())
				  .contactEmail(a.getContactEmail())
				  .annualTurnover(a.getAnnualTurnover())
				  .monthlyVolume(a.getMonthlyVolume())
				  .appStatus(a.getAppStatus())
				  .build();
	  }
}
