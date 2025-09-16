package br.edu.infnet.classroomapi.domain.entities;

import br.edu.infnet.classroomapi.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Professor {
    
    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Subject> subjects;

    public Professor(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = UserRole.PROFESSOR;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.subjects = new ArrayList<>();
    }

    public void addSubject(Subject subject) {
        if (this.subjects == null) {
            this.subjects = new ArrayList<>();
        }
        this.subjects.add(subject);
        subject.setProfessor(this);
    }

    public void removeSubject(Subject subject) {
        if (this.subjects != null) {
            this.subjects.remove(subject);
            subject.setProfessor(null);
        }
    }

    public void updateInfo(String name, String email) {
        this.name = name;
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }
}