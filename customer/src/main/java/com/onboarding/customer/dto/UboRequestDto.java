package com.onboarding.customer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UboRequestDto {
  @NotBlank 
  private String name;
  
  @NotBlank          
  private String ownershipPercent;
}
