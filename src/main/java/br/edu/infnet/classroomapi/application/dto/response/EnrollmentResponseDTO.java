package br.edu.infnet.classroomapi.application.dto.response;

import br.edu.infnet.classroomapi.domain.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponseDTO {

    private Long id;
    private StudentResponseDTO student;
    private SubjectResponseDTO subject;
    private EnrollmentStatus status;
    private BigDecimal grade;
    private Boolean approved;
    private LocalDateTime enrollmentDate;
    private LocalDateTime completionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}