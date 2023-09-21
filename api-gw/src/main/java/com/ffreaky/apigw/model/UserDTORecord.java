package com.ffreaky.apigw.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UserDTORecord(
        @Positive Long id,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String username,
        @Email String email,
        @NotBlank String phoneNumber) {
}