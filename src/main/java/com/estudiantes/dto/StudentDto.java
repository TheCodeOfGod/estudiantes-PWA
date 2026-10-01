package com.estudiantes.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StudentDto(
        Integer id,
        @JsonProperty("full_name") String fullName,
        String email,
        @JsonProperty("enrollment_date") LocalDate enrollmentDate) {
}
