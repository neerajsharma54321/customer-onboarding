package com.onboarding.customer.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.onboarding.customer.dto.ApplicationLeastResponseDto;
import com.onboarding.customer.dto.RegistrationRequest;
import com.onboarding.customer.dto.RegistrationResponse;
import com.onboarding.customer.dto.enums.AppStatus;

public interface RegistrationService {
	String register(RegistrationRequest req, MultipartFile doc) throws IOException;
	
	RegistrationResponse findApplicationById(String applicationId) throws IOException;
	
	AppStatus findApplicationStatusById(String applicationId);
	
	void updateStatus(String applicationId, AppStatus appStatus);

	List<ApplicationLeastResponseDto> findApplicationsByStatus(List<String> appStatuses);
}
