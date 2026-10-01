package com.estudiantes.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.estudiantes.dto.StudentDto;
import com.estudiantes.dto.StudentRequest;
import com.estudiantes.model.Student;
import com.estudiantes.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentDto> findAll() {
        return studentRepository.findAll(Sort.by("id")).stream()
                .map(this::toDto)
                .toList();
    }

    public StudentDto save(StudentRequest request) {
        Student student = new Student(request.fullName(), request.email(), request.enrollmentDate());
        return toDto(studentRepository.save(student));
    }

    private StudentDto toDto(Student student) {
        return new StudentDto(student.getId(), student.getFullName(),
                student.getEmail(), student.getEnrollmentDate());
    }
}
