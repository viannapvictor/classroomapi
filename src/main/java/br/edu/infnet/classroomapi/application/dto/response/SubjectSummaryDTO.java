package br.edu.infnet.classroomapi.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectSummaryDTO {

    private Long id;
    private String name;
    private String code;
    private Integer workload;
}