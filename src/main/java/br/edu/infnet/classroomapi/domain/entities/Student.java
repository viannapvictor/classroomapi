package br.edu.infnet.classroomapi.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    
    private Long id;
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private Address address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Enrollment> enrollments;

    public Student(String name, String cpf, String email, String phone, Address address) {
        validateRequiredFields(name, cpf, email);
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.enrollments = new ArrayList<>();
    }

    private void validateRequiredFields(String name, String cpf, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public void addEnrollment(Enrollment enrollment) {
        if (this.enrollments == null) {
            this.enrollments = new ArrayList<>();
        }
        this.enrollments.add(enrollment);
    }

    public void updateInfo(String name, String email, String phone, Address address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }
}