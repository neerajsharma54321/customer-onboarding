package com.onboarding.customer.dto;

import java.util.List;

import com.onboarding.customer.entity.Application;
import com.onboarding.customer.entity.Director;
import com.onboarding.customer.entity.Ubo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationRequest {

	@NotBlank
	private String legalName;

	@NotBlank
	private String legalStructure;

	@NotBlank
	private String countryOfIncorporation;

	@NotBlank
	private String registrationNumber;

	@NotBlank
	private String taxId;

	@NotBlank
	private String industryType;

	@Size(min = 1)
	@Valid
	private List<DirectorRequestDto> directors;

	@NotBlank
	private String contactPerson;

	@Email
	@NotBlank
	private String contactEmail;

	@NotBlank
	private String annualTurnover;

	@NotBlank
	private String monthlyVolume;

	@Size(min = 1)
	@Valid
	private List<UboRequestDto> ubos;

	public static Application mapTo(RegistrationRequest r) {
		return Application.builder().legalName(r.legalName).legalStructure(r.legalStructure)
				.countryOfIncorporation(r.countryOfIncorporation).registrationNumber(r.registrationNumber)
				.taxId(r.taxId).industryType(r.industryType)
				.contactPerson(r.contactPerson).contactEmail(r.contactEmail).annualTurnover(r.annualTurnover)
				.monthlyVolume(r.monthlyVolume).build();
	}

	public static List<Director> mapDirector(List<DirectorRequestDto> directoList, Application application) {
		return directoList.stream().map(d -> Director.builder().name(d.getName()).countryOfResidence(d.getCountryOfResidence())
				.nationalId(d.getNationalId()).application(application).build()).toList();
	}

	public static List<Ubo> mapUbo(List<UboRequestDto> uboList, Application application) {
		return uboList.stream().map(u -> Ubo.builder().name(u.getName()).ownershipPercent(u.getOwnershipPercent())
				.application(application).build()).toList();
	}
}