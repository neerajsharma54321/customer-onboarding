package com.onboarding.customer.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.onboarding.customer.dto.enums.AppStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table(name="applications")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Application {

  @Id
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
  
  @Builder.Default
  private Date createdAt = Date.from(Instant.now());
  
  private Date updatedAt;
  
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private AppStatus appStatus = AppStatus.INITIATED;

  @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Director> directors = new ArrayList<>();

  @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Ubo> ubos = new ArrayList<>();

  private String documentPath;
}