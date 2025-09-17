package br.edu.infnet.classroomapi.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponseDTO {

    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer workload;
    private ProfessorResponseDTO professor;
    private Long enrolledStudentsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}