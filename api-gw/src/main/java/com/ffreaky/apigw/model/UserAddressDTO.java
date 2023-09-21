package com.ffreaky.apigw.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UserAddressDTO(
        @Positive Long id,
        @Positive Long userId,
        @NotBlank String streetAddress,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String postalIndex,
        @NotBlank String country,
        @NotBlank String phoneNumber,
        @NotBlank String mobile) {
}
