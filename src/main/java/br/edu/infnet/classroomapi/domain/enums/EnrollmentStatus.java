package br.edu.infnet.classroomapi.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnrollmentStatus {
    ACTIVE("Active"),
    SUSPENDED("Suspended"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed");

    private final String description;
}