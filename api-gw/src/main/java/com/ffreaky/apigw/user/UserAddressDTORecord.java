package com.ffreaky.apigw.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UserAddressDTORecord(
        @Positive Long id,
        @NotBlank String streetAddress,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String postalIndex,
        @NotBlank String country,
        @NotBlank String phoneNumber,
        @NotBlank String mobile) {
}
