package com.onboarding.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import com.onboarding.customer.dto.ApplicationLeastResponseDto;
import com.onboarding.customer.dto.RegistrationRequest;
import com.onboarding.customer.dto.RegistrationResponse;
import com.onboarding.customer.dto.enums.AppStatus;
import com.onboarding.customer.entity.Application;
import com.onboarding.customer.error.ApplicationNotFoundException;
import com.onboarding.customer.repo.RegistrationRepository;
import com.onboarding.customer.service.RegistrationServiceImpl;
import com.onboarding.customer.util.KafkaProducerService;
import com.onboarding.customer.util.UniqueIdGenerator;

class RegistrationServiceImplTest {

    @InjectMocks
    private RegistrationServiceImpl service;

    @Mock
    private RegistrationRepository repository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private UniqueIdGenerator uniqueIdGenerator;

    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new RegistrationServiceImpl(repository, kafkaProducerService, uniqueIdGenerator);
        service.uploadDir = tempDir; 
    }

    @Test
    void register_Application_And_Should_Return_The_ApplicationId() throws Exception {
        RegistrationRequest request = TestData.registrationRequest();
        MockMultipartFile file = new MockMultipartFile("documents", "resume.pdf", "application/pdf", "Sample Data".getBytes());
        String appId = "APP123";

        when(uniqueIdGenerator.generate()).thenReturn(appId);
        when(repository.save(any(Application.class))).thenAnswer(inv -> inv.getArgument(0));

        String result = service.register(request, file);

        assertEquals(appId, result);
        verify(repository, times(1)).save(any());
       verify(kafkaProducerService, times(1)).sendApplication(any());
    }

    @Test
    void find_ApplicationById_And_should_ReturnResponse_with_file() throws Exception {
        String appId = "APP123";
        Path docPath = tempDir.resolve("testfile.txt");
        Files.writeString(docPath, "file content");

        Application app = new Application();
        app.setApplicationId(appId);
        app.setDocumentPath(docPath.toString());
        when(repository.findById(appId)).thenReturn(Optional.of(app));

        RegistrationResponse response = service.findApplicationById(appId);

        assertNotNull(response);
        assertEquals(appId, response.getApplicationId());
    }

    @Test
    void find_ApplicationById_And_shouldThrow_error_If_data_is_NotFound() {
        when(repository.findById("INVALID")).thenReturn(Optional.empty());
        assertThrows(ApplicationNotFoundException.class, () -> service.findApplicationById("INVALID"));
    }

    @Test
    void findApplicationStatusById_shouldReturnStatus() {
        String appId = "APP001";
        when(repository.findStatusById(appId)).thenReturn(Optional.of(AppStatus.PENDING));

        AppStatus status = service.findApplicationStatusById(appId);
        assertEquals(AppStatus.PENDING, status);
    }

    @Test
    void update_application_status_and_should_update_and_save() {
        String appId = "APP123";
        Application app = new Application();
        app.setApplicationId(appId);
        when(repository.findById(appId)).thenReturn(Optional.of(app));

        service.updateStatus(appId, AppStatus.APPROVED);

        assertEquals(AppStatus.APPROVED, app.getAppStatus());
        verify(repository).save(app);
    }

    @Test
    void find_applications_byStatus_and_shouldReturn_listOfApplication() {
        Application app = new Application();
        app.setApplicationId("APP001");
        app.setLegalName("ABC Ltd.");
        app.setLegalStructure("Limited");
        app.setCountryOfIncorporation("IN");
        app.setRegistrationNumber("123456");
        app.setIndustryType("Finance");
        app.setAppStatus(AppStatus.APPROVED);

        when(repository.findApplicationsByStatus(List.of("APPROVED")))
                .thenReturn(List.of(app));

        List<ApplicationLeastResponseDto> result = service.findApplicationsByStatus(List.of("APPROVED"));

        assertEquals(1, result.size());
        assertEquals("APP001", result.get(0).getApplicationId());
    }
}
