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
public class StudentResponseDTO {

    private Long id;
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private AddressResponseDTO address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}