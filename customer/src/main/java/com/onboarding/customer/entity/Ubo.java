package com.onboarding.customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Ubo {
  @Id
  @GeneratedValue
  private Long id;
  
  private String name;
  private String ownershipPercent;
  
  @ManyToOne
  @JoinColumn(name="application_id")
  private Application application;
}