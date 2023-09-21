package com.ffreaky.apigw.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record UserDTORecord(
        @Positive Long id,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String username,
        @Email String email,
        @NotBlank String phoneNumber,
        @NotNull Set<UserAddressDTORecord> address) {
}