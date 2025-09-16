package br.edu.infnet.classroomapi.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    PROFESSOR("Professor"),
    ADMIN("Administrator");

    private final String description;
}