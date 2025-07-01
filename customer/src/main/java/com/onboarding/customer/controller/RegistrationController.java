package com.onboarding.customer.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.onboarding.customer.dto.ApplicationLeastResponseDto;
import com.onboarding.customer.dto.RegistrationRequest;
import com.onboarding.customer.dto.RegistrationResponse;
import com.onboarding.customer.dto.StatusUpdateRequest;
import com.onboarding.customer.dto.enums.AppStatus;
import com.onboarding.customer.service.RegistrationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RegistrationController {

  private final RegistrationService service;

  @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Map<String,String>> register(
          @RequestPart("payload") RegistrationRequest payload,
          @RequestPart("documents") MultipartFile documents) throws Exception {

      String appId = service.register(payload, documents);
      return ResponseEntity.ok(Map.of("applicationId", appId));
  }
  
  @GetMapping(value = "/{appId}")
  public ResponseEntity<RegistrationResponse> findApplication(@PathVariable("appId") String applicationId) throws Exception {
	  return ResponseEntity.ok(service.findApplicationById(applicationId));
  }
  
  @GetMapping(value = "/status/{appId}")
  public ResponseEntity<AppStatus> findApplicationStatus(@PathVariable("appId") String applicationId) throws Exception {
	  return ResponseEntity.ok(service.findApplicationStatusById(applicationId));
  }
  
  @PutMapping(value = "/status/{appId}")
  public ResponseEntity<Map<String, String>> updateStatus(@PathVariable("appId") String applicationId, @RequestBody @Valid StatusUpdateRequest request ) {
	  service.updateStatus(applicationId, request.getStatus());
	  return ResponseEntity.ok(Map.of("status", request.getStatus().toString(), "message", "Application status changes"));
  }
  
  @GetMapping(value = "/applications")
  public ResponseEntity<List<ApplicationLeastResponseDto>> findFilteredApplications(@RequestParam(value = "status", required = false) List<String> appStatuses) {
	  List<ApplicationLeastResponseDto> applications = service.findApplicationsByStatus(appStatuses);
	  return ResponseEntity.<List<ApplicationLeastResponseDto>>ok(applications);
  }
  
}