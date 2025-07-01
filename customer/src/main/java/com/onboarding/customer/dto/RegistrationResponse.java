package com.onboarding.customer.dto;

import java.util.List;

import com.onboarding.customer.entity.Application;
import com.onboarding.customer.entity.Director;
import com.onboarding.customer.entity.Ubo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {

	private String applicationId;
	private String legalName;
	private String legalStructure;
	private String countryOfIncorporation;
	private String registrationNumber;
	private String taxId;
	private String industryType;
	private List<DirectorResponse> directors;
	private String contactPerson;
	private String contactEmail;
	private String annualTurnover;
	private String monthlyVolume;
	private List<UboResponse> ubos;
	private String statusMessage;
	private FileResponseDto fileInfo;

	public static RegistrationResponse toDto(Application app, FileResponseDto fileResponseDto) {
		return RegistrationResponse.builder().applicationId(app.getApplicationId()).legalName(app.getLegalName())
				.legalStructure(app.getLegalStructure()).countryOfIncorporation(app.getCountryOfIncorporation())
				.registrationNumber(app.getRegistrationNumber()).taxId(app.getTaxId())
				.industryType(app.getIndustryType()).contactPerson(app.getContactPerson())
				.contactEmail(app.getContactEmail()).annualTurnover(app.getAnnualTurnover())
				.monthlyVolume(app.getMonthlyVolume()).directors(toDirectorResponses(app.getDirectors()))
				.ubos(toUboResponses(app.getUbos())).statusMessage(app.getAppStatus().name())
				.fileInfo(fileResponseDto)
				.build();
	}

	private static List<UboResponse> toUboResponses(List<Ubo> ubos) {
		if (ubos == null)
			return List.of();
		return ubos.stream().map(u -> UboResponse.builder().name(u.getName()).ownershipPercent(u.getOwnershipPercent())
				.build()).toList();
	}

	private static List<DirectorResponse> toDirectorResponses(List<Director> directors) {
		if (directors == null)
			return List.of();
		return directors.stream().map(
				d -> DirectorResponse.builder().name(d.getName()).countryOfResidence(d.getCountryOfResidence()).nationalId(d.getNationalId()).build())
				.toList();

	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder(toBuilder = true)
	public static class FileResponseDto {
		private String fileName;
		private String fileType;
		private long fileSize;
		private String base64Content;
	}

}
