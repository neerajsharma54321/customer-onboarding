package com.onboarding.customer.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.onboarding.customer.dto.enums.AppStatus;
import com.onboarding.customer.entity.Application;

public interface RegistrationRepository
        extends JpaRepository<Application, String> { 
	
	@Query("SELECT a.appStatus FROM Application a WHERE a.applicationId = :applicationId")
	Optional<AppStatus> findStatusById(@Param("applicationId") String applicationId);
	
	@Query("SELECT a FROM Application a WHERE a.appStatus IN :appStatus")
	List<Application> findApplicationsByStatus(@Param("appStatus") List<String> appStatus);
}
