package com.onboarding.customer.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.onboarding.customer.dto.ApplicationEvent;
import com.onboarding.customer.dto.ApplicationLeastResponseDto;
import com.onboarding.customer.dto.ApplicationQueuePayload;
import com.onboarding.customer.dto.RegistrationRequest;
import com.onboarding.customer.dto.RegistrationResponse;
import com.onboarding.customer.dto.RegistrationResponse.FileResponseDto;
import com.onboarding.customer.dto.enums.AppStatus;
import com.onboarding.customer.entity.Application;
import com.onboarding.customer.error.ApplicationNotFoundException;
import com.onboarding.customer.repo.RegistrationRepository;
import com.onboarding.customer.util.KafkaProducerService;
import com.onboarding.customer.util.UniqueIdGenerator;

import jakarta.transaction.Transactional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	private final RegistrationRepository registrationRepository;

	private final KafkaProducerService kafkaProducerService;
	
	private final UniqueIdGenerator uniqueIdGenerator;

	public Path uploadDir = Paths.get("C://D/my code/uploadedfile");
	
	private static final Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class);
	
	public RegistrationServiceImpl(final RegistrationRepository registrationRepository, final KafkaProducerService kafkaProducerService, final UniqueIdGenerator uniqueIdGenerator) {
		this.registrationRepository = registrationRepository;
		this.kafkaProducerService = kafkaProducerService;
		this.uniqueIdGenerator = uniqueIdGenerator;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public String register(RegistrationRequest req, MultipartFile doc) throws IOException {
		log.info("saving of application register started.");
		Application application = RegistrationRequest.mapTo(req);

		application.setDirectors(RegistrationRequest.mapDirector(req.getDirectors(), application));
		application.setUbos(RegistrationRequest.mapUbo(req.getUbos(), application));
		String uniqueId = uniqueIdGenerator.generate();
		
		if (!doc.isEmpty()) {
			Path target = uploadDir.resolve(uniqueId+ "-" + doc.getOriginalFilename());
			Files.copy(doc.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
			application.setDocumentPath(target.toString());
		}

		// generate unique id
		application.setApplicationId(uniqueId);

		Application saved = registrationRepository.save(application);
		kafkaProducerService.sendApplication(ApplicationQueuePayload.mapTo(application));
		
		kafkaProducerService.sendNotification(ApplicationEvent.builder().applicationId(uniqueId).legalName(application.getLegalName())
				.legalStructure(application.getLegalStructure())
				.countryOfIncorporation(application.getCountryOfIncorporation())
				.industryType(application.getIndustryType())
				.registrationNumber(application.getRegistrationNumber())
				.status(application.getAppStatus().name())
				.build());
		
		log.info("application saved.");
		return saved.getApplicationId();
	}

	@Override
	public RegistrationResponse findApplicationById(String applicationId) throws IOException {
		log.info("find the application by id");
		Optional<Application> optionalApplication = registrationRepository.findById(applicationId);
		if(optionalApplication.isPresent()) {
		 	Application application = optionalApplication.get();
			Path filePath = Paths.get(application.getDocumentPath());
			byte[] fileBytes = Files.readAllBytes(filePath);
			FileResponseDto fileResponseDto = FileResponseDto.builder()
			.base64Content(Base64.getEncoder().encodeToString(fileBytes))
			.fileName(filePath.getFileName().toString())
			.fileSize(Files.size(filePath))
			.fileType(Files.probeContentType(filePath))
			.build();
			log.info("application fetched successfully.");
			return RegistrationResponse.toDto(application, fileResponseDto);
		} else {
			log.error("Unable to find the application");
			throw new ApplicationNotFoundException("Application id not found");
		}
	}

	@Override
	public AppStatus findApplicationStatusById(String applicationId) {
		log.info("find the application status by id");
		return registrationRepository.findStatusById(applicationId)
				.orElseThrow(() -> new ApplicationNotFoundException("Application id not found"));
	}

	@Override
	public void updateStatus(String applicationId, AppStatus appStatus) {
		log.info("update the application status by id");
		Application application = registrationRepository.findById(applicationId)
				.orElseThrow(() -> new ApplicationNotFoundException("Application id not found"));
		application.setAppStatus(appStatus);
		log.info("application status updated successfully");
		registrationRepository.save(application);
	}

	@Override
	public List<ApplicationLeastResponseDto> findApplicationsByStatus(List<String> appStatuses) {
		log.info("find the applications by status");
		if(ObjectUtils.isEmpty(appStatuses))
			appStatuses = List.of(AppStatus.APPROVED.name(), AppStatus.INITIATED.name());
		
		return registrationRepository.findApplicationsByStatus(appStatuses).stream()
				.map(app -> ApplicationLeastResponseDto.builder().applicationId(app.getApplicationId())
						.legalName(app.getLegalName()).legalStructure(app.getLegalStructure())
						.countryOfIncorporation(app.getCountryOfIncorporation())
						.registrationNumber(app.getRegistrationNumber()).industryType(app.getIndustryType())
						.status(app.getAppStatus().name())
						.build())
				.toList();
	}
}
