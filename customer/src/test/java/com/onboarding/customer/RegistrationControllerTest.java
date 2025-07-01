package com.onboarding.customer;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onboarding.customer.controller.RegistrationController;
import com.onboarding.customer.dto.ApplicationLeastResponseDto;
import com.onboarding.customer.dto.DirectorRequestDto;
import com.onboarding.customer.dto.RegistrationRequest;
import com.onboarding.customer.dto.RegistrationResponse;
import com.onboarding.customer.dto.StatusUpdateRequest;
import com.onboarding.customer.dto.UboRequestDto;
import com.onboarding.customer.dto.enums.AppStatus;
import com.onboarding.customer.service.RegistrationService;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private RegistrationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_applicant_and_shouldReturnApplicationId() throws Exception {
    	
        MockMultipartFile file = new MockMultipartFile("documents", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "Dummy Content".getBytes());

        RegistrationRequest request = new RegistrationRequest();
        request.setLegalName("Test Legal name");
        request.setLegalStructure("Limited");
        request.setCountryOfIncorporation("IN");
        request.setRegistrationNumber("123456");
        request.setTaxId("TAX123");
        request.setIndustryType("Finance");
        request.setDirectors(List.of(new DirectorRequestDto("John Doe", "ID001", "IN")));
        request.setContactPerson("John");
        request.setContactEmail("john@test.com");
        request.setAnnualTurnover("1000000");
        request.setMonthlyVolume("10000");
        request.setUbos(List.of(new UboRequestDto("Jane", "51")));

        MockMultipartFile payload = new MockMultipartFile(
                "payload", "payload", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request));

        Mockito.when(service.register(any(RegistrationRequest.class), any())).thenReturn("APP123");

        mockMvc.perform(multipart("/api/onboarding/register")
                        .file(payload)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationId").value("APP123"));
    }

    @Test
    void find_applicationById_and_shouldReturn_ApplicationDetails() throws Exception {
        String appId = "APP123";
        RegistrationResponse response = new RegistrationResponse();
        Mockito.when(service.findApplicationById(appId)).thenReturn(response);

        mockMvc.perform(get("/api/onboarding/{appId}", appId))
                .andExpect(status().isOk());
    }

    @Test
    void find_applicationStatus_shouldReturnStatus() throws Exception {
        String appId = "APP123";
        Mockito.when(service.findApplicationStatusById(appId)).thenReturn(AppStatus.PENDING);

        var a = mockMvc.perform(get("/api/onboarding/status/{appId}", appId))
                .andExpect(status().isOk());
    }

    @Test
    void updateApplicationStatus_and_should_ReturnSuccessMessage() throws Exception {
        String appId = "APP123";
        StatusUpdateRequest request = new StatusUpdateRequest();
        request.setStatus(AppStatus.APPROVED);

        mockMvc.perform(put("/api/onboarding/status/{appId}", appId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.message").value("Application status changes"));
    }

    @Test
    void find_filteredApplications_and_should_ReturnApplicationList() throws Exception {
        Mockito.when(service.findApplicationsByStatus(List.of("APPROVED")))
                .thenReturn(List.of(new ApplicationLeastResponseDto()));

        mockMvc.perform(get("/api/onboarding/applications")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk());
    }
}
