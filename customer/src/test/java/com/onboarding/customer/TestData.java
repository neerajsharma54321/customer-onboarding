package com.onboarding.customer;

import java.util.List;

import com.onboarding.customer.dto.DirectorRequestDto;
import com.onboarding.customer.dto.RegistrationRequest;
import com.onboarding.customer.dto.UboRequestDto;

public class TestData {
    public static RegistrationRequest registrationRequest() {
        RegistrationRequest req = new RegistrationRequest();
        req.setLegalName("TestCorp");
        req.setLegalStructure("Limited");
        req.setCountryOfIncorporation("IN");
        req.setRegistrationNumber("123456");
        req.setTaxId("TAX123");
        req.setIndustryType("Finance");
        req.setContactPerson("Alice");
        req.setContactEmail("alice@example.com");
        req.setAnnualTurnover("100000");
        req.setMonthlyVolume("5000");

        req.setDirectors(List.of(new DirectorRequestDto("John", "ID1", "IN")));
        req.setUbos(List.of(new UboRequestDto("Jane", "50")));
        return req;
    }
}
