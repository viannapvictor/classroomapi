package br.edu.infnet.classroomapi.infrastructure.security.services;

import br.edu.infnet.classroomapi.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorInfo {

    private Long id;
    private String name;
    private String email;
    private UserRole role;
}